package com.study.strzp.telegram.bot;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTestBot extends TelegramLongPollingBot {

    @Value("${telegram.botName}")
    String botName;

    @Value("${telegram.token}")
    String token;

    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        System.out.println(message);
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("I read this "+ message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Exception: " + e.toString());
        }
    }


    public String getBotUsername() {
        return botName;
    }

    public String getBotToken() {
        return token;
    }



}
