package org.zywx.wbpalmstar.plugin.uexcontrol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

public class OverwriteDatePickerDialog extends DatePickerDialog {

    private OnDateSetListener mListener;

	public OverwriteDatePickerDialog(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
        this.mListener = callBack;
	}

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        //针对5.0以上系统，采用系统默认的ui.
        if (Build.VERSION.SDK_INT >= 21) {
            super.onDateChanged(view, year, month, day);
            return;
        }
        DatePicker dp = getDatePicker();
        if (dp != null) {
            ViewGroup pickerView = (ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0);
            for (int i = 0; i < pickerView.getChildCount(); i++){
                ViewGroup item = (ViewGroup) pickerView.getChildAt(i);
                for (int j = 0; j < item.getChildCount(); j++){
                    View childView = item.getChildAt(j);
                    if (childView instanceof EditText){
                        EditText editText = (EditText) childView;
                        String content = editText.getText().toString();
                        if (TextUtils.isEmpty(content)){
                            switch (i){
                                case 0://year
                                    editText.setText(String.valueOf(year));
                                    break;
                                case 1://month
                                    editText.setText(String.valueOf(month + 1));
                                    break;
                                case 2://day
                                    editText.setText(String.valueOf(day));
                                    break;
                            }
                        }
                    }
                }
            }
        }
        super.onDateChanged(view, year, month, day);
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
		// TODO Auto-generated method stub

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
