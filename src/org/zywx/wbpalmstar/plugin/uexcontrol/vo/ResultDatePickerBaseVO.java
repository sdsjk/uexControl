package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class ResultDatePickerBaseVO extends DataBaseVO implements Serializable{
    private static final long serialVersionUID = -4669028553392515475L;
    private int year;
    private int month;

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
}
