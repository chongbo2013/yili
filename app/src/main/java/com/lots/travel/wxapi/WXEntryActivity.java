package com.lots.travel.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.login.LoginActivity;

import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.wxapi.task.WeChatLoginTask;

public class WXEntryActivity extends RxBaseActivity implements WeChatLoginTask.View {
	private WeChatLoginTask.Presenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxentry);
		SystemBarCompat.fullscreen(this,findViewById(R.id.fl_content),true);

		presenter = WeChatLoginTask.create(this,this);
		presenter.initWXAPI(this);

		if(isLogin()){
			presenter.executeWeChatAuth();
		}else {
			boolean ret = presenter.handleWeChatAuthResult(getIntent());
			if(!ret)
				presenter.executeWeChatAuth();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		presenter.handleWeChatAuthResult(intent);
	}

	private boolean isLogin(){
		return getIntent().getBooleanExtra(EXTRA_LOGIN,false);
	}

	private static final String EXTRA_LOGIN = "login";
	public static void toLogin(Context context){
		Intent intent = new Intent(context,WXEntryActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(EXTRA_LOGIN,true);
		context.startActivity(intent);
	}

	@Override
	public void showLoadingDialog(String msg) {
		showProgressDialog(msg);
	}

	@Override
	public void dismissLoadingDialog() {
		dismissProgressDialog();
	}

	@Override
	public void finishActivity() {
		finish();
	}

	@Override
	public void toMainActivity() {
		LoginActivity.toMain(this);
	}

	@Override
	public void sharingSuccess()
	{
		Toast.makeText(this,R.string.sharing_success,Toast.LENGTH_SHORT).show();
		finish();
	}
}