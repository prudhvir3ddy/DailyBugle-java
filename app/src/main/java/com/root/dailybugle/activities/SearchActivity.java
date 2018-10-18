package com.root.dailybugle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.root.dailybugle.R;
import com.root.dailybugle.adapters.SearchAdapter;
import com.root.dailybugle.models.Model;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton button;
    EditText editText;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter searchAdapter;
    List<Model> list;
    LoadToast lt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   list = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AndroidNetworking.initialize(getApplicationContext());
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        recyclerView=findViewById(R.id.srecyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        lt=new LoadToast(SearchActivity.this);
        final String url="https://newsapi.org/v2/everything";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lt.show();
                String k = editText.getText().toString();
                AndroidNetworking.get(url)
                        .addQueryParameter("q",k)
                        .addQueryParameter("apiKey","4c25c50ca9a0466e8b4991b61556e6ae")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                lt.success();
                                try {
                                    list=getJsonArrayList(response.getJSONArray("articles"));
                                    Log.d("list",""+list);
                                    searchAdapter = new SearchAdapter(list, SearchActivity.this);
                                    recyclerView.setAdapter(searchAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                searchAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(ANError anError) {
                                lt.error();
                                Log.d("error", "" + anError);
                            }
                        });
            }
        });


        bottomNavigationView=findViewById(R.id.snavigation);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.news){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                if(item.getItemId()==R.id.search){

                }
                if(item.getItemId()==R.id.favourites){
                    startActivity(new Intent(getApplicationContext(),FavouriteActivity.class));
                }
                return false;
            }
        });
    }
    private static List<Model> getJsonArrayList(JSONArray jsonArray) {

        List<Model> list=new ArrayList<>();

        if (jsonArray!=null){
            for (int i=0; i<jsonArray.length();i++){
                try {
                    JSONObject j = jsonArray.getJSONObject(i);
                    Model model=new Model();
                    JSONObject s=j.getJSONObject("source");
                    model.setAuthor(j.getString("author"));
                    model.setDesc(j.getString("description"));
                    model.setImage(j.getString("urlToImage"));
                    model.setUrl(j.getString("url"));
                    model.setTitle(j.getString("title"));
                    model.setSname(s.getString("name"));
                    model.setDate(j.getString("publishedAt"));

                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return list;

    }
}

