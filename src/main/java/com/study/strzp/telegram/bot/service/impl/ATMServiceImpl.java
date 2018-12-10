package com.study.strzp.telegram.bot.service.impl;

import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.util.MessageFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("atmService")
public class ATMServiceImpl implements CommandService {
    @Autowired
    @Qualifier("proxyRestTemplate")
    RestTemplate proxyRestTemplate;

    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    @Override
    public SendMessage handle(Update update) {
        SendMessage sendMessage = new SendMessage();
        SendLocation sendLocation = new SendLocation();
        sendLocation.setChatId(update.getMessage().getChatId());
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.enableHtml(true);
        MessageFormatter.addButtons(sendMessage);
        Location location = update.getMessage().getLocation();

        String res = proxyRestTemplate.getForObject(
                String.format("https://geocode-maps.yandex.ru/1.x/?geocode=%s,%s&kind=house&format=json",
                        location.getLongitude(), location.getLatitude()),
                String.class
        );
        JSONObject addressJson = new JSONObject(res);

        String city;
        String address;
        try {
            city = addressJson.getJSONObject("response").getJSONObject("GeoObjectCollection")
                    .getJSONArray("featureMember").getJSONObject(0)
                    .getJSONObject("GeoObject").getString("description");
            address = addressJson.getJSONObject("response").getJSONObject("GeoObjectCollection")
                    .getJSONArray("featureMember").getJSONObject(0)
                    .getJSONObject("GeoObject").getString("name");
        } catch (Exception e) {
            return sendMessage.setText("\"<b>Не удалось получить Ваше местоположение.</b>\"");
        }

        int streetNum = Integer.getInteger(address.replaceAll("[^0-9]", ""));
        city = city.replaceAll(",.*", "");
        address = address.replaceAll(",.*", "").
                replaceAll(" улица", "").
                replaceAll(" проспект", "").
                replaceAll(" проулок", "").
                replaceAll("улица ", "").
                replaceAll("проспект ", "").
                replaceAll("проулок ", "");

        try {
            String pbRes = restTemplate.getForObject(
                    String.format("https://api.privatbank.ua/p24api/infrastructure?atm&address=%s&city=%s&json=true"
                            , address, city),
                    String.class
            );
            JSONObject pbJson = new JSONObject(pbRes);
            JSONArray devices = pbJson.getJSONArray("devices");
            if (devices == null || devices.length() == 0) {
                sendMessage.setText("<b>Банкоматов рядом нет.</b> Возможно, местоположение было получено не точно. Попробуйте выбрать вручную ближайшую к Вам улицу.");
            } else {
                List<Integer> addresses = new ArrayList<>();
                for (int i = 0; i < devices.length(); i++) {
                    String tmpAddr = devices.getJSONObject(i).getString("fullAddressRu");
                    tmpAddr = tmpAddr.replaceAll("[^0-9]", "");
                    addresses.add(Integer.valueOf(tmpAddr));
                }
                String atmAddress = devices.getJSONObject(searchNearest(streetNum, addresses)).getString("fullAddressRu");
                String atmPlace = devices.getJSONObject(searchNearest(streetNum, addresses)).getString("placeRu");
                String atmLatitude = devices.getJSONObject(searchNearest(streetNum, addresses)).getString("latitude");
                String atmLongitude = devices.getJSONObject(searchNearest(streetNum, addresses)).getString("longitude");

                sendMessage.setText("<b>Ближайший к Вам банкомат находится по адресу:</b> " + atmAddress + " (" + atmPlace + ")");
                sendLocation.setLatitude(Float.valueOf(atmLatitude)).setLongitude(Float.valueOf(atmLongitude));
            }
        } catch (Exception e) {
            return sendMessage.setText("\"<b>Не удалось найти банкоматы.</b>\"");
        }
        return null;
    }

    private int searchNearest(int value, List<Integer> houseNumbers) {
        int lastKey = 0;
        int lastDif = 0;
        int dif;
        for (int i : houseNumbers) {
            if (houseNumbers.get(i) == value) {
                return i;
            }
            dif = Math.abs(value - houseNumbers.get(i));
            if (lastKey == 0 || dif < lastDif) {
                lastKey = i;
                lastDif = dif;
            }
        }
        return lastKey;
    }
}
