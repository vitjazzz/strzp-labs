package com.study.strzp.telegram.bot.service.impl;

import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Qualifier("currencyService")
public class CurrencyServiceImpl implements CommandService {

    public SendMessage handle(Update update) {

        String bucksOfficeSell = "";
        String bucksOfficeBuy = "";
        String euroOfficeSell = "";
        String euroOfficeBuy = "";
        String bucksOnlineSell = "";
        String bucksOnlineBuy = "";
        String euroOnlineSell = "";
        String euroOnlineBuy = "";


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

}
