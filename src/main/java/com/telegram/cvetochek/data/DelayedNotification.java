package com.telegram.cvetochek.data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedNotification implements Delayed {

    private final long nextNotification;


    public DelayedNotification(long nextNotification) {
        this.nextNotification = nextNotification;
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
