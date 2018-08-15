package com.iotek.view;

import com.iotek.ycbrowser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageBtn extends LinearLayout {
	private ImageView imageView;
	private TextView textView;

	public ImageBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ImageBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.imagebtn, this);
		imageView = (ImageView) findViewById(R.id.image_btn_image);
		textView = (TextView) findViewById(R.id.image_btn_text);
	}

	public ImageBtn(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setImageResource(int resId) {
		this.imageView.setImageResource(resId);
	}

	public String getText() {
		return textView.getText().toString();
	}

	public void setText(String text) {
		this.textView.setText(text);
	}

}
