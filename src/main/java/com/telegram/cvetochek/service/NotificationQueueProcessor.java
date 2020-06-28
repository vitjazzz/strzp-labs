package com.telegram.cvetochek.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.telegram.cvetochek.config.ComplimentsDictionary;
import com.telegram.cvetochek.data.DelayedNotification;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class NotificationQueueProcessor {

    @Autowired
    private AbsSender sender;

    @Autowired
    private ComplimentsDictionary complimentsDictionary;
    @Autowired
    private DelayCalculator delayCalculator;

    private ExecutorService processor = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder()
                    .setDaemon(false)
                    .setNameFormat("NotificationProcessor")
                    .build()
    );

    private final DelayQueue<DelayedNotification> notificationTimeQueue = new DelayQueue<>();


    @PostConstruct
    public void init() {
        processor.execute(() -> {
            while (true){
                DelayedNotification notification;
                try {
                    notification = notificationTimeQueue.take();
                } catch (InterruptedException e) {
                    log.error("Interrupted notification processor.", e);
                    return;
                }
                long nextNotification = delayCalculator.calculateNextNotification();
                log.info("Polled notification! Next notification should be at {}.",
                        new Date(nextNotification));
                notificationTimeQueue.add(
                        new DelayedNotification(nextNotification, notification.getUserId())
                );
                String compliment = EmojiParser.parseToUnicode(randomCompliment());
                try {
                    sender.execute(
                            new SendMessage()
                                    .setChatId(notification.getUserId())
                                    .setText(compliment)
                    );
                } catch (TelegramApiException e) {
                    log.error("Failed to send message.", e);
                }
            }
        });
    }

    private String randomCompliment(){
        Map<String, Set<String>> dictionary = complimentsDictionary.getDictionary();
        if (dictionary.isEmpty()) return "Oops...";
        Set<String> compliments = getRandomValue(dictionary.values());
        return compliments != null ? getRandomValue(compliments) : "Oops...";
    }

    private static <T> T getRandomValue(Collection<T> set){
        Random r = new Random();
        int randomIndex = r.nextInt(set.size());
        int index = 0;
        for (T value : set) {
            if (index++ == randomIndex){
                return value;
            }
        }
        return null;
    }

    public void addUser(String userId){
        notificationTimeQueue.add(new DelayedNotification(
                System.currentTimeMillis() + 5_000,
                userId
        ));
    }
}
