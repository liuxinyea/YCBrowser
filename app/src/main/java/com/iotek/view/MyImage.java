package com.iotek.view;

import com.iotek.ycbrowser.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyImage extends RelativeLayout{
	private String text="窗口";
	private TextView textView;
	private ImageView smallImageView;
	private ImageView bigImageView;
	private LinearLayout layout;
	private Boolean isCurrentWeb=false;
    public MyImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.web_image, this);
		textView=(TextView)findViewById(R.id.tv_bannertext);
		bigImageView=(ImageView)findViewById(R.id.webview_img);
    }
    public MyImage(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.web_image, this);
		textView=(TextView)findViewById(R.id.tv_bannertext);
		smallImageView=(ImageView)findViewById(R.id.imageView1);
		bigImageView=(ImageView)findViewById(R.id.webview_img);
		layout=(LinearLayout)findViewById(R.id.title_layout);
	}
    public void setLayoutColor(){
   	 layout.setBackgroundColor(Color.RED);
    }
    public void setCurrentWeb(Boolean isCurrentWeb){
    	this.isCurrentWeb=isCurrentWeb;
    }
    public Boolean getCurrentWeb(){
    	return this.isCurrentWeb;
    }
 public void setImage(Bitmap bitmap){
	 this.bigImageView.setImageBitmap(bitmap);
 }
  public ImageView getImage(){
	  return this.smallImageView;
  }
  public void setText(String text){
	  this.text=text;
	 this.textView.setText(text);
  }
  public String getText(){
	  return text;
  }
  
}
