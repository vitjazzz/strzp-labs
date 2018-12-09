package com.study.strzp.telegram.bot;

import com.study.strzp.telegram.bot.service.CommandService;
import com.study.strzp.telegram.bot.service.DefaultMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTestBot extends TelegramLongPollingBot {

    @Autowired
    @Qualifier("startService")
    CommandService startService;

    @Autowired
    @Qualifier("currencyService")
    CommandService currencyService;

    @Autowired
    DefaultMessageHandler defaultMessageHandler;


    @Value("${telegram.botName}")
    String botName;

    @Value("${telegram.token}")
    String token;

    public void onUpdateReceived(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        Message message = update.getMessage();
        SendMessage replyMessage = null;
        if(callback != null){
            switch (callback.getData()) {
                case "/currency":
                    replyMessage = currencyService.handle(update);
                    break;
                default:
                    replyMessage = defaultMessageHandler.handle(callback.getFrom().getId().toString());
                    break;
            }
        } else if (message != null){
            switch (message.getText()) {
                case "/start":
                    replyMessage = startService.handle(update);
                    break;
                default:
                    replyMessage = defaultMessageHandler.handle(message.getChatId().toString());
                    break;
            }
        }
        try {
            execute(replyMessage);
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
