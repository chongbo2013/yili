package com.lots.travel.main.info.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.UpdateUserInfo;
import com.lots.travel.network.model.result.EditUserInfo;
import com.lots.travel.network.service.UserService;
import com.lots.travel.store.FileStore;
import com.lots.travel.store.db.Account;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.SosonaOssClient;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.util.TakePictureUtil;
import com.lots.travel.util.TimeUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.ChoosePictureDialog;
import com.lots.travel.widget.EditUserLayout;
import com.lots.travel.widget.SimpleTextWatcher;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.blurry.Blurry;

public class EditUserInfoActivity extends RxBaseActivity implements View.OnClickListener,SimpleTextWatcher.OnTextListener {

    public static void toEdit(Fragment fragment,int requestCode){
        Intent intent = new Intent(fragment.getContext(),EditUserInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        fragment.startActivityForResult(intent,requestCode);
    }

    public static void toEdit(Activity context,int requestCode){
        Intent intent = new Intent(context,EditUserInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivityForResult(intent,requestCode);
    }

    public static final int REQ_PHOTO = 0;

    public static final int REQ_ALBUM = 1;

    public static final int REQ_CLIP = 2;

    private LinearLayout llUserInfo;

    private TextView tvTitle;
    private ImageView ivBackground;
    private ImageView ivPortrait;
    private TextView tvName;
    //按钮
    private TextView tvSave;

    private EditUserLayout vNickname,vSex,vBirth,vConstellation;

    private EditUserLayout vEmotion,vHeight,vWeight;

    private EditUserLayout vCompany,vProfession,vSchool,vDepartment,vLocation;

    private EditUserLayout vMail,vQQ;

    private ChoosePictureDialog dlgChoosePicture;
    private ChooseSexDialog dlgChooseSex;
    private ChooseBirthDialog dlgChooseBirth;
    private ChooseAreaDialog dlgChooseArea;
    private ChooseEmotionDialog dlgChooseEmotion;

    private Account mAccount;
    private boolean mPortraitChanged;
    private String mPictureName;
    private int mYear,mMonth,mDay;

    private SosonaOssClient mOssClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        SystemBarCompat.fullscreen(this);

        mAccount = new Account(AccountManager.getInstance().getAccount());
        if(savedInstanceState!=null)
            mAccount = savedInstanceState.getParcelable("account");

        initView();

        update(mAccount);

        mOssClient = new SosonaOssClient(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("account",mAccount);
    }

    private void initView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llUserInfo = (LinearLayout) findViewById(R.id.ll_user_info);

        ivBackground = (ImageView) findViewById(R.id.iv_background);
        ivPortrait = (ImageView) findViewById(R.id.iv_portrait);
        ivPortrait.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tv_name);
        findViewById(R.id.iv_back).setOnClickListener(this);

        tvSave = (TextView) findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);

        tvSave.setEnabled(false);

        vNickname = (EditUserLayout) findViewById(R.id.v_nickname);
        vNickname.addTextWatcher(new SimpleTextWatcher(R.id.v_nickname,this));
        vSex = (EditUserLayout) findViewById(R.id.v_sex);
        vSex.addTextWatcher(new SimpleTextWatcher(R.id.v_sex,this));
        vSex.setOnClickListener(this);
        vBirth = (EditUserLayout) findViewById(R.id.v_birth);
        vBirth.addTextWatcher(new SimpleTextWatcher(R.id.v_birth,this));
        vBirth.setOnClickListener(this);
        vConstellation = (EditUserLayout) findViewById(R.id.v_constellation);

        vEmotion = (EditUserLayout) findViewById(R.id.v_emotion);
        vEmotion.addTextWatcher(new SimpleTextWatcher(R.id.v_emotion,this));
        vEmotion.setOnClickListener(this);
        vHeight = (EditUserLayout) findViewById(R.id.v_height);
        vWeight = (EditUserLayout) findViewById(R.id.v_weight);

        vCompany = (EditUserLayout) findViewById(R.id.v_company);
        vProfession = (EditUserLayout) findViewById(R.id.v_profession);
        vProfession.addTextWatcher(new SimpleTextWatcher(R.id.v_profession,this));
        vSchool = (EditUserLayout) findViewById(R.id.v_school);
        vDepartment = (EditUserLayout) findViewById(R.id.v_department);
        vLocation = (EditUserLayout) findViewById(R.id.v_location);
        vLocation.addTextWatcher(new SimpleTextWatcher(R.id.v_location,this));
        vLocation.setOnClickListener(this);

        vMail = (EditUserLayout) findViewById(R.id.v_mail);
        vMail.addTextWatcher(new SimpleTextWatcher(R.id.v_mail,this));
        vQQ = (EditUserLayout) findViewById(R.id.v_qq);
        vQQ.addTextWatcher(new SimpleTextWatcher(R.id.v_qq,this));

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int totalRange = appBarLayout.getTotalScrollRange();
                if(totalRange==0)
                    return;

                float alpha = Math.abs(verticalOffset*1f/totalRange);
                llUserInfo.setAlpha(1-alpha);
                tvTitle.setAlpha(alpha);

            }
        });

        dlgChoosePicture = new ChoosePictureDialog(this, new ChoosePictureDialog.OnChooseListener() {
            @Override
            public void onTakePhoto() {
                mPictureName = TakePictureUtil.genName();
                TakePictureUtil.takeFromCamera(EditUserInfoActivity.this,REQ_PHOTO,new FileStore().getCacheImageDir().getAbsolutePath(),mPictureName);
            }

            @Override
            public void onTakeAlbum() {
                TakePictureUtil.takeFromAlbum(EditUserInfoActivity.this,REQ_ALBUM,getResources().getString(R.string.info_album_title));
            }
        });

        dlgChooseSex = new ChooseSexDialog(this, new ChooseSexDialog.OnChooseListener() {
            @Override
            public void onChoose(int sex) {
                String sexStr = Integer.toString(sex);
                mAccount.setSex(sexStr);
                setSex(sexStr);
            }
        });

        dlgChooseBirth = new ChooseBirthDialog(this, new ChooseBirthDialog.OnChooseListener() {
            @Override
            public void onChoose(int year, int month, int day) {
                mYear = year;
                mMonth = month;
                mDay = day;
                mAccount.setBirthday(String.format(Locale.ENGLISH,"%d-%02d-%02d",year,month+1,day));
                setBirth(year, month, day);
            }
        });

        dlgChooseEmotion = new ChooseEmotionDialog(this, new ChooseEmotionDialog.OnChooseListener() {
            @Override
            public void onChoose(int emotion) {
                mAccount.setEmotion(Integer.toString(emotion));
                setEmotion(emotion);
            }
        });

        dlgChooseArea = new ChooseAreaDialog(this, new ChooseAreaDialog.OnChooseListener() {
            @Override
            public void onChoose(String province, String city, String district) {
                mAccount.setProvince(province);
                mAccount.setCity(city);
                mAccount.setDistrict(district);
                setLocation(province, city, district);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK) {
            return;
        }

        switch (requestCode){
            case REQ_PHOTO: {
                String srcPath = new FileStore().getCacheImageDir().getAbsolutePath() + File.separator + mPictureName;
                mPictureName = clipPicture(srcPath);
                break;
            }

            case REQ_ALBUM: {
                String srcPath = FileUtil.getFilepath(this, data.getData());
                mPictureName = clipPicture(srcPath);
                break;
            }

            case REQ_CLIP: {
                String path = new FileStore().getCacheImageDir()+File.separator+mPictureName;
                mAccount.setPortraitUrl(path);
                mPortraitChanged = true;
                loadImage(path);
                break;
            }
        }

    }

    private String clipPicture(String srcPath){
        //压缩
        Bitmap bitmap = FileUtil.compress(srcPath, 400, 400);
        if(bitmap==null)
            return null;
        //存储
        String name = TakePictureUtil.genName();
        String dstPath = new FileStore().getCacheImageDir().getAbsolutePath() + File.separator + name;
        FileUtil.saveBitmap(new File(dstPath), bitmap);
        //调用系统剪切
        srcPath = dstPath;
        name = TakePictureUtil.genName();
        dstPath = new FileStore().getCacheImageDir().getAbsolutePath() + File.separator + name;
        TakePictureUtil.clipPhoto(this, REQ_CLIP, srcPath, Uri.fromFile(new File(dstPath)), 400, 400);
        return name;
    }

    private  void  loadImage(String url){
        RequestOptions backgroundOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        if (TextUtils.isEmpty(url))
            return;

        Glide.with(this)
                .asBitmap()
                .load(url)
                .apply(backgroundOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        ivPortrait.setImageBitmap(resource);
                        Blurry.with(EditUserInfoActivity.this)
                                .color(0x5F23B6B7)
                                .from(resource)
                                .into(ivBackground);
                    }
                });
    }

    private void setSex(String sex){
        int strId = R.string.secret;
        if(!TextUtils.isEmpty(sex)){
            if (sex.equals("1")){
                strId = R.string.male;
            } else if(sex.equals("2")) {
                strId = R.string.female;
            }
        }
        vSex.setText(strId);
    }

    private void setBirth(int year,int month,int day){
        if(year==-1 || month==-1 || day==-1){
            vBirth.setText("");
            vConstellation.setText("");
            return;
        }

        vBirth.setText(String.format(Locale.getDefault(),"%d-%02d-%02d",year,month+1,day));
        vConstellation.setText(TimeUtil.getConstellation(month, day));
    }

    public void setEmotion(int emotion){
        switch (emotion){
            case 0:
                vEmotion.setText(R.string.emotion_single);
                break;

            case 1:
                vEmotion.setText(R.string.emotion_married);
                break;

            case 2:
                vEmotion.setText(R.string.emotion_divorce);
                break;
        }
    }

    public void setLocation(String province,String city,String district){
        vLocation.setText(TypeUtil.mergeStrings(province,city,district));
    }

    private void update(Account account){
        String str;
        loadImage(account.getPortraitUrl());

        tvName.setText(account.getRealname());
        vNickname.setText(account.getUsername());

        str = account.getSex();
        setSex(str);

        //一起赋值
        str = account.getBirthday();
        if(!TextUtils.isEmpty(str)){
            try{
                String[] date = str.split("-");
                mYear = Integer.parseInt(date[0]);
                mMonth = Integer.parseInt(date[1])-1;
                mDay = Integer.parseInt(date[2]);
                setBirth(mYear,mMonth,mDay);
            }catch (Exception e){
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                setBirth(-1,-1,-1);
                Log.e(EditUserInfoActivity.class.getName(),e.toString());
            }
        }else{
            Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            setBirth(-1,-1,-1);
        }

        int emotion = -1;
        str = account.getEmotion();
        if(!TextUtils.isEmpty(str)){
            try{
                emotion = Integer.parseInt(str);
            }catch (Exception e){
                Log.d(EditUserInfoActivity.class.getName(),e.toString());
            }
        }
        setEmotion(emotion);

        vHeight.setText(account.getHeight()!=0
                ? Integer.toString(account.getHeight()):"");
        vWeight.setText(account.getWeight()!=0
                ? Integer.toString(account.getWeight()):"");

        vCompany.setText(account.getCompany());
        vProfession.setText(account.getProfession());
        vSchool.setText(account.getSchool());
        vProfession.setText(account.getProfession());
        vDepartment.setText(account.getDepartment());
        setLocation(account.getProvince(), account.getCity(), account.getDistrict());

        vMail.setText(account.getMail());
        vQQ.setText(account.getQq());
    }

    private void back(boolean success){
        setResult(success ?
                Activity.RESULT_OK:Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        back(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(false);
                break;

            case R.id.iv_portrait:
                dlgChoosePicture.show();
                break;

            case R.id.tv_save:
                refresh(mAccount);
                commit(mAccount);
                break;

            case R.id.v_sex:
                dlgChooseSex.show();
                break;

            case R.id.v_birth:
                dlgChooseBirth.show(mYear,mMonth,mDay);
                break;

            case R.id.v_emotion:
                dlgChooseEmotion.show();
                break;

            case R.id.v_location:
                dlgChooseArea.show(mAccount.getProvince(),mAccount.getCity(),mAccount.getDistrict());
                break;
        }
    }

    private boolean isEnabled(){
        return !vNickname.isEmpty() && !vBirth.isEmpty()
                && !vSex.isEmpty() && !vEmotion.isEmpty()
                && !vProfession.isEmpty() && !vLocation.isEmpty();
    }

    //获取控件数据刷新account
    private void refresh(Account account){
        String str;
        account.setUsername(vNickname.getText());

        str = vHeight.getText();
        if(str!=null){
            try {
                account.setHeight(Integer.parseInt(str));
            }catch (Exception e){
                Log.e(EditUserInfoActivity.class.getName(),e.toString());
            }
        }

        str = vWeight.getText();
        if(str!=null){
            try {
                account.setWeight(Integer.parseInt(str));
            }catch (Exception e){
                Log.e(EditUserInfoActivity.class.getName(),e.toString());
            }
        }

        account.setCompany(vCompany.getText());
        account.setProfession(vProfession.getText());
        account.setSchool(vSchool.getText());
        account.setDepartment(vDepartment.getText());

        account.setMail(vMail.getText());
        account.setQq(vQQ.getText());
    }

    private void commit(final Account account){
        Single.create(
                new SingleOnSubscribe<EditUserInfo>() {
                    @Override
                    public void subscribe(@io.reactivex.annotations.NonNull SingleEmitter<EditUserInfo> e) throws Exception {
                        UpdateUserInfo params = new UpdateUserInfo();
                        //上传图片
                        if(mPortraitChanged){
                            String url = mOssClient.uploadImage(EditUserInfoActivity.this,SosonaOssClient.ITEM_TYPE_ICON,mAccount.getPortraitUrl(),false);
                            if(url!=null){
                                params.setFaceImg(url);
                            }
                        }
                        params.setUserName(account.getUsername());
                        params.setSex(account.getSex());
                        params.setBirthday(account.getBirthday());
                        params.setHeight(account.getHeight());
                        params.setWeight(account.getWeight());
                        params.setConstellation(vConstellation.getText());
                        params.setMaritalStatus(account.getEmotion());
                        params.setMail(account.getMail());
                        params.setQq(account.getQq());
                        params.setCompany(account.getCompany());
                        params.setTitle(account.getProfession());
                        params.setSchool(account.getSchool());
                        params.setDepartment(account.getDepartment());
                        params.setProvince(account.getProvince());
                        params.setCity(account.getCity());
                        params.setDistrict(account.getDistrict());
                        params.setPhone(account.getMobilePhone());

                        try{
                            HttpResult<EditUserInfo> result = ServiceGenerator.createService(EditUserInfoActivity.this,UserService.class)
                                    .updateUserInfo(params)
                                    .blockingGet();
                            e.onSuccess(result.getData());
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }})
                .compose(this.<EditUserInfo>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<EditUserInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull EditUserInfo s) {
                        Account current = AccountManager.getInstance().getAccount();
                        current.update(s);
                        AccountManager.getInstance().update(current);

                        dismissProgressDialog();
                        back(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissProgressDialog();
                        new DefaultExceptionHandler().handleException(EditUserInfoActivity.this,e);
                    }
                });
    }

    @Override
    public void onTextChanged(int id,String text) {
        switch (id){
            case R.id.v_nickname:
            case R.id.v_sex:
            case R.id.v_birth:
            case R.id.v_emotion:
            case R.id.v_profession:
            case R.id.v_location:
                tvSave.setEnabled(isEnabled());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        dlgChooseSex.dismiss();
        dlgChoosePicture.dismiss();
        super.onDestroy();
    }
}
