package com.lots.travel.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;

import com.lots.travel.main.MainActivity;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.util.WeChatUtil;
import com.lots.travel.wxapi.WXEntryActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * PhoneLoginActivity  -  手机登录
 * WXEntryActivity     -  微信登录
 */
public class LoginActivity extends RxBaseActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parseIntent(getIntent());

		setContentView(R.layout.activity_login);
		SystemBarCompat.fullscreen(this,findViewById(R.id.rl_main),true);

		findViewById(R.id.btn_wechat).setOnClickListener(this);
		findViewById(R.id.btn_phone).setOnClickListener(this);
		findViewById(R.id.tv_terms).setOnClickListener(this);
		findViewById(R.id.tv_privacy).setOnClickListener(this);

		checkPermissions();
	}

	private static final int REQ_CODE_PERMISSIONS = 100;
	@AfterPermissionGranted(REQ_CODE_PERMISSIONS)
	private void checkPermissions() {
		String[] perms = {
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.CAMERA,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.ACCESS_FINE_LOCATION
		};
		if (EasyPermissions.hasPermissions(this, perms)) {
			// Already have permission, do the thing
			// ...
		} else {
			// Do not have permissions, request them now
			EasyPermissions.requestPermissions(this, getString(R.string.permission_audio_record), REQ_CODE_PERMISSIONS, perms);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		parseIntent(intent);
	}

	@Override
	public void onClick(View v) {
		Intent intent;

		switch(v.getId()) {
			case R.id.btn_phone:
				intent = new Intent(this,PhoneLoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				break;

			case R.id.btn_wechat:
				if(WeChatUtil.isWeChatAvailable(this))
					WXEntryActivity.toLogin(this);
				else
					Toast.makeText(this,R.string.login_wechat_noclient,Toast.LENGTH_SHORT).show();
				break;

			case R.id.tv_terms:
				;
				break;

			case R.id.tv_privacy:
				;
				break;
		}
	}

	private static final String EXTRA_TO_MAIN = "toMain";
	public static void toMain(Context context){
		Intent intent = new Intent(context,LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(EXTRA_TO_MAIN,true);
		context.startActivity(intent);
	}

	private void parseIntent(Intent intent){
		boolean toMain = intent.getBooleanExtra(EXTRA_TO_MAIN,false);
		if(toMain){
			Intent dstIntent = new Intent(this, MainActivity.class);
			dstIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(dstIntent);
			finish();
		}
	}

}