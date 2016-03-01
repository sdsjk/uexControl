package org.zywx.wbpalmstar.plugin.uexcontrol.layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import org.zywx.wbpalmstar.plugin.uexcontrol.EUExControl.ConfigOnDateSetListener;
import org.zywx.wbpalmstar.plugin.uexcontrol.OverwriteDatePickerDialog;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DateBaseVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DatePickerConfigVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.LimitDateVO;

import java.lang.reflect.Field;
import java.util.Date;

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

    public void showDatePicker() {
        if (mDatePickerDialog != null){
            mDatePickerDialog.setCancelable(true);
            mDatePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatePicker datePicker = mDatePickerDialog.getDatePicker();
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();
                    LimitDateVO minDate = mData.getMinDate();
                    LimitDateVO maxDate = mData.getMaxDate();
                    Date date = new Date(year, month, day);
                    //如果用户选择的日期不在设定的范围内，点击“确定”时会提示"日期超出范围，请重新选择"。同时对话框不会关闭
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
                    }
                }
            });

            mDatePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener (){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Field field = dialog.getClass().getSuperclass().getSuperclass().
                                getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible( true );
                        field.set(dialog, true );
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mDatePickerDialog.show();
        }
    }


}
