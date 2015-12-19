package org.zywx.wbpalmstar.plugin.uexcontrol;

public class JsConst {
    public static final int LIMIT_TYPE_ABSOLUTE = 0;
    public static final int LIMIT_TYPE_RELATIVE = 1;
    public static final String CALLBACK_OPEN_DATE_PICKER_WITH_CONFIG = "uexControl.cbOpenDatePickerWithConfig";
    public static final String ON_ERROR = "uexControl.onError";
    public static final int ERROR_RESULT_NO_PARAM = -1;//"no params!"
    public static final int ERROR_RESULT_MIN_DATE_ERROR = -2;//"min date params error!"
    public static final int ERROR_RESULT_MIN_DATE_RANGE_ERROR = -3;//"init date < min date exception!"
    public static final int ERROR_RESULT_MAX_DATE_ERROR = -4;//"max date params error!"
    public static final int ERROR_RESULT_MAX_DATE_RANGE_ERROR = -5;//"init date > max date exception!"
}
