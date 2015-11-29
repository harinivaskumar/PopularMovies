package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesActivityFragment extends Fragment implements AdapterView.OnItemClickListener{

    private final String LOG_TAG = PopularMoviesActivityFragment.class.getSimpleName();

    private final String KEY_SORT_BY_TYPE = "sortByType";
    private final String KEY_PAGE_NUMBER = "pageNumber";
    private final String KEY_MIN_VOTE_COUNT = "minVoteCount";
    private final String KEY_MOVIE_LIST = "movieList";

    private ArrayList<Movie> movieArrayList;
    private ImageViewAdapter mImageViewAdapter = null;
    private GridView mGridView = null;

    private String mSortByTypeStr, mPageNumberStr, mMinVoteCountStr;

    public PopularMoviesActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null ||
                !savedInstanceState.containsKey(KEY_MOVIE_LIST) ||
                !savedInstanceState.containsKey(KEY_SORT_BY_TYPE) ||
                !savedInstanceState.containsKey(KEY_PAGE_NUMBER) ||
                !savedInstanceState.containsKey(KEY_MIN_VOTE_COUNT)){

            Log.d(LOG_TAG, "onCreate : Movie Parcel 'movieList' not found. Access the Internet for Movie list!");
            movieArrayList = new ArrayList<Movie>();
            mSortByTypeStr = mPageNumberStr = mMinVoteCountStr = null;
        }
        else {
            Log.d(LOG_TAG, "onCreate : Read the Prefs Strings!");
            setSortByTypeStr(savedInstanceState.getString(KEY_SORT_BY_TYPE));
            setPageNumberStr(savedInstanceState.getString(KEY_PAGE_NUMBER));
            setMinVoteCountStr(savedInstanceState.getString(KEY_MIN_VOTE_COUNT));

            Log.d(LOG_TAG, "onCreate : Movie Parcel 'movieList' found. Open & use this for Painting!");
            movieArrayList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNetworkAvailable()) {
            if(movieArrayList.isEmpty()) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

                setSortByTypeStr(prefs.getString(getString(R.string.pref_sort_key),
                        getString(R.string.pref_sort_value_popularity)));
                setPageNumberStr(prefs.getString(getString(R.string.pref_page_key_config),
                        getString(R.string.pref_page_default)));
                setMinVoteCountStr(prefs.getString(getString(R.string.pref_min_vote_key_config),
                        getString(R.string.pref_min_vote_default)));

                updateMovieList(mSortByTypeStr, mPageNumberStr, mMinVoteCountStr);
                Log.d(LOG_TAG, "onStart : Movie ArrayList is Empty, so access Internet to Fetch MovieList!");
            }else{
                paintWithMoviePosters();
                Log.d(LOG_TAG, "onStart : Movie ArrayList is Full, so just do Painting!");
            }
            Log.d(LOG_TAG, "onStart : Prefs - " +
                    "SortByType[" + getSortByTypeStr() + "] " +
                    "PageNumber[" + getMinVoteCountStr() + "] " +
                    "MinVoteCount[" + getMinVoteCountStr() + "]");
        } else {
            Log.i(LOG_TAG, "onStart : You are Offline. Please, check your Network Connection!");
            Toast.makeText(getContext(),
                    "You are Offline.\nPlease, check your Network Connection!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortByTypeNewStr = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_value_popularity));
        String pageNumberNewStr = prefs.getString(getString(R.string.pref_page_key_config),
                getString(R.string.pref_page_default));
        String minVoteCountNewStr = prefs.getString(getString(R.string.pref_min_vote_key_config),
                getString(R.string.pref_min_vote_default));

        Log.d(LOG_TAG, "onResume : Prefs - " +
                "SortByType[" + getSortByTypeStr() + "] " +
                "PageNumber[" + getMinVoteCountStr() + "] " +
                "MinVoteCount[" + getMinVoteCountStr() + "]");

        if ((getSortByTypeStr().equals(sortByTypeNewStr)) &&
            (getPageNumberStr().equals(pageNumberNewStr)) &&
            (getMinVoteCountStr().equals(minVoteCountNewStr))) {
            paintWithMoviePosters();
            Log.d(LOG_TAG, "onResume : Movie Pref unchanged. If required just do Painting!");
        }else{
            updateMovieList(sortByTypeNewStr, pageNumberNewStr, minVoteCountNewStr);
            Log.d(LOG_TAG, "onResume : Movie Pref changed. So, access internet to Fetch New MovieList!");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState : Save the Prefs Strings!");
        outState.putString(KEY_SORT_BY_TYPE, getSortByTypeStr());
        outState.putString(KEY_PAGE_NUMBER, getPageNumberStr());
        outState.putString(KEY_MOVIE_LIST, getMinVoteCountStr());

        Log.d(LOG_TAG, "onSaveInstanceState : Save the Moive Parcel as 'movieList'!");
        outState.putParcelableArrayList(KEY_MOVIE_LIST, movieArrayList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        mImageViewAdapter = new ImageViewAdapter(getContext());
        mImageViewAdapter.setMovieList(movieArrayList);

        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(this);

        Log.d(LOG_TAG, "onCreateView : UI inflated & resources are locally populated!");
        return rootView;
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Toast.makeText(getContext(), "" + (1 + position), Toast.LENGTH_SHORT).show();
    }

    private String getSortByTypeStr() {
        return mSortByTypeStr;
    }

    private void setSortByTypeStr(String mSortByTypeStr) {
        this.mSortByTypeStr = mSortByTypeStr;
    }

    private String getPageNumberStr() {
        return mPageNumberStr;
    }

    private void setPageNumberStr(String mPageNumberStr) {
        this.mPageNumberStr = mPageNumberStr;
    }

    private String getMinVoteCountStr() {
        return mMinVoteCountStr;
    }

    private void setMinVoteCountStr(String mMinVoteCountStr) {
        this.mMinVoteCountStr = mMinVoteCountStr;
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

    private void updateMovieList(String sortByTypeStr, String pageNumberStr, String minVoteCountStr) {
        setSortByTypeStr(sortByTypeStr);
        setMinVoteCountStr(pageNumberStr);
        setMinVoteCountStr(minVoteCountStr);

        TMDBUrlBuilder tmdbUrlBuilder = new TMDBUrlBuilder();

        tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.DISCOVER_MOVIE);
        //tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_VIDEOS);
        //tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_REVIEWS);
        tmdbUrlBuilder.setSortByType(sortByTypeStr);
        tmdbUrlBuilder.setPageNumber(pageNumberStr);
        tmdbUrlBuilder.setMinVoteCount(minVoteCountStr);

        FetchMovieListTask movieListTask = new FetchMovieListTask(tmdbUrlBuilder);
        movieListTask.execute();
    }

    private void paintWithMoviePosters(){
        mImageViewAdapter.setMovieList(movieArrayList);
        Log.d(LOG_TAG, "Total Movies in MovieList is - " + movieArrayList.size());
        /*Toast.makeText(getActivity(),
                "Total Movies in MovieList is - " + movieArrayList.size(),
                Toast.LENGTH_SHORT)
                .show();*/
        mGridView.setAdapter(mImageViewAdapter);
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
                //Log.d(LOG_TAG_I, "doInBackground : Json String - " + jsonStr);
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
                Toast.makeText(getActivity(),
                        "Invalid API key!\nYou must be granted a valid key.",
                        Toast.LENGTH_SHORT)
                        .show();
            }else if (tmdbUrlBuilder.validateHttpResponseCode()){
                MovieListData movieListData = new MovieListData(jsonString);
                if (movieListData.parseMovieListData()) {
                    movieArrayList = movieListData.movieList;
                    paintWithMoviePosters();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieListData returned null");
                }
                /*MovieVideosData movieVideosData = new MovieVideosData(jsonString);
                if (movieVideosData.parseMovieVideosData()){
                    Toast.makeText(getActivity(),
                            "Total Videos in MovieVideos is - " + movieVideosData.getVideoCount(),
                            Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieVideosData returned null");
                }*/
                /*MovieReviewsData movieReviewsData = new MovieReviewsData(jsonString);
                if (movieReviewsData.parseMovieReviewsData()){
                    Toast.makeText(getActivity(),
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
