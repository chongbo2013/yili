package com.lots.travel.footprint;

import com.lots.travel.footprint.model.Footprint;

/**
 * Created by nalanzi on 2017/11/23.
 */

public interface OnFootprintListener {
    //播放视频
    void onPlayVideo(String url);
    //查看images
    void onScanImages(int selected,String[] images);
    //点赞
    void onAddZan(int flatPosition,String dateTable,String dateKey,String style);
    //评论
    void onAddComment(int flatPosition,Footprint data);
    //查看游记
    void onCheckNote(int flatPosition,String scheduleId);
    //查看用户
    void onCheckUser(long userId);
}
