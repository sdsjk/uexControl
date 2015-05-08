package org.zywx.wbpalmstar.plugin.uexcontrol;

import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.plugin.uexcontrol.InputDialog.OnInputFinishCallback;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class EUExControl extends EUExBase {
	public static final String CALLBACK_DATEPICKER = "uexControl.cbOpenDatePicker";
	public static final String CALLBACK_DATEPICKERWITHOUTDAY = "uexControl.cbOpenDatePickerWithoutDay";
	public static final String CALLBACK_TIMEPICKER = "uexControl.cbOpenTimePicker";
	public static final String CALLBACK_INPUT_COMPLETED = "uexControl.cbInputCompleted";
	public static final String CALLBACK_INPUTDIALOG = "uexControl.cbOpenInputDialog";
	public static final String CALLBACK_EDIT_COMPLETED = "uexControl.cbEditCompleted";

	public EUExControl(Context context, EBrowserView view) {
		super(context, view);
	}

	public void openDatePicker(String[] params) {
		int inYear, inMonth, inDay = 0;
		if (params.length == 3) {
			try {
				inYear = Integer.parseInt(params[0].trim());
				inMonth = Integer.parseInt(params[1].trim()) - 1;
				inDay = Integer.parseInt(params[2].trim());
				Log.i("date", "inYear1=" + inYear + ",inMonth1=" + inMonth
						+ ",inDay1=" + inDay);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				// 默认当前日期
				Calendar calendar = Calendar.getInstance();
				inYear = calendar.get(Calendar.YEAR);
				inMonth = calendar.get(Calendar.MONTH);
				inDay = calendar.get(Calendar.DAY_OF_MONTH);
			}
		} else {// 默认当前日期
			Calendar calendar = Calendar.getInstance();
			inYear = calendar.get(Calendar.YEAR);
			inMonth = calendar.get(Calendar.MONTH);
			inDay = calendar.get(Calendar.DAY_OF_MONTH);
		}
		Log.i("date", "inYear2=" + inYear + ",inMonth2=" + inMonth + ",inDay2="
				+ inDay);
		final int[] dateSet = new int[] { inYear, inMonth, inDay };
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {

				OverwriteDatePickerDialog datePickerDialog = new OverwriteDatePickerDialog(
						mContext, new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								Log.i("date", "year4=" + year + ",monthOfYear4="
										+ monthOfYear + ",dayOfMonth4="
										+ dayOfMonth);
								JSONObject jsonObject = new JSONObject();
								try {
									jsonObject
											.put(EUExCallback.F_JK_YEAR, year);
									jsonObject.put(EUExCallback.F_JK_MONTH,
											monthOfYear + 1);
									jsonObject.put(EUExCallback.F_JK_DAY,
											dayOfMonth);
									Log.i("jsonObject.toString()",
											jsonObject.toString() + "----");
									jsCallback(CALLBACK_DATEPICKER, 0,
											EUExCallback.F_C_JSON,
											jsonObject.toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

						}, dateSet[0], dateSet[1], dateSet[2]);
				datePickerDialog.setCancelable(true);
				datePickerDialog.show();
			}
		});
	}
	
	public void openDatePickerWithoutDay(String[] params) {
		int inYear, inMonth = 0;
		if (params.length == 2) {
			try {
				inYear = Integer.parseInt(params[0].trim());
				inMonth = Integer.parseInt(params[1].trim());
				Log.i("date", "inYear1=" + inYear + ",inMonth1=" + inMonth);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				// 默认当前日期
				Calendar calendar = Calendar.getInstance();
				inYear = calendar.get(Calendar.YEAR);
				inMonth = calendar.get(Calendar.MONTH);
			}
		} else {// 默认当前日期
			Calendar calendar = Calendar.getInstance();
			inYear = calendar.get(Calendar.YEAR);
			inMonth = calendar.get(Calendar.MONTH);
		}
		Log.i("date", "inYear2=" + inYear + ",inMonth2=" + inMonth);
		final int[] dateSet = new int[] { inYear, inMonth, 0 };
		((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				OverwriteDatePickerWithoutDayDialog datePickerWithoutDayDialog = new OverwriteDatePickerWithoutDayDialog(
						mContext, new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								Log.i("date", "year4=" + year
										+ ",monthOfYear4=" + monthOfYear
										+ ",dayOfMonth4=" + dayOfMonth);
								JSONObject jsonObject = new JSONObject();
								try {
									jsonObject
											.put(EUExCallback.F_JK_YEAR, year);
									jsonObject.put(EUExCallback.F_JK_MONTH,
											monthOfYear + 1);
									Log.i("jsonObject.toString()",
											jsonObject.toString() + "----");
									jsCallback(CALLBACK_DATEPICKERWITHOUTDAY,
											0, EUExCallback.F_C_JSON,
											jsonObject.toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, dateSet[0], dateSet[1], dateSet[2]);
				datePickerWithoutDayDialog.setCancelable(true);
				int year = dateSet[0], month = dateSet[1];
				if (month > 12) {
					year = year + month / 12;
					month = month % 12;
					if (month == 0) {
						month = 12;
						year = year - 1;
					}
				}
				if (year < 1900) {
					year = 1900;
					month = 01;
				}
				if (year > 2100) {
					year = 2100;
					month = 12;
				}
				datePickerWithoutDayDialog.setTitle(year + "-" + month);
				datePickerWithoutDayDialog.show();
				DatePicker dp = datePickerWithoutDayDialog.getDatePicker();
				if (dp != null) {
					((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
							.getChildAt(2).setVisibility(View.GONE);
				}
			}
		});
	}
	
	public void openTimePicker(String[] params) {
		int inHour, inMinute = 0;
		if (params.length == 2) {
			try {
				inHour = Integer.parseInt(params[0].trim());
				inMinute = Integer.parseInt(params[1].trim());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				// 默认当前时间
				Calendar calendar = Calendar.getInstance();
				inHour = calendar.get(Calendar.HOUR_OF_DAY);
				inMinute = calendar.get(Calendar.MINUTE);
			}
		} else {// 默认当前时间
			Calendar calendar = Calendar.getInstance();
			inHour = calendar.get(Calendar.HOUR_OF_DAY);
			inMinute = calendar.get(Calendar.MINUTE);
		}
		Log.i("time", "inHour=" + inHour + ",inMinute=" + inMinute);
		final int[] timeSet = new int[] { inHour, inMinute };
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {

				OverwriteTimePickerDialog timePickerDialog = new OverwriteTimePickerDialog(
						mContext, new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								JSONObject jsonObject = new JSONObject();
								try {
									jsonObject.put(EUExCallback.F_JK_HOUR,
											hourOfDay);
									jsonObject.put(EUExCallback.F_JK_MINUTE,
											minute);
									jsCallback(CALLBACK_TIMEPICKER, 0,
											EUExCallback.F_C_JSON,
											jsonObject.toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, timeSet[0], timeSet[1], true);
				timePickerDialog.setCancelable(true);
				timePickerDialog.show();
			}
		});
	}

	public void openInputDialog(String[] params) {
		if (params.length != 3) {
			return;
		}
		int inputType = InputDialog.INPUT_TYPE_NORMAL;
		try {
			inputType = Integer.parseInt(params[0].trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		final String inputHint = params[1];
		final String btnText = params[2];
		final int finalInputType = inputType;
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				InputDialog.show(mContext, finalInputType, inputHint, btnText,
						new OnInputFinishCallback() {
							@Override
							public void onInputFinish(InputDialog dialog) {
								jsCallback(CALLBACK_INPUT_COMPLETED, 0,
										EUExCallback.F_C_TEXT,
										dialog.getInputText());
								jsCallback(CALLBACK_INPUTDIALOG, 0,
                                        EUExCallback.F_C_TEXT,
                                        dialog.getInputText());
							}
						});
			}
		});

	}

	@Override
	protected boolean clean() {
		return false;
	}
}