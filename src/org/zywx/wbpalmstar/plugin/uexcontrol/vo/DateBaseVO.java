package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class DateBaseVO implements Serializable{
    private static final long serialVersionUID = 2719592232118457361L;
    private String year;
    private String month;
    private String day;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getFormatYear(){
        return Integer.parseInt(year);
    }

    public int getFormatMonth(){
        return Integer.parseInt(month);
    }

    public int getFormatDay(){
        return Integer.parseInt(day);
    }
}
