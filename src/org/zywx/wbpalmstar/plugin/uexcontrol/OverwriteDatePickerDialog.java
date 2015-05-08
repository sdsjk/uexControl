package org.zywx.wbpalmstar.plugin.uexcontrol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

public class OverwriteDatePickerDialog extends DatePickerDialog {
	public OverwriteDatePickerDialog(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
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

}
