package com.study.strzp.telegram.bot.service.impl;

import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.*;

@Service
@Qualifier("atmService")
public class ATMServiceImpl implements CommandService {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public SendMessage handle(Update update) {
        SendMessage sendMessage = new SendMessage();
        MessageFormatter.addButtons(sendMessage);
        Location location = update.getMessage().getLocation();

        try {
            String res = restTemplate.getForObject(
                    "https://geocode-maps.yandex.ru/1.x/?geocode=" + location.getLongitude() + "," + location.getLongitude() + "&kind=house&format=json",
                    String.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
