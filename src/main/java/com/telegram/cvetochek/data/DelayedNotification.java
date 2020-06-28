package com.telegram.cvetochek.data;

import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
public class DelayedNotification implements Delayed {

    private final long nextNotification;
    private final String userId;
//    private final String previousKey;


    public DelayedNotification(long nextNotification, String userId) {
        this.nextNotification = nextNotification;
        this.userId = userId;
//        this.previousKey = previousKey;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = nextNotification - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.SECONDS) - o.getDelay(TimeUnit.SECONDS));
    }
}
