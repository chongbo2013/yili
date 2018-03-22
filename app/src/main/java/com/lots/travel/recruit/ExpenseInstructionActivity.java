package com.lots.travel.recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.base.BaseActivity;
import com.lots.travel.recruit.model.ExpenseAdditional;
import com.lots.travel.recruit.widget.ExpenseItemsView;
import com.lots.travel.schedule.widget.AudioPlayer;
import com.lots.travel.schedule.widget.AudioRecorder;
import com.lots.travel.schedule.widget.DescriptionLayout;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.video.RequestRecordActivity;
import com.lots.travel.video.VideoUtil;
import com.lots.travel.widget.EditTextActivity;
import com.lots.travel.widget.ImageLoader;
import com.lots.travel.widget.images.Image;
import com.lots.travel.widget.images.ImagePickerActivity;
import com.lots.travel.widget.images.LookUpPictureActivity;

import java.util.List;

//旅费说明
public class ExpenseInstructionActivity extends BaseActivity implements View.OnClickListener,ImageLoader,DescriptionLayout.OnActionListener {
    public static final int REQ_ADD_IMAGES = 1;
    public static final int REQ_ADD_VIDEO = 2;
    public static final int REQ_EDIT_TEXT = 3;
    public static final int REQ_SCAN_IMAGES = 4;

    public static final int REQ_EDIT_PRICE = 5;

    private ScrollView vScroll;
    private ExpenseItemsView vExpenseInclude;
    private ExpenseItemsView vExpenseExclude;

    private DescriptionLayout vDescription;

    private View dPrice;
    private TextView tvPrice;

    private RequestOptions mRequestOptions;
    private AudioPlayer mAudioPlayer;
    private AudioRecorder mAudioRecorder;

    private ExpenseAdditional mAdditional;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_instruction);

        initData(getIntent(),savedInstanceState);
        initViews();
    }

    private void initData(Intent data,Bundle savedInstanceState){
        mAdditional = data.getParcelableExtra(EXPENSE_ADDITIONAL);
        if(savedInstanceState!=null)
            mAdditional = savedInstanceState.getParcelable(EXPENSE_ADDITIONAL);

        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        mAudioRecorder = new AudioRecorder();
        mAudioPlayer = new AudioPlayer();
    }

    private void initViews(){
        vScroll = (ScrollView) findViewById(R.id.v_scroll);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        //初始化item选取
        vExpenseInclude = (ExpenseItemsView) findViewById(R.id.v_expense_include);
        vExpenseExclude = (ExpenseItemsView) findViewById(R.id.v_expense_exclude);
        vExpenseInclude.setOnItemCheckChangedListener(new ExpenseItemsView.OnItemCheckChangedListener() {
            @Override
            public void onItemCheckChanged(int position, boolean checked) {
                vExpenseExclude.setItemEnabled(position,!checked);
            }
        });
        vExpenseExclude.setOnItemCheckChangedListener(new ExpenseItemsView.OnItemCheckChangedListener() {
            @Override
            public void onItemCheckChanged(int position, boolean checked) {
                vExpenseInclude.setItemEnabled(position,!checked);
            }
        });
        vExpenseInclude.setItemList(mAdditional.getIncludeExpenseItems());
        vExpenseExclude.setItemList(mAdditional.getExcludeExpenseItems());

        RadioGroup llTabs = (RadioGroup) findViewById(R.id.ll_tabs);
        llTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.tab_include){
                    vExpenseInclude.setVisibility(View.VISIBLE);
                    vExpenseExclude.setVisibility(View.INVISIBLE);
                }else{
                    vExpenseInclude.setVisibility(View.INVISIBLE);
                    vExpenseExclude.setVisibility(View.VISIBLE);
                }
            }
        });
        llTabs.check(R.id.tab_include);

        //初始化详情描述
        vDescription = (DescriptionLayout) findViewById(R.id.v_description);
        vDescription.setOnActionListener(this);
        vDescription.setVoiceControl(mAudioRecorder,mAudioPlayer);
        vDescription.setCurrentItem(mAdditional.getCurrentItem());

        vDescription.setText(mAdditional.getText());
        vDescription.setImageList(mAdditional.getImages());
        vDescription.setVoiceFilepath(mAdditional.getSoundFilepath(),0,mAdditional.getSoundLength());
        vDescription.setVideoSource(mAdditional.getVideoFilepath(),mAdditional.getVideoCover());

        //初始化差价
        dPrice = findViewById(R.id.d_price);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPrice.setOnClickListener(this);
        ToggleButton tbPrice = (ToggleButton) findViewById(R.id.tb_price);
        tbPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                mAdditional.setAddPrice(isChecked);
                dPrice.setVisibility(isChecked ? View.VISIBLE:View.GONE);
                tvPrice.setVisibility(isChecked ? View.VISIBLE:View.GONE);
                if(v.isChecked()){
                    vScroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        });
        tbPrice.setChecked(mAdditional.isAddPrice());
        if(mAdditional.isAddPrice()){
            tvPrice.setText(showPrice(mAdditional.getAddedPrice()+"",getString(R.string.unit_rmb)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXPENSE_ADDITIONAL,mAdditional);
    }

    @Override
    protected void onDestroy() {
        mAudioPlayer.stop();
        super.onDestroy();
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

            case R.id.tv_price:
                EditTextActivity.toEdit(this,REQ_EDIT_PRICE,
                        new EditTextActivity.Config()
                                .title(getString(R.string.instruction_expense_price_title))
                                .hint(getString(R.string.instruction_expense_price_hint))
                                .inputType(InputType.TYPE_CLASS_NUMBER));
                break;

            case R.id.btn_confirm:
                back(true);
                break;

            case R.id.btn_cancel:
                back(false);
                break;
        }
    }

    private void back(boolean success){
        if(success){
            Intent data = new Intent();
            data.putExtra(EXPENSE_ADDITIONAL,mAdditional);
            setResult(Activity.RESULT_OK,data);
        }else{
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void loadImage(String url, ImageView target) {
        Glide.with(this)
                .load(url)
                .apply(mRequestOptions)
                .into(target);
    }

    @Override
    public void onAction(int ac, Object data) {
        switch (ac){
            case DescriptionLayout.AC_CHANGE_TAB:
                mAdditional.setCurrentItem((Integer) data);
                vDescription.setCurrentItem((Integer) data);
                break;

            case DescriptionLayout.AC_ADD_IMAGES:
                ImagePickerActivity.toPick(this, REQ_ADD_IMAGES, 9-mAdditional.getImages().length, null);
                break;

            case DescriptionLayout.AC_ADD_VIDEO:
                RequestRecordActivity.toRecord(this,REQ_ADD_VIDEO,true);
                break;

            case DescriptionLayout.AC_EDIT_TEXT:
                EditTextActivity.toEdit(this,REQ_EDIT_TEXT,
                        new EditTextActivity.Config()
                        .text(mAdditional.getText())
                        .hint(getString(R.string.schedule_edit_text_hint))
                        .maxLength(100));

                break;

            case DescriptionLayout.AC_SCAN_IMAGES:
                LookUpPictureActivity.toLookUp(this, REQ_SCAN_IMAGES, true, (Integer) data, mAdditional.getImages());
                break;

            //语音长度大于0就有效
            case DescriptionLayout.AC_ADD_AUDIO:
                int length = (Integer) data;
                if(mAdditional.getSoundLength()!=length){
                    mAdditional.setSoundLength(length);
                }
                break;

            case DescriptionLayout.AC_DELETE_VIDEO:
                FileUtil.deleteFilesByPaths(mAdditional.getVideoFilepath(),mAdditional.getVideoCover());
                mAdditional.setVideoCover(null);
                mAdditional.setVideoFilepath(null);
                break;

            case DescriptionLayout.AC_PLAY_VIDEO:
                VideoUtil.play(this,mAdditional.getVideoFilepath());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode){
            case REQ_ADD_IMAGES:
                List<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGES);
                int srcLength = mAdditional.getImages().length;
                String[] strs = new String[srcLength+images.size()];
                if(srcLength>0)
                    System.arraycopy(mAdditional.getImages(),0,strs,0,srcLength);
                for (int i=0;i<images.size();i++){
                    strs[i+srcLength] = images.get(i).getPath();
                }
                mAdditional.setImages(strs);
                vDescription.setImageList(strs,9);
                break;

            case REQ_ADD_VIDEO:
                String videoPath = data.getStringExtra(RequestRecordActivity.VIDEO_PATH);
                String videoCoverPath = data.getStringExtra(RequestRecordActivity.VIDEO_COVER_PATH);
                mAdditional.setVideoFilepath(videoPath);
                mAdditional.setVideoCover(videoCoverPath);
                vDescription.setVideoSource(videoPath,videoCoverPath);
                break;

            case REQ_EDIT_TEXT:
                String text = data.getStringExtra(EditTextActivity.TEXT);
                mAdditional.setText(text);
                vDescription.setText(text);
                break;

            case REQ_SCAN_IMAGES:
                List<String> sPaths = data.getStringArrayListExtra(LookUpPictureActivity.OUTPUT_IMAGE_LIST);
                if(sPaths.size()!=mAdditional.getImages().length){
                    String[] aPaths = TypeUtil.list2array(sPaths);
                    mAdditional.setImages(aPaths);
                    vDescription.setImageList(aPaths,9);
                }
                break;

            case REQ_EDIT_PRICE:
                String number = data.getStringExtra(EditTextActivity.TEXT);
                if(TextUtils.isEmpty(number))
                    return;
                mAdditional.setAddedPrice(Integer.parseInt(number));
                tvPrice.setText(showPrice(number,getString(R.string.unit_rmb)));
                break;
        }

    }

    private SpannableStringBuilder showPrice(String value, String extra){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        int textColor,textSize;
        int end = 0;
        if(!TextUtils.isEmpty(value)){
            strBuilder.append(value);
            end = strBuilder.length();
            textColor = ContextCompat.getColor(this,R.color.color_main);
            textSize = getResources().getDimensionPixelSize(R.dimen.head_small);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if(!TextUtils.isEmpty(extra)){
            strBuilder.append(extra);
            textColor = ContextCompat.getColor(this,R.color.color_text);
            textSize = getResources().getDimensionPixelSize(R.dimen.fonts_tip);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strBuilder;
    }

    public static final String EXPENSE_ADDITIONAL = "ExpenseAdditional";

    public static void toInstruction(Activity context,int requestCode,ExpenseAdditional additional){
        Intent intent = new Intent(context,ExpenseInstructionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXPENSE_ADDITIONAL,additional);
        context.startActivityForResult(intent,requestCode);
    }

}
