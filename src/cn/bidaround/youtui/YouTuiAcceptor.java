package cn.bidaround.youtui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YouTuiAcceptor extends Activity {
	
	private MenuItem exit;
	private WebView webview;
	/* 应用appkey */
	private String appId;
	/* 邀请码 */
	private String inviteNum;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//initAppId();
		//initWebView();
        //读取邀请码并发送到服务器
        //doPost();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* 添加退出菜单 */
		exit = menu.add("Exit");
		/* 设置退出菜单图片 */
		exit.setIcon(R.drawable.close_btn);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/* 结束Activity */
		finish();
		return super.onOptionsItemSelected(item);
	}

	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}
	
	private void initAppId(){
		ApplicationInfo info;
        try{
            info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            int msg = info.metaData.getInt("YOUTUI_APPKEY");
            msg = msg + 0;
            appId = String.valueOf(msg);
        }catch(Exception e){
        	e.printStackTrace();
		}
	}
	
	public void doPost(){
		String actionUrl = "http://yt.bidaround.cn/accept";
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("appId", appId));
		params.add(new BasicNameValuePair("inviteNum", inviteNum));
		//files.put("tempAndroid.txt", new File("/sdcard/temp.txt"));
		try {
			//请求到服务器
			post(actionUrl, params);
		} catch(Exception e){
        	e.printStackTrace();
		}
	}
	
	private void initWebView(){
		// 调用内嵌浏览器 
		webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient());
		
        /*向服务器发送接收邀请验证请求*/
        String urlString = "http://yt.bidaround.cn";
        //String urlString = "http://192.168.2.106";
		String urlStr = "";
		if (appId==null || appId.length() == 0) {
			urlStr = urlString + "/activity/noappId/";
		}else{
			urlStr = urlString + "/activity/inviteUrl?appId=" + appId;
		}
		webview.loadUrl(urlStr);
		setContentView(webview);
	}
	
	private void getInviteNum(){
		if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
            File path = Environment.getExternalStorageDirectory();// 获得SD卡路径  
            // File path = new File("/mnt/sdcard/");  
            File[] files = path.listFiles();// 读取  
            getFileName(files);  
        }  
        /*SimpleAdapter adapter = new SimpleAdapter(this, name, R.layout.sd_list,  
                new String[] { "Name" }, new int[] { R.id.txt_tv });  
        lv.setAdapter(adapter);  
        for(int i = 0; i < name.size(); i++) {  
            Log.i("zeng", "list.  name:  " + name.get(i));  
        }*/
	}
	
	/** 从文件名中读取邀请码  **/
	private String getFileName(File[] files) {  
        // 先判断目录是否为空，否则会报空指针
		if (files == null) {  
			return null;
		}
		String fn = null;
        for(File file : files){  
            if (file.isDirectory()) {
                //若是文件目录。继续读
            	getFileName(file.listFiles());  
                //Log.i("zeng", "若是文件目录。继续读2" + file.getName().toString() + file.getPath().toString());  
            }else{  
                String fileName = file.getName();  
                //扫描apk 安装包中包含youtui 标示的文件名，youtui_+ appkey + 邀请码
                if(fileName.endsWith(".apk") && fileName.contains("youtui_"+ appId)) {  
                    fn = fileName.substring(0, fileName.lastIndexOf(".")).toString(); 
                    Log.i("done", "apk包名：：   " + fn);  
                    break;
                }  
            }  
        }
        return fn;
    }
	
	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * @param actionUrl
	 * @param params
	 * @return
	 */
	private void post(String actionUrl, List<NameValuePair> params){ 
		HttpClient httpclient = new DefaultHttpClient();
		//你的URL
		HttpPost httppost = new HttpPost(actionUrl);
		 
		try{
		   //Your DATA
		   httppost.setEntity(new UrlEncodedFormEntity(params));
		   HttpResponse response;
		   response=httpclient.execute(httppost);
		  }catch (ClientProtocolException e) {
			  e.printStackTrace();
		  }catch (IOException e) {
			  e.printStackTrace();
		  }
        } 
}