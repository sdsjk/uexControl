package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class LimitDateVO implements Serializable{
    private static final long serialVersionUID = 2255474047555563874L;
    private int limitType;
    private DateBaseVO data;
    private long formatDate;

    public int getLimitType() {
        return limitType;
    }

    public void setLimitType(int limitType) {
        this.limitType = limitType;
    }

    public DateBaseVO getData() {
        return data;
    }

    public void setData(DateBaseVO data) {
        this.data = data;
    }

    public long getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(long formatDate) {
        this.formatDate = formatDate;
    }
}
