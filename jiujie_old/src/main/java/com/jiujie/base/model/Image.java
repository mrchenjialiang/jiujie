package com.jiujie.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/8/10.
 * Email:576507648@qq.com
 */

public class Image implements Parcelable{
    private String path;
    private String name;
    private boolean isDir;
    private List<Image> imageList;

    public Image() {
    }

    protected Image(Parcel in) {
        path = in.readString();
        name = in.readString();
        isDir = in.readByte() != 0;
        imageList = in.createTypedArrayList(Image.CREATOR);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (isDir != image.isDir) return false;
        if (path != null ? !path.equals(image.path) : image.path != null) return false;
        if (name != null ? !name.equals(image.name) : image.name != null) return false;
        return imageList != null ? imageList.equals(image.imageList) : image.imageList == null;

    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isDir ? 1 : 0);
        result = 31 * result + (imageList != null ? imageList.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeByte((byte) (isDir ? 1 : 0));
        dest.writeTypedList(imageList);
    }
}
