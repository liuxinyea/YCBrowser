package com.iotek.fragment;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

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
	float viewStartX;
	float startX ;
	float lastX;
	int startY;
	int lastY;
	float endX;
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
	 * 界面中需要动态改变的部分不能写在oncreatView中，因为这里oncreateView只是刚开始时 被调用
	 *  集合的循环并在件的监听中删除用一个Integer的集合辅助解决
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
					ObjectAnimator translationX;
					translationX = ObjectAnimator.ofFloat((View)v.getParent().getParent(), "translationX", 0, 1000);
					translationX.setDuration(800);
					translationX.addListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							super.onAnimationEnd(animation);
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
					translationX.start();
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
			//系统判断的滑动最小距离
			final int TouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
			myImage.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(final View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startX=event.getRawX();
						startY= (int) event.getRawY();
						lastY= (int) event.getRawY();
						viewStartX=v.getX();
	                    lastX = event.getRawX();// 获取触摸事件触摸位置的原始X坐标
	                    startB=v.getBottom();
	                    startL=v.getLeft();
	                    startR=v.getRight();
	                    startT=v.getTop();
						break;
					case MotionEvent.ACTION_MOVE:
                        float dx=event.getRawX()-lastX;
                        int dy= (int) (event.getRawY()-startY);
						Log.e("scrollerDy",dy+"");
						if(v.getParent().getParent()!=null&&Math.abs(dy)<=TouchSlop){//在适当的时机屏蔽父容器的触摸事件
							v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
						}else{
							v.getParent().getParent().requestDisallowInterceptTouchEvent(false);
						}
						v.setX(v.getX() +dx);//修改的是translateX的属性
						lastX =  event.getRawX();
						break;
					case MotionEvent.ACTION_UP:
						endX = (int) event.getRawX();
						int scrollSize = 150;
						 ObjectAnimator translationX;
						if (Math.abs((int) event.getRawX()-startX)>=scrollSize) {
							Log.e("TouchUpRemove",(int) event.getRawX()-startX+"");
							if(event.getRawX()-startX>0){
								translationX = ObjectAnimator.ofFloat(v, "translationX", event.getRawX()-startX, event.getRawX()-startX+1000);
							}else{
								translationX = ObjectAnimator.ofFloat(v, "translationX", event.getRawX()-startX, event.getRawX()-startX-1000);
							}

							translationX.setDuration(800);
							translationX.addListener(new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									super.onAnimationEnd(animation);
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
								}
							});
							translationX.start();
						}else if(Math.abs((int) event.getRawX()-startX)>TouchSlop){
							Log.e("TouchUpBack",(int) event.getRawX()-startX+"");
							translationX = ObjectAnimator.ofFloat(v, "translationX", (int) event.getRawX()-startX,0);
                            translationX.setDuration(200);
							translationX.addListener(new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									super.onAnimationEnd(animation);
									v.setX(viewStartX);
//									v.layout(startL, startT, startR, startB);
								}
							});
							translationX.start();
//	                    	v.layout(startL, startT, startR, startB);
	                    }else{
							Log.e("TouchUpClick",(int) event.getX()-startX+"");
	                    	((MainActivity) getActivity()).shoeWebView(list_index.get(index));
	    					 layout_window.setVisibility(View.GONE);
	    					 linearLayout.removeView(v);
	    					MainActivity activity=(MainActivity) getActivity();
	    					activity.show();
	                    }
	                	break;
						case MotionEvent.ACTION_CANCEL:
							Log.e("TouchUpBack",(int) event.getRawX()-startX+"");
							translationX = ObjectAnimator.ofFloat(v, "translationX", (int) event.getRawX()-startX,0);
							translationX.setDuration(200);
							translationX.addListener(new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									super.onAnimationEnd(animation);
									v.setX(viewStartX);
//									v.layout(startL, startT, startR, startB);
								}
							});
							translationX.start();
							break;
					default:
						break;
					}
					return true;
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
	private void moveViewByLayout(View view, int rawX, int rawY) {
		int left = rawX - view.getWidth() / 2;
		int top = rawY -view.getHeight() / 2;
		int width = left + view.getWidth();
		int height = top + view.getHeight();
		view.layout(left, top, width, height);
	}
	OnWebViewListener imagViewListener;

	/**
	 * @return null
	 * @ 回调实现删除浏览器窗口等操作
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
