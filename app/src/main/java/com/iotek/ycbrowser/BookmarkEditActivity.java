package com.iotek.ycbrowser;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotek.dao.BookMarkDao;
import com.iotek.entity.WebsiteInfo;
import com.iotek.util.Constants;
import com.iotek.util.Util;

public class BookmarkEditActivity extends FragmentActivity implements
		OnClickListener {
	ImageView bookmark_edit_iv_back;
	TextView bookmark_edit_tv_confirm;
	EditText bookmark_edit_et_name;
	EditText bookmark_edit_et_url;
	WebsiteInfo editWebsiteInfo = new WebsiteInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bookmark_edit);

		editWebsiteInfo.setId(getIntent().getIntExtra(Constants.DB_BOOKMARK_ID,
				-1));
		editWebsiteInfo.setName(getIntent().getStringExtra(
				Constants.DB_BOOKMARK_NAME));
		editWebsiteInfo.setURL(getIntent().getStringExtra(
				Constants.DB_BOOKMARK_URL));

		bookmark_edit_iv_back = (ImageView) findViewById(R.id.bookmark_edit_iv_back);
		bookmark_edit_tv_confirm = (TextView) findViewById(R.id.bookmark_edit_tv_confirm);
		bookmark_edit_et_name = (EditText) findViewById(R.id.bookmark_edit_et_name);
		bookmark_edit_et_url = (EditText) findViewById(R.id.bookmark_edit_et_url);

		bookmark_edit_et_name.setText(editWebsiteInfo.getName());
		bookmark_edit_et_url.setText(editWebsiteInfo.getURL());

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
			editWebsiteInfo.setName(String.valueOf(bookmark_edit_et_name
					.getText()));
			editWebsiteInfo.setURL(String.valueOf(bookmark_edit_et_url
					.getText()));
			BookMarkDao.updateBookmark(this, editWebsiteInfo);
			Util.displayToast(this,
					getResources().getString(R.string.update_bookmark_toast));
			finish();
			break;

		default:
			break;
		}

	}

}
