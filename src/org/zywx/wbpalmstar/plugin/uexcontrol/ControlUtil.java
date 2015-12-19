package org.zywx.wbpalmstar.plugin.uexcontrol;

import android.text.TextUtils;

import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DateBaseVO;

import java.util.Calendar;

public class ControlUtil {

    public static long convertDateToLongNormal(DateBaseVO date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getFormatYear(), date.getFormatMonth() - 1,
                date.getFormatDay());
        return calendar.getTimeInMillis();
    }

    public static boolean isAbsoluteDateValid(DateBaseVO date){
        if (TextUtils.isEmpty(date.getMonth()) || TextUtils.isEmpty(date.getYear()) ||
                TextUtils.isEmpty(date.getDay()) || date.getFormatDay() < 1 ||
                date.getFormatMonth() < 1 || date.getFormatYear() < 1){
            return false;
        }
        return true;
    }

    public static boolean isRelativeDateValid(DateBaseVO date) {
        if (TextUtils.isEmpty(date.getMonth()) && TextUtils.isEmpty(date.getYear()) &&
                TextUtils.isEmpty(date.getDay())){
            return false;
        }
//        if (Math.abs(date.getFormatDay()) > 30 || Math.abs(date.getFormatMonth()) > 11){
//            return false;
//        }
        return true;
    }

    public static long getRelativeDateNormal(DateBaseVO initDate, DateBaseVO date) {
        int convertYear = initDate.getFormatYear();
        int convertMonth = initDate.getFormatMonth();
        int convertDay = initDate.getFormatDay();
        String limitYear = date.getYear();
        String limitMonth = date.getMonth();
        String limitDay = date.getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.set(convertYear, convertMonth - 1, convertDay);
        if (!TextUtils.isEmpty(limitDay)){
            calendar.add(Calendar.DAY_OF_MONTH, date.getFormatDay());
        }else if(!TextUtils.isEmpty(limitMonth)){
            calendar.add(Calendar.MONTH, date.getFormatMonth());
        }else if (!TextUtils.isEmpty(limitYear)){
            calendar.add(Calendar.YEAR, date.getFormatYear());
        }
        return calendar.getTimeInMillis();
    }
}
