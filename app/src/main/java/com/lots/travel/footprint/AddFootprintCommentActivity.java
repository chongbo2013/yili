package com.lots.travel.footprint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.footprint.model.CommentParams;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.ImageItemAdapter;
import com.lots.travel.footprint.widget.SpaceItemDecoration;
import com.lots.travel.main.info.mine.model.FollowNote;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.AddComment;
import com.lots.travel.network.model.request.AddZan;
import com.lots.travel.network.model.result.Comments;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.video.VideoUtil;
import com.lots.travel.widget.ImageLoader;
import com.lots.travel.widget.PartsLayoutManager;
import com.lots.travel.widget.RoundImageView;
import com.lots.travel.widget.TweetBottomView;
import com.lots.travel.widget.images.LookUpPictureActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/5.
 */

public class AddFootprintCommentActivity extends RxBaseActivity implements ImageLoader, View.OnClickListener,AddFootprintCommentFragment.OnChooseTargetListener {
    private static final String COMMENT_PARAMS = "CommentParams";

    public static final String COMMENT_COUNT = "CommentCount";
    public static final String ZAN_COUNT = "ZanCount";

    public static void toComment(Fragment fragment, int requestCode, Footprint footprint){
        Intent intent = new Intent(fragment.getContext(),AddFootprintCommentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(COMMENT_PARAMS,new CommentParams(footprint));
        fragment.startActivityForResult(intent,requestCode);
    }

    public static void toComment(Fragment fragment, int requestCode, FollowNote followNote){
        Intent intent = new Intent(fragment.getContext(),AddFootprintCommentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(COMMENT_PARAMS,new CommentParams(followNote));
        fragment.startActivityForResult(intent,requestCode);
    }

    private CommentParams mCommentParams;
    private RequestOptions mRequestOptions;

    private TextView tvComment,tvPraise;
    private ImageItemAdapter mImageAdapter;

    private TweetBottomView vTweetBottom;

    private AddFootprintCommentFragment mFragment;
    private Comments.Item mTargetComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_footprint_comment);
        initArgs(getIntent(),savedInstanceState);
        initViews();
    }

    private void initArgs(Intent data,Bundle savedInstanceState){
        mCommentParams = data.getParcelableExtra(COMMENT_PARAMS);
        if(savedInstanceState!=null)
            mCommentParams = savedInstanceState.getParcelable(COMMENT_PARAMS);
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(COMMENT_PARAMS,mCommentParams);
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(mCommentParams.getEventStyle().equals(Footprint.NOTE) ?
                R.string.footprint_comment_note:R.string.footprint_comment_footprint);
        //footprint显示
        RoundImageView ivCover = (RoundImageView) findViewById(R.id.iv_cover);
        ivCover.setOnClickListener(this);
        RecyclerView rvImages = (RecyclerView) findViewById(R.id.rv_images);
        TextView tvShortDesc = (TextView) findViewById(R.id.tv_short_desc);
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);

        int space = DensityUtil.dp2px(this, 10);
        rvImages.setLayoutManager(new PartsLayoutManager(this, rvImages, 3, space * 2));
        mImageAdapter = new ImageItemAdapter(this);
        rvImages.setAdapter(mImageAdapter);
        rvImages.addItemDecoration(new SpaceItemDecoration(space));
        mImageAdapter.setOnItemClickListener(new ImageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, String[] images) {
                processClick(mCommentParams, pos);
            }
        });

        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvComment.setOnClickListener(this);
        tvPraise = (TextView) findViewById(R.id.tv_praise);
        tvPraise.setOnClickListener(this);


        //如果有video就显示video，否则显示images，在不同情况下的点击事件的处理也是不同的
        String[] images = TypeUtil.str2arrays(mCommentParams.getTripImgs());
        if (TextUtils.isEmpty(mCommentParams.getTripVideo())) {
            if (images.length == 0) {
                ivCover.setVisibility(View.INVISIBLE);
                ivCover.setTagVisible(false);
                rvImages.setVisibility(View.GONE);
            } else {
                ivCover.setVisibility(View.VISIBLE);
                ivCover.setTagVisible(false);
                loadImage(images[0], ivCover);
                rvImages.setVisibility(images.length >= 3 ? View.VISIBLE : View.GONE);
                mImageAdapter.setImageList(1, images);
            }
        } else {
            ivCover.setVisibility(View.VISIBLE);
            ivCover.setTagVisible(true);
            String[] videoImages = TypeUtil.str2arrays(mCommentParams.getTripVideoImg());
            if (videoImages != null && videoImages.length > 0)
                loadImage(videoImages[0], ivCover);
            if (images.length >= 2) {
                rvImages.setVisibility(View.VISIBLE);
                mImageAdapter.setImageList(0, images);
            } else {
                rvImages.setVisibility(View.GONE);
            }
        }

        String str = mCommentParams.getGpsAddress();
        tvLocation.setText(str);
        tvLocation.setVisibility(TextUtils.isEmpty(str) ? View.INVISIBLE : View.VISIBLE);

        str = mCommentParams.getTripTitle();
        tvShortDesc.setText(str);
        tvShortDesc.setVisibility(TextUtils.isEmpty(str) ? View.GONE : View.VISIBLE);

        showComment();
        showZan();

        vTweetBottom = (TweetBottomView) findViewById(R.id.v_tweet_bottom);
        vTweetBottom.setOnTweetListener(new TweetBottomView.OnTweetListener() {
            @Override
            public void onSend(String text) {
                AddComment params = new AddComment();
                params.setDataTable(mCommentParams.getEventStyle());
                params.setDataKey(mCommentParams.getId());
                params.setEventType(AddComment.COMMENT_EVENT_PJ);
                params.setUserId(AccountManager.getInstance().getUserId());
                params.setStyle(AddComment.STYLE_ADD);
                params.setContent(text);
                if(mTargetComment!=null) {
                    params.setReplyId(mTargetComment.getId());
                    params.setReplyUserId(mTargetComment.getUserId()+"");
                }
                ServiceGenerator.createService(AddFootprintCommentActivity.this,CommonService.class)
                        .addComment(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<HttpResult<String>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                showProgressDialog();
                            }

                            @Override
                            public void onSuccess(@NonNull HttpResult<String> result) {
                                dismissProgressDialog();
                                vTweetBottom.clear();
                                mCommentParams.incComment();

                                showComment();
                                mFragment.refreshComments(mCommentParams.getCommentTotles());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                dismissProgressDialog();
                            }
                        });
            }
        });
        vTweetBottom.setHint("请输入评论内容");

        //评论部分
        mFragment = AddFootprintCommentFragment.create(mCommentParams);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, mFragment)
                .commit();
    }

    private void showComment(){
        tvComment.setText(String.format(Locale.getDefault(), "%d", mCommentParams.getCommentTotles()));
        Drawable drawable = ContextCompat.getDrawable(this, mCommentParams.getHasZan() == 1 ?
                R.drawable.tag_comment : R.drawable.tag_comment);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvComment.setCompoundDrawables(drawable, null, null, null);
    }

    private void showZan(){
        tvPraise.setText(String.format(Locale.getDefault(), "%d", mCommentParams.getZanTotles()));
        Drawable drawable = ContextCompat.getDrawable(this, mCommentParams.getHasZan() == 1 ?
                R.drawable.tag_praise_en : R.drawable.tag_praise_dis);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvPraise.setCompoundDrawables(drawable, null, null, null);
    }

    private void back(){
        Intent data = new Intent();
        data.putExtra(COMMENT_COUNT,mCommentParams.getCommentTotles());
        data.putExtra(ZAN_COUNT,mCommentParams.getZanTotles());
        setResult(Activity.RESULT_OK,data);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void loadImage(String url, ImageView target) {
        Glide.with(this)
                .load(url)
                .apply(mRequestOptions)
                .into(target);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                back();
                break;

            case R.id.iv_cover:
                processClick(mCommentParams, -1);
                break;

            case R.id.tv_comment:
                mTargetComment = null;
                vTweetBottom.setHint("请输入评论内容");
                break;

            case R.id.tv_praise: {
                AddZan params = new AddZan();
                params.setDataTable(mCommentParams.getEventStyle());
                params.setDataKey(mCommentParams.getId());
                params.setStyle(mCommentParams.getHasZan()==1 ? AddZan.ZAN_COMMENT_DELETE:AddZan.ZAN_COMMENT_ADD);
                ServiceGenerator.createService(this, CommonService.class)
                        .addZan(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<HttpResult<String>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new SingleAdapter<HttpResult<String>>() {
                            @Override
                            public void onSuccess(@NonNull HttpResult<String> result) {
                                if(mCommentParams.getHasZan()==1){
                                    mCommentParams.setHasZan(0);
                                    mCommentParams.decZan();
                                }else{
                                    mCommentParams.setHasZan(1);
                                    mCommentParams.incZan();
                                }

                                showZan();
                                mFragment.refreshZans(mCommentParams.getZanTotles());
                            }

                            @Override
                            public void onError(Throwable e) {}
                        });
                break;
            }
        }
    }

    private void processClick(CommentParams data, int imagePos) {
        //如果没有video，则查看图片；否则，播放视频或者查看图片
        boolean hasVideo = !TextUtils.isEmpty(data.getTripVideo());
        boolean hasImages = !TextUtils.isEmpty(data.getTripImgs());
        if (!hasVideo && !hasImages)
            return;

        if (imagePos == -1 && hasVideo) {

            playVideo(TypeUtil.str2arrays(data.getTripVideo())[0]);
        } else if (imagePos == -1/* && !hasVideo*/) {
            scanImages(0, mImageAdapter.getImageList());
        } else if (/*imagePos!=-1 && */hasVideo) {
            scanImages(imagePos, mImageAdapter.getImageList());
        } else/* if(imagePos!=-1 && !hasVideo)*/ {
            scanImages(1 + imagePos, mImageAdapter.getImageList());
        }
    }

    private void playVideo(String url) {
        VideoUtil.play(this,url);
    }

    private void scanImages(int selected, String[] images) {
        LookUpPictureActivity.toLookUp(this, 0, false, selected, images);
    }

    @Override
    public void onChooseTarget(Comments.Item target) {
        mTargetComment = target;
        if(target!=null && target.getUserName()!=null)
            vTweetBottom.setHint("回复 "+target.getUserName());
    }

}
