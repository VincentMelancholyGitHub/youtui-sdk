package cn.bidaround.youtui.social.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

/**
 * Description: created by qyj on January 7, 2014
 */
public class TencentWeiboSSOActivity extends Activity {
	private Tencent mTencent;
	private String state;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		getData();
		login();
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
	 * 登录QQ
	 */
	private void login() {
		init();
		if (mTencent.isSessionValid()) {
			// 显示获取Token正常
			Intent intent = new Intent();
			intent.putExtra("AccessToken", getToken());
			intent.putExtra("ExpiresTime", getExpiresTime());
			intent.putExtra("OpenId", getOpenId());
			result(YoutuiConstants.RESULT_SUCCESSFUL, intent);
		} else {
			mTencent.login(this, YoutuiConstants.TENCENT_SCOPE,
					new IUiListener() {
						@Override
						public void onComplete(JSONObject response) {
							// 显示获取Token正常
							Intent intent = new Intent();
							intent.putExtra("AccessToken", getToken());
							intent.putExtra("ExpiresTime", String.valueOf(mTencent.getExpiresIn()));
							intent.putExtra("OpenId", getOpenId());
							result(YoutuiConstants.RESULT_SUCCESSFUL, intent);
						}

						@Override
						public void onError(UiError e) {
							// 显示授权错误
							String error = e.errorMessage;
							Intent intent = new Intent();
							intent.putExtra("Error", error);
							result(YoutuiConstants.RESULT_ERROR, intent);
						}

						@Override
						public void onCancel() {
							// 显示授权取消
							Intent intent = new Intent();
							intent.putExtra("Cancel", "授权取消");
							result(YoutuiConstants.RESULT_CANCEL, intent);
						}
					});
		}
	}

	/**
	 * 获取token(注意：需要登录后才能够获取token。但如果没有登录直接执行mTencent.getAccessToken()不会报错，返回空值)
	 */
	private String getToken() {
		String token = mTencent.getAccessToken();
		return token;
	}
	
	/**
	 * 获取token(注意：需要登录后才能够获取token。但如果没有登录直接执行mTencent.getAccessToken()不会报错，返回空值)
	 */
	private String getOpenId() {
		String openid = mTencent.getOpenId();
		return openid;
	}

	/**
	 * 获取token有效期(请先登录)
	 */
	private String getExpiresTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, (int) mTencent.getExpiresIn() / 1000);
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(calendar.getTime());
		return date;
	}
	
	/**
	 * 获取所需参数
	 */
	private void getData() {
		Intent intent = getIntent();
		state = intent.getStringExtra("state");
	}

	/**
	 * 返回授权信息。
	 */
	private void result(int i, Intent intent) {
		intent.putExtra("state", state);
		this.setResult(i, intent);
		this.finish();
	}
}
