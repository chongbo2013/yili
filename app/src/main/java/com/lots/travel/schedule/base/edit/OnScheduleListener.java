package com.lots.travel.schedule.base.edit;

/**
 * Created by nalanzi on 2017/12/26.
 */

public interface OnScheduleListener {
    //添加desc
    void onAddDesc(int pos);

    //编辑文本
    void onEditText(int pos,String text);

    //录制语音 - 语音录制完成以后
    void onAddAudio(int pos, int length);

    //录制视频 - 发起操作
    void onAddVideo(int pos);

    //查看图片
    void onScanImages(int pos,String images,int imagePos);

    //增加图片
    void onAddImages(int pos);

    //景点、酒店、餐厅等详情浏览
    void onScanDetails(String url);

    //添加景点、酒店、餐厅、入住酒店
    void onAddComponents(int pos);

    //删除景点、酒店、餐厅、入住酒店
    void onDeleteComponent(int pos);

    //添加traffic
    void onAddTraffic(int pos);

    // 删除traffic
    void onDeleteTraffic(int pos);
}
