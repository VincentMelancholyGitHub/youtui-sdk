package cn.bidaround.youtui.social.activity;

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
public class TencentDirectionalShareActivity extends Activity {
	private Tencent mTencent;
	private String title;
	private String description;
	private String target_url;
	private String image_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		directionalShare();
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
	 * 定向分享的内容
	 */
	private void directionalShare() {
		getData();
		Bundle params = new Bundle();
		params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE,
				Tencent.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(Tencent.SHARE_TO_QQ_TITLE, title);// 分享标题
		params.putString(Tencent.SHARE_TO_QQ_SUMMARY, description);// 分享描述
		params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, target_url);// 跳转链接
		params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, image_url);// 网络图片的URL，请严格控制尺寸，否则不能显示。（65px*65px较佳）
		// params.putString(Tencent.SHARE_TO_QQ_IMAGE_LOCAL_URL,"/storage/sdcard0/Tencent/QQfile_recv/3.gif");//本地图片的URL，不建议采用这种方式
		share(params);
	}

	/**
	 * 定向分享功能(不需要登录或获取token就可以执行)
	 */
	private void share(Bundle params) {
		mTencent.shareToQQ(TencentDirectionalShareActivity.this, params,
				new IUiListener() {
					@Override
					public void onComplete(JSONObject response) {
						// 显示分享成功
						Intent intent = new Intent();
						intent.putExtra("Success", "分享成功");
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
						//intent.putExtra("Cancel", "分享取消");
						intent.putExtra("Cancel", "分享成功");//这是腾讯sdk的一个bug
						result(YoutuiConstants.RESULT_CANCEL, intent);
					}
				});
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
	 * 返回分享状况信息。
	 */
	private void result(int i, Intent intent) {
		this.setResult(i, intent);
		this.finish();
	}
}
