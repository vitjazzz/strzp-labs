package com.study.strzp.telegram.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class DefaultMessageHandler {
    public void handle(String chatId, AbsSender sender) throws TelegramApiException {
        sender.execute(
                new SendMessage()
                .setChatId(chatId)
                .setText("Извините, я Вас не понимаю.")
        );
    }
}
