package cn.bidaround.youtui.social.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Description: created by qyj on January 7, 2014
 */
public class SMSActivity extends Activity {
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendSMS();
	}

	/**
	 * 填写短信内容并跳转到短信发送界面
	 */
	private void sendSMS() {// smscontent为短信的内容
		getData();
		Uri smsToUri = Uri.parse("smsto://");// 可以填写确定发送的电话号码，如"smsto://15889937371"
		Intent smsIntent = new Intent(android.content.Intent.ACTION_SENDTO,
				smsToUri);
		smsIntent.putExtra("sms_body", content);
		startActivity(smsIntent);
		finish();
	}

	/**
	 * 获取所需参数
	 */
	private void getData() {
		Intent intent = getIntent();
		content = intent.getStringExtra("content");
	}
}