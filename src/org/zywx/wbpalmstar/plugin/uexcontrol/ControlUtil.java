package org.zywx.wbpalmstar.plugin.uexcontrol;

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
        if (date.getFormatDay() < 1 ||
                date.getFormatMonth() < 1 || date.getFormatYear() < 1){
            return false;
        }
        return true;
    }

    public static boolean isRelativeDateValid(DateBaseVO date) {
//        if (Math.abs(date.getFormatDay()) > 30 || Math.abs(date.getFormatMonth()) > 11){
//            return false;
//        }
        return true;
    }

    public static long getRelativeDateNormal(DateBaseVO initDate, DateBaseVO date) {
        int convertYear = initDate.getFormatYear();
        int convertMonth = initDate.getFormatMonth();
        int convertDay = initDate.getFormatDay();
        int limitYear = date.getYear();
        int limitMonth = date.getMonth();
        int limitDay = date.getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.set(convertYear, convertMonth - 1, convertDay);
        if (limitDay!=0){
            calendar.add(Calendar.DAY_OF_MONTH, date.getFormatDay());
        }else if(limitMonth!=0){
            calendar.add(Calendar.MONTH, date.getFormatMonth());
        }else if (limitYear!=0){
            calendar.add(Calendar.YEAR, date.getFormatYear());
        }
        return calendar.getTimeInMillis();
    }
}
