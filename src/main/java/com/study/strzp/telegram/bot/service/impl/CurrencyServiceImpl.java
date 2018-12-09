package com.study.strzp.telegram.bot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.strzp.telegram.bot.entity.Currency;
import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Qualifier("currencyService")
public class CurrencyServiceImpl implements CommandService {

    private final RestTemplate restTemplate;

    public CurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SendMessage handle(Update update) {

        String bucksOfficeSell;
        String bucksOfficeBuy;
        String euroOfficeSell = "";
        String euroOfficeBuy = "";
        String bucksOnlineSell = "";
        String bucksOnlineBuy = "";
        String euroOnlineSell = "";
        String euroOnlineBuy = "";

        List<Currency> urlResponse = getUrlResponse("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5");

        bucksOfficeBuy = urlResponse.get(0).getBuy();
        bucksOfficeSell = urlResponse.get(0).getSale();


        SendMessage sendMessage = new SendMessage();
        MessageFormatter.addButtons(sendMessage);
        sendMessage.enableMarkdown(true);
        sendMessage.enableHtml(true);
        sendMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
        sendMessage.setText("Наличный курс ПриватБанка (в отделениях):\n" +
                " \n" +
                "<b>\uD83D\uDCB5USD (Доллар США)</b>\n" +
                "Продажа:  <b>" + bucksOfficeSell + " грн.</b>\n" +
                "Покупка:  <b>" + bucksOfficeBuy + " грн.</b>\n" +
                " \n" +
                "<b>\uD83D\uDCB6EUR (Евро)</b>\n" +
                "Продажа:  <b>" + euroOfficeSell + " грн.</b>\n" +
                "Покупка:  <b>" + euroOfficeBuy + " грн.</b>\n" +
                " \n" +
                "Безналичный курс ПриватБанка (конвертация по картам, Приват24, пополнение вкладов):\n" +
                " \n" +
                "<b>\uD83D\uDCB5USD (Доллар США)</b>\n" +
                "Продажа:  <b>" + bucksOnlineSell + " грн.</b>\n" +
                "Покупка:  <b>" + bucksOnlineBuy + " грн.</b>\n" +
                " \n" +
                "<b>\uD83D\uDCB6EUR (Евро)</b>\n" +
                "Продажа:  <b>" + euroOnlineSell + " грн.</b>\n" +
                "Покупка:  <b>" + euroOnlineBuy + " грн.</b>");

        return sendMessage;
    }


    private List<Currency> getUrlResponse(String url) {


        return Collections.singletonList(restTemplate.getForObject(url, Currency.class));

    }

}
