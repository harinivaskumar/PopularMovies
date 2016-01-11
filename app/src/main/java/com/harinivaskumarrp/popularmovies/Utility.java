package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Hari Nivas Kumar R P on 1/9/2016.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    // Added from StackOverFlow
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        Log.e(LOG_TAG, "No Internet Connection available.");
        return false;
    }
}
