package com.karag.speedometer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDatetime {
    private LocalDateTime localDateTime;

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public static final DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public CustomDatetime() {
    }

    public CustomDatetime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
    public String DatetimeToString(){
        return localDateTime.format(formatObj);
    }

    public LocalDateTime StringToDatetime(String dateString){
        setLocalDateTime(LocalDateTime.parse(dateString, formatObj));
        return getLocalDateTime();
    }
    public String getDate(){
        return localDateTime.getDayOfMonth()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getYear();
    }
    public String getTime(){
        return localDateTime.getHour()+":"+localDateTime.getMinute();
    }

}
