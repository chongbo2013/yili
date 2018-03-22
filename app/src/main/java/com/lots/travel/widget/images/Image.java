package com.lots.travel.widget.images;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by huanghaibin_dev
 * on 2016/7/11.
 */

public class Image implements Parcelable {
    private int id;
    private String path;
    private String thumbPath;
    private boolean isSelect;
    private String folderName;
    private String name;
    private long date;

    public Image(){}

    protected Image(Parcel in) {
        id = in.readInt();
        path = in.readString();
        thumbPath = in.readString();
        isSelect = in.readByte() != 0;
        folderName = in.readString();
        name = in.readString();
        date = in.readLong();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Image) {
            return this.path.equals(((Image) o).getPath());
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeString(thumbPath);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeString(folderName);
        dest.writeString(name);
        dest.writeLong(date);
    }
}
