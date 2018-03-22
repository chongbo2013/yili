package com.lots.travel.main.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.base.ExitHelper;
import com.lots.travel.login.LoginActivity;
import com.lots.travel.main.info.mine.EditUserInfoActivity;
import com.lots.travel.store.FileStore;
import com.lots.travel.util.FileUtil;

import java.io.File;

/**
 * Created by nalanzi on 2017/9/26.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener,Switch.OnCheckedChangeListener{

    public static void toSetting(Fragment fragment,int requestCode){
        Intent intent = new Intent(fragment.getContext(),SettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        fragment.startActivityForResult(intent,requestCode);
    }

    private static final int REQ_EDIT_INFO = 1;

    private TextView tvCache;
    private boolean mUserInfoChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.iv_back).setOnClickListener(this);

        findViewById(R.id.tv_personal).setOnClickListener(this);
        findViewById(R.id.tv_address).setOnClickListener(this);
        findViewById(R.id.tv_certification).setOnClickListener(this);

        findViewById(R.id.fl_cache).setOnClickListener(this);
        findViewById(R.id.tv_about).setOnClickListener(this);
        ((Switch)findViewById(R.id.switch_message_sound)).setOnCheckedChangeListener(this);
        ((Switch)findViewById(R.id.switch_message_vibrate)).setOnCheckedChangeListener(this);
        findViewById(R.id.tv_exit).setOnClickListener(this);

        tvCache = (TextView) findViewById(R.id.tv_cache);

        calculateCacheSize();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        setResult(mUserInfoChanged ?
                Activity.RESULT_OK:Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.iv_back:
                back();
                break;

            case R.id.tv_personal:
                EditUserInfoActivity.toEdit(this,REQ_EDIT_INFO);
                break;

            case R.id.tv_address:
                intent = new Intent(this,ShippingAddressListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            case R.id.tv_certification:
                String authStatus = AccountManager.getInstance().getAccount().getAuthStatus();
                CheckCertificationActivity.toCheck(this,authStatus!=null && "1".equals(authStatus),false);
                break;

            case R.id.fl_cache:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.cache_clean)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cleanCache();
                                tvCache.setText("0KB");
                            }
                        })
                        .show();
                break;

            case R.id.tv_about:
                intent = new Intent(this,AboutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            case R.id.tv_exit:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.exit_app)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //清除当前登录用户
                                AccountManager.getInstance().logout();

                                //清除缓存
                                cleanCache();

                                //发送退出广播，finish所有页面
                                ExitHelper.exit();

                                //启动登录页面
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_EDIT_INFO){
            mUserInfoChanged = resultCode==Activity.RESULT_OK;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_message_sound:
                ;
                break;

            case R.id.switch_message_vibrate:
                ;
                break;
        }
    }

    private void calculateCacheSize(){
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getFilesDir();
        File cacheDir = getCacheDir();
//        File externalCacheDir = getExternalCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        //不考虑该目录，存储的是aliyun视频编辑所需的相关素材
        //fileSize += FileUtil.getDirSize(externalCacheDir);

        fileSize += new FileStore().getCacheSize();

        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        tvCache.setText(cacheSize);
    }

    private void cleanCache(){
        FileUtil.deleteFilesByDirectory(getCacheDir());
        FileUtil.deleteFilesByDirectory(getExternalCacheDir());
        //不考虑该目录，存储的是aliyun视频编辑所需的相关素材
        //FileUtil.deleteFilesByDirectory(new FileStore().getCacheDir());
    }

}
