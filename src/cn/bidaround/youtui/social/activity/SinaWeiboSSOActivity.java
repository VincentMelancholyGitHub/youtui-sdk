package cn.bidaround.youtui.social.activity;

import java.text.SimpleDateFormat;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Description: created by qyj on January 7, 2014
 */
public class SinaWeiboSSOActivity extends Activity {
	private String state;
	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	/**
	 * @see {@link Activity#onCreate}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		// 通过单点登录 (SSO) 获取 Token
		sso_login();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 创建微博实例
		mWeiboAuth = new WeiboAuth(this, YoutuiConstants.SINA_WEIBO_APP_ID,
				YoutuiConstants.SINA_WEIBO_REDIRECT_URL,
				YoutuiConstants.SINA_WEIBO_SCOPE);
	}

	/**
	 * SSO单点登录
	 */
	private void sso_login() {
		getData();
		mSsoHandler = new SsoHandler(SinaWeiboSSOActivity.this, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener());
	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示获取Token正常
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(mAccessToken
								.getExpiresTime()));
				Intent intent = new Intent();
				intent.putExtra("AccessToken", mAccessToken.getToken());
				intent.putExtra("ExpiresTime", String.valueOf(mAccessToken.getExpiresTime()));
				result(YoutuiConstants.RESULT_SUCCESSFUL, intent);
			} else {
				// 显示应用程序签名有误
				// 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
				String code = values.getString("code");
				Intent intent = new Intent();
				intent.putExtra("Code", code);
				result(YoutuiConstants.RESULT_SIGNATURE_ERROR, intent);
			}
		}

		@Override
		public void onCancel() {
			// 显示授权取消
			Intent intent = new Intent();
			intent.putExtra("Cancel", "授权取消");
			result(YoutuiConstants.RESULT_CANCEL, intent);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			// 显示授权其他错误
			String error = e.getMessage();
			Intent intent = new Intent();
			intent.putExtra("Auth_Exception", error);
			result(YoutuiConstants.RESULT_ERROR, intent);
		}
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
