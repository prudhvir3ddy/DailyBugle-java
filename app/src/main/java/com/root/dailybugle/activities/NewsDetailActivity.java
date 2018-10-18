package com.root.dailybugle.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.root.dailybugle.R;
import com.root.dailybugle.database.AppExecutors;
import com.root.dailybugle.database.NewsModel;
import com.root.dailybugle.database.NewsRoomDatabase;
import com.root.dailybugle.utils.Constants;
import com.root.dailybugle.widget.NewsAppWidget;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView description, author, date;
    Button button, button2;
    ProgressBar progressBar;
    private NewsRoomDatabase roomDatabase;
    boolean isExistFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        final NewsRoomDatabase newsRoomDatabase;
        newsRoomDatabase = NewsRoomDatabase.getDatabase(getApplicationContext());

        final Bundle bundle = getIntent().getExtras();
        description = findViewById(R.id.eventdescription);
        imageView = findViewById(R.id.eventimg);
        author = findViewById(R.id.authortext);
        date = findViewById(R.id.dateText);
        button = findViewById(R.id.urlButton);
        button2 = findViewById(R.id.favButton);
        progressBar = findViewById(R.id.progress);
        roomDatabase = NewsRoomDatabase.getDatabase(getApplicationContext());
        final String source = bundle.getString(Constants.SOURCE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(source);
        final String title = bundle.getString(Constants.TITLE);
        final String author1 = bundle.getString(Constants.AUTHOR);
        final String publish = bundle.getString(Constants.PUBLISHEDAT);
        final String desc = bundle.getString(Constants.DESCRIPTION);
        author.setText(author1);
        date.setText(publish);
        description.setText(desc);
        final String image = bundle.getString(Constants.URLTOIMAGE);
        final String url = bundle.getString(Constants.URL);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                isExistFav = newsRoomDatabase.newsDao().getFav(title, publish);
                if (isExistFav)
                    button2.setText("Remove Favourite");
            }
        });
        Picasso.with(this)
                .load(image)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setImageResource(R.drawable.news);
                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final NewsModel newsModel = new NewsModel(source, author1, title, desc, image, url, publish,true);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(button2.getText().toString()=="Remove Favourite"){
                            roomDatabase.newsDao().delete(newsModel);
                        }
                        else {
                            roomDatabase.newsDao().insert(newsModel);
                        }

                    }

                });
                Intent intent = new Intent(getApplicationContext(), NewsAppWidget.class);
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewsAppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(intent);
                finish();

            }
        });
    }
}