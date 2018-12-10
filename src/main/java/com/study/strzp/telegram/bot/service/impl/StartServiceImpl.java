package com.study.strzp.telegram.bot.service.impl;

import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Qualifier("startService")
public class StartServiceImpl implements CommandService {

    @Override
    public void handle(Update update, AbsSender sender) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        MessageFormatter.addButtons(sendMessage);
        sender.execute(sendMessage
                .setChatId(message.getChatId().toString())
                .enableMarkdown(true)
                .setText("Привет! Я умею показывать актуальный курс валют ПриватБанка. Выберите нужный пункт из списка.")
        );
    }
}