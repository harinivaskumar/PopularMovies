package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    private static final String LOG_TAG = ReviewsActivity.class.getSimpleName();

    public static final String REVIEW_URI = "URI";
    public static final String KEY_MOVIE_ID = "movieId";

    private String movieId = null;
    public static Context mContext;
    public static ArrayList<Review> reviewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_reviews);
        reviewArrayList = new ArrayList<Review>();

        Bundle args = getIntent().getExtras();
        movieId = args.getString(KEY_MOVIE_ID);

//        Toast.makeText(this, "You have clicked Review Button for Movie " + movieId,
//                Toast.LENGTH_SHORT).show();

        TMDBUrlBuilder tmdbUrlBuilder = new TMDBUrlBuilder();
        tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_REVIEWS);
        tmdbUrlBuilder.setMovieId(Integer.parseInt(movieId));

        FetchMovieReviewTask movieReviewTask = new FetchMovieReviewTask(tmdbUrlBuilder);
        movieReviewTask.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class FetchMovieReviewTask extends AsyncTask<Void, Void, String> {

        private final String LOG_TAG_I = FetchMovieReviewTask.class.getSimpleName();

        TMDBUrlBuilder tmdbUrlBuilder;

        public FetchMovieReviewTask(TMDBUrlBuilder tmdbUrlBuilder){
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
                Toast.makeText(mContext,
                        "Invalid API key!\nYou must be granted a valid key.",
                        Toast.LENGTH_SHORT)
                        .show();
            }else if (tmdbUrlBuilder.validateHttpResponseCode()){
                MovieReviewsData movieReviewsData = new MovieReviewsData(jsonString);
                if (movieReviewsData.parseMovieReviewsData()){
                    reviewArrayList = movieReviewsData.reviewList;
                    Toast.makeText(mContext,
                            "Total Reviews in MovieReviews is - " + movieReviewsData.getReviewCount(),
                            Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieReviewsData returned null");
                }
            }
        }
    }
}
