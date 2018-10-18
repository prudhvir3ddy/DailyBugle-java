package com.root.dailybugle.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.root.dailybugle.R;
import com.root.dailybugle.database.AppExecutors;
import com.root.dailybugle.database.NewsModel;
import com.root.dailybugle.database.NewsRoomDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    public static final String TOAST_ACTION = "toast";
    private final Context context;

    List<NewsModel> list;

    private int appWidgetId;

    int pos;

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        list = new ArrayList<>();
        final NewsRoomDatabase newsRoomDatabase;
        newsRoomDatabase = NewsRoomDatabase.getDatabase(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                list = newsRoomDatabase.newsDao().getAllNews1();
            }
        });

    }

    @Override
    public void onDataSetChanged() {
            final NewsRoomDatabase newsRoomDatabase;
            newsRoomDatabase = NewsRoomDatabase.getDatabase(context);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    list = newsRoomDatabase.newsDao().getAllNews1();
                }
            });
        }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.news_app_widget_model);


        remoteViews.setTextViewText(R.id.b2, list.get(position).getTitle());
        final NewsModel model = list.get(position);
        final String url = model.getImage();

        try {
            Bitmap b = Picasso.with(context).load(url).get();
            remoteViews.setImageViewBitmap(R.id.home_imgview, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle extras = new Bundle();
        extras.putInt("WidgetDataProvider", position);
        extras.putString("image", model.getImage());
        extras.putString("desc", model.getDesc());
        extras.putString("title", model.getTitle());
        extras.putString("author", model.getAuthor());
        extras.putString("source", model.getSname());
        extras.putString("url", model.getUrl());
        extras.putString("publishedAt", model.getDate());
        extras.putBoolean("ButtonText", model.isThere());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.b2, fillInIntent);
        remoteViews.setOnClickFillInIntent(R.id.c1, fillInIntent);
        remoteViews.setOnClickFillInIntent(R.id.home_imgview, fillInIntent);


        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}