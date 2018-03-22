package com.lots.travel.main;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.booking.BookingChoosePlaceActivity;
import com.lots.travel.footprint.PublishFootprintDialog;
import com.lots.travel.footprint.PublishImagesActivity;
import com.lots.travel.footprint.PublishVideoActivity;
import com.lots.travel.main.message.MessageTabActivity;
import com.lots.travel.schedule.note.CreateNoteActivity;
import com.lots.travel.schedule.route.RouteBottomActivity;
import com.lots.travel.video.RequestRecordActivity;

/**
 * Created by nalanzi on 2017/9/1.
 */

public class ShortcutPopup implements MenuBar.OnChooseListener {
    public static final int REQ_VIDEO_RECORD = 10001;
    private MainActivity mContext;
    private ViewGroup mContainer;

    private View mRoot;
//    private BlurringView mBlurringView;
    private MenuBar mMenuBar;

    private PublishFootprintDialog mPublishDialog;

    public ShortcutPopup(MainActivity context, View content) {
        mContext = context;
        ViewParent parent = content.getParent();

        if(parent!=null && parent instanceof ViewGroup)
            mContainer = (ViewGroup) parent;
        else
            return;

        mRoot = LayoutInflater.from(content.getContext()).inflate(R.layout.popup_shortcut,null);
        mContainer.addView(mRoot,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRoot.setVisibility(View.GONE);

//        mBlurringView = (BlurringView) mRoot.findViewById(R.id.blurring_view);
//        mBlurringView.setBlurredView(content);

        mMenuBar = (MenuBar) mRoot.findViewById(R.id.menu_bar);
        mMenuBar.setOnChooseListener(this);
    }

    @Override
    public void onChoose(int type) {
        mRoot.setVisibility(View.GONE);

        Intent intent;

        switch(type){
            case MenuBar.ITEM_BOOKING_TRAVEL:
                intent = new Intent(mContext, BookingChoosePlaceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
                break;

            case MenuBar.ITEM_BRIGADE_SNAP:
                ;
                break;

            case MenuBar.ITEM_CREATE_TRAVEL:
                intent = new Intent(mContext, RouteBottomActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
                break;

            case MenuBar.ITEM_FOOTPRINT:
                if(mPublishDialog==null){
                    mPublishDialog = new PublishFootprintDialog(mContext, new PublishFootprintDialog.OnChooseListener() {
                        @Override
                        public void onChooseVideo() {
                            PublishVideoActivity.toVideo(mContext);
                        }

                        @Override
                        public void onChooseAlbum() {
                            PublishImagesActivity.toPublish(mContext,false);
                        }

                        @Override
                        public void onChoosePhoto() {
                            PublishImagesActivity.toPublish(mContext,true);
                        }
                    });
                }
                mPublishDialog.show();
                break;

            case MenuBar.ITEM_MALL:
                RequestRecordActivity.toRecord(mContext,REQ_VIDEO_RECORD,true);
                break;

            case MenuBar.ITEM_MESSAGE:
                intent = new Intent(mContext, MessageTabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
                break;

            case MenuBar.ITEM_TRAVEL_LIVE:
                ;
                break;

            case MenuBar.ITEM_TRAVEL_NOTES:
                intent = new Intent(mContext, CreateNoteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
                break;
        }

    }

    public void processActivityResult(int requestCode,int result,Intent data){
        switch (requestCode){
            case REQ_VIDEO_RECORD:
                if(result== Activity.RESULT_OK){
                    String videoPath = data.getStringExtra(RequestRecordActivity.VIDEO_PATH);
                    Toast.makeText(mContext,videoPath,Toast.LENGTH_SHORT).show();
                    Log.e("视频路径;",videoPath);
                }
                break;
        }
    }

    public void show(){
        mRoot.setVisibility(View.VISIBLE);
        mMenuBar.expand();
    }

    //将dialog dismiss掉
    public void dismissDialog(){
        if(mPublishDialog!=null)
            mPublishDialog.dismiss();
    }

}
