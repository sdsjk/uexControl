package org.zywx.wbpalmstar.plugin.uexcontrol;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.plugin.uexcontrol.InputDialog.OnInputFinishCallback;
import org.zywx.wbpalmstar.plugin.uexcontrol.layout.ConfigLimitDatePickerDialog;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DateBaseVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.DatePickerConfigVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.ResultDatePickerBaseVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.ResultDatePickerVO;
import org.zywx.wbpalmstar.plugin.uexcontrol.vo.ResultOnErrorVO;

import java.util.Calendar;

public class EUExControl extends EUExBase {
    public static final String CALLBACK_DATEPICKER = "uexControl.cbOpenDatePicker";
    public static final String CALLBACK_DATEPICKERWITHOUTDAY = "uexControl.cbOpenDatePickerWithoutDay";
    public static final String CALLBACK_TIMEPICKER = "uexControl.cbOpenTimePicker";
    public static final String CALLBACK_INPUT_COMPLETED = "uexControl.cbInputCompleted";
    public static final String CALLBACK_INPUTDIALOG = "uexControl.cbOpenInputDialog";

    //回调函数id
    public String openDatePickerFuncId;
    public String openDatePickerWithoutDayFuncId;
    public String openTimePickerFuncId;
    public String openInputDialogFuncId;

    private String openDatePickerWithConfigFuncId;


    String parmBg = ""; // 设置背景参数

    public EUExControl(Context context, EBrowserView view) {
        super(context, view);
    }

    public void openDatePicker(String[] params) {
        int inYear, inMonth, inDay = 0;
        if (params.length >= 3) {
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
        if (params.length == 4) {
            openDatePickerFuncId = params[3];
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
                            if (null != openDatePickerFuncId) {
                                callbackToJs(Integer.parseInt(openDatePickerFuncId), false, jsonObject);
                            } else {
                                jsCallback(CALLBACK_DATEPICKER, 0, EUExCallback.F_C_JSON, jsonObject.toString());
                            }
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

    public void openDatePickerWithConfig(String[] params){
        ResultOnErrorVO resultOnErrorVO = new ResultOnErrorVO();
        if (params == null || params.length < 1){
            resultOnErrorVO.setErrorCode(JsConst.ERROR_RESULT_NO_PARAM);
            onErrorCallback(resultOnErrorVO);
            return;
        }
        if(params.length == 2) {
            openDatePickerWithConfigFuncId = params[1];
        }
        DateBaseVO initDate = new DateBaseVO();
        Calendar calendar = Calendar.getInstance();
        initDate.setYear(calendar.get(Calendar.YEAR));
        initDate.setMonth(calendar.get(Calendar.MONTH));
        initDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        String json = params[0];
        DatePickerConfigVO dataVO = DataHelper.gson.fromJson(json, DatePickerConfigVO.class);
        if (dataVO != null){
            if (dataVO.getInitDate() == null){
                dataVO.setInitDate(initDate);
            }
            if (dataVO.isWithoutDay()){

            }else{
                long initDateLong = ControlUtil.convertDateToLongNormal(dataVO.getInitDate());
                if (dataVO.getMinDate() != null && dataVO.getMinDate().getData() != null){
                    long minDateLong = 0l;
                    if (dataVO.getMinDate().getLimitType() == JsConst.LIMIT_TYPE_ABSOLUTE){
                        if (!ControlUtil.isAbsoluteDateValid(dataVO.getMinDate().getData())){
                            resultOnErrorVO.setErrorCode(JsConst.ERROR_RESULT_MIN_DATE_ERROR);
                            onErrorCallback(resultOnErrorVO);
                            return;
                        }
                        minDateLong = ControlUtil.convertDateToLongNormal(
                                dataVO.getMinDate().getData());
                    }else{
                        if (ControlUtil.isRelativeDateValid(dataVO.getMinDate().getData())){
                            minDateLong = ControlUtil.getRelativeDateNormal(dataVO.getInitDate(),
                                    dataVO.getMinDate().getData());
                        }
                    }
                    if (minDateLong != 0l){
                        if (minDateLong > initDateLong){
                            resultOnErrorVO.setErrorCode(JsConst.ERROR_RESULT_MIN_DATE_RANGE_ERROR);
                            onErrorCallback(resultOnErrorVO);
                            return;
                        }
                        dataVO.getMinDate().setFormatDate(minDateLong);
                    }
                }
                if (dataVO.getMaxDate() != null && dataVO.getMaxDate().getData() != null){
                    long maxDateLong = 0l;
                    if (dataVO.getMaxDate().getLimitType() == JsConst.LIMIT_TYPE_ABSOLUTE){
                        if (!ControlUtil.isAbsoluteDateValid(dataVO.getMaxDate().getData())){
                            resultOnErrorVO.setErrorCode(JsConst.ERROR_RESULT_MAX_DATE_ERROR);
                            onErrorCallback(resultOnErrorVO);
                            return;
                        }
                        maxDateLong = ControlUtil.convertDateToLongNormal(
                                dataVO.getMaxDate().getData());
                    }else{
                        if (ControlUtil.isRelativeDateValid(dataVO.getMaxDate().getData())){
                            maxDateLong = ControlUtil.getRelativeDateNormal(dataVO.getInitDate(),
                                    dataVO.getMaxDate().getData());
                        }
                    }
                    if (maxDateLong != 0l){
                        if (maxDateLong < initDateLong){
                            resultOnErrorVO.setErrorCode(JsConst.ERROR_RESULT_MAX_DATE_RANGE_ERROR);
                            onErrorCallback(resultOnErrorVO);
                            return;
                        }
                        dataVO.getMaxDate().setFormatDate(maxDateLong);
                    }
                }
            }
        }else{
            errorCallback(0,0,"error params!");
            return;
        }
        ConfigOnDateSetListener listener = new ConfigOnDateSetListener(dataVO.getDatePickerId(),
                dataVO.isWithoutDay());
        ConfigLimitDatePickerDialog datePickerDialog = new ConfigLimitDatePickerDialog(mContext,
                dataVO, listener);
        datePickerDialog.showDatePicker(listener);
    }

    private void onErrorCallback(ResultOnErrorVO errorVO) {
        callBackPluginJs(JsConst.ON_ERROR, DataHelper.gson.toJson(errorVO));
    }

    public class ConfigOnDateSetListener implements OnDateSetListener{

        private String id;
        private boolean withoutDay;

        public ConfigOnDateSetListener(String id, boolean withoutDay) {
            this.id = id;
            this.withoutDay = withoutDay;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ResultDatePickerBaseVO result;
            if (withoutDay){
                result = new ResultDatePickerBaseVO();
                result.setMonth(monthOfYear);
            }else{
                result = new ResultDatePickerVO();
                ((ResultDatePickerVO) result).setDay(dayOfMonth);
                result.setMonth(monthOfYear + 1);
            }
            result.setDatePickerId(id);
            result.setYear(year);

            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            if (null != openDatePickerWithConfigFuncId) {
                String str = DataHelper.gson.toJson(result);
                try {
                    JSONObject obj = new JSONObject(str);
                    callbackToJs(Integer.parseInt(openDatePickerWithConfigFuncId), false, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                callBackPluginJs(JsConst.CALLBACK_OPEN_DATE_PICKER_WITH_CONFIG,
                        DataHelper.gson.toJson(result));
            }
        }
    }
    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    public void openDatePickerWithoutDay(String[] params) {
        int inYear, inMonth = 0;
        if (params.length >= 2) {
            try {
                inYear = Integer.parseInt(params[0].trim());
                inMonth = Integer.parseInt(params[1].trim()) - 1;
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
        if (params.length == 3) {
            openDatePickerWithoutDayFuncId = params[2];
        }
        final int[] dateSet = new int[] { inYear, inMonth, 1 };
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
                            if (null != openDatePickerWithoutDayFuncId) {
                                callbackToJs(Integer.parseInt(openDatePickerWithoutDayFuncId), false, jsonObject);
                            } else {
                                jsCallback(CALLBACK_DATEPICKERWITHOUTDAY,
                                        0, EUExCallback.F_C_JSON,
                                        jsonObject.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, dateSet[0], dateSet[1], dateSet[2]);
                datePickerWithoutDayDialog.setCancelable(true);
                // init title
                datePickerWithoutDayDialog.setTitle(dateSet[0] + "-" + dateSet[1]);
                datePickerWithoutDayDialog.show();
                DatePicker dp = datePickerWithoutDayDialog.getDatePicker();
                if (dp != null) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                            .getChildAt(2).setVisibility(View.GONE);
                    // reset title
                    datePickerWithoutDayDialog.setTitle(dp.getYear() + "-" + (dp.getMonth() + 1));
                }
            }
        });
    }

    public void openTimePicker(String[] params) {
        int inHour, inMinute = 0;
        if (params.length >= 2) {
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
        if (params.length == 3) {
            openTimePickerFuncId = params[2];
        }
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

                            if (null != openTimePickerFuncId) {
                                callbackToJs(Integer.parseInt(openTimePickerFuncId), false, jsonObject);
                            } else {
                                jsCallback(CALLBACK_TIMEPICKER, 0,
                                        EUExCallback.F_C_JSON,
                                        jsonObject.toString());
                            }
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
        // if (params.length != 4) {
        // return;
        // }
        int inputType = InputDialog.INPUT_TYPE_NORMAL;
        try {
            inputType = Integer.parseInt(params[0].trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        final String inputHint = params[1];
        final String btnText = params[2];
        final int finalInputType = inputType;
        if (params.length >= 4) {
            try {
                String str = params[3]; //如果第四个参数传的是回调函数
                Integer.parseInt(str);
                openInputDialogFuncId = str;
            } catch (NumberFormatException e) {
                parmBg = params[3];
            }
            if (params.length == 5) {
                openInputDialogFuncId = params[4];
            }
            Log.i("openInputDialog", parmBg);
        }

        ((Activity) mContext).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                InputDialog.show(mContext, finalInputType, inputHint, btnText,
                        new OnInputFinishCallback() {
                            @Override
                            public void onInputFinish(InputDialog dialog) {
                                if(null != openInputDialogFuncId) {
                                    callbackToJs(Integer.parseInt(openInputDialogFuncId), false, dialog.getInputText());
                                } else {
                                    jsCallback(CALLBACK_INPUT_COMPLETED, 0,
                                            EUExCallback.F_C_TEXT,
                                            dialog.getInputText());
                                    jsCallback(CALLBACK_INPUTDIALOG, 0,
                                            EUExCallback.F_C_TEXT,
                                            dialog.getInputText());
                                }
                            }
                        }, getParm(parmBg));
            }
        });
    }

    public DialogModel getParm(String parm) {
        DialogModel model = null;
        if (!("".equals(parm))) {
            model = new DialogModel();
            try {
                JSONObject jsonObject = new JSONObject(parm);
                String dialogBg = jsonObject.optString("dialogBg", "");
                model.dialogBg = dialogBg;
                String dialogETBg = jsonObject.optString("dialogETBg", "");
                model.dialogETBg = getUrl(dialogETBg);
                String dialogButBg = jsonObject.optString("dialogButBg", "");
                model.dialogButBg = getUrl(dialogButBg);
                return model;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return model;
    }

    public String getUrl(String url) {
        String imgPath = BUtility.makeRealPath(
                BUtility.makeUrl(mBrwView.getCurrentUrl(), url),
                mBrwView.getCurrentWidget().m_widgetPath,
                mBrwView.getCurrentWidget().m_wgtType);
        Log.i("openInputDialog", "imgPath==" + imgPath);
        return imgPath;
    }

    @Override
    protected boolean clean() {
        return false;
    }
}