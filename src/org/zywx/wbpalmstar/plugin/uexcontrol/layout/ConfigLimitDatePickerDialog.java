package org.zywx.wbpalmstar.plugin.uexcontrol.layout;

import android.app.DatePickerDialog;
import android.content.Context;

import org.zywx.wbpalmstar.plugin.uexcontrol.EUExControl.ConfigOnDateSetListener;
import org.zywx.wbpalmstar.plugin.uexcontrol.OverwriteDatePickerDialog;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DateBaseVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DatePickerConfigVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.LimitDateVO;

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
            mDatePickerDialog.show();
        }
    }


}
