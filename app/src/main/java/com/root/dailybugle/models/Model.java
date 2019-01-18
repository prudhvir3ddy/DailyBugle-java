package com.root.dailybugle.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {
    private String date;
    private String sname;
    private String author;
    private String title;
    private String desc;
    private String image;
    private String url;

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    private Model(Parcel in) {
        date = in.readString();
        sname = in.readString();
        author = in.readString();
        title = in.readString();
        desc = in.readString();
        image = in.readString();
        url = in.readString();
    }

    public Model() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(sname);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(image);
        dest.writeString(url);
    }
}
