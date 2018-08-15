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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.iotek.util.Constants;
import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class TextSizePopupWindow extends PopupWindow implements OnClickListener {

	private View mMenuView;

	private RadioButton mRbSmall;
	private RadioButton mRbNomal;
	private RadioButton mRbBig;
	private RadioButton mRbSuperBig;

	private Button mBtnCancel;

	private LinearLayout mLayoutSmall;
	private LinearLayout mLayoutNomal;
	private LinearLayout mLayoutBig;
	private LinearLayout mLayoutSuperBig;

	private Button mBtnCancle;
	private int newTextSize;
	private int oldTextSize;

	LayoutInflater mInflater;

	OnClickListener itemsOnClick;
	Activity context;

	public TextSizePopupWindow(final Activity context, int textSize) {
		super(context);
		this.context = context;
		this.oldTextSize = textSize;
		this.newTextSize = textSize;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = mInflater.inflate(R.layout.popupwindow_textsize, null);

		initView();
		initData(oldTextSize);

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

	private void initData(int textSize) {
		// TODO Auto-generated method stub
		// 设置图片 mGoodsImage
		switch (textSize) {
		case Constants.SMALL:
			mRbSmall.setChecked(true);
			break;
		case Constants.NOMAL:
			mRbNomal.setChecked(true);
			break;
		case Constants.BIG:
			mRbBig.setChecked(true);
			break;
		case Constants.SUPERBIG:
			mRbSuperBig.setChecked(true);
			break;

		default:
			break;
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		mLayoutSmall = (LinearLayout) mMenuView
				.findViewById(R.id.textsize_layout_small);
		mLayoutNomal = (LinearLayout) mMenuView
				.findViewById(R.id.textsize_layout_nomal);
		mLayoutBig = (LinearLayout) mMenuView
				.findViewById(R.id.textsize_layout_big);
		mLayoutSuperBig = (LinearLayout) mMenuView
				.findViewById(R.id.textsize_layout_super_big);

		mRbSmall = (RadioButton) mMenuView.findViewById(R.id.textsize_small);
		mRbNomal = (RadioButton) mMenuView.findViewById(R.id.textsize_nomal);
		mRbBig = (RadioButton) mMenuView.findViewById(R.id.textsize_big);
		mRbSuperBig = (RadioButton) mMenuView
				.findViewById(R.id.textsize_super_big);

		mBtnCancel = (Button) mMenuView.findViewById(R.id.textsize_cancle);

		mLayoutBig.setOnClickListener(this);
		mLayoutNomal.setOnClickListener(this);
		mLayoutSmall.setOnClickListener(this);
		mLayoutSuperBig.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textsize_layout_small:
			newTextSize = Constants.SMALL;
			break;
		case R.id.textsize_layout_nomal:
			newTextSize = Constants.NOMAL;
			break;
		case R.id.textsize_layout_big:
			newTextSize = Constants.BIG;
			break;
		case R.id.textsize_layout_super_big:
			newTextSize = Constants.SUPERBIG;
			break;
		case R.id.textsize_cancle:

			break;

		default:
			break;
		}
		TextSizePopupWindow.this.dismiss();

	}

	public int getNewTextSize() {
		return newTextSize;
	}
}
