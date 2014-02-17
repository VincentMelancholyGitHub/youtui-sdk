package cn.bidaround.youtui.social.activity;

import com.bidaround.youtui.outer.helper.DownloadImage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

/**
 * Description: created by qyj on January 7, 2014
 */
public class WeiXinShareActivity extends Activity {
	// IWXAPI是第三方app和微信通信的openapi接口
	private IWXAPI api;
	// mark为0时是定向分享；为1时是朋友圈分享。默认为0
	private String mark = "0";// mark=0时微信定向分享;mark=1时微信朋友圈分享
	private String title;
	private String description;
	private String target_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		shareContent();
	}

	/**
	 * 初始化微信
	 */
	private void init() {
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, YoutuiConstants.WEIXIN_APP_ID,
				true);
		// 将该app注册到微信
		api.registerApp(YoutuiConstants.WEIXIN_APP_ID);
	}

	/**
	 * 微信分享的内容
	 */
	private void shareContent() {
		getData();
		WXMediaMessage msg = new WXMediaMessage();
		msg.title = title;// 在这里填写APP的名称，不超过512Bytes
		msg.description = description;// 在这里填写APP的简介，限制长度不超过1KB

		ApplicationInfo info;
		String filename = null;
		try {
			info = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			int youtui_appkey = info.metaData.getInt("YOUTUI_APPKEY");
			youtui_appkey = youtui_appkey + 0;
			filename = String.valueOf(youtui_appkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filename != null) {
			Bitmap logoBitmap = DownloadImage.loadImage(Environment
					.getExternalStorageDirectory().toString()
					+ YoutuiConstants.FILE_SAVE_PATH, filename);
			if (logoBitmap != null) {
				msg.setThumbImage(logoBitmap);// logo图片大小不超过32KB
			}
		}
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = target_url;// 在这里填写点击跳转的链接
		msg.mediaObject = webpage;
		share(msg);
	}

	/**
	 * 微信分享功能的实现
	 */
	private void share(WXMediaMessage msg) {
		if (!api.isWXAppInstalled()) {
			// 显示分享错误
			String error = "您还没有安装微信客户端，请安装后再进行分享！";
			Intent intent = new Intent();
			intent.putExtra("Error", error);
			result(YoutuiConstants.RESULT_ERROR, intent);
			return;
		}
		if (api.getWXAppSupportAPI() < YoutuiConstants.WEIXIN_TIMELINE_SUPPORTED_VERSION) {
			// 显示分享错误
			String error = "朋友圈分享需要微信客户端4.2版本以上支持，请升级您的客户端！";
			Intent intent = new Intent();
			intent.putExtra("Error", error);
			result(YoutuiConstants.RESULT_ERROR, intent);
			return;
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());// 唯一标识一个请求
		req.message = msg;
		if (mark.equals("0")) {
			// 如果scene填WXSceneSession，那么消息会发送至微信的会话内。
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else {
			// 如果scene填WXSceneTimeline，那么消息会发送至朋友圈。scene默认值为WXSceneSession。
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}

		// sendReq是第三方app的IWXAPI主动发送消息给微信，发送完成之后会切回到第三方app界面。
		// sendResp是微信向第三方app的IWXAPI请求数据，第三方app回应数据之后会切回到微信界面。
		if (api.sendReq(req)) {
			// 显示分享成功
			Intent intent = new Intent();
			intent.putExtra("Success", "分享成功！");
			result(YoutuiConstants.RESULT_SUCCESSFUL, intent);
		} else {
			// 显示分享错误
			Intent intent = new Intent();
			intent.putExtra("Error", "分享失败！");
			result(YoutuiConstants.RESULT_ERROR, intent);
		}
	}

	/**
	 * 获取所需参数
	 */
	private void getData() {
		Intent intent = getIntent();
		mark = intent.getStringExtra("mark");
		title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		target_url = intent.getStringExtra("target_url");
	}

	/**
	 * 返回分享状况信息。
	 */
	private void result(int i, Intent intent) {
		this.setResult(i, intent);
		this.finish();
	}
}