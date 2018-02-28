package com.images;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/22.
 */

public class ImageBean implements Parcelable {

    private String path;
    private boolean isSelect = false;

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            ImageBean bean = new ImageBean();
            bean.path = source.readString();
            bean.isSelect = source.readByte()!=0;
            return bean;
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "ImageBean {"+
                "path='" + path+'\''+
                ",isSelect="+isSelect+
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeByte((byte)(isSelect ? 1:0));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
