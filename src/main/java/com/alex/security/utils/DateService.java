package com.alex.security.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;



public class DateService {

    public static LocalDateTime getDateNow() {
        return LocalDateTime.now();
    }

    public static LocalDateTime addOneDayToDate(LocalDateTime dateTime) {
        return dateTime.plusDays(5);
    }

    public static Date addFiveMinutesToDate(LocalDateTime dateTime) {
        return convertToDate(dateTime.plusDays(5));
    }


    public static Date convertToDate(LocalDateTime localDateTime){
       return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }



}
