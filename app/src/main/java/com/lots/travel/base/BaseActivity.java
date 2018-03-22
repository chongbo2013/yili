package com.lots.travel.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.login.LoginActivity;

/**
 * 后期考虑增加三个抽象方法：
 *     void initArgs(Intent data,Bundle savedInstanceState)
 *     void initViews()
 *     void loadData()
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog loadingDialog;

    public void showProgressDialog(){
        showProgressDialog(getString(R.string.load_ing));
    }

    public void showProgressDialog(String msg){
        msg = TextUtils.isEmpty(msg) ?
                getString(R.string.load_ing):msg;
        showProgressDialog(msg,false);
    }

    public void showProgressDialog(boolean cancelable){
        showProgressDialog(getString(R.string.load_ing),cancelable);
    }

    public void showProgressDialog(String msg,boolean cancelable){
        if(loadingDialog ==null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setIndeterminate(true);
        }
        loadingDialog.setCancelable(cancelable);
        loadingDialog.setMessage(msg);
        loadingDialog.show();
    }

    public void dismissProgressDialog(){
        if(loadingDialog !=null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    private ExitHelper exitHelper;

    private AlertDialog exitDialog;

    public void showExitDialog(){
        if(exitDialog==null){
            exitDialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.exit_token_invalidate)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //清除当前登录用户
                            AccountManager.getInstance().logout();

                            //发送退出广播，finish所有页面
                            ExitHelper.exit();

                            //启动登录页面
                            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create();
        }
        exitDialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exitHelper = new ExitHelper(this);
        exitHelper.register();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();

        if(exitDialog!=null)
            exitDialog.dismiss();

        exitHelper.unregister();
        super.onDestroy();
    }

}
