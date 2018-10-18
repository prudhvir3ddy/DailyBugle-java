package com.root.dailybugle.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.root.dailybugle.BuildConfig;
import com.root.dailybugle.R;
import com.root.dailybugle.adapters.HomeAdapter;
import com.root.dailybugle.models.Model;
import com.root.dailybugle.utils.Connection;
import com.root.dailybugle.utils.Constants;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Object item = "technology";
    private LoadToast lt;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeAdapter adapter;
    private List<Model> list;
    private BottomNavigationView bottomNavigationView;
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        list = new ArrayList<>();
        lt = new LoadToast(MainActivity.this);
        bottomNavigationView = findViewById(R.id.nnavigation);
        recyclerView = findViewById(R.id.hrecyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(list, MainActivity.this);
        recyclerView.setAdapter(adapter);
         connection = new Connection(getApplicationContext());
        lt.show();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.search)
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                if (item.getItemId() == R.id.favourites)
                    startActivity(new Intent(getApplicationContext(), FavouriteActivity.class));
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.news_menu, menu);
        MenuItem spinnerMenuItem = menu.findItem(R.id.miSpinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(spinnerMenuItem);
        spinner.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.choice, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerAdapter.getPosition(item.toString()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                item = adapterView.getItemAtPosition(position);
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                if (item.toString().equals("technology")) {
                    getData("technology");
                }
                if (item.toString().equals("business"))
                    getData("business");
                if (item.toString().equals("sports"))
                    getData("sports");
                if (item.toString().equals("general"))
                    getData("general");
                if (item.toString().equals("entertainment"))
                    getData("entertainment");
                if (item.toString().equals("health"))
                    getData("health");
                if (item.toString().equals("science"))
                    getData("science");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getData("technology");
            }
        });

        return true;
    }

    private void getData(String s){
        list.clear();
        if(connection.isInternet())
            fetchData(Constants.HEAD_URL+s+"&"+Constants.APIKEY+"="+BuildConfig.API_KEY);
        else
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
    }
    private void fetchData(String url) {

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lt.success();
                        try {
                            list = getJsonArrayList(response.getJSONArray("articles"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.onrefresh(list);
                    }

                    @Override
                    public void onError(ANError anError) {
                        lt.error();
                        Log.d("error", "" + anError);
                    }
                });
    }

    private static List<Model> getJsonArrayList(JSONArray jsonArray) {

        List<Model> list = new ArrayList<>();

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject j = jsonArray.getJSONObject(i);
                    Model model = new Model();
                    JSONObject s = j.getJSONObject(Constants.SOURCE);
                    model.setAuthor(j.optString(Constants.AUTHOR));
                    model.setDesc(j.optString(Constants.DESCRIPTION));
                    model.setImage(j.optString(Constants.URLTOIMAGE));
                    model.setUrl(j.optString(Constants.URL));
                    model.setTitle(j.optString(Constants.TITLE));
                    model.setSname(s.optString(Constants.NAME));
                    model.setDate(j.optString(Constants.PUBLISHEDAT));
                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return list;

    }
}
