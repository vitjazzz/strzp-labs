package com.study.strzp.telegram.bot.service.impl;

import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Qualifier("startService")
public class StartService implements CommandService {

    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        MessageFormatter.addButtons(sendMessage);
        return sendMessage
                .setChatId(message.getChatId().toString())
                .enableMarkdown(true)
                .setText("Привет! Я умею показывать актуальный курс валют ПриватБанка. Выберите нужный пункт из списка.");

    }
}
