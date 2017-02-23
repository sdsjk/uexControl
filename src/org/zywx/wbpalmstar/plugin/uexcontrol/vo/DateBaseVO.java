package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class DateBaseVO implements Serializable{
    private static final long serialVersionUID = 2719592232118457361L;
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getFormatYear(){
        return year;
    }

    public int getFormatMonth(){
        return month;
    }

    public int getFormatDay(){
        return day;
    }
}
