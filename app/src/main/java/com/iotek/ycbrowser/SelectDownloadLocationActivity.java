package com.iotek.ycbrowser;

import java.io.File;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.iotek.adapter.FileItemAdapter;
import com.iotek.entity.FileEntity;

public class SelectDownloadLocationActivity extends Activity implements OnClickListener{
	TextView tv_cancel;
	TextView tv_commit;
	TextView tv_current_location;
	ListView fileList;
	
	LinkedList<FileEntity> fileData = new LinkedList<FileEntity>();// 文件列表信息
	File currentFile;// 当前文件夹
	FileItemAdapter mFileItemAdapter;// 文件信息适配器
	Handler mHandler;// 处理最新文件列表显示
	String rootPath;// 根目录路径
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_download_location);
		
		initView();
		
		mFileItemAdapter = new FileItemAdapter(this);
		fileList.setAdapter(mFileItemAdapter);
		rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		currentFile = new File(rootPath);
		setCurrentLocation();
		
		getData(rootPath);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					mFileItemAdapter.resetData(fileData);
					break;
				default:
					break;
				}
			}
		};
		
	}

	private void setCurrentLocation() {
		tv_current_location.setText("当前位置："+currentFile.getAbsolutePath());
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_cancel = (TextView) findViewById(R.id.cancel_select_location);
		tv_cancel.setOnClickListener(this);
		tv_commit = (TextView) findViewById(R.id.commit_select_location);
		tv_commit.setOnClickListener(this);
		tv_current_location = (TextView) findViewById(R.id.curent_location);
		fileList = (ListView) findViewById(R.id.location_list);
		fileList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FileEntity mFileEntity = fileData.get(position);
				if (mFileEntity.isDirectory()) {
					// 如果当前选择是文件夹
					currentFile = new File(mFileEntity.getPath());
					setCurrentLocation();
					getData(mFileEntity.getPath());
				} 
			}

		});
	}
	
	
	/**
	 * 判断当前文件夹是否为根目录 true： false：列出当前目录的父目录的所有文件
	 * 
	 * @param path
	 */
	public void isRootDirectory() {
		if (rootPath.equals(currentFile.getAbsolutePath())) {
			finish();
		} else {
			String parentPath = currentFile.getParent();
			currentFile = new File(parentPath);
			setCurrentLocation();
			getData(parentPath);
		}
	}
	
	
	/**
	 * 查找一个路径下所有文件
	 * 
	 * @param path
	 */
	public void findAllFilesByPath(String path) {
		fileData.clear();
		if (path == null || path.equals("")) {
			return;
		}
		File parentFile = new File(path);
		File[] files = parentFile.listFiles();

		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				FileEntity fileEntity = new FileEntity();
				boolean isDirectory = files[i].isDirectory();

				// 判断是否为文件夹并设置图标
				fileEntity.setDirectory(isDirectory);
				if (fileEntity.isDirectory()) {
					if(files[i].list().length > 0){
						fileEntity.setPicSrcID(R.drawable.icon_folder);
					}else{
						fileEntity.setPicSrcID(R.drawable.icon_empty);
					}
				} else {
					fileEntity.setPicSrcID(R.drawable.icon_file);
				}
				// 设置文件名称
				fileEntity.setName(files[i].getName().toString());
				// 设置文件路径
				fileEntity.setPath(files[i].getAbsolutePath());
				// 设置文件大小
				fileEntity.setSize(String.valueOf(files[i].length()));
				fileData.add(fileEntity);
			}
		}
	}

	@Override
	public void onBackPressed() {
		isRootDirectory();
	}

	/**
	 * 根据路径查询该路径下所有文件（夹）路径列表
	 * 
	 * @param path
	 */
	private void getData(final String path) {
		// 查询列表为耗时操作，开辟一个子线程进行此操作
		new Thread() {
			@Override
			public void run() {
				findAllFilesByPath(path);
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		Intent backResult = new Intent();
		switch (v.getId()) {
		case R.id.cancel_select_location:
			backResult.putExtra("isChange", false);
			
			break;
		case R.id.commit_select_location:
			backResult.putExtra("isChange", true);
			backResult.putExtra("location", currentFile.getAbsolutePath());
			break;
		default:
			break;
		}
		setResult(RESULT_OK, backResult);
		finish();
	}
}
