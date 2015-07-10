package org.zywx.wbpalmstar.plugin.uexcontrol;

import java.io.IOException;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.base.ResoureFinder;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class InputDialog extends Dialog implements
		android.view.View.OnClickListener {

	public static final String TAG = "InputDialog";
	public static final int INPUT_TYPE_NORMAL = 0;
	public static final int INPUT_TYPE_DIGITAL = 1;
	public static final int INPUT_TYPE_EMAIL = 2;
	public static final int INPUT_TYPE_URL = 3;
	public static final int INPUT_TYPE_PWD = 4;
	public static final int MSG_ACTION_SHOW_SOFTINPUT = 1;
	public static final int MSG_ACTION_HIDE_SOFTINPUT = 2;

	private Button btnSend;
	private EditText etInput;
	
	private LinearLayout layout;
	
	private OnInputFinishCallback callback;
	private InputMethodManager imm = null;
	private ResoureFinder finder;
	private View mainView;
	
	public static DialogModel model;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_ACTION_SHOW_SOFTINPUT) {
				// 隐式打开软键盘，因为用户可能有物理键盘
				imm.showSoftInput(etInput, 0);
			}
		};
	};

	public InputDialog(Context context) {
		super(context, ResoureFinder.getInstance(context).getStyleId(
				"Style_platform_dialog"));
		finder = ResoureFinder.getInstance(context);
		init(context);
	}

	private void init(Context context) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.windowAnimations = finder
				.getStyleId("Anim_plugin_control_input_dialog");
		/* 让对话框居底，并且横向填满 */
		params.gravity = Gravity.BOTTOM | Gravity.FILL_HORIZONTAL;
		setContentView(finder.getLayoutId("plugin_control_input_dialog_main"));
		mainView = getWindow().getDecorView();
		btnSend = (Button) findViewById(finder
				.getId("plugin_control_input_button"));
		layout=(LinearLayout)findViewById(EUExUtil.getResIdID("layoutId"));
		if(model!=null){
			layout.setBackgroundColor(Color.parseColor(model.dialogBg));
		}
		btnSend.setOnClickListener(this);
		etInput = (EditText) findViewById(finder
				.getId("plugin_control_input_text"));
		imm = (InputMethodManager) getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
	}
	// 监听按钮事件
	@Override
	public void onClick(View v) {
		if (v == btnSend) {
			// 关闭输入法必须要在关闭Dialog之前调用，试这个破东西尼玛累死我了— —！
			// 之前是隐式的打开软键盘，现在隐式的关闭软键盘
			imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
			this.cancel();
			if (this.callback != null) {
				callback.onInputFinish(this);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 判断点击是否在对话框外围，是的话关闭对话框
			float y = event.getY();
			int topPx = mainView.getTop();
			if (y < topPx) {
				etInput.requestFocus();
				imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
				this.cancel();
				return true;
			} 
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 设置输入框输入类型
	 * 
	 * @param inputType
	 * 
	 * 
	 * 
	 */
	public void setInputType(int inputType) {
		switch (inputType) {
		case INPUT_TYPE_NORMAL:
			etInput.setInputType(EditorInfo.TYPE_CLASS_TEXT);
			break;
		case INPUT_TYPE_DIGITAL:
			etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
			break;
		case INPUT_TYPE_EMAIL:
			etInput.setInputType(EditorInfo.TYPE_CLASS_TEXT
					| EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			break;
		case INPUT_TYPE_URL:
			etInput.setInputType(EditorInfo.TYPE_CLASS_TEXT
					| EditorInfo.TYPE_TEXT_VARIATION_URI);
			break;
		case INPUT_TYPE_PWD:
			etInput.setInputType(EditorInfo.TYPE_CLASS_TEXT
					| EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
			break;
		}
	}

	/**
	 * 获得输入框的文字
	 * 
	 * @return
	 */
	public String getInputText() {
		return etInput.getText().toString();
	}

	/**
	 * 设置输入框提示文字
	 * 
	 * @param hint
	 */
	public void setInputHint(String hint) {
		etInput.setHint(hint);
	}

	@SuppressWarnings("deprecation")
	public void setInputBg(Context mContext) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(mContext.getAssets().open(
					model.dialogETBg));
			etInput.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置按钮显示文字
	 * 
	 * @param text
	 */
	public void setButtonText(String text) {
		btnSend.setText(text);
	}

	@SuppressWarnings("deprecation")
	public void setButtonBg(Context mContext) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(mContext.getAssets().open(
					model.dialogButBg));
			btnSend.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public EditText getEditText() {
		return etInput;
	}

	// 设置回调函数
	public void setCallback(OnInputFinishCallback callback) {
		this.callback = callback;
	}

	@Override
	public void show() {
		super.show();
		// etInput.requestFocus();
		// 显示输入法需要等UI绘制完成才能调用，通过发消息延迟调用显示输入法
		handler.sendEmptyMessage(MSG_ACTION_SHOW_SOFTINPUT);
	}

	/**
	 * 显示输入对话框
	 * 
	 * @param context
	 *            上下文
	 * @param inputHint
	 *            输入提示
	 * @param btnText
	 *            按钮文字
	 * @param inputType
	 *            输入类型
	 * @see InputDialog
	 */
	public static void show(Context context, int inputType, String inputHint,
			String btnText, OnInputFinishCallback callback, DialogModel dialogModel) {
		model=dialogModel;
		InputDialog inputDialog = new InputDialog(context);
		inputDialog.setInputType(inputType);
		inputDialog.setInputHint(inputHint);
		inputDialog.setButtonText(btnText);
		inputDialog.setCallback(callback);
		if(model!=null){
			inputDialog.setInputBg(context);
			inputDialog.setButtonBg(context);
		}
		inputDialog.show();
	}

	public static interface OnInputFinishCallback {
		void onInputFinish(InputDialog dialog);
	}
}
