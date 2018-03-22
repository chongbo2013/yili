package com.lots.travel.network.model.result;

import java.util.List;

/**
 * Created by nalanzi on 2018/1/5.
 */

public class Comments {
    private int count;
    private List<Item> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    public static class Item{
        /**
         "id","",//评论id
         "dataTable","",//1评论；2评价；
         "dataKey","",//
         "eventType","",//
         "userId","",//
         "userName","",//昵称
         "realName","",//真实姓名
         "faceImg","",//头像
         "content","",//评论内容
         "creattime","",//评论时间
         "replyUserId","",//回复对象userId
         "replyUserName","",//回复对象名称
         "sex",""//性别  1男2女0其他
         */
        private String id;
        private String dataTable;
        private String dataKey;
        private String eventType;
        private long userId;
        private String userName;
        private String realName;
        private String faceImg;
        private String content;
        private long creattime;
        private long replyUserId;
        private String replyUserName;
        private String sex;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getFaceImg() {
            return faceImg;
        }

        public void setFaceImg(String faceImg) {
            this.faceImg = faceImg;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreattime() {
            return creattime;
        }

        public void setCreattime(long creattime) {
            this.creattime = creattime;
        }

        public long getReplyUserId() {
            return replyUserId;
        }

        public void setReplyUserId(long replyUserId) {
            this.replyUserId = replyUserId;
        }

        public String getReplyUserName() {
            return replyUserName;
        }

        public void setReplyUserName(String replyUserName) {
            this.replyUserName = replyUserName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

}
