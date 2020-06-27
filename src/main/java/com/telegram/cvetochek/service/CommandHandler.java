package com.telegram.cvetochek.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface CommandHandler {
    void handle(Update update, AbsSender sender) throws TelegramApiException;
}
