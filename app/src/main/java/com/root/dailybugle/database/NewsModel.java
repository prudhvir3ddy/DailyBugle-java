package com.root.dailybugle.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "news_row",primaryKeys = {"sname","date"})
public class NewsModel {
    @NonNull
    String sname,date;
    String author,title,desc,image,url;
    boolean isThere;

    public NewsModel(String sname,String author,String title,String desc,String image,String url,String date,boolean isThere){
        this.sname=sname;
        this.author=author;
        this.title=title;
        this.desc=desc;
        this.image=image;
        this.url=url;
        this.date=date;
        this.isThere=isThere;
    }
//    @Ignore
//    public NewsModel(String sname,String author,String title,String desc,String image,String url,String date,boolean isThere){
//        this.sname=sname;
//        this.author=author;
//        this.title=title;
//        this.desc=desc;
//        this.image=image;
//        this.url=url;
//        this.date=date;
//        this.isThere=isThere;
//    }

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

    public boolean isThere() { return isThere; }

    public void setThere(boolean there) { isThere = there; }

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
//
//    public int getId() { return id; }
//
//    public void setId(int id) { this.id = id; }

}
