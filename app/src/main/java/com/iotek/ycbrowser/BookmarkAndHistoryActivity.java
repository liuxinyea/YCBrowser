package com.iotek.ycbrowser;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;

public class BookmarkAndHistoryActivity extends FragmentActivity {
	public static final String TAB_BOOKMARK_TAG = "bookmark";
	public static final String TAB_HISTORY_TAG = "history";

	TabHost host;
	ImageView bah_iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bookmarkandhistory);
		host = (TabHost) findViewById(R.id.tabhost);
		host.setup();
		host.addTab(host.newTabSpec(TAB_BOOKMARK_TAG)
				.setIndicator(getResources().getString(R.string.tab_bookmark))
				.setContent(R.id.fragment_bookmark));
		host.addTab(host.newTabSpec(TAB_HISTORY_TAG)
				.setIndicator(getResources().getString(R.string.tab_history))
				.setContent(R.id.fragment_history));

		bah_iv_back = (ImageView) findViewById(R.id.bah_iv_back);
		bah_iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}
}
