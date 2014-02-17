package cn.bidaround.youtui.social.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * Description: created by qyj on January 7, 2014
 */
public class TencentQZoneShareActivity extends Activity {
	private Tencent mTencent;
	private String title;
	private String description;
	private String target_url;
	private String image_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		qzoneShare();
	}

	/**
	 * 初始化Tencent
	 */
	private void init() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(YoutuiConstants.TENCENT_APP_ID,
					this.getApplicationContext());
		}
	}

	/**
	 * 分享到QQ空间的内容
	 */
	private void qzoneShare() {
		getData();
		Bundle params = new Bundle();
		params.putString(Tencent.SHARE_TO_QQ_TITLE, title);// 分享标题
		params.putString(Tencent.SHARE_TO_QQ_SUMMARY, description);// 分享描述
		params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, target_url);// 跳转链接
		// 分享的图片。接口暂不支持发送多张图片的能力。若传入多张图片，则会自动选入第一张图片作为预览图，多图的能力将会在以后支持。虽然这项功能不完善，但却是必选的参数。
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(image_url);// 图片URL
		params.putStringArrayList(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		doShareToQzone(params);
	}

	/**
	 * 分享到QQ空间
	 */
	private void doShareToQzone(Bundle params) {
		mTencent.shareToQzone(TencentQZoneShareActivity.this, params,
				new IUiListener() {
					@Override
					public void onComplete(JSONObject response) {
						// 显示分享成功
						String Response = response.toString();
						Intent intent = new Intent();
						intent.putExtra("Response", Response);
						result(YoutuiConstants.RESULT_SUCCESSFUL, intent);
					}

					@Override
					public void onError(UiError e) {
						// 显示分享错误
						String error = e.errorMessage;
						Intent intent = new Intent();
						intent.putExtra("Error", error);
						result(YoutuiConstants.RESULT_ERROR, intent);
					}

					@Override
					public void onCancel() {
						// 显示分享取消
						Intent intent = new Intent();
						intent.putExtra("Cancel", "分享取消");
						result(YoutuiConstants.RESULT_CANCEL, intent);
					}
				});
		//this.finish();// 当sdk bug修好后这条语句请删除
	}

	/**
	 * 获取所需参数
	 */
	private void getData() {
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		target_url = intent.getStringExtra("target_url");
		image_url = intent.getStringExtra("image_url");
	}

	/**
	 * 返回授权信息。
	 */
	private void result(int i, Intent intent) {
		this.setResult(i, intent);
		this.finish();
	}
}
