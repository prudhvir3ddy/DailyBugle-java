package com.root.dailybugle.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.root.dailybugle.utils.Constants;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.root.dailybugle.utils.Connection;

public class MainActivity extends AppCompatActivity {

    private Object item = "technology";
    private LoadToast lt;
    private HomeAdapter adapter;
    private static final String SpinnerKey = "sK";
    private static final String ArraylistKey = "alK";
    private ArrayList<Model> list;
    private boolean reinit;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reinit = true;
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        list = new ArrayList<>();
        lt = new LoadToast(MainActivity.this);
        if (savedInstanceState != null) {

            item = savedInstanceState.getCharSequence(SpinnerKey);
            reinit = false;
            list = savedInstanceState.getParcelableArrayList(ArraylistKey);
        } else {
            lt.show();
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.nnavigation);
        RecyclerView recyclerView = findViewById(R.id.hrecyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(list, MainActivity.this);
        recyclerView.setAdapter(adapter);


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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SpinnerKey, item.toString());
        outState.putParcelableArrayList(ArraylistKey, list);
        super.onSaveInstanceState(outState);
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
                if (reinit) {
                    if (item.toString().equals(getString(R.string.technology))) {
                        getData(getString(R.string.technology));
                    }
                    if (item.toString().equals(getString(R.string.business)))
                        getData(getString(R.string.business));
                    if (item.toString().equals(getString(R.string.sports)))
                        getData(getString(R.string.sports));
                    if (item.toString().equals(getString(R.string.general)))
                        getData(getString(R.string.general));
                    if (item.toString().equals(getString(R.string.entertainment)))
                        getData(getString(R.string.entertainment));
                    if (item.toString().equals(getString(R.string.health)))
                        getData(getString(R.string.health));
                    if (item.toString().equals(getString(R.string.science)))
                        getData(getString(R.string.science));
                } else {
                    reinit = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  getData(getString(R.string.technology));
            }
        });

        return true;
    }

    private void getData(String s){
        list.clear();
        new Connection(getApplicationContext(), this, s).execute();


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
                            list = getJsonArrayList(response.getJSONArray(Constants.ARTICLES));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.onrefresh(list);
                    }

                    @Override
                    public void onError(ANError anError) {
                        lt.error();
                    }
                });
    }

    class Connection extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        MainActivity parent;
        String s;

        Connection(Context context, MainActivity parent, String s) {
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
                fetchData(Constants.HEAD_URL + s + "&" + Constants.APIKEY + "=" + BuildConfig.API_KEY);
            else {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                lt.error();
            }
        }

    }

}

