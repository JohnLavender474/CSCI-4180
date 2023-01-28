package com.csci4810.utils;

import java.util.Random;

public class UtilMethods {

    private static final Random RAND = new Random(System.currentTimeMillis());

    public static int getRand(int min, int max) {
        return RAND.nextInt(max - min) + min;
    }

}
