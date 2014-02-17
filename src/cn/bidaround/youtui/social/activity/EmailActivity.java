package cn.bidaround.youtui.social.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Description: created by qyj on January 7, 2014
 */
public class EmailActivity extends Activity {
	private String subject;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendEmail();
	}

	/**
	 * 填写邮件内容并跳转到邮件发送界面
	 */
	private void sendEmail() {
		getData();
		Uri uri = Uri.parse("mailto:");// 可以直接填写确定的收件人，如"mailto:1353015987@qq.com"
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		// it.putExtra(Intent.EXTRA_EMAIL, email_address);// 收件人（一定要为String[]）
		it.putExtra(Intent.EXTRA_SUBJECT, subject); // 邮件的主题
		it.putExtra(Intent.EXTRA_TEXT, content);// 邮件的内容
		startActivity(it);
		finish();
	}

	/**
	 * 获取所需参数
	 */
	private void getData() {
		Intent intent = getIntent();
		subject = intent.getStringExtra("subject");
		content = intent.getStringExtra("content");
	}
}