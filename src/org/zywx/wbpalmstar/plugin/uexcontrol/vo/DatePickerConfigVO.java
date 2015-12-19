package org.zywx.wbpalmstar.plugin.uexcontrol.vo;

import java.io.Serializable;

public class DatePickerConfigVO extends DataBaseVO implements Serializable{
    private static final long serialVersionUID = -7688164916165226584L;
    private boolean withoutDay;
    private DateBaseVO initDate;
    private LimitDateVO minDate;
    private LimitDateVO maxDate;

    public DateBaseVO getInitDate() {
        return initDate;
    }

    public void setInitDate(DateBaseVO initDate) {
        this.initDate = initDate;
    }

    public LimitDateVO getMinDate() {
        return minDate;
    }

    public void setMinDate(LimitDateVO minDate) {
        this.minDate = minDate;
    }

    public LimitDateVO getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LimitDateVO maxDate) {
        this.maxDate = maxDate;
    }

    public boolean isWithoutDay() {
        return withoutDay;
    }

    public void setWithoutDay(boolean withoutDay) {
        this.withoutDay = withoutDay;
    }
}
