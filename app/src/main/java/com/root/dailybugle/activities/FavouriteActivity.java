package com.root.dailybugle.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.root.dailybugle.R;
import com.root.dailybugle.adapters.FavouriteAdapter;
import com.root.dailybugle.database.NewsModel;
import com.root.dailybugle.database.NewsRoomDatabase;
import com.root.dailybugle.database.NewsViewModel;
import com.root.dailybugle.models.Model;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FavouriteAdapter adapter;
    NewsRoomDatabase newsRoomDatabase;
    BottomNavigationView bottomNavigationView;
    LiveData<List<NewsModel>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        newsRoomDatabase=NewsRoomDatabase.getDatabase(getApplicationContext());
        recyclerView=findViewById(R.id.frecyclerview);
        bottomNavigationView=findViewById(R.id.fnavigation);
        bottomNavigationView.setSelectedItemId(R.id.favourites);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.news)
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                if(item.getItemId()==R.id.search)
                    startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                if(item.getItemId()==R.id.favourites){
                }
                return false;
            }
        });

        NewsViewModel viewModel= ViewModelProviders.of(this).get(NewsViewModel.class);
        viewModel.getList().observe(this, new Observer<List<NewsModel>>() {
            @Override
            public void onChanged(@Nullable List<NewsModel> newsModels) {
                adapter=new FavouriteAdapter(newsModels,FavouriteActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });


    }
}