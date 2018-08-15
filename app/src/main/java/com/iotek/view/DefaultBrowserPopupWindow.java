package com.iotek.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.iotek.util.Constants;
import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class DefaultBrowserPopupWindow extends PopupWindow implements
		OnClickListener {

	private View mMenuView;

	private RadioButton mRbBaidu;
	private RadioButton mRbSougou;
	private RadioButton mRbTaobao;
	private RadioButton mRbYouku;
	private RadioButton mRbTianmao;
	private RadioButton mRb58;
	private RadioButton mRbSouhu;
	private RadioButton mRbQuna;
	private RadioButton mRbTengxun;

	private Button mBtnCancel;

	private LinearLayout mLayoutBaidu;
	private LinearLayout mLayoutSougou;
	private LinearLayout mLayoutTaobao;
	private LinearLayout mLayoutYouku;
	private LinearLayout mLayoutTengxun;
	private LinearLayout mLayoutSouhu;
	private LinearLayout mLayoutTianmao;
	private LinearLayout mLayout58;
	private LinearLayout mLayoutQuna;

	LayoutInflater mInflater;

	OnClickListener itemsOnClick;
	Activity context;
	String defaultBUrl;
	String newBUrl;

	public DefaultBrowserPopupWindow(final Activity context, String defaultBUrl) {
		super(context);
		this.context = context;
		this.defaultBUrl = defaultBUrl;
		this.newBUrl = defaultBUrl;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = mInflater.inflate(
				R.layout.popupwindow_default_main_browser, null);

		initView();
		initData(defaultBUrl);

		this.setContentView(mMenuView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);

		// 设置PopupWindow弹出窗体的高
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		int height = wm.getDefaultDisplay().getHeight();
		this.setHeight((int) (0.4 * height));
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置PopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xffffffff);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 设置下面的activity半透明
		Util.refreshBackground(0.7f, context);

	}

	private void initData(String browserUrl) {
		// TODO Auto-generated method stub
		if (browserUrl.equals(Constants.BAIDU)) {
			mRbBaidu.setChecked(true);
		} else if (browserUrl.equals(Constants.SOUGOU)) {
			mRbSougou.setChecked(true);
		} else if (browserUrl.equals(Constants.SOUHU)) {
			mRbSouhu.setChecked(true);
		} else if (browserUrl.equals(Constants.TAOBAO)) {
			mRbTaobao.setChecked(true);
		} else if (browserUrl.equals(Constants.TIANMAO)) {
			mRbTianmao.setChecked(true);
		} else if (browserUrl.equals(Constants.TENGXUN)) {
			mRbTengxun.setChecked(true);
		} else if (browserUrl.equals(Constants.XIECHENG)) {
			mRbQuna.setChecked(true);
		} else if (browserUrl.equals(Constants.TONGCHENG)) {
			mRb58.setChecked(true);
		} else if (browserUrl.equals(Constants.YOUKU)) {
			mRbYouku.setChecked(true);
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		mLayoutBaidu = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_baidu);
		mLayoutSougou = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_sougou);
		mLayoutTaobao = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_taobao);
		mLayoutYouku = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_youku);
		mLayoutTengxun = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_tengxun);
		mLayoutTianmao = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_tianmao);
		mLayout58 = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_58);
		mLayoutQuna = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_xiecheng);
		mLayoutSouhu = (LinearLayout) mMenuView
				.findViewById(R.id.default_browser_layout_souhu);

		mRbSougou = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_sougou);
		mRbBaidu = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_baidu);
		mRbTaobao = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_taobao);
		mRbYouku = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_youku);
		mRb58 = (RadioButton) mMenuView.findViewById(R.id.default_browser_58);
		mRbQuna = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_xiecheng);
		mRbTengxun = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_tengxun);
		mRbTianmao = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_tianmao);
		mRbSouhu = (RadioButton) mMenuView
				.findViewById(R.id.default_browser_souhu);

		mBtnCancel = (Button) mMenuView
				.findViewById(R.id.default_browser_cancle);

		mLayoutBaidu.setOnClickListener(this);
		mLayoutSougou.setOnClickListener(this);
		mLayoutTaobao.setOnClickListener(this);
		mLayoutYouku.setOnClickListener(this);
		mLayout58.setOnClickListener(this);
		mLayoutSouhu.setOnClickListener(this);
		mLayoutTengxun.setOnClickListener(this);
		mLayoutTianmao.setOnClickListener(this);
		mLayoutQuna.setOnClickListener(this);

		mBtnCancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.default_browser_layout_sougou:
			newBUrl = Constants.SOUGOU;
			break;
		case R.id.default_browser_layout_baidu:
			newBUrl = Constants.BAIDU;
			break;
		case R.id.default_browser_layout_youku:
			newBUrl = Constants.YOUKU;
			break;
		case R.id.default_browser_layout_taobao:
			newBUrl = Constants.TAOBAO;
			break;
		case R.id.default_browser_layout_souhu:
			newBUrl = Constants.SOUHU;
			break;
		case R.id.default_browser_layout_58:
			newBUrl = Constants.TONGCHENG;
			break;
		case R.id.default_browser_layout_tengxun:
			newBUrl = Constants.TENGXUN;
			break;
		case R.id.default_browser_layout_tianmao:
			newBUrl = Constants.TIANMAO;
			break;
		case R.id.default_browser_layout_xiecheng:
			newBUrl = Constants.XIECHENG;
			break;
		case R.id.default_browser_cancle:

			break;

		default:
			break;
		}
		DefaultBrowserPopupWindow.this.dismiss();

	}

	public String getNewBUrl() {
		return newBUrl;
	}
}
