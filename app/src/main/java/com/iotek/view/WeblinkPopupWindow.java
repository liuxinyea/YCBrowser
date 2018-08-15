package com.iotek.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class WeblinkPopupWindow extends PopupWindow {

	private View mMenuView;

	private ImageBtn mBtnWangyi;
	private ImageBtn mBtnGanji;
	private ImageBtn mBtnTengxun;
	private ImageBtn mBtnRenren;
	private ImageBtn mBtnSouhu;
	private ImageBtn mBtnTianya;
	private ImageBtn mBtnFenghuang;
	private ImageBtn mBtnTaobao;
	private ImageBtn mBtnBaidu;
	private ImageBtn mBtnXinlang;

	private Button mBtnCancle;

	LayoutInflater mInflater;

	OnClickListener itemsOnClick;
	Activity context;

	public WeblinkPopupWindow(final Activity context,
			OnClickListener itemsOnClick) {
		super(context);
		this.context = context;
		this.itemsOnClick = itemsOnClick;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = mInflater.inflate(R.layout.popupwindow_weblink, null);

		initView();
		addData();

		this.setContentView(mMenuView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
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

	private void addData() {
		// TODO Auto-generated method stub
		// 设置图片 mGoodsImage
		mBtnBaidu.setImageResource(R.drawable.home_mainpage_icon_baidu);
		mBtnBaidu.setText(context.getString(R.string.default_browser_baidu));
		mBtnFenghuang.setImageResource(R.drawable.home_mainpage_icon_ifeng);
		mBtnFenghuang.setText(context.getString(R.string.weblink_fenghuang));
		mBtnGanji.setImageResource(R.drawable.home_icon_ganji);
		mBtnGanji.setText(context.getString(R.string.weblink_ganji));
		mBtnRenren.setImageResource(R.drawable.home_icon_renren);
		mBtnRenren.setText(context.getString(R.string.weblink_renren));
		mBtnSouhu.setImageResource(R.drawable.home_icon_sohu);
		mBtnSouhu.setText(context.getString(R.string.default_browser_souhu));
		mBtnTaobao.setImageResource(R.drawable.home_mainpage_icon_taobao);
		mBtnTaobao.setText(context.getString(R.string.default_browser_taobao));
		mBtnTengxun.setImageResource(R.drawable.home_icon_qq);
		mBtnTengxun
				.setText(context.getString(R.string.default_browser_tengxun));
		mBtnTianya.setImageResource(R.drawable.home_icon_tianya);
		mBtnTianya.setText(context.getString(R.string.weblink_tianya));
		mBtnWangyi.setImageResource(R.drawable.home_icon_163);
		mBtnWangyi.setText(context.getString(R.string.weblink_wangyi));
		mBtnXinlang.setImageResource(R.drawable.home_mainpage_icon_sina);
		mBtnXinlang.setText(context.getString(R.string.weblink_xinlang));

	}

	private void initView() {
		// TODO Auto-generated method stub
		mBtnBaidu = (ImageBtn) mMenuView.findViewById(R.id.weblink_baidu);
		mBtnFenghuang = (ImageBtn) mMenuView
				.findViewById(R.id.weblink_fenghuang);
		mBtnGanji = (ImageBtn) mMenuView.findViewById(R.id.weblink_ganji);
		mBtnRenren = (ImageBtn) mMenuView.findViewById(R.id.weblink_renren);
		mBtnSouhu = (ImageBtn) mMenuView.findViewById(R.id.weblink_souhu);
		mBtnTaobao = (ImageBtn) mMenuView.findViewById(R.id.weblink_taobao);
		mBtnTengxun = (ImageBtn) mMenuView.findViewById(R.id.weblink_tengxun);
		mBtnTianya = (ImageBtn) mMenuView.findViewById(R.id.weblink_tianya);
		mBtnWangyi = (ImageBtn) mMenuView.findViewById(R.id.weblink_wangyi);
		mBtnXinlang = (ImageBtn) mMenuView.findViewById(R.id.weblink_xinlang);

		mBtnCancle = (Button) mMenuView.findViewById(R.id.weblink_cancle);

		mBtnBaidu.setOnClickListener(itemsOnClick);
		mBtnFenghuang.setOnClickListener(itemsOnClick);
		mBtnGanji.setOnClickListener(itemsOnClick);
		mBtnRenren.setOnClickListener(itemsOnClick);
		mBtnSouhu.setOnClickListener(itemsOnClick);
		mBtnTaobao.setOnClickListener(itemsOnClick);
		mBtnTengxun.setOnClickListener(itemsOnClick);
		mBtnTianya.setOnClickListener(itemsOnClick);
		mBtnWangyi.setOnClickListener(itemsOnClick);
		mBtnXinlang.setOnClickListener(itemsOnClick);

		mBtnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 销毁弹出框
				WeblinkPopupWindow.this.dismiss();
			}
		});
	}

}
