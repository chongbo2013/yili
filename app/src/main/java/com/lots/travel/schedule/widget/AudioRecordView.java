package com.lots.travel.schedule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.util.DensityUtil;

import java.io.File;

/**
 * Created by nalanzi on 2017/9/16.
 */
//录音控件
//
public class AudioRecordView extends View {
    //没有文件
    public static final int STATE_RESET = 0;
    //正在录音
    public static final int STATE_RECORDING = 1;
    //录音完毕 - 播放完毕以后，进入STATE_RECORD_COMPLETED
    public static final int STATE_RECORD_COMPLETED = 2;
    //正在播放
    public static final int STATE_PLAYING = 3;
    //暂停播放
    public static final int STATE_PAUSE = 4;

    private int state;

    private String recordPath;

    private float runningProgress;
    private long totalDuration;

    private int progressCircleRadius;
    private Drawable recordDrawable;

    private Drawable startPlayDrawable;

    private Drawable pausePlayDrawable;

    private int circleBackgroundColor;
    private int circleProgressColor;

    private RectF circleRect;
    private Paint circlePaint;

    private int centerX,centerY;

    private AudioRecorder audioRecorder;

    private AudioPlayer audioPlayer;

    public AudioRecordView(Context context) {
        this(context,null);
    }

    public AudioRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);

        recordDrawable = ContextCompat.getDrawable(context,R.drawable.img_audio_recording);
        progressCircleRadius = Math.max(recordDrawable.getIntrinsicHeight(),recordDrawable.getIntrinsicWidth())/2+1;
        progressCircleRadius += DensityUtil.dp2px(context,6);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(DensityUtil.dp2px(context,1));

        circleBackgroundColor = ContextCompat.getColor(context,R.color.color_divider);
        circleProgressColor = ContextCompat.getColor(context,R.color.color_main);

        startPlayDrawable = ContextCompat.getDrawable(context,R.drawable.img_audio_play);
        pausePlayDrawable = ContextCompat.getDrawable(context,R.drawable.img_audio_pause);

        state = STATE_RESET;
        recordPath = null;
        totalDuration = 0L;

        circleRect = new RectF();
    }

    public void restore(Slice slice){
        recordPath = slice.recordPath;
    }

    public void save(Slice slice){
        slice.state = state;
    }

    public void deleteRecordFile(){
        File file = new File(recordPath);
        if (file.exists())
            file.delete();
    }

    public void reset(){
        if(audioRecorder!=null)
            audioRecorder.stopRecord();

        if(audioPlayer!=null)
            audioPlayer.stop();

        state = STATE_RESET;
    }

    public void setFilePath(String path){
        recordPath = path;
    }

    private void initRecorder(){
        if(audioRecorder==null){
            audioRecorder = new AudioRecorder();
            audioRecorder.setFilePath(recordPath);
            audioRecorder.setMaxDuration(60*1000);
        }
    }

    private void initPlayer(){
        if(audioPlayer==null){
            audioPlayer = new AudioPlayer();
            audioPlayer.setOnCallbackListener(new AudioPlayer.OnCallbackListener() {
                @Override
                public void onCompleted() {
                    audioPlayer.stop();
                    state = STATE_RECORD_COMPLETED;
                    ViewCompat.postInvalidateOnAnimation(AudioRecordView.this);
                }

                @Override
                public void onError() {
                    audioPlayer.stop();
                    state = STATE_RECORD_COMPLETED;
                    ViewCompat.postInvalidateOnAnimation(AudioRecordView.this);
                }

            });
        }
    }

    private boolean touchDown;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        boolean handled = action!=MotionEvent.ACTION_MOVE;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(state==STATE_RESET){
                    state=STATE_RECORDING;
                    ViewCompat.postInvalidateOnAnimation(this);
                    //开始录音
                    if(audioRecorder==null)
                        initRecorder();

                    audioRecorder.startRecord();
                }

                touchDown = state!=STATE_RESET && state!=STATE_RECORDING;
                break;

            case MotionEvent.ACTION_MOVE:
                handled = state==STATE_RECORDING;
                break;

            case MotionEvent.ACTION_CANCEL:
                if(state==STATE_RECORDING){
                    //取消录音
                    totalDuration = audioRecorder.stopRecord();
                    ViewCompat.postInvalidateOnAnimation(this);
                }else if(state==STATE_PLAYING || state==STATE_PAUSE){
                    audioPlayer.stop();
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                touchDown = false;
                break;

            case MotionEvent.ACTION_UP:
                if(state==STATE_RECORDING){
                    //取消录音
                    totalDuration = audioRecorder.stopRecord();
                    state = STATE_RECORD_COMPLETED;
                    ViewCompat.postInvalidateOnAnimation(this);
                }else if(state==STATE_RECORD_COMPLETED){
                    //开始播放
                    if(audioPlayer==null)
                        initPlayer();
                    audioPlayer.start(recordPath,0);
                    runningProgress = 0f;
                    state = STATE_PLAYING;
                    ViewCompat.postInvalidateOnAnimation(this);
                }else if(state==STATE_PLAYING){
                    //暂停
                    audioPlayer.pause();
                    state = STATE_PAUSE;
                    ViewCompat.postInvalidateOnAnimation(this);
                }else if(state==STATE_PAUSE){
                    //继续播放
                    audioPlayer.continuePlay();
                    state = STATE_PLAYING;
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                touchDown = false;
                break;
        }

        return handled;
    }

    /**
     * STATE_RESET - 无文件
     * STATE_RECORDING - 正在录音
     * STATE_RECORD_COMPLETED - 录音完毕
     * STATE_PLAYING - 正在播放
     * STATE_PLAY_PAUSE - 暂停播放
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX,centerY);

        switch (state){
            case STATE_RESET:
                //只绘制背景
                recordDrawable.draw(canvas);
                break;

            case STATE_RECORDING:
                recordDrawable.draw(canvas);

                circlePaint.setColor(circleBackgroundColor);
                canvas.drawCircle(0,0,progressCircleRadius,circlePaint);

                circlePaint.setColor(circleProgressColor);
                runningProgress = audioRecorder!=null ? audioRecorder.getProgress():0f;
                canvas.drawArc(circleRect,-90f, runningProgress *360f,false,circlePaint);
                break;

            case STATE_RECORD_COMPLETED:
                startPlayDrawable.draw(canvas);
                break;

            case STATE_PLAYING:
                pausePlayDrawable.draw(canvas);

                circlePaint.setColor(circleBackgroundColor);
                canvas.drawCircle(0,0,progressCircleRadius,circlePaint);

                circlePaint.setColor(circleProgressColor);
                runningProgress = audioPlayer!=null ? audioPlayer.getProgress():0f;
                canvas.drawArc(circleRect,-90f, runningProgress*360f,false,circlePaint);
                break;

            case STATE_PAUSE:
                startPlayDrawable.draw(canvas);
                break;
        }

        canvas.translate(-centerX,-centerY);

        if(state==STATE_RECORDING || state==STATE_PLAYING)
            ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        centerX = getPaddingLeft()+(getWidth()-getPaddingLeft()-getPaddingRight())/2;
        centerY = getPaddingTop()+(getHeight()-getPaddingTop()-getPaddingBottom())/2;

        int radius = Math.max(recordDrawable.getIntrinsicWidth(),recordDrawable.getIntrinsicHeight())/2;
        recordDrawable.setBounds(-radius,-radius,radius,radius);

        radius = Math.max(startPlayDrawable.getIntrinsicWidth(),startPlayDrawable.getIntrinsicHeight())/2;
        startPlayDrawable.setBounds(-radius,-radius,radius,radius);

        radius = Math.max(pausePlayDrawable.getIntrinsicWidth(),pausePlayDrawable.getIntrinsicHeight())/2;
        pausePlayDrawable.setBounds(-radius,-radius,radius,radius);

        circleRect.set(-progressCircleRadius,-progressCircleRadius,progressCircleRadius,progressCircleRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureSize(widthMeasureSpec,getPaddingLeft()+getPaddingRight());
        int height = measureSize(heightMeasureSpec,getPaddingTop()+getPaddingBottom());

        setMeasuredDimension(width,height);
    }

    private int measureSize(int spec,int padding){
        int size = MeasureSpec.getSize(spec);
        int mode = MeasureSpec.getMode(spec);
        int recordSize = (int) ((progressCircleRadius+circlePaint.getStrokeWidth())*2+.5f);

        switch (mode){
            case MeasureSpec.EXACTLY:
                return size;

            case MeasureSpec.AT_MOST:
                return Math.min(size,recordSize+padding);

            case MeasureSpec.UNSPECIFIED:
            default:
                return recordSize+padding;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(audioPlayer!=null)
            audioPlayer.stop();

        if(audioRecorder!=null)
            audioRecorder.stopRecord();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if(audioPlayer!=null)
            audioPlayer.stop();

        if(audioRecorder!=null)
            audioRecorder.stopRecord();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if(audioPlayer!=null)
            audioPlayer.stop();

        if(audioRecorder!=null)
            audioRecorder.stopRecord();
    }

    public static class Slice implements Parcelable{
        protected int state;
        protected String recordPath;
        protected float playProgress;
        protected long totalDuration;

        protected Slice(Parcel in) {
            state = in.readInt();
            recordPath = in.readString();
            playProgress = in.readFloat();
            totalDuration = in.readLong();
        }

        public static final Creator<Slice> CREATOR = new Creator<Slice>() {
            @Override
            public Slice createFromParcel(Parcel in) {
                return new Slice(in);
            }

            @Override
            public Slice[] newArray(int size) {
                return new Slice[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(state);
            dest.writeString(recordPath);
            dest.writeFloat(playProgress);
            dest.writeLong(totalDuration);
        }
    }

}
