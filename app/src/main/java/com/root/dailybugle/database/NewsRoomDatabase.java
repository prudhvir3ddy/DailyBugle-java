package com.root.dailybugle.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.root.dailybugle.R;

@Database(entities = {NewsModel.class},version = 1,exportSchema = false)
public abstract class NewsRoomDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
    private static volatile NewsRoomDatabase INSTANCE;

    public static NewsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NewsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsRoomDatabase.class, context.getString(R.string.database_name))
                            .build();
                }
            }
        }
        return INSTANCE;
}
}
