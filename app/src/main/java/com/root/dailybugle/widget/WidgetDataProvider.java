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
import com.root.dailybugle.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    public static final String TOAST_ACTION = "toast";
    private final Context context;

    private List<NewsModel> list;

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
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
        extras.putString(Constants.URLTOIMAGE, model.getImage());
        extras.putString(Constants.DESCRIPTION, model.getDesc());
        extras.putString(Constants.TITLE, model.getTitle());
        extras.putString(Constants.AUTHOR, model.getAuthor());
        extras.putString(Constants.SOURCE, model.getSname());
        extras.putString(Constants.URL, model.getUrl());
        extras.putString(Constants.PUBLISHEDAT, model.getDate());


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