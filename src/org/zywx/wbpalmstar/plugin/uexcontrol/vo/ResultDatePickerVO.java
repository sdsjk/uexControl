package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class ResultDatePickerVO extends ResultDatePickerBaseVO implements Serializable {
    private static final long serialVersionUID = -8960393639119042601L;
    private int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
