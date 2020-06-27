package com.telegram.cvetochek.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Qualifier("subscriptionHandler")
@Slf4j
public class SubscribeCommandHandler implements CommandHandler {

    @Autowired
    private NotificationQueueProcessor notificationQueueProcessor;

    public void handle(Update update, AbsSender sender) throws TelegramApiException {
        String userId = null;
        if (update.getMessage() != null && update.getMessage().getChatId() != null){
            userId = update.getMessage().getChatId().toString();
        } else if (update.getCallbackQuery() != null && update.getCallbackQuery().getFrom() != null) {
            userId = update.getCallbackQuery().getFrom().getId().toString();
        }

        if (userId == null){
            log.error("Failed to extract userId.");
            throw new RuntimeException("Failed to extract userId.");
        }
        notificationQueueProcessor.addUser(userId);
        sender.execute(
                new SendMessage()
                .setChatId(userId)
                .setText("Subscribed!")
        );
    }
}
