package com.study.strzp.telegram.bot.service.impl;

import com.study.strzp.telegram.bot.entity.Currency;
import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Double.parseDouble;
import static java.lang.String.format;

@Service
@Qualifier("currencyService")
public class CurrencyServiceImpl implements CommandService {

    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    public CurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SendMessage handle(Update update) {

        Currency[] officeExchange = getCurrenciesResponse("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5");
        Currency[] onlineExchange = getCurrenciesResponse("https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11");

        SendMessage sendMessage = new SendMessage();
        MessageFormatter.addButtons(sendMessage);
        sendMessage.enableMarkdown(true);
        sendMessage.enableHtml(true);
        sendMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
        sendMessage.setText("Наличный курс ПриватБанка (в отделениях):\n" +
                " \n" +
                "<b>\uD83D\uDCB5USD (Доллар США)</b>\n" +
                "Продажа:  <b>" + format("%.3f", parseDouble(officeExchange[0].getSale())) + " грн.</b>\n" +
                "Покупка:  <b>" + format("%.3f", parseDouble(officeExchange[0].getBuy())) + " грн.</b>\n" +
                " \n" +
                "<b>\uD83D\uDCB6EUR (Евро)</b>\n" +
                "Продажа:  <b>" + format("%.3f", parseDouble(officeExchange[1].getSale())) + " грн.</b>\n" +
                "Покупка:  <b>" + format("%.3f", parseDouble(officeExchange[1].getBuy())) + " грн.</b>\n" +
                " \n" +
                "Безналичный курс ПриватБанка (конвертация по картам, Приват24, пополнение вкладов):\n" +
                " \n" +
                "<b>\uD83D\uDCB5USD (Доллар США)</b>\n" +
                "Продажа:  <b>" + format("%.3f", parseDouble(onlineExchange[0].getSale())) + " грн.</b>\n" +
                "Покупка:  <b>" + format("%.3f", parseDouble(onlineExchange[0].getBuy())) + " грн.</b>\n" +
                " \n" +
                "<b>\uD83D\uDCB6EUR (Евро)</b>\n" +
                "Продажа:  <b>" + format("%.3f", parseDouble(onlineExchange[1].getSale())) + " грн.</b>\n" +
                "Покупка:  <b>" + format("%.3f", parseDouble(onlineExchange[1].getBuy())) + " грн.</b>");

        return sendMessage;
    }

    private Currency[] getCurrenciesResponse(String url) {
        return restTemplate.getForObject(url, Currency[].class);
    }
}
