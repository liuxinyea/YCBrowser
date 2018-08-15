package com.iotek.ycbrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.iotek.util.BrightnessTool;
import com.iotek.util.Constants;
import com.iotek.util.Util;
import com.iotek.view.DefaultBrowserPopupWindow;
import com.iotek.view.TextSizePopupWindow;

public class WebSettingActivity extends Activity {
	public static final int RUQUEST_LOCATION = 1;

	public final static String SETTINGS = "settings";

	TextView tv_home;// 显示主页名称 setting_web_home
	TextView tv_textsize;// 显示网页字体设置 setting_text_size

	ImageView img_setting_ad;// 拦截广告开关 iamge_setting_ad
	ImageView img_setting_pic;// 无图模式开关 iamge_setting_pic
	ImageView img_setting_full_screen;// 全屏模式开关iamge_setting_full_screen

	TextView tv_download_location;// 下载文件位置 setting_download_location

	ImageView img_setting_form;// 显示是否记住表单iamge_setting_form
	ImageView img_setting_js;
	SeekBar sb_brightness;// 调节屏幕亮度
	// 设置字体大小
	private int oldTextSize;
	private String textSizeName;
	private int newtextSize;
	private int textPercent;

	private String defaultUrl;// 默认主页
	private String urlName;

	private int sbProgress;// 亮度条进度

	private boolean ifHasPic;// 是否有图

	private String downloadLocation;// 默认下载位置

	private boolean isJavaScriptEnabled;// 是否支持JavaScript

	private boolean isNOAd;// 是否拦截广告

	private boolean isSaveForm;// 是否保存表单

	private boolean isFullScreen;// 是否全屏

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_setting);

		Intent intent = getIntent();
		if (intent != null) {
			int actionNumber = intent.getIntExtra("ActionNumber", 0);
			switch (actionNumber) {
			case 1:
				textPercent = intent.getIntExtra("TextSize", 100);
				defaultUrl = intent.getStringExtra("defaultUrl");
				ifHasPic = intent.getBooleanExtra("ifPic", true);
				downloadLocation = intent.getStringExtra("downloadLocation");
				isJavaScriptEnabled = intent.getBooleanExtra(
						"isJavaScriptEnabled", true);
				isNOAd = intent.getBooleanExtra("isNOAd", false);
				isSaveForm = intent.getBooleanExtra("isSaveForm", isSaveForm);
				isFullScreen = intent.getBooleanExtra("isFullScreen", false);
			default:
				break;
			}

		}

		initView();
		sb_brightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.d(SETTINGS, "sbProgress" + sbProgress);
				BrightnessTool.saveBrightness(getContentResolver(), sbProgress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (BrightnessTool.isAutoBrightness(WebSettingActivity.this
						.getContentResolver())) {

					BrightnessTool.stopAutoBrightness(WebSettingActivity.this);
					Log.d(SETTINGS,
							"stopAutoBrightness"
									+ BrightnessTool
											.isAutoBrightness(WebSettingActivity.this
													.getContentResolver()));
				}

				BrightnessTool.setBrightness(WebSettingActivity.this, progress);
				sbProgress = progress;
			}
		});

	}

	private void initView() {
		tv_home = (TextView) findViewById(R.id.setting_web_home);
		tv_textsize = (TextView) findViewById(R.id.setting_text_size);
		tv_download_location = (TextView) findViewById(R.id.setting_location);
		tv_download_location.setText(downloadLocation.substring(19));
		sb_brightness = (SeekBar) findViewById(R.id.setting_basic_brightness_sb);
		sb_brightness.setMax(255);
		sb_brightness.setProgress(BrightnessTool
				.getScreenBrightness(WebSettingActivity.this));
		img_setting_pic = (ImageView) findViewById(R.id.image_setting_pic);
		img_setting_js = (ImageView) findViewById(R.id.iamge_setting_javascript);
		img_setting_ad = (ImageView) findViewById(R.id.iamge_setting_ad);
		img_setting_form = (ImageView) findViewById(R.id.iamge_setting_form);
		img_setting_full_screen = (ImageView) findViewById(R.id.image_setting_full_screen);

		judgeOnOff(!ifHasPic, img_setting_pic);
		judgeOnOff(isJavaScriptEnabled, img_setting_js);
		judgeOnOff(isNOAd, img_setting_ad);
		judgeOnOff(isSaveForm, img_setting_form);
		judgeOnOff(isFullScreen, img_setting_full_screen);
		judgeOldText();
		judgeHomeBrowser(defaultUrl);

	}

	private void judgeOnOff(Boolean flag, ImageView imag) {
		// TODO Auto-generated method stub
		if (flag) {
			imag.setImageResource(R.drawable.setting_on);
		} else {
			imag.setImageResource(R.drawable.setting_off);
		}
	}

	/**
	 * 控件点击事件
	 * 
	 * @param v
	 */
	public void settingClickAction(View v) {
		switch (v.getId()) {
		case R.id.setting_back:// 返回键
			TurnToMainActivity();
			break;
		case R.id.setting_basic_home:// 设置默认主页
			// 从share中读取默认网页
			popDefaultBrowserWindow();
			break;
		case R.id.setting_basic_textsize:// 设置字体大小
			popTextSizeWindow();
			break;
		case R.id.setting_perfect_ad:// 设置拦截广告
			isNOAd = !isNOAd;
			judgeOnOff(isNOAd, img_setting_ad);
			break;

		case R.id.setting_perfect_pic:// 设置无图模式
			ifHasPic = !ifHasPic;
			judgeOnOff(!ifHasPic, img_setting_pic);
			break;
		case R.id.setting_perfect_full_screen:// 设置全屏式
			isFullScreen = !isFullScreen;
			judgeOnOff(isFullScreen, img_setting_full_screen);
			break;

		case R.id.setting_download_location:// 设置下载文件路径
			Intent selectLocationIntent = new Intent(WebSettingActivity.this,
					SelectDownloadLocationActivity.class);
			startActivityForResult(selectLocationIntent, RUQUEST_LOCATION);
			break;
		case R.id.setting_download_list:// 下载管理
			startActivity(new Intent(WebSettingActivity.this,
					DownloadActivity.class));
			break;
		case R.id.setting_senior_javascript:// 设置是否支持javascript
			isJavaScriptEnabled = !isJavaScriptEnabled;
			judgeOnOff(isJavaScriptEnabled, img_setting_js);
			break;
		case R.id.setting_senior_form:// 设置是否记住表单
			Util.displayToast(this, "设置是否记住密码");
			isSaveForm = !isSaveForm;
			judgeOnOff(isSaveForm, img_setting_form);
			break;
		case R.id.setting_senior_cleandata:// 清除数据
			Intent intent = new Intent(WebSettingActivity.this,
					ClearDataActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_recover:// 初始化默认设置
			makeSure();

			break;

		default:
			break;
		}
	}

	private void makeSure() {
		// TODO Auto-generated method stub
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				WebSettingActivity.this);
		dialog.setMessage(getString(R.string.dialog_init));
		dialog.setCancelable(false);
		dialog.setPositiveButton(getString(R.string.dialog_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						initialize();
					}
				});
		dialog.setNegativeButton(getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		defaultUrl = Constants.BAIDU;
		textPercent = 100;
		isNOAd = false;
		ifHasPic = true;
		downloadLocation = Constants.DOWNLOAD_DEFAULT_LOCATION;
		isJavaScriptEnabled = true;
		judgeHomeBrowser(defaultUrl);
		judgeOldText();
		judgeOnOff(!ifHasPic, img_setting_pic);
		judgeOnOff(isJavaScriptEnabled, img_setting_js);
		judgeOnOff(isNOAd, img_setting_ad);
		tv_download_location.setText(downloadLocation.substring(19));
	}

	private void TurnToMainActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(WebSettingActivity.this, MainActivity.class);
		intent.putExtra("newtextSize", textPercent);
		intent.putExtra("newBrowserUrl", defaultUrl);
		intent.putExtra("ifHasPic", ifHasPic);
		intent.putExtra("downloadLocation", downloadLocation);
		intent.putExtra("isJavaScriptEnabled", isJavaScriptEnabled);
		intent.putExtra("isNOAd", isNOAd);
		intent.putExtra("isSaveForm", isSaveForm);
		intent.putExtra("isFullScreen", isFullScreen);
		intent.putExtra("ActionNumber", 2);
		startActivity(intent);

	}

	private void judgeHomeBrowser(String browserUrl) {
		// TODO Auto-generated method stub

		if (browserUrl.equals(Constants.BAIDU)) {
			urlName = getString(R.string.default_browser_baidu);
		} else if (browserUrl.equals(Constants.SOUGOU)) {
			urlName = getString(R.string.default_browser_sougou);
		} else if (browserUrl.equals(Constants.SOUHU)) {
			urlName = getString(R.string.default_browser_souhu);
		} else if (browserUrl.equals(Constants.TAOBAO)) {
			urlName = getString(R.string.default_browser_taobao);
		} else if (browserUrl.equals(Constants.TIANMAO)) {
			urlName = getString(R.string.default_browser_tianmao);
		} else if (browserUrl.equals(Constants.TENGXUN)) {
			urlName = getString(R.string.default_browser_tengxun);
		} else if (browserUrl.equals(Constants.XIECHENG)) {
			urlName = getString(R.string.default_browser_xiecheng);
		} else if (browserUrl.equals(Constants.TONGCHENG)) {
			urlName = getString(R.string.default_browser_58);
		} else if (browserUrl.equals(Constants.YOUKU)) {
			urlName = getString(R.string.default_browser_youku);
		}
		tv_home.setText(urlName);

	}

	private void judgeOldText() {
		// TODO Auto-generated method stub
		Log.d(SETTINGS, "textPercent" + textPercent);
		switch (textPercent) {
		case 75:
			oldTextSize = Constants.SMALL;
			textSizeName = getString(R.string.textsize_small);
			break;
		case 100:
			oldTextSize = Constants.NOMAL;
			textSizeName = getString(R.string.textsize_nomal);
			break;
		case 125:
			oldTextSize = Constants.BIG;
			textSizeName = getString(R.string.textsize_big);
			break;
		case 150:
			oldTextSize = Constants.SUPERBIG;
			textSizeName = getString(R.string.textsize_super_big);
			Log.d(SETTINGS, "oldTextSize" + oldTextSize);
			break;

		default:
			break;
		}
		tv_textsize.setText(textSizeName);
	}

	TextSizePopupWindow mTextSizePopupWindow;

	private void popTextSizeWindow() {
		// TODO Auto-generated method stub
		// 实例化PopupWindow
		mTextSizePopupWindow = new TextSizePopupWindow(WebSettingActivity.this,
				oldTextSize);

		// 显示窗口

		mTextSizePopupWindow.showAtLocation(WebSettingActivity.this
				.findViewById(R.id.linearlayout_web_setting), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
		// 消失监听
		mTextSizePopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// 如果textSize改变，则重新写入
				newtextSize = mTextSizePopupWindow.getNewTextSize();
				if (newtextSize != oldTextSize) {
					switch (newtextSize) {
					case Constants.SMALL:
						textPercent = 75;
						break;
					case Constants.NOMAL:
						textPercent = 100;
						break;
					case Constants.BIG:
						textPercent = 125;
						break;
					case Constants.SUPERBIG:
						textPercent = 150;
						break;

					default:
						break;

					}

				}
				judgeOldText();

				Util.refreshBackground(1f, WebSettingActivity.this);
			}
		});
	}

	DefaultBrowserPopupWindow mDefaultBrowserPopupWindow;

	private void popDefaultBrowserWindow() {
		// TODO Auto-generated method stub
		// 实例化PopupWindow

		mDefaultBrowserPopupWindow = new DefaultBrowserPopupWindow(
				WebSettingActivity.this, defaultUrl);

		// 显示窗口

		mDefaultBrowserPopupWindow.showAtLocation(WebSettingActivity.this
				.findViewById(R.id.linearlayout_web_setting), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
		// 消失监听
		mDefaultBrowserPopupWindow
				.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						// TODO Auto-generated method stub
						defaultUrl = mDefaultBrowserPopupWindow.getNewBUrl();
						Util.displayToast(WebSettingActivity.this, String
								.valueOf(mDefaultBrowserPopupWindow
										.getNewBUrl()));
						judgeHomeBrowser(defaultUrl);

						Util.refreshBackground(1f, WebSettingActivity.this);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			TurnToMainActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RUQUEST_LOCATION:
			if (resultCode == RESULT_OK) {
				if (data.getBooleanExtra("isChange", false)) {
					downloadLocation = data.getStringExtra("location");
					tv_download_location
							.setText(downloadLocation.substring(19));
				}
			}
			break;

		default:
			break;
		}
	}
}
