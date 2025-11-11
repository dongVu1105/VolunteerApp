package com.dongVu1105.notification_service.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component

public class DateTimeFormatter {


    public String format (Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());

        if(elapseSeconds < 60){
            return elapseSeconds + " seconds ago";
        } else if (elapseSeconds < 60*60){
            long elapseMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
            return elapseMinutes + " minutes ago";
        } else if (elapseSeconds < 60*60*24){
            long elapseHours = ChronoUnit.HOURS.between(instant, Instant.now());
            return elapseHours +" hours ago";
        }
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }
}
