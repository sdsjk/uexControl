package org.zywx.wbpalmstar.plugin.uexcontrol.layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            return  monthStr.replace('月', ' ').trim();
        }
        if (monthStr.equalsIgnoreCase("Jan")) {
            return "1";
        }
        if (monthStr.equalsIgnoreCase("Feb")) {
            return "2";
        }
        if (monthStr.equalsIgnoreCase("Mar")) {
            return "3";
        }
        if (monthStr.equalsIgnoreCase("Apr")) {
            return "4";
        }
        if (monthStr.equalsIgnoreCase("May")) {
            return "5";
        }
        if (monthStr.equalsIgnoreCase("Jun")) {
            return "6";
        }
        if (monthStr.equalsIgnoreCase("Jul")) {
            return "7";
        }
        if (monthStr.equalsIgnoreCase("Aug")) {
            return "8";
        }
        if (monthStr.equalsIgnoreCase("Sep")) {
            return "9";
        }
        if (monthStr.equalsIgnoreCase("Oct")) {
            return "10";
        }
        if (monthStr.equalsIgnoreCase("Nov")) {
            return "11";
        }
        if (monthStr.equalsIgnoreCase("Dec")) {
            return "12";
        }
        return "1";
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
                    DatePicker datePicker = mDatePickerDialog.getDatePicker();
                    int year = 0;
                    int month = 0;
                    int day = 0;
                    LimitDateVO minDate = mData.getMinDate();
                    LimitDateVO maxDate = mData.getMaxDate();

                    ViewGroup pickerView = (ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0);
                    for (int i = 0; i < pickerView.getChildCount(); i++) {
                        ViewGroup item = (ViewGroup) pickerView.getChildAt(i);
                        for (int j = 0; j < item.getChildCount(); j++) {
                            View childView = item.getChildAt(j);
                            if (childView instanceof EditText) {
                                EditText editText = (EditText) childView;
                                String content = editText.getText().toString();
                                if (!TextUtils.isEmpty(content)) {
                                    switch (i) {
                                        case 0://year
                                            if (isChinese) {
                                                year = Integer.parseInt(editText.getText().toString());
                                            } else { //如果语言是英文，此处就是  月  了
                                                month = Integer.parseInt(transMonth(editText.getText().toString())) - 1;
                                            }
                                            break;
                                        case 1://month
                                            if (isChinese) {
                                                month = Integer.parseInt(transMonth(editText.getText().toString())) - 1;
                                            } else { //如果语言是英文，此处就是  日  了
                                                day = Integer.parseInt(editText.getText().toString());
                                            }
                                            break;
                                        case 2://day
                                            if (isChinese) {
                                                day = Integer.parseInt(editText.getText().toString());
                                            } else { //如果语言是英文，此处就是  年  了
                                                year = Integer.parseInt(editText.getText().toString());
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
                    //如果用户选择的日期不在设定的范围内，点击“确定”时会提示"日期超出范围，请重新选择"。同时对话框不会关闭
//                    Log.i("Date:", date.toString() + "     maxDate:" + new Date(maxDate.getFormatDate()).toString() + "   minDate:" + new Date(minDate.getFormatDate()).toString());
                    if (date.getTime() > maxDate.getFormatDate() || date.getTime() < minDate.getFormatDate()) {
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
                        return;
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
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mDatePickerDialog.getDatePicker().getWindowToken(), 0);
                }
            });
            mDatePickerDialog.show();
        }
    }


}
