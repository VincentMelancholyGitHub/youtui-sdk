package com.bidaround.youtui.outer.helper;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class BaseUIListener implements IUiListener {
	private Context mContext;
	private String mScope;
	private Handler handler = new Handler();

	public BaseUIListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public BaseUIListener(Context mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
	}

	@Override
	public void onComplete(final JSONObject response) {

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Util.showResultDialog(mContext, response.toString(),
						"onComplete");
				Util.dismissDialog();
			}
		});

	}

	@Override
	public void onError(final UiError e) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Util.showResultDialog(mContext, "errorMsg:" + e.errorMessage
						+ "errorDetail:" + e.errorDetail, "onError");
				Util.dismissDialog();
			}
		});

	}

	@Override
	public void onCancel() {

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Util.toastMessage((Activity) mContext, "onCancel");
			}
		});

	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}
