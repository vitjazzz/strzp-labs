package com.telegram.cvetochek.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.telegram.cvetochek.data.DelayedNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@Slf4j
public class NotificationQueueProcessor {

    @Autowired
    private AbsSender sender;

    private Set<String> subscribedUsers = new HashSet<>();

    private ExecutorService processor = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder()
                    .setDaemon(false)
                    .setNameFormat("NotificationProcessor")
                    .build()
    );

    private final DelayQueue<DelayedNotification> notificationTimeQueue = new DelayQueue<>();


    @PostConstruct
    public void init() {
        Random r = new Random();
        processor.execute(() -> {
            while (true){
                try {
                    notificationTimeQueue.take();
                } catch (InterruptedException e) {
                    log.error("Interrupted notification processor.", e);
                }
                long nextNotification = System.currentTimeMillis() + r.nextInt(100_000) + 10_000;
                log.info("Polled notification! Next notification should be at {}.",
                        new Date(nextNotification));
                notificationTimeQueue.add(new DelayedNotification(nextNotification));
                String userId = subscribedUsers.stream().findFirst().orElse(null);
                if (userId == null) continue;
                try {
                    sender.execute(
                            new SendMessage()
                                    .setChatId(userId)
                                    .setText("Hello there")
                    );
                } catch (TelegramApiException e) {
                    log.error("Failed to send message.", e);
                }
            }
        });
        notificationTimeQueue.add(new DelayedNotification(System.currentTimeMillis() + 5_000));
    }

    public void addUser(String userId){
        subscribedUsers.add(userId);
    }
}
