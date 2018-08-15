package com.iotek.ycbrowser;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.iotek.util.Util;

public class ClearDataActivity extends Activity implements OnClickListener {
	LinearLayout mLayoutCookie;
	LinearLayout mLayoutCache;
	LinearLayout mLayoutPwd;
	LinearLayout mLayoutDelete;

	RadioButton mRbCookie;
	RadioButton mRbCache;
	RadioButton mRbForm;

	Button mBtnBack;

	WebView webView;
	private static final String TAG = ClearDataActivity.class.getSimpleName();
	private static final String APP_CACAHE_DIRNAME = "/webcache";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_clear_data);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		webView = new WebView(ClearDataActivity.this);

		mLayoutCache = (LinearLayout) findViewById(R.id.clear_data_layout_cache);
		mLayoutCookie = (LinearLayout) findViewById(R.id.clear_data_layout_cookie);
		mLayoutPwd = (LinearLayout) findViewById(R.id.clear_data_layout_form);
		mLayoutDelete = (LinearLayout) findViewById(R.id.clear_data_layout_delte);
		mRbCache = (RadioButton) findViewById(R.id.clear_data_cache);
		mRbCookie = (RadioButton) findViewById(R.id.clear_data_cookie);
		mRbForm = (RadioButton) findViewById(R.id.clear_data_pwd);

		mBtnBack = (Button) findViewById(R.id.clear_data_back);

		mLayoutCache.setOnClickListener(ClearDataActivity.this);
		mLayoutCookie.setOnClickListener(ClearDataActivity.this);
		mLayoutPwd.setOnClickListener(ClearDataActivity.this);
		mLayoutDelete.setOnClickListener(ClearDataActivity.this);
		mBtnBack.setOnClickListener(ClearDataActivity.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.clear_data_layout_cache:
			mRbCache.setChecked(!mRbCache.isChecked());
			break;
		case R.id.clear_data_layout_cookie:
			mRbCookie.setChecked(!mRbCookie.isChecked());
			break;
		case R.id.clear_data_layout_form:
			mRbForm.setChecked(!mRbForm.isChecked());
			break;
		case R.id.clear_data_layout_delte:
			makeSure();
			break;
		case R.id.clear_data_back:
			finish();

			break;

		default:
			break;
		}
	}

	private void makeSure() {
		// TODO Auto-generated method stub
		if (mRbCache.isChecked() || mRbCookie.isChecked()
				|| mRbForm.isChecked()) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					ClearDataActivity.this);
			dialog.setMessage(getString(R.string.dialog_delete));
			dialog.setCancelable(false);
			dialog.setPositiveButton(getString(R.string.dialog_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (mRbCache.isChecked()) {
								// webView.clearCache(false);
								clearWebViewCache();
								Util.displayToast(
										ClearDataActivity.this,
										getString(R.string.dialog_delete_success));

							}
							if (mRbCookie.isChecked()) {
								CookieManager cookieManager = CookieManager
										.getInstance();
								cookieManager.removeAllCookie();

								if (!cookieManager.hasCookies()) {
									Util.displayToast(
											ClearDataActivity.this,
											getString(R.string.dialog_delete_success));
								}
							}
							if (mRbForm.isChecked()) {
								WebViewDatabase wVD = WebViewDatabase
										.getInstance(ClearDataActivity.this);
								if (wVD.hasFormData()) {
									wVD.clearFormData();
									if (!wVD.hasFormData()) {
										Util.displayToast(
												ClearDataActivity.this,
												getString(R.string.dialog_delete_success));

									}
								}

							}
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
		} else {
			Util.displayToast(ClearDataActivity.this,
					getString(R.string.dialog_null));
		}
	}

	/**
	 * 清除WebView缓存
	 */
	public void clearWebViewCache() {

		// 清理Webview缓存数据库
		try {
			deleteDatabase("webview.db");
			deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// WebView 缓存文件
		File appCacheDir = new File(getFilesDir().getAbsolutePath()
				+ APP_CACAHE_DIRNAME);
		Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

		File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
				+ "/webviewCache");
		Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

		// 删除webview 缓存目录
		if (webviewCacheDir.exists()) {
			deleteFile(webviewCacheDir);
		}
		// 删除webview 缓存 缓存目录
		if (appCacheDir.exists()) {
			deleteFile(appCacheDir);
		}
	}

	/**
	 * 递归删除 文件/文件夹
	 * 
	 * @param file
	 */
	public void deleteFile(File file) {

		Log.i(TAG, "delete file path=" + file.getAbsolutePath());

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();

		} else {
			Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
		}

	}

}
