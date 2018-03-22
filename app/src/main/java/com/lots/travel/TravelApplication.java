package com.lots.travel;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.alivc.player.AliVcMediaPlayer;
import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.common.logger.Logger;
import com.aliyun.downloader.DownloaderManager;
import com.duanqu.qupai.jni.ApplicationGlue;
import com.lots.travel.network.ObtainInterfaceFileTask;
import com.lots.travel.store.db.GreenDaoManager;

import io.rong.imkit.RongIM;
import io.rong.push.RongPushClient;

/**
 * Created by nalanzi on 2017/9/2.
 */

public class TravelApplication extends MultiDexApplication {

	private static TravelApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		if (!getApplicationInfo().packageName
				.equals(getCurProcessName(getApplicationContext())))
			return;

		mInstance = this;

		//数据库
		GreenDaoManager.getInstance().init(this);

		//获取接口版本、接口文件
		new ObtainInterfaceFileTask().execute();

		//趣拍
		DownloaderManager.getInstance().init(this);
		System.loadLibrary("openh264");
		System.loadLibrary("encoder");
		System.loadLibrary("AliFaceAlignmentModule");
		System.loadLibrary("aliface_jni");
		System.loadLibrary("QuCore-ThirdParty");
		System.loadLibrary("QuCore");
		QupaiHttpFinal.getInstance().initOkHttpFinal();
		Logger.setDebug(true);

		//阿里云推流sdk
		System.loadLibrary("gnustl_shared");
		System.loadLibrary("qupai-media-thirdparty");
		System.loadLibrary("qupai-media-jni");
		ApplicationGlue.initialize(this);

		//播放器sdk
		AliVcMediaPlayer.init(this);

		//融云
		RongIM.init(this);
	}

	private static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	public static TravelApplication getInstance() {
		return mInstance;
	}

}
