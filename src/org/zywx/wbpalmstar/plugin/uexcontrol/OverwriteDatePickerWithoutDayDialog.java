package org.zywx.wbpalmstar.plugin.uexcontrol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

public class OverwriteDatePickerWithoutDayDialog extends DatePickerDialog {

    private OnDateSetListener mListener;

	public OverwriteDatePickerWithoutDayDialog(Context context,int style,
			OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		super(context,style, callBack, year, monthOfYear, dayOfMonth);
        this.mListener = callBack;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在打开dialog之前屏蔽键盘。
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	protected void onStop() {
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.setTitle(year + "-" + String.valueOf(month + 1));
	}

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == BUTTON_POSITIVE){
            if (mListener != null) {
                try {
                    DatePicker dp = getDatePicker();
                    if (dp != null) {
                        ViewGroup pickerView = (ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0);
                        int year = dp.getYear();
                        int month = dp.getMonth();
                        int day = dp.getDayOfMonth();
                        for (int i = 0; i < pickerView.getChildCount(); i++){
                            ViewGroup item = (ViewGroup) pickerView.getChildAt(i);
                            for (int j = 0; j < item.getChildCount(); j++){
                                View view = item.getChildAt(j);
                                if (view instanceof EditText){
                                    String content = ((EditText) view).getText().toString();
                                    switch (i){
                                        case 0://year
                                            year = Integer.parseInt(content);
                                            break;
                                        case 1://month
                                            month = Integer.parseInt(content) - 1;
                                            break;
                                        case 2://day
                                            day = Integer.parseInt(content);
                                            break;
                                    }
                                }
                            }
                        }
                        mListener.onDateSet(dp, year,month, day);
                    }
                } catch (Exception e) {
                    super.onClick(dialog, which);
                }
            }else {
                super.onClick(dialog, which);
            }
        }else{
            super.onClick(dialog, which);
        }
    }
}
