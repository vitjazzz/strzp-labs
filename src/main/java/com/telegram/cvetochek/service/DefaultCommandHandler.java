package com.telegram.cvetochek.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Qualifier("defaultHandler")
public class DefaultCommandHandler implements CommandHandler {
    public void handle(Update update, AbsSender sender) throws TelegramApiException {
        sender.execute(
                new SendMessage()
                .setChatId(update.getMessage().getChatId().toString())
                .setText(":(")
        );
    }
}
