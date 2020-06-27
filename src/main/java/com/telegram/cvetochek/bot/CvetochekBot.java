package com.telegram.cvetochek.bot;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.telegram.cvetochek.data.DelayedNotification;
import com.telegram.cvetochek.service.HandlerSelector;
import com.telegram.cvetochek.service.NotificationQueueProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

@Slf4j
@Component
public class CvetochekBot extends TelegramLongPollingBot {

    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Value("${telegram.botName}")
    String botName;

    @Value("${telegram.token}")
    String token;

    @Autowired
    private HandlerSelector handlerSelector;

    @PostConstruct
    public void init(){
        System.out.println("x");
    }

    public void onUpdateReceived(Update update) {
        executorService.execute(() -> {
            Message message = update.getMessage();
            try {
                handlerSelector.select(message.getText()).handle(update, this);
            } catch (Exception e) {
                log.error("Error handling command.", e);
            }
        });
    }


    public String getBotUsername() {
        return botName;
    }

    public String getBotToken() {
        return token;
    }

}
