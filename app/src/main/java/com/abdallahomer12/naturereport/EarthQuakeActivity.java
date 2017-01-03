package com.abdallahomer12.naturereport;

import android.content.Context;
import android.content.Intent;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class EarthQuakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeInfo>> {
    ListView l;
    private EarthquakeAdapter mAdapter;
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query";

    private TextView emptyTextView;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_quake);

        l = (ListView) findViewById(R.id.list_view);
        emptyTextView = (TextView)findViewById(R.id.empty_view);
        l.setEmptyView(emptyTextView);
        mAdapter = new EarthquakeAdapter(this, new ArrayList<EarthquakeInfo>());

        l.setAdapter(mAdapter);



        l.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                EarthquakeInfo earthquakeInfo = mAdapter.getItem(position);
                String earthquakeUri = earthquakeInfo.getUri();
                Uri uri = Uri.parse(earthquakeUri);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
        ConnectivityManager conMngr =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = conMngr.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }else{
            View waiting2 = findViewById(R.id.waiting);
            waiting2.setVisibility(View.GONE);

            emptyTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<EarthquakeInfo>> onCreateLoader(int id, Bundle args) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String magnitude = pref.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));

        String orderBy = pref.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri reqUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = reqUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", magnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);


        return new EarthquakeAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<EarthquakeInfo>> loader, List<EarthquakeInfo> data) {
        View waiting1 = findViewById(R.id.waiting);
        waiting1.setVisibility(View.GONE);
        emptyTextView.setText(R.string.no_earthquakes);
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeInfo>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            Intent i = new Intent(EarthQuakeActivity.this,SettingActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
