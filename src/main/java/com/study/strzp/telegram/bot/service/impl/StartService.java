package com.study.strzp.telegram.bot.service.impl;

import com.study.strzp.telegram.bot.service.CommandService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

@Service
@Qualifier("startService")
public class StartService implements CommandService {

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        List<List<InlineKeyboardButton>> replyButtons = Arrays.asList(
                Arrays.asList(
                        new InlineKeyboardButton("Курс валют")
                                .setCallbackData("/currency"),
                        new InlineKeyboardButton("Банкомати")
                                .setCallbackData("/atm")
                )
        );
        return new SendMessage()
                .setChatId(message.getChatId().toString())
                .enableMarkdown(true)
                .setText("Привет! Я умею показывать актуальный курс валют ПриватБанка. Выберите нужный пункт из списка.")
                .setReplyMarkup(new InlineKeyboardMarkup()
                        .setKeyboard(replyButtons)
                );
    }
}
