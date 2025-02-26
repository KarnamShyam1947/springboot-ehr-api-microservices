package com.ehr.utils;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static String getClassName(Object o) {
        String completeClassName = o.getClass().getName();
        String[] split = completeClassName.split("[.]");
        int n = split.length;
        return split[n - 1];
    }

    public static Date getAddedDate(int time, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(value, time);
        return calendar.getTime();
    }

    public static String generateOTP() {
        SecureRandom sr = new SecureRandom();
        return String.valueOf(sr.nextInt(100000, 999999));
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom sr = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = sr.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
