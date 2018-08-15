package com.iotek.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class ToolPopupWindow extends PopupWindow {

	private View mMenuView;

	private ImageBtn mIbCopyLink;
	private ImageBtn mIbSaoYiSao;

	LayoutInflater mInflater;

	OnClickListener itemsOnClick;
	Activity context;

	public ToolPopupWindow(final Activity context, OnClickListener itemsOnClick) {
		super(context);
		this.context = context;
		this.itemsOnClick = itemsOnClick;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = mInflater.inflate(R.layout.popupwindow_tool, null);

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
		mIbCopyLink.setImageResource(R.drawable.infoflow_menu_copy);
		mIbCopyLink.setText(context.getString(R.string.tool_copy_link));
		mIbSaoYiSao.setImageResource(R.drawable.scan_barcode_black);
		mIbSaoYiSao.setText(context.getString(R.string.tool_saoyisao));

	}

	private void initView() {
		// TODO Auto-generated method stub

		mIbCopyLink = (ImageBtn) mMenuView.findViewById(R.id.tool_cpoy_link);
		mIbSaoYiSao = (ImageBtn) mMenuView.findViewById(R.id.tool_saoyisao);

		mIbCopyLink.setOnClickListener(itemsOnClick);
		mIbSaoYiSao.setOnClickListener(itemsOnClick);

	}

}
