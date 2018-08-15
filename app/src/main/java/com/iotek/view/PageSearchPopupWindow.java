package com.iotek.view;

import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class PageSearchPopupWindow extends PopupWindow {

	private View mMenuView;

	private ImageView mImgback;
	private ImageView mImgforward;
	private EditText mEtInput;

	private Button mBtnCancle;

	LayoutInflater mInflater;

	OnClickListener itemsOnClick;
	Activity context;

	public PageSearchPopupWindow(final Activity context,
			OnClickListener itemsOnClick) {
		super(context);
		this.context = context;
		this.itemsOnClick = itemsOnClick;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = mInflater.inflate(R.layout.popupwindow_page_search, null);

		initView();

		this.setContentView(mMenuView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体的高
		//this.setHeight(LayoutParams.WRAP_CONTENT);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		int height = wm.getDefaultDisplay().getHeight();
		this.setHeight((int) (0.1 * height));
		
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置PopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimTop);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xffB0B0BB);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 设置下面的activity半透明
		// Util.refreshBackground(0.7f, context);

	}

	private void initView() {
		// TODO Auto-generated method stub
		mImgback = (ImageView) mMenuView.findViewById(R.id.page_search_back);
		mImgforward = (ImageView) mMenuView
				.findViewById(R.id.page_search_forward);
		mEtInput = (EditText) mMenuView.findViewById(R.id.page_search_input);
		mBtnCancle = (Button) mMenuView.findViewById(R.id.page_search_cancle);

		mImgback.setOnClickListener(itemsOnClick);
		mImgforward.setOnClickListener(itemsOnClick);

		mBtnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 销毁弹出框
				PageSearchPopupWindow.this.dismiss();
			}
		});
	}

	public String getKeyWords() {
		return mEtInput.getText().toString();
	}

	public EditText getEtInput() {
		return mEtInput;
	}
}
