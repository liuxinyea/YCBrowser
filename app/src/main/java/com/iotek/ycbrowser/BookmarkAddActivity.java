package com.iotek.ycbrowser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotek.dao.BookMarkDao;
import com.iotek.entity.WebsiteInfo;
import com.iotek.util.Util;

public class BookmarkAddActivity extends Activity implements OnClickListener {

	ImageView bookmark_edit_iv_back;
	TextView bookmark_edit_tv_confirm;
	EditText bookmark_edit_et_name;
	EditText bookmark_edit_et_url;
	WebsiteInfo addWebsiteInfo = new WebsiteInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bookmark_add);

		// 数据获取
		addWebsiteInfo = new WebsiteInfo(getIntent().getStringExtra("name"),
				getIntent().getStringExtra("url"), (Bitmap) getIntent()
						.getParcelableExtra("image"), false);

		bookmark_edit_iv_back = (ImageView) findViewById(R.id.bookmark_edit_iv_back);
		bookmark_edit_tv_confirm = (TextView) findViewById(R.id.bookmark_edit_tv_confirm);
		bookmark_edit_et_name = (EditText) findViewById(R.id.bookmark_edit_et_name);
		bookmark_edit_et_url = (EditText) findViewById(R.id.bookmark_edit_et_url);

		bookmark_edit_et_name.setText(addWebsiteInfo.getName());
		bookmark_edit_et_url.setText(addWebsiteInfo.getURL());

		bookmark_edit_iv_back.setOnClickListener(this);
		bookmark_edit_tv_confirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bookmark_edit_iv_back:
			finish();
			break;
		case R.id.bookmark_edit_tv_confirm:
			addWebsiteInfo.setName(String.valueOf(bookmark_edit_et_name
					.getText()));
			addWebsiteInfo
					.setURL(String.valueOf(bookmark_edit_et_url.getText()));
			if (BookMarkDao.findBookmarkByURL(this, addWebsiteInfo.getURL())) {
				BookMarkDao.updateBookmarkByURL(this, addWebsiteInfo);
				Util.displayToast(
						this,
						getResources().getString(
								R.string.add_bookmark_update_toast));
				finish();
			} else {
				BookMarkDao.insertBookmark(this, addWebsiteInfo);
				Util.displayToast(this,
						getResources().getString(R.string.add_bookmark_toast));
				finish();
			}

			finish();
			break;

		default:
			break;
		}

	}

}
