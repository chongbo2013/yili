package com.lots.travel.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.AccountManager;
import com.lots.travel.recruit.widget.ExpenseItemsView.ExpenseItem;
import com.lots.travel.store.FileStore;

import java.io.File;
import java.util.List;

/**
 * Created by nalanzi on 2018/1/4.
 */

public class ExpenseAdditional implements Parcelable {
    private List<ExpenseItem> includeExpenseItems;
    private List<ExpenseItem> excludeExpenseItems;

    private int currentItem;

    private String text;
    private String[] images;

    private long soundLength;
    private String soundFilepath;

    private String videoFilepath;
    private String videoCover;

    private boolean addPrice;
    private int addedPrice;

    public ExpenseAdditional(){
        currentItem = 1;
        images = new String[0];
        soundFilepath = new FileStore().getCacheVoiceDir()+ File.separator+
                AccountManager.getInstance().getUserId()+"_"+System.currentTimeMillis()+"_expense_additional.amr";
    }

    public String getIncludeExpense(){
        StringBuilder strBuilder = new StringBuilder();
        int count = 0;
        for (int i=0;i<includeExpenseItems.size();i++){
            ExpenseItem tag = includeExpenseItems.get(i);
            if(!tag.isChecked())
                continue;
            if(count!=0)
                strBuilder.append(",");
            ++count;
            strBuilder.append(tag.getPx());
        }
        return strBuilder.toString();
    }

    public String getExcludeExpense(){
        StringBuilder strBuilder = new StringBuilder();
        int count = 0;
        for (int i=0;i<excludeExpenseItems.size();i++){
            ExpenseItem tag = excludeExpenseItems.get(i);
            if(!tag.isChecked())
                continue;
            if(count!=0)
                strBuilder.append(",");
            ++count;
            strBuilder.append(tag.getPx());
        }
        return strBuilder.toString();
    }

    protected ExpenseAdditional(Parcel in) {
        includeExpenseItems = in.createTypedArrayList(ExpenseItem.CREATOR);
        excludeExpenseItems = in.createTypedArrayList(ExpenseItem.CREATOR);
        currentItem = in.readInt();
        text = in.readString();
        images = in.createStringArray();
        soundLength = in.readLong();
        soundFilepath = in.readString();
        videoFilepath = in.readString();
        videoCover = in.readString();
        addPrice = in.readByte() != 0;
        addedPrice = in.readInt();
    }

    public static final Creator<ExpenseAdditional> CREATOR = new Creator<ExpenseAdditional>() {
        @Override
        public ExpenseAdditional createFromParcel(Parcel in) {
            return new ExpenseAdditional(in);
        }

        @Override
        public ExpenseAdditional[] newArray(int size) {
            return new ExpenseAdditional[size];
        }
    };

    public List<ExpenseItem> getIncludeExpenseItems() {
        return includeExpenseItems;
    }

    public void setIncludeExpenseItems(List<ExpenseItem> includeExpenseItems) {
        this.includeExpenseItems = includeExpenseItems;
    }

    public List<ExpenseItem> getExcludeExpenseItems() {
        return excludeExpenseItems;
    }

    public void setExcludeExpenseItems(List<ExpenseItem> excludeExpenseItems) {
        this.excludeExpenseItems = excludeExpenseItems;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public long getSoundLength() {
        return soundLength;
    }

    public void setSoundLength(long soundLength) {
        this.soundLength = soundLength;
    }

    public String getSoundFilepath() {
        return soundFilepath;
    }

    public void setSoundFilepath(String soundFilepath) {
        this.soundFilepath = soundFilepath;
    }

    public String getVideoFilepath() {
        return videoFilepath;
    }

    public void setVideoFilepath(String videoFilepath) {
        this.videoFilepath = videoFilepath;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public boolean isAddPrice() {
        return addPrice;
    }

    public void setAddPrice(boolean addPrice) {
        this.addPrice = addPrice;
    }

    public int getAddedPrice() {
        return addedPrice;
    }

    public void setAddedPrice(int addedPrice) {
        this.addedPrice = addedPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(includeExpenseItems);
        dest.writeTypedList(excludeExpenseItems);
        dest.writeInt(currentItem);
        dest.writeString(text);
        dest.writeStringArray(images);
        dest.writeLong(soundLength);
        dest.writeString(soundFilepath);
        dest.writeString(videoFilepath);
        dest.writeString(videoCover);
        dest.writeByte((byte) (addPrice ? 1 : 0));
        dest.writeInt(addedPrice);
    }
}
