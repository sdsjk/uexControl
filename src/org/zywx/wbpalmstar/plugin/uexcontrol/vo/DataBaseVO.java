package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class DataBaseVO implements Serializable{
    private static final long serialVersionUID = 3719238623468927566L;
    private String datePickerId;

    public String getDatePickerId() {
        return datePickerId;
    }

    public void setDatePickerId(String datePickerId) {
        this.datePickerId = datePickerId;
    }
}
