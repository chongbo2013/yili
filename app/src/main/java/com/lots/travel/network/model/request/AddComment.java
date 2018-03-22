package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/5.
 */

public class AddComment {
    /**
     * 事件对象分类：spot 景点，trip 行程；node 游记；youji 足迹；zhibo直播
     * 在点赞、评论时使用
     */

    public static final String DATA_TYPE_SPOT = "spot";

    public static final String DATA_TYPE_TRIP = "trip";

    public static final String DATA_TYPE_NOTE = "note";

    public static final String DATA_TYPE_FOOTPRINT = "youji";

    public static final String DATA_TYPE_LIVE = "zhibo";

    /**
     * 评论类型：1 - 评论、2 - 评价
     */
    //评价
    public static final String COMMENT_EVENT_PJ = "1";
    //评论
    public static final String COMMENT_EVENT_PL = "2";

    public static final String STYLE_ADD = "add";
    public static final String STYLE_DELETE = "delete";

    private long userId;
    //操作类型：add:添加;delete:删除
    private String style;
    //事件类型：1:评论;2:评价
    private String eventType;
    //spot景点、trip行程、note游记、youji足迹、zhibo直播
    private String dataTable;
    private String dataKey;
    //内容
    private String content;
    //星级或评分
    private String score;
    //评论的图片（最多9张）
    private String imgs;
    private String video;
    private String videoImg;
    private String sound;
    private String soundLen;
    private String kp;
    //1靠谱、2不靠谱
    private String kpStyle;
    private String kpScore;
    //回复某条评论的id
    private String replyId;
    //回复某用户的评论
    private String replyUserId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSoundLen() {
        return soundLen;
    }

    public void setSoundLen(String soundLen) {
        this.soundLen = soundLen;
    }

    public String getKp() {
        return kp;
    }

    public void setKp(String kp) {
        this.kp = kp;
    }

    public String getKpStyle() {
        return kpStyle;
    }

    public void setKpStyle(String kpStyle) {
        this.kpStyle = kpStyle;
    }

    public String getKpScore() {
        return kpScore;
    }

    public void setKpScore(String kpScore) {
        this.kpScore = kpScore;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }
}
