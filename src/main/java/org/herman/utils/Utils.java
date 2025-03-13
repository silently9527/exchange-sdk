package org.herman.utils;

import java.util.Random;
import java.util.UUID;

public abstract class Utils {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void sleepSeconds(long time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int randomInt(int max) {
        return new Random().nextInt(max);
    }


}
