package cn.bidaround.youtui.social.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Description: created by qyj on January 7, 2014
 */
public class RenrenSSOActivity extends Activity {
	private RennClient rennClient;
	private String state;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		getData();
		login();
	}

	/**
	 * 人人初始化
	 */
	private void init() {
		rennClient = RennClient.getInstance(this);
		rennClient.init(YoutuiConstants.RENREN_APP_ID,
				YoutuiConstants.RENREN_API_KEY,
				YoutuiConstants.RENREN_SECRET_KEY);
		rennClient.setScope(YoutuiConstants.RENREN_SCOPE);
		rennClient.setTokenType("bearer");
		rennClient.setLoginListener(new LoginListener() {
			@Override
			public void onLoginSuccess() {
				// TODO Auto-generated method stub
				// 显示获取Token正常
				Intent intent = new Intent();
				intent.putExtra("AccessToken", getToken());
				result(YoutuiConstants.RESULT_SUCCESSFUL, intent);
			}

			@Override
			public void onLoginCanceled() {
				// TODO Auto-generated method stub
				// 显示授权取消
				Intent intent = new Intent();
				intent.putExtra("Cancel", "授权取消");
				result(YoutuiConstants.RESULT_CANCEL, intent);
			}
		});
	}

	/**
	 * 人人登录
	 */
	private void login() {
		rennClient.login(RenrenSSOActivity.this);
	}

	/**
	 * 人人注销
	 */
	private void logout() {
		rennClient.logout();
	}

	/**
	 * 获取token(请先登录)
	 */
	private String getToken() {
		String token = rennClient.getAccessToken().accessToken;
		return token;
	}

	/**
	 * 获取token有效期(请先登录)
	 */
	private String getExpiresTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,
				(int) rennClient.getAccessToken().expiresIn);
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
		logout();
		this.finish();
	}
}