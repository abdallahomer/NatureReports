package com.abdallahomer12.naturereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by ProG_AbdALlAh on 12/30/2016.
 */

public class EarthquakeAsyncTaskLoader extends AsyncTaskLoader<List<EarthquakeInfo>> {
    private String mUrl;


    public EarthquakeAsyncTaskLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }

    @Override
    public List<EarthquakeInfo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<EarthquakeInfo> result = EarthquakeUtils.fetchEarthquakeData(mUrl);
        return result;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


}
