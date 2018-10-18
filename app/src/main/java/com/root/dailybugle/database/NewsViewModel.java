package com.root.dailybugle.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    public LiveData<List<NewsModel>> list;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        NewsRoomDatabase newsRoomDatabase=NewsRoomDatabase.getDatabase(this.getApplication());
        list=newsRoomDatabase.newsDao().getAllNews();
    }

    public LiveData<List<NewsModel>> getList(){
        return list;
    }
}
