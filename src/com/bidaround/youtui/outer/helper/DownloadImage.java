package com.bidaround.youtui.outer.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.client.ClientProtocolException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DownloadImage {

	/**
	 * 下载图片
	 */
	public static void downloadImage(final String url, final String savePath,
			final String filename) {
		new Thread() {
			public void run() {
				try {
					down_file(url, savePath, filename);
					// 下载文件，参数：第一个URL，第二个存放路径
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 加载系统本地图片
	 */
	@SuppressWarnings("unused")
	public static Bitmap loadImage(final String url, final String filename) {
		try {
			FileInputStream fis = new FileInputStream(url + filename);
			if (fis != null) {
				return BitmapFactory.decodeStream(fis);
			} else {
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 下载文件功能
	 */
	private static void down_file(String url, String path, String filename)
			throws IOException {
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		int fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		FileUtils util = new FileUtils();
		if (util.isFileExist(path + filename)) {
			return;
		}
		util.creatSDDir(path);
		File file = util.creatSDFile(path + filename);// 保存的文件名
		FileOutputStream fos = new FileOutputStream(file);

		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
		} while (true);
		try {
			is.close();
		} catch (Exception ex) {
			Log.e("tag", "error: " + ex.getMessage(), ex);
		}
	}
}
