package com.cooker.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by thinhly on 7/15/16.
 */
public class Testing {
    public static void main(String[] args) {
        DateTime now = new DateTime();
        Date now2 = new Date();
        System.out.println(now.toString());
        System.out.println(now2.toString());
        System.out.println(System.currentTimeMillis());
    }
}
