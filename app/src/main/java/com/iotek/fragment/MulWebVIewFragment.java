package com.iotek.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.iotek.view.MyImage;
import com.iotek.ycbrowser.MainActivity;
import com.iotek.ycbrowser.R;

/**
 * @info 浏览器多窗口浏览
 * @author 刘鑫烨
 * 
 */
public class MulWebVIewFragment extends Fragment {
	private LinearLayout linearLayout;
	private LinearLayout layout_window;
	private LinearLayout button_linearlayout;
	private MyImage myImage;
	private Button btn_back;
	private Button btn_addWeb;
	private List<Bitmap> list_picture;
	private Button btn_closeAll;
	private List<Integer> list_index = new ArrayList<Integer>();
	private List<String> titles = new ArrayList<String>();
	private List<MyImage> list_image=new ArrayList<MyImage>();
	int currentWeb;
	int startX ;
	int lastX;
	int endX;
	 int startL;
     int startB;
     int startR;
     int startT;
	int i;

	/**
	 * 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_mul_webview, container,
				false);
		linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
		layout_window = (LinearLayout) view.findViewById(R.id.window);
		button_linearlayout = (LinearLayout) view
				.findViewById(R.id.button_linearlayout);
		button_linearlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 无，屏蔽mainactivity点击事件
			}
		});
		btn_addWeb = (Button) view.findViewById(R.id.add_web);
		btn_back = (Button) view.findViewById(R.id.go_back);
		list_picture = new ArrayList<Bitmap>();
		btn_closeAll = (Button) view.findViewById(R.id.close_all);
		return view;
	}

	/**
	 * @see 界面中需要动态改变的部分不能写在oncreatView中，因为这里oncreateView只是刚开始时 被调用
	 * @see 集合的循环并在件的监听中删除用一个Integer的集合辅助解决
	 */
	@Override
	public void onStart() {
		super.onStart();
		initData();
		if (linearLayout != null) {
			linearLayout.removeAllViews();
			list_image.clear();
		}
		for (i = 0; i < list_picture.size(); i++) {
			myImage = new MyImage(getActivity());
			myImage.setImage(list_picture.get(i));
			myImage.setText(titles.get(i));
			if (i == currentWeb) {
				myImage.setLayoutColor();
				myImage.setCurrentWeb(true);
			}
			linearLayout.addView(myImage);
			list_index.add(i);
			list_image.add(myImage);
			final int index = i;
			myImage.getImage().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					linearLayout.removeViewAt(list_index.get(index));
					((MainActivity) getActivity()).deleteWebView(list_index
							.get(index));
					/* MyImage image1=(MyImage) v; */
					if (linearLayout.getChildCount() != 0
					&& list_image.get(list_index.get(index)).getCurrentWeb()) {
						if (currentWeb == linearLayout.getChildCount()) {
							currentWeb -= 1;
							MyImage image = (MyImage) linearLayout
									.getChildAt(currentWeb);
							image.setLayoutColor();
							image.setCurrentWeb(true);
						} else {
							/*currentWeb -= 1;*/
							MyImage image = (MyImage) linearLayout
									.getChildAt(currentWeb);
							image.setLayoutColor();
							image.setCurrentWeb(true);
						}
					} else {//如果删除的不是当前页或者不是最后一个，则判断删除的webView的下标
						     //如果小于当前页处理，否则不处理
						if(list_index.get(index)<currentWeb){
							currentWeb -= 1;
						}
						
					}
					list_image.remove(list_index.get(index));
					for (int j = 0; j < list_index.size(); j++) {
						if (j > index) {
							list_index.set(j, list_index.get(j) - 1);
						}
					}
				}
			});
			myImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).shoeWebView(list_index
							.get(index));
					Log.d("faragment", list_index.get(index) + "");
					layout_window.setVisibility(View.GONE);
					linearLayout.removeView(v);
					MainActivity activity = (MainActivity) getActivity();
					Log.d(" 点击显示", list_picture.size() + "");
					activity.show();
				}
			});
			myImage.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
	                    startX = (int) event.getX();
	                    lastX = (int) event.getX();// 获取触摸事件触摸位置的原始X坐标     
	                    
	                    startB=v.getBottom();
	                    startL=v.getLeft();
	                    startR=v.getRight();
	                    startT=v.getTop();
						break;
					case MotionEvent.ACTION_MOVE:
						 int dx = (int) event.getX() - lastX;
	                      int l = v.getLeft() + dx;
	                      int b = v.getBottom();
	                      int r = v.getRight() + dx;     
	                      int t = v.getTop();   
	                      v.layout(l, t, r, b);     
	                      lastX = (int) event.getX();     
	                     v.postInvalidate();    
						break;
					case MotionEvent.ACTION_UP:
						endX = (int) event.getX();
						int scrollSize = 100;
						if (Math.abs((int) event.getX()-startX)>=scrollSize) {
							linearLayout.removeView(v);
							((MainActivity) getActivity())
									.deleteWebView(list_index.get(index));
						
							MyImage image1 = (MyImage) v;
							if (linearLayout.getChildCount() != 0
									&& image1.getCurrentWeb()) {
								if (currentWeb == linearLayout.getChildCount()) {
									currentWeb -= 1;
									MyImage image = (MyImage) linearLayout
											.getChildAt(currentWeb);
									image.setLayoutColor();
									image.setCurrentWeb(true);
								} else {
									/*currentWeb -= 1;*/
									MyImage image = (MyImage) linearLayout
											.getChildAt(currentWeb);
									image.setLayoutColor();
									image.setCurrentWeb(true);
								}
							} else {//如果删除的不是当前页或者不是最后一个，则判断删除的webView的下标
								     //如果小于当前页处理，否则不处理
								if(list_index.get(index)<currentWeb){
									currentWeb -= 1;
								}
							}
							for (int j = 0; j < list_index.size(); j++) {
								if (j > index) {
									list_index.set(j, list_index.get(j) - 1);
								}
							}
						}else if(Math.abs((int) event.getX()-startX)>=20){	
	                    	v.layout(startL, startT, startR, startB); 
	                    }else{
	                    	((MainActivity) getActivity()).shoeWebView(list_index.get(index));
	    					 layout_window.setVisibility(View.GONE);
	    					 linearLayout.removeView(v);
	    					MainActivity activity=(MainActivity) getActivity();
	    					activity.show();
	                    }
	                	break;
					default:
						break;
					}
					return false;
				}
			});
		}
		btn_addWeb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layout_window.setVisibility(View.GONE);
				((MainActivity) getActivity()).addWebView();
				MainActivity activity = (MainActivity) getActivity();
				activity.show();
				linearLayout.removeView(v);
			}
		});
		btn_closeAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity activity = (MainActivity) getActivity();
				activity.closeAllWeb();
				linearLayout.removeAllViews();
				layout_window.setVisibility(View.GONE);
				activity.show();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layout_window.setVisibility(View.GONE);

				MainActivity activity = (MainActivity) getActivity();
				activity.deleteCurrentBitmap();
				activity.show();
			}
		});
	}

	public void windowGone() {
		layout_window.setVisibility(View.GONE);
	}

	public void initData() {
		MainActivity activity = (MainActivity) getActivity();
		list_picture = activity.getBitmap();
		Log.d("窗口数目", list_picture.size()+"");
		this.titles = activity.getWebTitle();
		this.currentWeb = activity.getCurrentWeb();
		Log.d("当前页", currentWeb+"");
		Log.d("list_bitmap", String.valueOf(list_picture.size()));
	}

	OnWebViewListener imagViewListener;

	/**
	 * @return null
	 * @see 回调实现删除浏览器窗口等操作
	 * @param webViewListener
	 */
	public void setOnDeletWebView(OnWebViewListener webViewListener) {
		this.imagViewListener = webViewListener;
	}

	public interface OnWebViewListener {
		public void deleteWebView(int deleteIndex);

		public void shoeWebView(int showIndex);

		public void addWebView();
	}
}
