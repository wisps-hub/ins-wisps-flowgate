package com.wisps.flowgate.common.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
    private static volatile long currentMillis;

    static {
        currentMillis = System.currentTimeMillis();
        Thread daemon = new Thread(() -> {
            while (true){
                currentMillis = System.currentTimeMillis();
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                }catch (Throwable ignored){}
            }
        });
        daemon.setDaemon(true);
        daemon.setName("common-fd-time-tick-thread");
        daemon.start();
    }

    public static long currentMillis(){
        return currentMillis;
    }
}
