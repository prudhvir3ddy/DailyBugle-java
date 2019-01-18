package com.root.dailybugle.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.root.dailybugle.BuildConfig;
import com.root.dailybugle.R;
import com.root.dailybugle.adapters.SearchAdapter;
import com.root.dailybugle.models.Model;
import com.root.dailybugle.utils.Constants;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText editText;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private ArrayList<Model> list;
    private LoadToast lt;
    private String k;

    private static ArrayList<Model> getJsonArrayList(JSONArray jsonArray) {

        ArrayList<Model> list = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
   list = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AndroidNetworking.initialize(getApplicationContext());
        ImageButton button = findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        recyclerView=findViewById(R.id.srecyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        lt=new LoadToast(SearchActivity.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lt.show();
                k = editText.getText().toString();
                new Connection(getApplicationContext(), SearchActivity.this, k).execute();

            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.snavigation);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.news)
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                if(item.getItemId()==R.id.favourites)
                    startActivity(new Intent(getApplicationContext(),FavouriteActivity.class));
                return false;
            }
        });
    }

    private void getData(String k) {
        AndroidNetworking.get(Constants.SEARCH_URL)
                .addQueryParameter(Constants.QUERY,k)
                .addQueryParameter(Constants.APIKEY,BuildConfig.API_KEY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lt.success();
                        try {
                            list = getJsonArrayList(response.getJSONArray(Constants.ARTICLES));
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
                    }
                });
    }

    class Connection extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        SearchActivity parent;
        String s;

        Connection(Context context, SearchActivity parent, String s) {
            this.context = context;
            this.parent = parent;
            this.s = s;
        }

        boolean isInternet() {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo[] info = manager.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }
            return false;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return isInternet();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if (aBoolean)
                getData(k);
            else {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                lt.error();
            }
        }

    }
}

