package com.iotek.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class FunctionKeyPopupWindow extends PopupWindow {
	private View mMenuView;

	private ImageBtn mBtnbookmarkHistory;
	private ImageBtn mBtnDownload;
	private ImageBtn mBtnShare;
	private ImageBtn mBtnSetup;

	private ImageBtn mBtnAddBookmark;
	private ImageBtn mBtnNoPicture;
	private ImageBtn mBtnNight;
	private ImageBtn mBtnRefresh;

	private ImageBtn mBtnWeblink;
	private ImageBtn mBtnTool;
	private ImageBtn mBtnPageSearch;
	private ImageBtn mBtnExit;

	private ImageView mBtnFunctionKey;
	LayoutInflater mInflater;

	OnClickListener itemsOnClick;
	Activity context;

	public FunctionKeyPopupWindow(final Activity context,
			OnClickListener itemsOnClick) {
		super(context);
		this.context = context;
		this.itemsOnClick = itemsOnClick;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = mInflater.inflate(R.layout.function_popupwindow, null);

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

		FunctionKeyPopupWindow.this
				.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						// TODO Auto-generated method stub
						Util.refreshBackground(1f, context);
					}
				});

	}

	private void addData() {
		// TODO Auto-generated method stub
		// 设置图片 mGoodsImage
		mBtnAddBookmark.setImageResource(R.drawable.menu_add_book_mark);
		mBtnAddBookmark.setText(context
				.getString(R.string.function_add_bookmark));
		mBtnWeblink.setImageResource(R.drawable.app_icon_frequent);
		mBtnWeblink.setText(context.getString(R.string.function_weblink));
		mBtnbookmarkHistory
				.setImageResource(R.drawable.video_brief_btn_collect_selected_empty);
		mBtnbookmarkHistory.setText(context
				.getString(R.string.function_bookmark_history));
		mBtnDownload.setImageResource(R.drawable.video_center_off_icon);
		mBtnDownload.setText(context.getString(R.string.function_download));
		mBtnExit.setImageResource(R.drawable.menu_exit);
		mBtnExit.setText(context.getString(R.string.function_exit));
		/*
		 * mBtnNight.setImageResource(R.drawable.icon_fine_night);
		 * mBtnNight.setText(context.getString(R.string.function_night));
		 *//*
			 * mBtnNoPicture.setImageResource(R.drawable.usercenter_menu_no_pic);
			 * mBtnNoPicture
			 * .setText(context.getString(R.string.function_no_picture));
			 */mBtnPageSearch.setImageResource(R.drawable.menu_pagesearch);
		mBtnPageSearch
				.setText(context.getString(R.string.function_page_search));
		mBtnRefresh.setImageResource(R.drawable.menu_refresh);
		mBtnRefresh.setText(context.getString(R.string.function_refresh));
		mBtnSetup.setImageResource(R.drawable.setting_default_browser_auto);
		mBtnSetup.setText(context.getString(R.string.function_setup));
		mBtnShare.setImageResource(R.drawable.function_share);
		mBtnShare.setText(context.getString(R.string.function_share));
		mBtnTool.setImageResource(R.drawable.bookmark_pc_plat_icon);
		mBtnTool.setText(context.getString(R.string.function_tool));
	}

	private void initView() {
		// TODO Auto-generated method stub
		mBtnbookmarkHistory = (ImageBtn) mMenuView
				.findViewById(R.id.function_bookmark_history);
		mBtnAddBookmark = (ImageBtn) mMenuView
				.findViewById(R.id.function_add_bookmark);
		mBtnWeblink = (ImageBtn) mMenuView.findViewById(R.id.function_weblink);
		mBtnDownload = (ImageBtn) mMenuView
				.findViewById(R.id.function_download);
		mBtnExit = (ImageBtn) mMenuView.findViewById(R.id.function_exit);
		mBtnNight = (ImageBtn) mMenuView.findViewById(R.id.function_night);
		mBtnNoPicture = (ImageBtn) mMenuView
				.findViewById(R.id.function_no_picture);
		mBtnPageSearch = (ImageBtn) mMenuView
				.findViewById(R.id.function_page_search);
		mBtnRefresh = (ImageBtn) mMenuView.findViewById(R.id.function_refresh);
		mBtnSetup = (ImageBtn) mMenuView.findViewById(R.id.function_setup);
		mBtnShare = (ImageBtn) mMenuView.findViewById(R.id.function_share);
		mBtnTool = (ImageBtn) mMenuView.findViewById(R.id.function_tool);

		mBtnbookmarkHistory.setOnClickListener(itemsOnClick);
		mBtnAddBookmark.setOnClickListener(itemsOnClick);
		mBtnWeblink.setOnClickListener(itemsOnClick);
		mBtnDownload.setOnClickListener(itemsOnClick);
		mBtnExit.setOnClickListener(itemsOnClick);
		mBtnNight.setOnClickListener(itemsOnClick);
		mBtnNoPicture.setOnClickListener(itemsOnClick);
		mBtnPageSearch.setOnClickListener(itemsOnClick);
		mBtnRefresh.setOnClickListener(itemsOnClick);
		mBtnSetup.setOnClickListener(itemsOnClick);
		mBtnShare.setOnClickListener(itemsOnClick);
		mBtnTool.setOnClickListener(itemsOnClick);

		mBtnFunctionKey = (ImageView) mMenuView
				.findViewById(R.id.function_function_key);
		mBtnFunctionKey.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 销毁弹出框
				Log.d("wx", "close popWindow");
				FunctionKeyPopupWindow.this.dismiss();
			}
		});
	}

	public View getMenuView() {
		return mMenuView;
	}
}
