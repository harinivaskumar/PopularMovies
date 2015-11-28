package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesActivityFragment extends Fragment {

    private final String LOG_TAG = PopularMoviesActivityFragment.class.getSimpleName();

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
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        Log.e(LOG_TAG, "No Internet Connection available.");
        return false;
    }

    private void updateMovieList() {
        int urlEndPoint = 0, movieId = 157336;
        int pageNumber = 1, sortByType = 1, minVoteCount = 100;
        TMDBUrlBuilder tmdbUrlBuilder = new TMDBUrlBuilder(urlEndPoint, pageNumber,
                        sortByType, minVoteCount, movieId);

        tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.DISCOVER_MOVIE);
        //tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_VIDEOS);
        //tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_REVIEWS);

        FetchMovieListTask movieListTask = new FetchMovieListTask(tmdbUrlBuilder);
        movieListTask.execute();
    }

    public void onStart() {
        super.onStart();
        if (isNetworkAvailable()) {
            updateMovieList();
        } else {
            Toast.makeText(getContext(),
                    "You are Offline.\nPlease, check your Network Connection!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    public class FetchMovieListTask extends AsyncTask<Void, Void, String> {

        private final String LOG_TAG_I = FetchMovieListTask.class.getSimpleName();

        TMDBUrlBuilder tmdbUrlBuilder;

        public FetchMovieListTask(TMDBUrlBuilder tmdbUrlBuilder){
            this.tmdbUrlBuilder = tmdbUrlBuilder;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            String jsonStr = null;

            try {
                URL url = tmdbUrlBuilder.getTmdbURLInstance();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Referred Android API guide
                tmdbUrlBuilder.apiStatusCodeData = new APIStatusCodeData();
                tmdbUrlBuilder.apiStatusCodeData.apiStatusCodes.setHttpStatusCode(urlConnection.getResponseCode());
                tmdbUrlBuilder.apiStatusCodeData.apiStatusCodes.setHttpStatusMessage(urlConnection.getResponseMessage());

                if (!tmdbUrlBuilder.checkResponseCode(urlConnection.getErrorStream())) {
                    return null;
                }

                jsonStr = tmdbUrlBuilder.readJSONStringFromIOStream(urlConnection.getInputStream());
                Log.d(LOG_TAG_I, "doInBackground : Json String - " + jsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG_I, "doInBackground : IOException");
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            if (!tmdbUrlBuilder.validateHttpResponseCode()){
                Toast.makeText(getContext(),
                        "Invalid API key!\nYou must be granted a valid key.",
                        Toast.LENGTH_SHORT)
                        .show();
            }else if (tmdbUrlBuilder.validateHttpResponseCode()){
                MovieListData movieListData = new MovieListData(jsonString);
                if (movieListData.parseMovieListData()) {
                    Toast.makeText(getContext(),
                            "Total Movies in MovieList is - " + movieListData.getMovieCount(),
                            Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieListData returned null");
                }
                /*MovieVideosData movieVideosData = new MovieVideosData(jsonString);
                if (movieVideosData.parseMovieVideosData()){
                    Toast.makeText(getContext(),
                            "Total Videos in MovieVideos is - " + movieVideosData.getVideoCount(),
                            Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieVideosData returned null");
                }*/
                /*MovieReviewsData movieReviewsData = new MovieReviewsData(jsonString);
                if (movieReviewsData.parseMovieReviewsData()){
                    Toast.makeText(getContext(),
                            "Total Reviews in MovieReviews is - " + movieReviewsData.getReviewCount(),
                            Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieReviewsData returned null");
                }*/
            }
        }
    }
}
