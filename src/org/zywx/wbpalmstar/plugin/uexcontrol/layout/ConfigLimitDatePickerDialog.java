package org.zywx.wbpalmstar.plugin.uexcontrol.layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.zywx.wbpalmstar.plugin.uexcontrol.EUExControl.ConfigOnDateSetListener;
import org.zywx.wbpalmstar.plugin.uexcontrol.OverwriteDatePickerDialog;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DateBaseVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DatePickerConfigVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.LimitDateVO;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigLimitDatePickerDialog{
    private Context mContext;
    private DatePickerConfigVO mData;
    private DatePickerDialog mDatePickerDialog;

    public ConfigLimitDatePickerDialog(Context context, DatePickerConfigVO data,
                                       ConfigOnDateSetListener listener) {
        this.mContext = context;
        this.mData = data;
        initView(listener);
    }

    private void initView(ConfigOnDateSetListener listener) {
        if (mData == null){
            return;
        }
        DateBaseVO initDate = mData.getInitDate();
        LimitDateVO minDate = mData.getMinDate();
        LimitDateVO maxDate = mData.getMaxDate();
        if (mData.isWithoutDay()){
            //暂时空余
        }else{
            mDatePickerDialog = new OverwriteDatePickerDialog(mContext, listener,
                    initDate.getFormatYear(), initDate.getFormatMonth() - 1,
                    initDate.getFormatDay());
            if (minDate != null && minDate.getData() != null){
                mDatePickerDialog.getDatePicker().setMinDate(minDate.getFormatDate());
            }
            if (maxDate != null  && maxDate.getData() != null){
                mDatePickerDialog.getDatePicker().setMaxDate(maxDate.getFormatDate());
            }
        }
    }

    public String transMonth(String monthStr) {
        if (monthStr.contains("月")) {
            monthStr = monthStr.replace('月', ' ').trim();
        }
        if (monthStr.equalsIgnoreCase("Jan") || monthStr.equals("一")) {
            return "1";
        }
        if (monthStr.equalsIgnoreCase("Feb") || monthStr.equals("二")) {
            return "2";
        }
        if (monthStr.equalsIgnoreCase("Mar") ||  monthStr.equals("三")) {
            return "3";
        }
        if (monthStr.equalsIgnoreCase("Apr") ||  monthStr.equals("四")) {
            return "4";
        }
        if (monthStr.equalsIgnoreCase("May") ||  monthStr.equals("五")) {
            return "5";
        }
        if (monthStr.equalsIgnoreCase("Jun") ||  monthStr.equals("六")) {
            return "6";
        }
        if (monthStr.equalsIgnoreCase("Jul") ||  monthStr.equals("七")) {
            return "7";
        }
        if (monthStr.equalsIgnoreCase("Aug") ||  monthStr.equals("八")) {
            return "8";
        }
        if (monthStr.equalsIgnoreCase("Sep") ||  monthStr.equals("九")) {
            return "9";
        }
        if (monthStr.equalsIgnoreCase("Oct") ||  monthStr.equals("十")) {
            return "10";
        }
        if (monthStr.equalsIgnoreCase("Nov") ||  monthStr.equals("十一")) {
            return "11";
        }
        if (monthStr.equalsIgnoreCase("Dec") ||  monthStr.equals("十二")) {
            return "12";
        }
        return monthStr;
    }
    public void showDatePicker(final ConfigOnDateSetListener listener) {
        boolean isChineseTemp = true;
        Locale local =  mContext.getResources().getConfiguration().locale;
        if (!local.getLanguage().endsWith("zh")) {
            isChineseTemp = false;
        }
        final boolean isChinese = isChineseTemp;
        if (mDatePickerDialog != null){
            mDatePickerDialog.setCancelable(true);

            mDatePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 某些手机手动输入时间后保存无效，需要释放焦点后才能正常保存
                    if (dialog instanceof DatePickerDialog) {
                        ((DatePickerDialog) dialog).getWindow().getDecorView().clearFocus();
                    }
                    DatePicker datePicker = mDatePickerDialog.getDatePicker();
                    int year = 0;
                    int month = 0;
                    int day = 0;
                    LimitDateVO minDate = mData.getMinDate();
                    LimitDateVO maxDate = mData.getMaxDate();
                    if (Build.VERSION.SDK_INT >= 21) {
                        System.out.println("Time:" + datePicker.getYear() + "  " + datePicker.getMonth() + "    " + datePicker.getDayOfMonth());
                        listener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        return;
                    }

                    ViewGroup pickerView = (ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0);

                    for (int i = 0; i < pickerView.getChildCount(); i++) {
                        ViewGroup item = (ViewGroup) pickerView.getChildAt(i);
                        for (int j = 0; j < item.getChildCount(); j++) {
                            View childView = item.getChildAt(j);
                            if (childView instanceof EditText) {
                                EditText editText = (EditText) childView;
                                String content = editText.getText().toString();
                                String reqEx = "[^0-9]";
                                Pattern p = Pattern.compile(reqEx);
                                Matcher m = p.matcher(editText.getText().toString());
                                String value = m.replaceAll("").trim();

                                if (!TextUtils.isEmpty(content)) {
                                    switch (i) {
                                        case 0://year
                                            if (isChinese) {
                                                year = Integer.parseInt(value);
                                            } else { //如果语言是英文，此处就是  月  了
                                                month = Integer.parseInt(value) - 1;
                                            }
                                            break;
                                        case 1://month
                                            if (isChinese) {
                                                month = Integer.parseInt(value) - 1;
                                            } else { //如果语言是英文，此处就是  日  了
                                                day = Integer.parseInt(value);
                                            }
                                            break;
                                        case 2://day
                                            if (isChinese) {
                                                day = Integer.parseInt(value);
                                            } else { //如果语言是英文，此处就是  年  了
                                                year = Integer.parseInt(value);
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    Date date = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    int dateVal = Integer.parseInt(sdf.format(new Date(date.getTime())));
                    int minDateVal = 0;
                    int maxDateVal = 0;
                    if (minDate != null) {
                        minDateVal = Integer.parseInt(sdf.format(new Date(minDate.getFormatDate())));
                    }
                    if (maxDate != null) {
                        maxDateVal = Integer.parseInt(sdf.format(new Date(maxDate.getFormatDate())));
                    }
                    //如果用户选择的日期不在设定的范围内，点击“确定”时会提示"日期超出范围，请重新选择"。同时对话框不会关闭
                    if ((minDate == null && dateVal > maxDateVal) ||
                            (maxDate == null && dateVal < minDateVal) ||
                            (minDate != null && maxDate != null && (dateVal > maxDateVal || dateVal < minDateVal) )) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getSuperclass().
                                    getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            // 将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, false);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mContext, "日期超出范围，请重新选择", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            Field field = dialog.getClass().getSuperclass().getSuperclass().
                                    getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            // 将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //回调onDateSet接口
                        listener.onDateSet(datePicker, year, month, day);
                    }
                }
            });

            mDatePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Field field = dialog.getClass().getSuperclass().getSuperclass().
                                getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //隐藏键盘
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mDatePickerDialog.getDatePicker().getWindowToken(), 0);
                }
            });
            mDatePickerDialog.show();
        }
    }


}
