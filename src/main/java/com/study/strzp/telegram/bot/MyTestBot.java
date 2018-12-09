package com.study.strzp.telegram.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.Serializable;
import java.util.List;

public class MyTestBot extends TelegramLongPollingBot {

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
        return "luchshij_bot_dlja_sdachi_lab_bot";
    }

    public String getBotToken() {
        return "710382160:AAGbQ_Tqr5BfiI4tngnLxuYk04xlJ3Henr4";
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MyTestBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
