package com.study.strzp.telegram.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class DefaultMessageHandler {
    public SendMessage handle(String chatId) {
        return new SendMessage()
                .setChatId(chatId)
                .setText("Извините, я Вас не понимаю.");
    }
}
