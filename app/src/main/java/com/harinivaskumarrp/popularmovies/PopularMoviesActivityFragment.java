package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesActivityFragment extends Fragment {

    private final String LOG_TAG = PopularMoviesActivityFragment.class.getSimpleName();

    // please, add in your movie database APP ID from www.themoviedb.org
    private String MY_MOVIE_APP_ID = "Your Personal TMDB.org APP ID";

    public PopularMoviesActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ImageViewAdapter imageViewAdapter = new ImageViewAdapter(getContext());

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(imageViewAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + (1 + position), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    // Added from StackOverFlow
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return true;
        }
        Log.e(LOG_TAG, "No Internet Connection available.");
        return false;
    }

    private void updateMovieList(){
        int pageNumber = 1;
        int sortByType = 2;
        int minVoteCount = 100;

        FetchMovieTask movieTask = new FetchMovieTask(pageNumber, sortByType, minVoteCount);
        movieTask.execute();
    }

    public void onStart(){
        super.onStart();
        if (isNetworkAvailable()){
            updateMovieList();
        }else{
            Toast.makeText(getContext(),
                    "You are Offline.\nPlease, check your Network Connection!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, String> {

        private final String LOG_TAG_I = FetchMovieTask.class.getSimpleName();

        private final String DISCOVER_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        private final String PAGE_PARAM = "page";
        private final String VOTE_COUNT_PARAM = "vote_count.gte";
        private final String SORT_PARAM = "sort_by";
        private final String APPID_PARAM = "api_key";

        private final String POPULARITY_DESC = "popularity.desc";
        private final String HIGHEST_RATING = "vote_average.desc";

        private final int POPULARITY = 1;
        private final int RATING = 2;

        private final int SUCCESS_HTTP_OK = 200;
        private final int CLIENT_ERROR_BAD_REQUEST = 400;

        int mPageNumber = 0;
        int mSortByType = 0;
        int mMinVoteCount = 0;
        int mResponseCode = 0;

        public FetchMovieTask (int pageNumber, int sortByType, int minVoteCount) {
            setPageNumber(pageNumber);
            setSortByType(sortByType);
            setMinVoteCount(minVoteCount);
        }

        private int getPageNumber() {
            return mPageNumber;
        }

        private void setPageNumber(int pageNumber) {
            this.mPageNumber = pageNumber;
        }

        private int getSortByType() {
            return mSortByType;
        }

        private void setSortByType(int sortByType) {
            this.mSortByType = sortByType;
        }

        private int getMinVoteCount() {
            return mMinVoteCount;
        }

        private void setMinVoteCount(int minVoteCount) {
            this.mMinVoteCount = minVoteCount;
        }

        public int getResponseCode() {
            return mResponseCode;
        }

        public void setResponseCode(int responseCode) {
            this.mResponseCode = responseCode;
        }

        protected Uri getMovieUri (){
            if (getSortByType() == POPULARITY) {
                return (Uri.parse(DISCOVER_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, getPageNumber() + "")
                        .appendQueryParameter(SORT_PARAM, POPULARITY_DESC)
                        .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                        .build());
            }else{
                return (Uri.parse(DISCOVER_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, getPageNumber() + "")
                        .appendQueryParameter(VOTE_COUNT_PARAM, getMinVoteCount() + "")
                        .appendQueryParameter(SORT_PARAM, HIGHEST_RATING)
                        .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                        .build());
            }
        }

        protected boolean checkResponseCode (HttpURLConnection urlConnection)
                throws IOException{

            // Referred Android API guide
            setResponseCode(urlConnection.getResponseCode());
            String responseMessage = urlConnection.getResponseMessage();

            if (getResponseCode() == CLIENT_ERROR_BAD_REQUEST){
                Log.e(LOG_TAG_I, "Response: " + responseMessage + "/" + mResponseCode);
                return false;
            }else if(getResponseCode() == SUCCESS_HTTP_OK){
                Log.d(LOG_TAG_I, "Response: " + responseMessage + "/" + mResponseCode);
                return true;
            }
            return false;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                Uri movieUri = getMovieUri();

                String discoverMovieURL = URLDecoder.decode(movieUri.toString(), "UTF-8");
                //Log.d(LOG_TAG_I, "discoverMovieURL : " + discoverMovieURL);

                URL url = new URL(discoverMovieURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (!checkResponseCode(urlConnection)){
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null){
                    Log.e(LOG_TAG_I, "InputStream is Null!");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                movieJsonStr = buffer.toString();
                //Log.d(LOG_TAG_I, "MovieList Json String - " + movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG_I, "IOException");
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG_I, "IOException");
                    e.printStackTrace();
                }
            }

            return movieJsonStr;
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);

            if (getResponseCode() == CLIENT_ERROR_BAD_REQUEST) {
                Toast.makeText(getContext(),
                        "Invalid API key!\nYou must be granted a valid key.",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
