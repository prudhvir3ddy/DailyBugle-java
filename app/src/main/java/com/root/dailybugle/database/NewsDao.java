package com.root.dailybugle.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsModel newsModel);

    @Delete
    void delete(NewsModel newsModel);

    @Query("SELECT * from news_row")
    LiveData<List<NewsModel>> getAllNews();

    @Query("SELECT * from news_row")
    List<NewsModel> getAllNews1();
}
