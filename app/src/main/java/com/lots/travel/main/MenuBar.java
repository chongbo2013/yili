package com.lots.travel.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.lots.travel.R;

import java.util.List;

/**
 * 直接使用RelativeLayout简单快捷，扩展性不好
 * 后续如果新增内容，修改为GridView之类
 */
public class MenuBar extends RelativeLayout implements View.OnClickListener{
    private static final int STATE_INVALID = 0;

    private static final int STATE_EXPAND = 1;

    private static final int STATE_COLLAPSE = 2;

    private ImageView ivClose;

    private ViewGroup[] itemHolders;

    //true - expand、false - collapse
    private int mExpandState = STATE_INVALID;
    private int mExpandStateDelay = STATE_INVALID;

    private boolean isLaidOut = false;
    private int mAnimatingSpace;

    private long mAnimatingToken = -1L;

    private Runnable callback = new Runnable() {
        @Override
        public void run() {
            if(mExpandStateDelay==STATE_INVALID
                    || mExpandState==mExpandStateDelay)
                return;

            if(mExpandStateDelay==STATE_EXPAND){
                expand();
            }else if(mExpandStateDelay==STATE_COLLAPSE){
                collapse();
            }

            mExpandStateDelay = STATE_INVALID;
        }
    };

    private OnChooseListener mOnChooseListener;

    public MenuBar(Context context) {
        this(context,null);
    }

    public MenuBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_menubar,this,true);

        init();
    }

    public void setOnChooseListener(OnChooseListener listener){
        mOnChooseListener = listener;
    }

    private void init(){
        itemHolders = new ViewGroup[8];

        itemHolders[0] = (ViewGroup) findViewById(R.id.ll_create_travel);
        itemHolders[1] = (ViewGroup) findViewById(R.id.ll_travel_notes);
        itemHolders[2] = (ViewGroup) findViewById(R.id.ll_footprint);
        itemHolders[3] = (ViewGroup) findViewById(R.id.ll_booking_travel);
        itemHolders[4] = (ViewGroup) findViewById(R.id.ll_brigade_snap);
        itemHolders[5] = (ViewGroup) findViewById(R.id.ll_travel_live);
        itemHolders[6] = (ViewGroup) findViewById(R.id.ll_message);
        itemHolders[7] = (ViewGroup) findViewById(R.id.ll_mall);

        for (ViewGroup itemHolder : itemHolders) {
            itemHolder.setOnClickListener(this);
        }

        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
    }

    public void expand(){
        if(!isLaidOut){
            mExpandStateDelay = STATE_EXPAND;
            return;
        }

        if(mAnimatingSpace==0)
            return;

        SpringChain springChain = SpringChain.create(40, 6, 50, 7);

        mAnimatingToken = System.currentTimeMillis();
        for (final ViewGroup view : itemHolders) {
            springChain.addSpring(new SyncSpringListener(mAnimatingToken) {

                @Override
                public void onSpringActivate(Spring spring) {
                    if(getToken()==mAnimatingToken)
                        view.setVisibility(VISIBLE);
                }

                @Override
                public void onSpringUpdate(Spring spring) {
                    if(getToken()==mAnimatingToken){
                        float value = (float) Math.min(1, spring.getCurrentValue());

                        view.setTranslationY(value * mAnimatingSpace);
                        float scale = 1f - value;
                        view.setScaleX(scale);
                        view.setScaleY(scale);
                    }
                }
            });
        }

        List<Spring> springs = springChain.getAllSprings();

        for (int i = 0; i < springs.size(); i++) {
            springs.get(i).setCurrentValue(1);
        }

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setDuration(300);

        //开始spring动画
        springChain.setControlSpringIndex(0).getControlSpring().setEndValue(0);
        //开始按钮旋转动画
        ivClose.startAnimation(rotateAnimation);

        mExpandState = STATE_EXPAND;
    }

    public void collapse(){
        if(!isLaidOut){
            mExpandStateDelay = STATE_COLLAPSE;
            return;
        }

        if(mAnimatingSpace==0)
            return;

        SpringChain springChain = SpringChain.create(50, 4, 60, 4);

        mAnimatingToken = System.currentTimeMillis();
        for (final ViewGroup view : itemHolders) {
            springChain.addSpring(new SyncSpringListener(mAnimatingToken) {

                @Override
                public void onSpringUpdate(Spring spring) {
                    if(getToken()==mAnimatingToken){
                        float value = (float) Math.min(1, spring.getCurrentValue());

                        view.setTranslationY( value * mAnimatingSpace);
                        if (value > 0) {
                            float scale = 1f -  value;
                            scale = Math.max(0, scale);
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }

                    }
                }
            });
        }

        List<Spring> springs = springChain.getAllSprings();

        for (int i = 0; i < springs.size(); i++) {
            springs.get(i).setCurrentValue(0);
        }

        RotateAnimation rotateAnimation = new RotateAnimation(270, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setDuration(400);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(mOnChooseListener!=null){
                    mOnChooseListener.onChoose(ITEM_CLOSE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        springChain.setControlSpringIndex(itemHolders.length-1).getControlSpring().setEndValue(1);
        ivClose.startAnimation(rotateAnimation);

        mExpandState = STATE_COLLAPSE;
    }

    /**
     * 点击选中item动画
     */
    private void resolveClickedItemAnimation(final View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setDuration(200);
        animationSet.setFillAfter(false);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationSet);
    }

    /**
     * 未选中items动画
     */
    private void resolveUnClickedItemAnimation(final View view){

        for(final ViewGroup item:itemHolders){
            if(item!=view){
                AnimationSet animationSet = new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, .0f, 1.0f, .0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);

                animationSet.setInterpolator(new AccelerateInterpolator());
                animationSet.setDuration(200);
                animationSet.setFillAfter(false);

                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        item.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                item.startAnimation(animationSet);
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mAnimatingSpace = getHeight()-itemHolders[0].getTop();

        isLaidOut = mAnimatingSpace>0;
        if(isLaidOut)
            post(callback);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if(id==R.id.iv_close){
            if(mExpandState==STATE_EXPAND)
                collapse();
            else
                expand();
        }else{
            resolveUnClickedItemAnimation(v);

            resolveClickedItemAnimation(v);

            RotateAnimation rotateAnimation = new RotateAnimation(270, 0,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            rotateAnimation.setDuration(400);
            rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(mOnChooseListener!=null) {
                        mOnChooseListener.onChoose(id);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            ivClose.startAnimation(rotateAnimation);

            mExpandState = STATE_COLLAPSE;
        }
    }

    private static class SyncSpringListener extends SimpleSpringListener{
        private long token;

        SyncSpringListener(long token){
            this.token = token;
        }

        long getToken(){
            return token;
        }

    }

    public static final int ITEM_CLOSE = R.id.iv_close;
    public static final int ITEM_BOOKING_TRAVEL = R.id.ll_booking_travel;
    public static final int ITEM_BRIGADE_SNAP = R.id.ll_brigade_snap;
    public static final int ITEM_CREATE_TRAVEL = R.id.ll_create_travel;
    public static final int ITEM_FOOTPRINT = R.id.ll_footprint;
    public static final int ITEM_MALL = R.id.ll_mall;
    public static final int ITEM_MESSAGE = R.id.ll_message;
    public static final int ITEM_TRAVEL_LIVE = R.id.ll_travel_live;
    public static final int ITEM_TRAVEL_NOTES = R.id.ll_travel_notes;

    public interface OnChooseListener{
        void onChoose(int type);
    }

}
