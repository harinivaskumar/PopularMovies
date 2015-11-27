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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesActivityFragment extends Fragment {

    private final String LOG_TAG = PopularMoviesActivityFragment.class.getSimpleName();

    // please, add in your movie database APP ID from www.themoviedb.org
    private String MY_MOVIE_APP_ID = "Your Personal APP_ID";

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
        int pageNumber = 1;
        int sortByType = 2;
        int minVoteCount = 100;

        FetchMovieTask movieTask = new FetchMovieTask(pageNumber, sortByType, minVoteCount);
        movieTask.execute();
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

    public class FetchMovieTask extends AsyncTask<Void, Void, String> {

        private final String LOG_TAG_I = FetchMovieTask.class.getSimpleName();

        private final String DISCOVER_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        private final String PAGE_PARAM = "page";
        private final String VOTE_COUNT_PARAM = "vote_count.gte";
        private final String SORT_PARAM = "sort_by";
        private final String APPID_PARAM = "api_key";

        private final String POPULARITY_DESC = "popularity.desc";
        private final String HIGHEST_RATING = "vote_average.desc";

        private final int DISCOVER_MOVIE = 1;

        private final int POPULARITY = 1;
        private final int RATING = 2;

        private final int HTTP_REQUEST_FAILURE = 0;
        private final int HTTP_SUCCESS_HTTP_OK = 200;
        private final int HTTP_CLIENT_ERROR_BAD_REQUEST = 400;

        int mUrlHeadType = 0;
        int mPageNumber = 0;
        int mSortByType = 0;
        int mMinVoteCount = 0;
        int mResponseCode = 0;
        String mResponseMessage = null;

        public FetchMovieTask(int pageNumber, int sortByType, int minVoteCount) {
            setUrlHeadType(DISCOVER_MOVIE);
            setPageNumber(pageNumber);
            setSortByType(sortByType);
            setMinVoteCount(minVoteCount);
            setResponseCode(HTTP_REQUEST_FAILURE);
            setResponseMessage(null);
        }

        private int getUrlHeadType() {
            return mUrlHeadType;
        }

        private void setUrlHeadType(int urlHeadType) {
            this.mUrlHeadType = urlHeadType;
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

        private int getResponseCode() {
            return mResponseCode;
        }

        private void setResponseCode(int responseCode) {
            this.mResponseCode = responseCode;
        }

        private String getResponseMessage() {
            return mResponseMessage;
        }

        private void setResponseMessage(String responseMessage) {
            this.mResponseMessage = responseMessage;
        }

        private Uri getTmdbUri() {

            String baseURLStr = null;

            if (getUrlHeadType() == DISCOVER_MOVIE) {
                baseURLStr = DISCOVER_MOVIE_BASE_URL;

                if (getSortByType() == POPULARITY) {
                    return (Uri.parse(baseURLStr).buildUpon()
                            .appendQueryParameter(PAGE_PARAM, getPageNumber() + "")
                            .appendQueryParameter(SORT_PARAM, POPULARITY_DESC)
                            .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                            .build());
                } else if (getSortByType() == RATING) {
                    return (Uri.parse(baseURLStr).buildUpon()
                            .appendQueryParameter(PAGE_PARAM, getPageNumber() + "")
                            .appendQueryParameter(VOTE_COUNT_PARAM, getMinVoteCount() + "")
                            .appendQueryParameter(SORT_PARAM, HIGHEST_RATING)
                            .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                            .build());
                }
            }
            return null;
        }

        private URL getTmdbURLInstance() {

            URL tmdbURL = null;

            try {
                Uri tmdbUri = getTmdbUri();

                if (tmdbUri == null) {
                    Log.e(LOG_TAG_I, "URI build Failed");
                } else {
                    String tmdbURLStr = URLDecoder.decode(tmdbUri.toString(), "UTF-8");
                    Log.d(LOG_TAG_I, "TMDB URL : " + tmdbURLStr);

                    tmdbURL = new URL(tmdbURLStr);
                }
            } catch (UnsupportedEncodingException | MalformedURLException e) {
                Log.e(LOG_TAG_I, "getTmdbURLInstance : UnsupportedEncodingException/MalformedURLException");
                e.printStackTrace();
            }
            return tmdbURL;
        }

        private boolean checkResponseCode(InputStream inputErrorStream)
                throws IOException {

            if (getResponseCode() == HTTP_CLIENT_ERROR_BAD_REQUEST) {
                Log.d(LOG_TAG_I, "Response: " + mResponseMessage + "/" + mResponseCode);

                String jsonErrorStr =
                        readJSONStringFromIOStream(inputErrorStream);

                Log.d(LOG_TAG_I, "Json Error String - " + jsonErrorStr);
            } else if (getResponseCode() == HTTP_SUCCESS_HTTP_OK) {
                Log.d(LOG_TAG_I, "Response: " + mResponseMessage + "/" + mResponseCode);
                return true;
            }
            return false;
        }

        private String readJSONStringFromIOStream(InputStream inputStream) {

            BufferedReader bufferedReader = null;
            String jsonStr = null;

            if (inputStream == null) {
                Log.e(LOG_TAG_I, "readJSONStringFromIOStream : InputStream is Null");
            } else {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer bufferStr = new StringBuffer();
                    String lineStr = "";

                    while ((lineStr = bufferedReader.readLine()) != null) {
                        bufferStr.append(lineStr);
                    }

                    jsonStr = bufferStr.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG_I, "readJSONStringFromIOStream : IOException");
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        Log.e(LOG_TAG_I, "readJSONStringFromIOStream : IOException");
                        e.printStackTrace();
                    }
                }
            }
            return jsonStr;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            String jsonStr = null;

            try {
                URL url = getTmdbURLInstance();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Referred Android API guide
                setResponseCode(urlConnection.getResponseCode());
                setResponseMessage(urlConnection.getResponseMessage());

                if (!checkResponseCode(urlConnection.getErrorStream())) {
                    return null;
                }

                jsonStr = readJSONStringFromIOStream(urlConnection.getInputStream());
                Log.d(LOG_TAG_I, "Json String - " + jsonStr);
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
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);

            if (getResponseCode() == HTTP_CLIENT_ERROR_BAD_REQUEST) {
                Toast.makeText(getContext(),
                        "Invalid API key!\nYou must be granted a valid key.",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
