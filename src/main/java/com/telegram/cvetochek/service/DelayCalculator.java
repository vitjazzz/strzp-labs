package com.telegram.cvetochek.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DelayCalculator {
    private  static  final ZoneId zoneId = ZoneId.of("Europe/Kiev");

    public long calculateNextNotification(){
        LocalDateTime now = LocalDateTime.now(zoneId);
        Random random = ThreadLocalRandom.current();
        int nowHour = now.getHour();
        int randomValue = random.nextInt(10);
        int randomHour = random.nextInt(13) + 10;
        int randomMinute = random.nextInt(59) + 1;
        if (nowHour < 15 && nowHour > 7 && randomValue < 5) {
            int addHours = random.nextInt(20 - nowHour) + 3;
            return now.plusHours(addHours)
                    .atZone(zoneId).toEpochSecond() * 1000;
        } else if (randomValue < 5) {
            return now.plusDays(1)
                    .withHour(randomHour)
                    .withMinute(randomMinute).
                            atZone(zoneId).toEpochSecond() * 1000;

        } else if (randomValue < 8) {
            return now.plusDays(2)
                    .withHour(randomHour)
                    .withMinute(randomMinute).
                            atZone(zoneId).toEpochSecond() * 1000;
        } else {
            return now.plusDays(3)
                    .withHour(randomHour)
                    .withMinute(randomMinute).
                            atZone(zoneId).toEpochSecond() * 1000;
        }
    }
}
