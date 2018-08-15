package com.iotek.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.WindowManager;
import android.widget.Toast;

import com.iotek.database.BrowserDatabaseHelper;
import com.iotek.entity.HistoryInfo;
import com.iotek.entity.WebBasic;
import com.iotek.entity.WebsiteInfo;
import com.iotek.ycbrowser.R;

public class Util {
	/**
	 * Display Toast
	 * 
	 * @param context
	 * @param info
	 *            2016/10/17
	 */
	public static void displayToast(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	/**
	 * change background color alpha
	 * 
	 * @param f
	 * @param context
	 */
	public static void refreshBackground(float f, Activity context) {
		// TODO Auto-generated method stub
		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = f;
		context.getWindow().setAttributes(params);
	}

	/**
	 * Bitmap转byte[]
	 * 
	 * @param bitmap
	 * @return byte[]
	 */
	public static byte[] img(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 获得String类型的当前时间
	 * 
	 * @return String
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);

		return dateString;
	}

	/**
	 * 获得Date类型的当前时间
	 * 
	 * @return Date
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * String 类型转Date 类型
	 * 
	 * @param stringDate
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String stringDate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateDate = formatter.parse(stringDate);
		return dateDate;
	}

	/**
	 * Date 类型转String 类型
	 * 
	 * @param dateDate
	 * @return
	 * @throws ParseException
	 */
	public static String dateToString(Date dateDate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringDate = formatter.format(dateDate);
		return stringDate;
	}

	/**
	 * Date类型增加天数
	 * 
	 * @param date
	 * @param value
	 * @return
	 */
	public static Date addDay(Date date, int value) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_YEAR, value);
		return now.getTime();
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static String getDirs(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	/**
	 * 查询全部历史记录
	 * 
	 * @param context
	 * @return List<HistoryInfo>全部历史记录
	 */
	public static List<HistoryInfo> queryAllHistory(Context context) {
		List<HistoryInfo> allHistoryList = new ArrayList<HistoryInfo>();
		Uri uri = Uri.parse("content://com.iotek.database.provider/history");
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor
						.getColumnIndex(Constants.DB_HISTORY_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(Constants.DB_HISTORY_NAME));
				String url = cursor.getString(cursor
						.getColumnIndex(Constants.DB_HISTORY_URL));
				byte[] image = cursor.getBlob(cursor
						.getColumnIndex(Constants.DB_HISTORY_IMAGE));
				String time = cursor.getString(cursor
						.getColumnIndex(Constants.DB_HISTORY_TIME));

				Bitmap bmpout = BitmapFactory.decodeByteArray(image, 0,
						image.length);

				allHistoryList
						.add(new HistoryInfo(id, name, url, bmpout, time));

			}
		}
		cursor.close();
		return allHistoryList;
	}

	/**
	 * 查询时返回所有的书签实体类
	 */
	public static List<WebsiteInfo> queryAllBookmark(Context context) {
		final String QUERY_ALLBOOKMARK = "select* from bookmark";

		BrowserDatabaseHelper dbHelper;
		SQLiteDatabase db;

		dbHelper = new BrowserDatabaseHelper(context, Constants.DB_NAME, null,
				6);
		db = dbHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery(QUERY_ALLBOOKMARK, null);
		List<WebsiteInfo> websiteInfoInfoList = new ArrayList<WebsiteInfo>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_NAME));
				String url = cursor.getString(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_URL));
				byte[] image = cursor.getBlob(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_IMAGE));

				Bitmap bmpout = BitmapFactory.decodeByteArray(image, 0,
						image.length);

				websiteInfoInfoList.add(new WebsiteInfo(id, name, url, bmpout,
						false));

			} while (cursor.moveToNext());
		}
		cursor.close();

		return websiteInfoInfoList;
	}

	/**
	 * 判断URL中是否含有广告
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public static boolean hasAd(Context context, String url) {
		Resources res = context.getResources();
		String[] adUrls = res.getStringArray(R.array.adBlockUrl);
		for (String adUrl : adUrls) {
			if (url.contains(adUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 关键字自动补全数据源
	 * 
	 * @return
	 */
	public static LinkedList<WebBasic> searchData(Context context) {
		LinkedList<WebBasic> mSearchData = new LinkedList<WebBasic>();
		for (WebsiteInfo mWebsiteInfo : queryAllBookmark(context)) {
			mSearchData.add(new WebBasic(R.drawable.icon_web_book, mWebsiteInfo
					.getName(), mWebsiteInfo.getURL()));
		}
		for (HistoryInfo mHistoryInfo : queryAllHistory(context)) {
			mSearchData.add(new WebBasic(R.drawable.icon_web_foot, mHistoryInfo
					.getName(), mHistoryInfo.getURL()));
		}
		return mSearchData;
	}

	/**
	 * 字符串匹配（一半包含及匹配成功）
	 * 
	 * @param dbString
	 * @param inputString
	 * @return
	 */
	public static boolean StringMatch(String dbString, String inputString) {
		int matchingDegree = 0;
		boolean isMatch = false;

		for (int i = 0; i < inputString.length(); i++) {
			if (dbString.contains(String.valueOf(inputString.charAt(i)))) {
				matchingDegree++;
			}
			if (matchingDegree >= inputString.length()) {
				isMatch = true;
				break;
			}
		}
		return isMatch;
	}
}
