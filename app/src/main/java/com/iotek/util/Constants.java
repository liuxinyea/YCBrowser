package com.iotek.util;

import java.util.LinkedList;
import java.util.List;

import com.iotek.entity.WebBasic;

public class Constants {
	//关键字搜所数据源
	public static List<WebBasic> SEARCHLIST = new LinkedList<WebBasic>();

	// 数据库常量
	public static final String DB_NAME = "BookMark.db";
    
	public static final String NIGHT_CSS="javascript: (function() {\n" +  
            "  \n" +  
            "    css = document.createElement('link');\n" +  
            "    css.id = 'xxx_browser_2016';\n" +  
            "    css.rel = 'stylesheet';\n" +  
            "    css.href = 'data:text/css,div,section,ul,li,a,h1,h2,h3,p,link,textarea,form,select,input,span,button,em,menu,aside,table,tr,td,nav,img,dl,dt,dd, html, body,strong{background:#222222 !important;color:#888888!important;border-color:#555555 !important;scrollbar-arrow-color:#CCCCCC !important;scrollbar-base-color:#222222 !important;scrollbar-shadow-color:#222222 !important;scrollbar-face-color:#222222 !important;scrollbar-highlight-color:#222222 !important;scrollbar-dark-shadow-color:#222222 !important;scrollbar-3d-light-color:#222222 !important;scrollbar-track-color:#222222 !important;}strong{display:block;}a,a *{color:#888888 !important;text-decoration:none !important;}a:visited,a:visited *,a:active,a:active *{color:#555555 !important;}a:hover,a:hover *{color:#888888 !important;background:#222222 !important;}input,select,option,button,textarea{color:#888888 !important;background:#222222 !important;border:#555555 !important;border-color: #888888 #888888 #888888 #888888 !important;}input:focus,select:focus,option:focus,button:focus,textarea:focus,input:hover,input[type=button],input[type=submit],input[type=reset],input[type=image] {border-color: #888888 #888888 #888888 #888888 !important;}input[type=button]:focus,input[type=submit]:focus,input[type=reset]:focus,input[type=image]:focus, input[type=button]:hover,input[type=submit]:hover,input[type=reset]:hover,input[type=image]:hover {color:#888888 !important;background:#222222 !important; border-color: #888888 #888888 #888888 #888888 !important;}html{-webkit-filter: contrast(50%);}';\n" +  
            "    document.getElementsByTagName('head')[0].appendChild(css);\n" +  
            "  \n" +  
            "})();"; 
	public static final String DAY_CSS="javascript: (function() {\n" +  
            "  \n" +  
            "    css = document.createElement('link');\n" +  
            "    css.id = 'xxx_browser_2016';\n" +  
            "    css.rel = 'stylesheet';\n" +  
            "    css.href = 'data:text/css,div,section,ul,li,a,h1,h2,h3,p,link,textarea,form,select,input,span,button,em,menu,aside,table,tr,td,nav,img,dl,dt,dd, html, body,strong{background:#ffffff !important;color:#222222!important;border-color:#aaaaaa !important;scrollbar-arrow-color:#333333 !important;scrollbar-base-color:#dddddd !important;scrollbar-shadow-color:#dddddd !important;scrollbar-face-color:#dddddd !important;scrollbar-highlight-color:#dddddd !important;scrollbar-dark-shadow-color:#dddddd !important;scrollbar-3d-light-color:#dddddd !important;scrollbar-track-color:#dddddd !important;}strong{display:block;}a,a *{color:#222222 !important;text-decoration:none !important;}a:visited,a:visited *,a:active,a:active *{color:#aaaaaa !important;}a:hover,a:hover *{color:#888888 !important;background:#ffffff !important;}input,select,option,button,textarea{color:#888888 !important;background:#ffffff !important;border:#aaaaaa !important;border-color: #888888 #888888 #888888 #888888 !important;}input:focus,select:focus,option:focus,button:focus,textarea:focus,input:hover,input[type=button],input[type=submit],input[type=reset],input[type=image] {border-color: #888888 #888888 #888888 #888888 !important;}input[type=button]:focus,input[type=submit]:focus,input[type=reset]:focus,input[type=image]:focus, input[type=button]:hover,input[type=submit]:hover,input[type=reset]:hover,input[type=image]:hover {color:#888888 !important;background:#ffffff !important; border-color: #888888 #888888 #888888 #888888 !important;}html{-webkit-filter: contrast(50%);}';\n" +  
            "    document.getElementsByTagName('head')[0].appendChild(css);\n" +  
            "  \n" +  
            "})();"; 
	public static final String DB_BOOKMARK_TABLENAME = "bookmark";
	public static final String DB_BOOKMARK_ID = "id";
	public static final String DB_BOOKMARK_NAME = "name";
	public static final String DB_BOOKMARK_URL = "url";
	public static final String DB_BOOKMARK_IMAGE = "image";

	public static final String DB_HISTORY_TABLENAME = "history";
	public static final String DB_HISTORY_ID = "id";
	public static final String DB_HISTORY_NAME = "name";
	public static final String DB_HISTORY_URL = "url";
	public static final String DB_HISTORY_IMAGE = "image";
	public static final String DB_HISTORY_TIME = "time";

	// log的tag常量
	public static final String LOG_TAG_BOOKMARK = "Bookmark";
	public static final String LOG_TAG_HISTORY = "History";

	// 字体大小
	public static final int SMALL = 1;
	public static final int NOMAL = 0;
	public static final int BIG = 2;
	public static final int SUPERBIG = 3;

	// 网站网址设置
	public static final String BAIDU = "http://www.baidu.com";
	public static final String SOUGOU = "https://www.sogou.com/";
	public static final String SOUHU = "http://www.sohu.com/";
	public static final String TIANMAO = "https://www.tmall.com/";
	public static final String TAOBAO = "https://www.taobao.com/";
	public static final String TONGCHENG = "http://sh.58.com/";
	public static final String TENGXUN = "http://www.qq.com/";
	public static final String XIECHENG = "http://www.ctrip.com/";
	public static final String YOUKU = "http://www.youku.com/";
	public static final String FENGHUANG = "http://www.ifeng.com/";
	public static final String GANJI = "http://www.ganji.com/";
	public static final String RENREN = "http://www.renren.com/";
	public static final String WANGYI = "http://www.163.com/";
	public static final String XINLANG = "http://www.sina.com.cn/";
	public static final String TIANYA = "http://www.tianya.cn/";

	// 下载显示图片
	public static final int DOWNLOAD_ICON_APK = 1;
	public static final int DOWNLOAD_ICON_TXT = 2;
	public static final int DOWNLOAD_ICON_OTHER = 3;

	// 更新TextView（Handler）
	public static final int UPDATE = 0;
	public static final int DOWNLOAD_LIST_UPDATE = 100;
	
	//下载状态显示
	public static final String DOWNLOADING="正在下载";
	public static final String DOWNLOADSTOP="暂停";

	// 默认下载位置
	public static final String DOWNLOAD_DEFAULT_LOCATION = "/storage/emulated/0/Download";
	
	
}
