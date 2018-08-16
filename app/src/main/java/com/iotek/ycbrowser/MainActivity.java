package com.iotek.ycbrowser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

import com.iotek.adapter.WebBasicAdapter;
import com.iotek.dao.DownloadNDoneDao;
import com.iotek.dao.HistoryDao;
import com.iotek.entity.DownloadItem;
import com.iotek.entity.HistoryInfo;
import com.iotek.fragment.MulWebVIewFragment;
import com.iotek.manager.DownloadManager;
import com.iotek.service.DownloadService;
import com.iotek.sharesdk.onekeyshare.OnekeyShare;
import com.iotek.util.Constants;
import com.iotek.util.IOUtils;
import com.iotek.util.Util;
import com.iotek.view.FunctionKeyPopupWindow;
import com.iotek.view.ImageBtn;
import com.iotek.view.ItemLongClickedPopWindow;
import com.iotek.view.MyWebView;
import com.iotek.view.PageSearchPopupWindow;
import com.iotek.view.ToolPopupWindow;
import com.iotek.view.WeblinkPopupWindow;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnItemClickListener {
	/**
	 * 检测网络状态变化
	 * 
	 * 
	 */
	private IntentFilter intentFilter;
	private NetworkChangeReceiver changeReceiver;

	//public String url = Constants.BAIDU;
	boolean firstClick;// 是否第一次按退出键
	TimeCount timeCount;// 连续两次按键退出计时器
	Timer timer;

	List<String> mUrlList = new LinkedList<String>();// 自定义回退栈
	int currentUrlPosition = 0;// 当前网页位置
	WebBasicAdapter mAdapter;

	private static final String APP_CACAHE_DIRNAME = "/webcache";
	WebSettings settings;

	// 多窗口变量
	SwipeRefreshLayout mRefreshLayout;
	private FrameLayout web_frameLayout;
	MyWebView mWebView;
	WebView webView;
	LinearLayout layout;
	FragmentManager manager;
	MulWebVIewFragment vIewWindow = new MulWebVIewFragment();
	private List<Bitmap> list_picture = new ArrayList<Bitmap>();
	private String title;
	private List<String> titles = new ArrayList<String>();
	int currentWeb = 0;

	LinearLayout search_bar;
	LinearLayout navigation_bar;

	Button btn_back;// 返回上一网页
	Button btn_next;// 进入下一网页
	Button btn_function;// 功能键
	Button btn_home;// 返回主页
	TextView tv_multiwindow;// 多窗口

	EditText keywords;// 输入页内关键字

	AutoCompleteTextView et_keywords;// 搜索关键字
	Button btn_search_cancel;// 搜索按钮
	// ProgressBar web_load_progress;// 网页加载进度

	FunctionKeyPopupWindow mFunctionKeyPopupWindow;// 功能键选择

	boolean isWebviewPageFinisihed = false;// 页面加载完成标志位
	boolean isHistoryAdded = false;// 历史记录添加与否
	Intent intentService;

	// 点击位置确定
	private int downX = 0;
	private int downY = 0;

	// 浏览器初始化设置
	int textPercent = 100;// 字体大小
	String defauleUrl = Constants.BAIDU;// 主页设置
	boolean ifHasPic = true;// 加载图片模式
	String downloadLocation = Constants.DOWNLOAD_DEFAULT_LOCATION;// 默认下载位置
	boolean isJavaScriptEnabled = true;// 是否支持javaScritpt
	boolean isNight = false;// 夜间模式
	boolean isNOAd = false;// 是否开启广告拦截标志位
	boolean isSaveForm = true;// 是否保存网页表单
	boolean isFullScreen = false;// 是否全屏

	boolean isReceivedTitle = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		/**
		 * 注册检测网络变化广播
		 */
		intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		changeReceiver = new NetworkChangeReceiver();
		registerReceiver(changeReceiver, intentFilter);

		/**
		 * 初始化智能搜索数据源
		 */
		Constants.SEARCHLIST.addAll(Util.searchData(MainActivity.this));

		firstClick = false;// 初始化第一次按Back为false
		timeCount = new TimeCount(2000, 1000);// 初始化back键计时器

		/**
		 * WebIconDatabase 即图标数据库管理对象，所有的WebView均请求相同的图标数据库对象
		 */
		WebIconDatabase.getInstance().open(
				Util.getDirs(getCacheDir().getAbsolutePath() + "/icons/"));

		initView();// 初始化界面元素
		defaultSetting();// 初始化设置参数
		initWebView();// WebView设置
		initData();//
	}

	protected void hideBar(Boolean isHide) {
		// TODO Auto-generated method stub
		if (isHide) {
			// 隐藏状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			// 隐藏导航栏
			View decorView = getWindow().getDecorView();
			int option2 = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(option2);
		} else {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	private void initWebView() {
		//View组件显示的内容可以通过cache机制保存为bitmap,
		webView.setDrawingCacheEnabled(true);
		//浏览器设置类
		settings = webView.getSettings();
		//提高渲染的优先级
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
		// 开启 DOM storage API 功能
		settings.setDomStorageEnabled(true);
		// 开启 database storage API 功能
		settings.setDatabaseEnabled(true);
		String cacheDirPath = getFilesDir().getAbsolutePath()
				+ APP_CACAHE_DIRNAME;
		// 设置数据库缓存路径
		settings.setDatabasePath(cacheDirPath);
		// 设置 Application Caches 缓存目录
		settings.setAppCachePath(cacheDirPath);
		// 开启 Application Caches 功能
		settings.setAppCacheEnabled(true);
		settings.setSupportZoom(true);
		settings.setTextZoom(textPercent);
		settings.setLoadsImagesAutomatically(ifHasPic);
		settings.setJavaScriptEnabled(true);
		settings.setSaveFormData(isSaveForm);
		hideBar(isFullScreen);

		webView.loadUrl(defauleUrl);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				final String tmpTitle = title;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						et_keywords.setText(tmpTitle);
					}
				});
				isReceivedTitle = true;

				// 添加历史记录
				String name = view.getTitle();
				String historyURL = view.getUrl();
				Bitmap icon;
				if (name == null) {

				} else {
					if (view.getFavicon() != null) {
						icon = view.getFavicon();
					} else {
						icon = BitmapFactory.decodeResource(getResources(),
								R.drawable.ic_launcher);// 空即为默认
					}

					HistoryInfo historyInfo = new HistoryInfo(name, historyURL,
							icon, Util.getStringDate());
					HistoryDao.insertHistory(MainActivity.this, historyInfo);
				}
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (isNight) {
					view.loadUrl(Constants.NIGHT_CSS);
					view.loadUrl("javascript:function()");
				} else {
					view.loadUrl(Constants.DAY_CSS);
					view.loadUrl("javascript:function()");
				}
				if (newProgress == 100) {
					// 隐藏进度条
					mRefreshLayout.setRefreshing(false);
				} else {
					if (!mRefreshLayout.isRefreshing())
						mRefreshLayout.setRefreshing(true);
				}

				// web_load_progress.setProgress(newProgress);
			}
		});

		// 下载监听：
		webView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				webView.goBack();
				// intent download activity
				if (!contentDisposition.equals("")) {
					startDownloadActivity(getApplicationContext(), url,
							IOUtils.getFileNameByContent(contentDisposition));
				} else {
					startDownloadActivity(getApplicationContext(), url, null);
				}

				// intent start service
				intentService = new Intent(getApplicationContext(),
						DownloadService.class);
				startService(intentService);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				mUrlList.add(url);
				currentUrlPosition = mUrlList.size() - 1;
				Log.d("wpx",
						"shouldOverrideUrlLoading currentUrlPosition-------->"
								+ currentUrlPosition);
				Log.d("wpx",
						"shouldOverrideUrlLoading mUrlList.size     -------->"
								+ mUrlList.size());
				view.loadUrl(url);// 在当前的webview中跳转到新的url
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				isReceivedTitle = false;

				btn_search_cancel.setBackground(getResources().getDrawable(
						R.drawable.web_search));
				isWebviewPageFinisihed = true;
				if (isJavaScriptEnabled) {
					if (isNight) {
						view.loadUrl(Constants.NIGHT_CSS);
						view.loadUrl("javascript:function()");
					} else {
						view.loadUrl(Constants.DAY_CSS);
						view.loadUrl("javascript:function()");
					}
				} else {

				}

				/*
				 * runOnUiThread(new Runnable() {
				 * 
				 * @Override public void run() {
				 * web_load_progress.setVisibility(ProgressBar.GONE);
				 * 
				 * } });
				 */

				// 更新历史记录的图标
				String title = view.getTitle();
				String historyURL = view.getUrl();
				Bitmap icon;
				if (title == null) {

				} else {
					if (view.getFavicon() != null) {
						icon = view.getFavicon();
					} else {
						icon = BitmapFactory.decodeResource(getResources(),
								R.drawable.ic_launcher);// 空即为默认
					}
					HistoryInfo historyInfo = new HistoryInfo(title,
							historyURL, icon, Util.getStringDate());
					HistoryDao.updateHistoryImage(MainActivity.this,
							historyInfo);
				}

			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				btn_search_cancel.setBackground(getResources().getDrawable(
						R.drawable.action_cancel));
				/*
				 * et_keywords.setText(getResources().getString(
				 * R.string.loading_info));
				 */
				// web_load_progress.setVisibility(ProgressBar.VISIBLE);
				isWebviewPageFinisihed = false;
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				url = url.toLowerCase();

				// TODO 广告拦截开启
				if (isNOAd) {
					if (!Util.hasAd(MainActivity.this, url)) {
						return super.shouldInterceptRequest(view, url);
					} else {
						return new WebResourceResponse(null, null, null);
					}
				} else {
					return super.shouldInterceptRequest(view, url);
				}
			}

		});

		// 通过onTouch获得长按事件的点击位置；onTouch在OnLongClick之前执行
		webView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				downX = (int) event.getX();
				downY = (int) event.getY();
				return false;
			}
		});
		// 给webview设置长按事件
		webView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				final WebView.HitTestResult result = ((WebView) v)
						.getHitTestResult();
				if (null == result) {
					return false;
				}
				int type = result.getType();
				if (type == WebView.HitTestResult.UNKNOWN_TYPE) {
					return false;
				}
				if (type == WebView.HitTestResult.EDIT_TEXT_TYPE) {
					// let TextViewhandles context menu
					return true;
				}
				final ItemLongClickedPopWindow itemLongClickedPopWindow = new ItemLongClickedPopWindow(
						MainActivity.this,
						ItemLongClickedPopWindow.IMAGE_VIEW_POPUPWINDOW);
				// Setup custom handlingdepending on the type
				switch (type) {
				case WebView.HitTestResult.PHONE_TYPE:
					// 处理拨号
					break;
				case WebView.HitTestResult.EMAIL_TYPE:
					// 处理Email
					break;
				case WebView.HitTestResult.GEO_TYPE:
					// 处理地图
					break;
				case WebView.HitTestResult.SRC_ANCHOR_TYPE:
					AlertDialog.Builder multiDia = new AlertDialog.Builder(
							MainActivity.this);
					multiDia.setTitle("提示信息");
					multiDia.setMessage(result.getExtra().toString());
					multiDia.setPositiveButton("复制",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
									cmb.setText(result.getExtra().toString());
									Toast.makeText(MainActivity.this,
											cmb.getText(), Toast.LENGTH_LONG)
											.show();
								}
							});
					multiDia.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					multiDia.create().show();
					break;
				case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
					// 带链接的图片类型
					break;
				case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
					String imgurl = result.getExtra();
					// 通过GestureDetector获取按下的位置，来定位PopWindow显示的位置//未使用

					itemLongClickedPopWindow.showAtLocation(v, Gravity.TOP
							| Gravity.LEFT, downX, downY + 145);
					break;
				default:
					break;
				}

				itemLongClickedPopWindow.getView(
						R.id.popupwindow_longclicked_showImage)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								itemLongClickedPopWindow.dismiss();
								// 显示图片
								webView.loadUrl(result.getExtra());
							}
						});
				itemLongClickedPopWindow.getView(
						R.id.popupwindow_longclicked_saveImage)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								itemLongClickedPopWindow.dismiss();
								// 下载图片:下载监听
								startDownloadActivity(MainActivity.this,
										result.getExtra(), null);
							}
						});
				itemLongClickedPopWindow.getView(
						R.id.popupwindow_longclicked_shareImage)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								itemLongClickedPopWindow.dismiss();
								// 分享图片
								showPicShare(result.getExtra());// TODO
																// 分享图片好像要有参数

							}

						});
				return true;
			}
		});

	}

	private void showPicShare(String url) {
		// TODO Auto-generated method stub
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		oks.setImageUrl(url);
		// 启动分享GUI
		oks.show(this);
	}

	private void defaultSetting() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("browser", MODE_PRIVATE);
		if (pref != null) {
			defauleUrl = pref.getString("defaultBrowser", Constants.BAIDU);
			textPercent = pref.getInt("textPercent", 100);
			ifHasPic = pref.getBoolean("ifHasPic", true);
			downloadLocation = pref.getString("downloadLocation",
					Constants.DOWNLOAD_DEFAULT_LOCATION);
			isJavaScriptEnabled = pref.getBoolean("isJavaScriptEnabled", true);
			isNOAd = pref.getBoolean("isNOAd", false);
			isSaveForm = pref.getBoolean("isSaveForm", true);
			isNight = pref.getBoolean("isNight", isNight);
			isFullScreen = pref.getBoolean("isFullScreen", false);
		}
	}

	private void initData() {
		web_frameLayout.addView(mWebView);

		mAdapter = new WebBasicAdapter(Constants.SEARCHLIST,
				getApplicationContext());
		et_keywords.setAdapter(mAdapter);
		et_keywords.setThreshold(1); // 设置输入一个字符 提示，默认为2
		et_keywords.setOnItemClickListener(this);
	}

	@Override
	public void onBackPressed() {
		timeCount.start();
		if (firstClick) {
			finish();
		} else {
			Util.displayToast(MainActivity.this,
					getResources().getString(R.string.back_tip));
		}
	}

	private void initView() {
		// web_load_progress = (ProgressBar)
		// findViewById(R.id.web_load_progress);
		search_bar = (LinearLayout) findViewById(R.id.layout_search_bar);
		navigation_bar = (LinearLayout) findViewById(R.id.layout_navigation_bar);

		btn_back = (Button) findViewById(R.id.web_back);
		btn_back.setOnClickListener(this);

		btn_next = (Button) findViewById(R.id.web_forward);
		btn_next.setOnClickListener(this);

		btn_function = (Button) findViewById(R.id.web_function_key);
		btn_function.setOnClickListener(this);

		btn_home = (Button) findViewById(R.id.web_home);
		btn_home.setOnClickListener(this);

		tv_multiwindow = (TextView) findViewById(R.id.web_multi_windows);
		tv_multiwindow.setOnClickListener(this);

		et_keywords = (AutoCompleteTextView) findViewById(R.id.search_keywords);

		btn_search_cancel = (Button) findViewById(R.id.search_cancel_action);
		btn_search_cancel.setOnClickListener(this);

		web_frameLayout = (FrameLayout) findViewById(R.id.framLayout_webView);
		layout = (LinearLayout) findViewById(R.id.webViewLayout);
		mWebView = new MyWebView(this);
		mRefreshLayout = mWebView.getSwipeRefreshLayout();
		webView = mWebView.getWebView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.web_back:
			if (webView.canGoBack()) {
				webView.goBack();
			} else if (currentUrlPosition > 0) {
				webView.loadUrl(mUrlList.get(currentUrlPosition - 1));
				currentUrlPosition -= 1;
			} else {
				Util.displayToast(this,
						getResources().getString(R.string.start_info));
			}
			break;
		case R.id.web_forward:
			if (webView.canGoForward()) {
				webView.goForward();
			} else if (currentUrlPosition < mUrlList.size() - 1) {
				webView.loadUrl(mUrlList.get(currentUrlPosition + 1));
				currentUrlPosition += 1;
			} else {
				Util.displayToast(this,
						getResources().getString(R.string.end_info));
			}
			break;
		case R.id.web_function_key:
			popFunctionKeyWindow();
			break;
		case R.id.web_home:
			webView.loadUrl(defauleUrl);
			break;
		case R.id.web_multi_windows:
			Bitmap bitmap = null;
			Bitmap smallBitmap = null;
			for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
				
				if (i == currentWeb) {// 每次对当前界面进行截图并添加到集合中
					Log.d("点击的当前页", currentWeb + "");
					MyWebView myWebView = (MyWebView) web_frameLayout
							.getChildAt(i);
					WebView view = myWebView.getWebView();
					if (view.getDrawingCache() != null) {
						view.destroyDrawingCache();
					}
					view.buildDrawingCache();
					title = view.getTitle();
					bitmap = view.getDrawingCache();
					Log.d("bitmap宽度", bitmap.getWidth() + "");
					smallBitmap = zoomBitMap(bitmap, bitmap.getWidth() / 1.5,
							bitmap.getHeight() / 1.5);
					titles.add(currentWeb, title);
					list_picture.add(currentWeb, smallBitmap);
				}
				
			}
			vIewWindow = new MulWebVIewFragment();
			manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.layout_main, vIewWindow);
			transaction.commit();
			break;
		case R.id.search_cancel_action:
			if (isWebviewPageFinisihed) {
				String url = et_keywords.getText().toString();
				if (url.contains("http://")||url.contains("https://")){

				}else{
					url="https://"+url;
				}
				if (TextUtils.isEmpty(url)) {
					et_keywords.setError(getResources().getString(
							R.string.search_url_none));
				} else {
					if (IsUrl(url)) {
						webView.loadUrl(url);
					} else {
						et_keywords.setError(getResources().getString(
								R.string.search_url_error));
					}
				}

			} else {
				webView.stopLoading();
			}
			break;

		default:
			break;
		}

	}

	private void popFunctionKeyWindow() {
		// TODO Auto-generated method stub
		// 实例化PopupWindow
		mFunctionKeyPopupWindow = new FunctionKeyPopupWindow(MainActivity.this,
				functionWindowOnClick);
		judgeIfPicture();
		judgeISNight();
		// 显示窗口

		mFunctionKeyPopupWindow.showAtLocation(
				MainActivity.this.findViewById(R.id.layout_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

	}

	private OnClickListener functionWindowOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.function_add_bookmark:

				/**
				 * 添加书签
				 */
				if (isWebviewPageFinisihed) {
					String title = webView.getTitle();
					String url = webView.getUrl();
					Bitmap icon;
					if (webView.getFavicon() != null) {
						icon = webView.getFavicon();
					} else {
						icon = BitmapFactory.decodeResource(getResources(),
								R.drawable.ic_launcher);// 假数据
					}
					Intent bookmarkAddIntent = new Intent(MainActivity.this,
							BookmarkAddActivity.class);
					bookmarkAddIntent.putExtra("name", title);
					bookmarkAddIntent.putExtra("url", url);
					bookmarkAddIntent.putExtra("image", icon);

					mFunctionKeyPopupWindow.dismiss();
					startActivity(bookmarkAddIntent);
				} else {
					Util.displayToast(MainActivity.this, getResources()
							.getString(R.string.add_bookmark_wait_toast));
				}

				break;
			case R.id.function_weblink:
				mFunctionKeyPopupWindow.dismiss();
				popWeblinkWindow();

				break;
			case R.id.function_bookmark_history:
				/**
				 * 书签历史记录按钮
				 */
				Intent bookmarkAndHistoryIntent = new Intent(MainActivity.this,
						BookmarkAndHistoryActivity.class);
				mFunctionKeyPopupWindow.dismiss();
				startActivity(bookmarkAndHistoryIntent);

				break;
			case R.id.function_download:
				mFunctionKeyPopupWindow.dismiss();
				startDownloadActivity(getApplicationContext(), null, null);
				break;
			case R.id.function_exit:
				finish();
				break;
			case R.id.function_night:
				mFunctionKeyPopupWindow.dismiss();
				if (isJavaScriptEnabled) {
					if (isNight) {
						isNight = false;
						for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
							MyWebView view = (MyWebView) web_frameLayout
									.getChildAt(i);
							view.getWebView().loadUrl(Constants.DAY_CSS);
							view.getWebView().loadUrl("javascript:function()");
						}
					} else {
						isNight = true;
						for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
							MyWebView view = (MyWebView) web_frameLayout
									.getChildAt(i);
							view.getWebView().loadUrl(Constants.NIGHT_CSS);
							view.getWebView().loadUrl("javascript:function()");
						}
						judgeISNight();
					}
				} else {
					AlertDialog.Builder multiDia = new AlertDialog.Builder(
							MainActivity.this);
					multiDia.setTitle("提示信息");
					multiDia.setMessage("浏览器未开启JavaScript功能，是否开启？。");
					multiDia.setPositiveButton("开启",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									isJavaScriptEnabled = true;
									for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
										MyWebView myWebView = (MyWebView) web_frameLayout.getChildAt(i);
										WebView view = myWebView.getWebView();
										WebSettings setting = view.getSettings();
										setting.setJavaScriptEnabled(isJavaScriptEnabled);
									}
									Log.d("aaa", "asa");
									isNight = !isNight;
									if (isNight) {

										// isNight = false;
										for (int i = 0; i < web_frameLayout
												.getChildCount(); i++) {
											MyWebView view = (MyWebView) web_frameLayout
													.getChildAt(i);

											view.getWebView().loadUrl(
													Constants.NIGHT_CSS);
											view.getWebView().loadUrl(
													"javascript:function()");
										}
									} else {
										// isNight = true;
										Log.d("ccc", "ccc");
										for (int i = 0; i < web_frameLayout
												.getChildCount(); i++) {
											MyWebView view = (MyWebView) web_frameLayout
													.getChildAt(i);
											view.getWebView().loadUrl(
													Constants.DAY_CSS);
											view.getWebView().loadUrl(
													"javascript:function()");
										}
										judgeISNight();
									}
								}
							});
					multiDia.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					multiDia.create().show();
				}
				break;
			case R.id.function_no_picture:
				// 判断是否无图模式
				ifHasPic = !ifHasPic;
				settings.setLoadsImagesAutomatically(ifHasPic);
				judgeIfPicture();
				mFunctionKeyPopupWindow.dismiss();
				break;
			case R.id.function_page_search:

				mFunctionKeyPopupWindow.dismiss();
				popPageSearchWindow();
				break;
			case R.id.function_refresh:
				mFunctionKeyPopupWindow.dismiss();
				webView.reload();
				break;
			case R.id.function_setup:
				mFunctionKeyPopupWindow.dismiss();
				turnToWebSetting();
				break;
			case R.id.function_share:
				mFunctionKeyPopupWindow.dismiss();
				showShare();
				break;
			case R.id.function_tool:
				mFunctionKeyPopupWindow.dismiss();
				popToolWindow();

				/*
				 * Intent intent = new Intent(MainActivity.this,
				 * CaptureActivity.class); startActivityForResult(intent, 1);
				 */
				break;
			default:
				break;
			}

		}

	};
	ToolPopupWindow mToolPopupWindow;

	/**
	 * 显示工具箱window 王雪
	 * 
	 */
	protected void popToolWindow() {
		// TODO Auto-generated method stub
		mToolPopupWindow = new ToolPopupWindow(MainActivity.this,
				toolWindowOnClick);

		// 显示窗口

		mToolPopupWindow.showAtLocation(
				MainActivity.this.findViewById(R.id.layout_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
		mToolPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				Util.refreshBackground(1f, MainActivity.this);

			}
		});
	}

	private OnClickListener toolWindowOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mToolPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.tool_cpoy_link:
				// 复制链接
				String url=webView.getUrl();
				ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				cmb.setText(url);
				Util.displayToast(MainActivity.this, getResources().getString(R.string.copy_tip));
				break;
			case R.id.tool_saoyisao:
				// 扫一扫
                 Intent intent=new Intent(MainActivity.this,CaptureActivity.class);
                 startActivityForResult(intent, 1);
				break;

			default:
				break;
			}
		}

	};

	PageSearchPopupWindow mPageSearchPopupWindow;

	/**
	 * 页内搜索显示窗口
	 */
	protected void popPageSearchWindow() {
		mPageSearchPopupWindow = new PageSearchPopupWindow(MainActivity.this,
				pageSearchOnClick);

		// 显示窗口
		// keywords = (EditText)
		// mPageSearchPopupWindow.getView().findViewById(R.id.page_search_input);
		mPageSearchPopupWindow.showAtLocation(
				MainActivity.this.findViewById(R.id.layout_main), Gravity.TOP
						| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
		mPageSearchPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				webView.clearMatches();
			}
		});

	}

	private OnClickListener pageSearchOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String pageKeyWords = mPageSearchPopupWindow.getKeyWords();
			switch (v.getId()) {
			case R.id.page_search_back:
				if (TextUtils.isEmpty(pageKeyWords)) {
					/*
					 * mPageSearchPopupWindow.getEtInput().setError(getResources
					 * ().getString( R.string.page_keywords));
					 */
					Util.displayToast(MainActivity.this, getResources()
							.getString(R.string.page_keywords));
				} else {
					webView.findAllAsync(pageKeyWords);
					webView.findNext(true);
				}
				break;
			case R.id.page_search_forward:
				if (TextUtils.isEmpty(pageKeyWords)) {
					/*
					 * mPageSearchPopupWindow.getEtInput().setError(getResources
					 * ().getString( R.string.page_keywords));
					 */
					Util.displayToast(MainActivity.this, getResources()
							.getString(R.string.page_keywords));
				} else {
					webView.findAllAsync(pageKeyWords);
					webView.findNext(false);
				}
				break;

			default:
				break;
			}

		}
	};

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(webView.getTitle());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(webView.getUrl());
		// text是分享文本，所有平台都需要这个字段
		oks.setText(webView.getUrl());
		// 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(webView.getUrl());
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("ShareSDK");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(webView.getUrl());

		// 启动分享GUI
		oks.show(this);
	}

	/**
	 * 判断是否无图模式
	 * 
	 * 王雪
	 */
	protected void judgeIfPicture() {
		// TODO Auto-generated method stub

		ImageBtn ibNoPicture = (ImageBtn) mFunctionKeyPopupWindow.getMenuView()
				.findViewById(R.id.function_no_picture);
		if (ifHasPic) {
			ibNoPicture.setImageResource(R.drawable.usercenter_menu_no_pic);
			ibNoPicture.setText(getString(R.string.function_no_picture));

		} else {
			ibNoPicture.setImageResource(R.drawable.reader_btn_sav);
			ibNoPicture.setText(getString(R.string.function_has_picture));

		}
	}

	private void judgeISNight() {
		ImageBtn ibIsNight = (ImageBtn) mFunctionKeyPopupWindow.getMenuView()
				.findViewById(R.id.function_night);
		if (isNight) {
			ibIsNight.setImageResource(R.drawable.icon_fine);
			ibIsNight.setText(getString(R.string.function_day));

		} else {
			ibIsNight.setImageResource(R.drawable.icon_fine_night);
			ibIsNight.setText(getString(R.string.function_night));

		}
	}

	protected void turnToWebSetting() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, WebSettingActivity.class);
		intent.putExtra("TextSize", textPercent);
		intent.putExtra("defaultUrl", defauleUrl);
		intent.putExtra("ifPic", ifHasPic);
		intent.putExtra("downloadLocation", downloadLocation);
		intent.putExtra("isJavaScriptEnabled", isJavaScriptEnabled);
		intent.putExtra("isNOAd", isNOAd);
		intent.putExtra("isSaveForm", isSaveForm);
		intent.putExtra("isFullScreen", isFullScreen);
		intent.putExtra("ActionNumber", 1);

		startActivity(intent);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub
		TextView title = (TextView) view.findViewById(R.id.web_basic_title);
		et_keywords.setText(title.getText().toString());
		TextView urlContent = (TextView) view.findViewById(R.id.web_basic_url);
		webView.loadUrl(urlContent.getText().toString());

	}

	WeblinkPopupWindow mWeblinkPopupWindow;

	protected void popWeblinkWindow() {
		mWeblinkPopupWindow = new WeblinkPopupWindow(MainActivity.this,
				weblinkWindowOnClick);

		// 显示窗口

		mWeblinkPopupWindow.showAtLocation(
				MainActivity.this.findViewById(R.id.layout_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
		mWeblinkPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				Util.refreshBackground(1f, MainActivity.this);

			}
		});
	}

	private OnClickListener weblinkWindowOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method
			String url  = Constants.BAIDU;
			switch (v.getId()) {
			case R.id.weblink_baidu:
				url = Constants.BAIDU;
				break;
			case R.id.weblink_fenghuang:
				url = Constants.FENGHUANG;
				break;

			case R.id.weblink_ganji:
				url = Constants.GANJI;
				break;

			case R.id.weblink_renren:
				url = Constants.RENREN;
				break;

			case R.id.weblink_souhu:
				url = Constants.SOUHU;
				break;
			case R.id.weblink_taobao:
				url = Constants.TAOBAO;
				break;
			case R.id.weblink_tengxun:
				url = Constants.TENGXUN;
				break;

			case R.id.weblink_tianya:
				url = Constants.TIANYA;
				break;

			case R.id.weblink_wangyi:
				url = Constants.WANGYI;
				break;

			case R.id.weblink_xinlang:
				url = Constants.XINLANG;
				break;

			default:
				break;
			}
			mWeblinkPopupWindow.dismiss();
			webView.loadUrl(url);
		}
	};

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		if (getIntent() != null) {
			int actionNumber = getIntent().getIntExtra("ActionNumber", 0);
			switch (actionNumber) {
			case 1:
				break;
			case 2:
				turnFromWebSetting();
				break;
			case 3:
				// 书签界面跳转，传值：被点击书签的URL
				webView.loadUrl(getIntent().getStringExtra("bookmarkURL"));
				break;
			case 4:
				// 历史记录界面跳转，传值：被点击历史记录的URL
				webView.loadUrl(getIntent().getStringExtra("historyURL"));
				break;
			default:

				break;
			}
		}
	}

	/**
	 * 从设置界面跳回的处理
	 * 
	 * 王雪
	 */
	private void turnFromWebSetting() {
		int tp = getIntent().getIntExtra("newtextSize", 100);
		String url = getIntent().getStringExtra("newBrowserUrl");
		Boolean ifP = getIntent().getBooleanExtra("ifHasPic", ifHasPic);
		String dl = getIntent().getStringExtra("downloadLocation");
		Boolean isJS = getIntent().getBooleanExtra("isJavaScriptEnabled",
				isJavaScriptEnabled);
		Boolean isAdNo = getIntent().getBooleanExtra("isNOAd", isNOAd);
		Boolean isSF = getIntent().getBooleanExtra("isSaveForm", isSaveForm);
		Boolean isFS = getIntent()
				.getBooleanExtra("isFullScreen", isFullScreen);
		if (tp != textPercent) {
			textPercent = tp;
			for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
				MyWebView myWebView = (MyWebView) web_frameLayout.getChildAt(i);
				WebView view = myWebView.getWebView();
				WebSettings setting = view.getSettings();
				setting.setTextZoom(textPercent);
			}
		}
		if (url != defauleUrl) {
			defauleUrl = url;
		}
		if (ifP != ifHasPic) {
			ifHasPic = ifP;
			for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
				MyWebView myWebView = (MyWebView) web_frameLayout.getChildAt(i);
				WebView view = myWebView.getWebView();
				WebSettings setting = view.getSettings();
				setting.setLoadsImagesAutomatically(ifHasPic);
			}
		}
		if (downloadLocation != dl) {
			downloadLocation = dl;
		}
		if (isJS != isJavaScriptEnabled) {
			isJavaScriptEnabled = isJS;
			for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
				MyWebView myWebView = (MyWebView) web_frameLayout.getChildAt(i);
				WebView view = myWebView.getWebView();
				WebSettings setting = view.getSettings();
				setting.setJavaScriptEnabled(isJavaScriptEnabled);
			}
		}
		if (isNOAd != isAdNo) {
			isNOAd = isAdNo;
		}
		if (isSaveForm != isSF) {
			isSaveForm = isSF;
			for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
				MyWebView myWebView = (MyWebView) web_frameLayout.getChildAt(i);
				WebView view = myWebView.getWebView();
				WebSettings setting = view.getSettings();
				setting.setSaveFormData(isSaveForm);
			}

		}
		if (isFullScreen != isFS) {
			isFullScreen = isFS;
			hideBar(isFullScreen);
		}

	}

	@Override
	protected void onRestart() {
		/*
		 * if (getIntent().hasExtra("bookmarkURL")) { Toast.makeText(this,
		 * getIntent().getStringExtra("bookmarkURL"),
		 * Toast.LENGTH_SHORT).show(); Log.d(Constants.LOG_TAG_BOOKMARK,
		 * getIntent().getStringExtra("bookmarkURL")); }
		 * Log.d(Constants.LOG_TAG_BOOKMARK, Constants.LOG_TAG_BOOKMARK);
		 * Log.d(Constants.LOG_TAG_BOOKMARK,
		 * getIntent().getStringExtra("bookmarkURL"));
		 */
		super.onRestart();
	}

	/**
	 * 自定义计时器，用于计算两次退出键按键间隔时间
	 * 
	 * @author Administrator
	 */
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			firstClick = true;
		}

		@Override
		public void onFinish() {
			firstClick = false;
		}
	}

	// 对截图所得的bitmap进行缩放操作
	public static Bitmap zoomBitMap(Bitmap bitmap, double d, double e) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) d / w);
		float scaleHeight = ((float) e / h);
		matrix.postScale(scaleWidth, scaleHeight); // 不改变原来图像大小
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public void deleteWebView(int deleteIndex) {
		// 删除指定位置的webVIew窗口和它原来的截图和标题
		list_picture.remove(deleteIndex);
		titles.remove(deleteIndex);
		Log.d("activity_list_bitmap", list_picture.size() + "");
		// 判断是不是都删除了
		if (web_frameLayout.getChildCount() == 1) {
			web_frameLayout.removeViewAt(deleteIndex);
			mWebView = new MyWebView(this);
			mRefreshLayout = mWebView.getSwipeRefreshLayout();
			webView = mWebView.getWebView();
			mRefreshLayout = mWebView.getSwipeRefreshLayout();
			initWebView();
			web_frameLayout.addView(mWebView);
			tv_multiwindow.setText(web_frameLayout.getChildCount() + "");
			this.show();
			vIewWindow.windowGone();
			currentWeb = 0;
		} else {
			web_frameLayout.removeViewAt(deleteIndex);
			// 因为每次删除后,list_web中所有的在被删除项之后的窗口都会向前移动一个位置，所以设置deleteIndex为可见就行
			if (deleteIndex == web_frameLayout.getChildCount()) {
				web_frameLayout.getChildAt(deleteIndex - 1).setVisibility(
						View.VISIBLE);
				currentWeb = deleteIndex - 1;
			} else {
				web_frameLayout.getChildAt(deleteIndex).setVisibility(
						View.VISIBLE);
				currentWeb = deleteIndex;
			}
			Log.d("currentWeb", currentWeb + "");
		}
	}

	public void shoeWebView(int showIndex) {
		// 除了被点击的网页之外的都设置为不可见，被点击的为可见
		for (int i = 0; i < web_frameLayout.getChildCount(); i++) {
			if (i == showIndex) {
				web_frameLayout.getChildAt(i).setVisibility(View.VISIBLE);
			} else {
				web_frameLayout.getChildAt(i).setVisibility(View.GONE);
			}
		}
		list_picture.remove(showIndex);
		titles.remove(showIndex);
		currentWeb = showIndex;// 当前网页的标志位
	}

	public void show() {
		Log.d("显示", list_picture.size() + "");
		layout.setVisibility(View.VISIBLE);
		tv_multiwindow.setText(String.valueOf(web_frameLayout.getChildCount()));
	}

	public void closeAllWeb() {// 关闭所有窗口并生成一个
		web_frameLayout.removeAllViews();
		mWebView = new MyWebView(this);
		mRefreshLayout = mWebView.getSwipeRefreshLayout();
		webView = mWebView.getWebView();
		mRefreshLayout = mWebView.getSwipeRefreshLayout();
		initWebView();
		web_frameLayout.addView(mWebView);
		tv_multiwindow.setText(String.valueOf(web_frameLayout.getChildCount()));
		list_picture.clear();
		titles.clear();
		currentWeb = 0;
		Log.d("全部删除", list_picture.size() + "");
	}

	public void addWebView() {// 每次添加把最新添加的设置为可见，更改当前网页的标志
		mWebView = new MyWebView(this);
		webView = mWebView.getWebView();
		mRefreshLayout = mWebView.getSwipeRefreshLayout();
		initWebView();
		//因为容器是framelayout,所以新添加的view会覆盖在最上层
		web_frameLayout.addView(mWebView);
		int num = web_frameLayout.getChildCount();
		currentWeb = web_frameLayout.getChildCount() - 1;
		tv_multiwindow.setText(num + "");
	}

	public void deleteCurrentBitmap() {
		// 每次返回删除接下来要显示的窗口的截图
		list_picture.remove(currentWeb);
	}

	public int getCurrentWeb() {
		return currentWeb;
	}

	public List<Bitmap> getBitmap() {
		// TODO Auto-generated method stub
		return list_picture;
	}

	public List<String> getWebTitle() {
		return titles;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				final String text = data.getStringExtra("result");
				AlertDialog.Builder multiDia = new AlertDialog.Builder(
						MainActivity.this);
				multiDia.setTitle("是否复制到剪切板");
				multiDia.setMessage(text);
				multiDia.setPositiveButton("复制",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String url=webView.getUrl();
								ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
								cmb.setText(text);
								Toast.makeText(MainActivity.this,
										cmb.getText(), Toast.LENGTH_LONG)
										.show();
							}
						});
				multiDia.setNegativeButton("打开链接",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								webView.loadUrl(text);
							}
						});
				multiDia.create().show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(changeReceiver);
		Log.d("wx", "onDestroy");
		SharedPreferences.Editor edit = getSharedPreferences("browser",
				MODE_PRIVATE).edit();
		edit.putString("defaultBrowser", defauleUrl);
		edit.putInt("textPercent", textPercent);
		edit.putBoolean("ifHasPic", ifHasPic);
		edit.putString("downloadLocation", downloadLocation);
		edit.putBoolean("isJavaScriptEnabled", isJavaScriptEnabled);
		edit.putBoolean("isNOAd", isNOAd);
		edit.putBoolean("isSaveForm", isSaveForm);
		edit.putBoolean("isNight", isNight);
		edit.putBoolean("isFullScreen", isFullScreen);
		edit.commit();

		// 程序结束 保存下载中的数据
		if (intentService != null) {
			Log.d("zy", "-------->stopService");
			stopService(intentService);
		}

		List<DownloadItem> downloadList = DownloadManager.getInstance()
				.getDownloadList();
		for (DownloadItem downloadItem : downloadList) {
			String url = downloadItem.getUrl();
			downloadItem.getRunnable().setStop(true);
			int npos = downloadItem.getRunnable().getCurrentDownloadPos();
			String name = downloadItem.getFileName();
			int type = downloadItem.getType();
			Log.d("zy", downloadItem.getRunnable().toString()+url + "下载停止的位置：" + String.valueOf(npos) + name + type);
			DownloadNDoneDao.InsertDownloadNC(this, url, npos, name, type);
		}
		DownloadManager.getInstance().chearAllDownloadItem();
	}

	/**
	 * 监听网络变化内部类
	 * 
	 * @author Administrator
	 */
	class NetworkChangeReceiver extends BroadcastReceiver {
		

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable()) {
				Util.displayToast(MainActivity.this,
						getResources().getString(R.string.net_ok));
			} else {
				Util.displayToast(MainActivity.this,
						getResources().getString(R.string.net_dismiss));
			}
		}
	}

	/**
	 * start DownloadActivity
	 * 
	 * @param context
	 * @param url
	 */
	private void startDownloadActivity(Context context, String url,
			String fileName) {
		
		
		
		Intent intentActivity = new Intent(context, DownloadActivity.class);
		if (url != null) {
			intentActivity.putExtra("url", url);
		}
		if (fileName != null) {
			intentActivity.putExtra("fileName", fileName);
		}
		startActivity(intentActivity);
		
	}

	/**
	 * 验证网址Url
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	private boolean IsUrl(String str) {
		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		return match(regex, str);
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
