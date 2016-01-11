package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = ReviewsActivity.class.getSimpleName();

    private static final String INFO_NO_REVIEWS = "No Reviews";
    private static final String INFO_OFFLINE = "You are Offline!";
    private static final String INFO_OK_BUTTON = "Ok";

    public static final String REVIEW_URI = "URI";
    public static final String KEY_MOVIE_ID = "movieId";
    public static final String KEY_MOVIE_TITLE = "title";
    public static final String KEY_MOVIE_POSTER = "poster";
    private static final String KEY_REVIEW_ARRAY_LIST = "reviewArrayList";

    private static Movie mMovie;
    private static Context mContext;
    private static ArrayList<Review> reviewArrayList;

    @Bind(R.id.review_collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.review_main_content) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.review_backdrop) ImageView movieBackDrop;

    private final int MAX_CARDS = 8;

    private final int[] cardViewId = {
            R.id.review_card1, R.id.review_card2, R.id.review_card3,
            R.id.review_card4, R.id.review_card5, R.id.review_card6,
            R.id.review_card7, R.id.review_card8
    };
    CardView[] cardView = new CardView[MAX_CARDS];

    private final int[] titleTextId = {
            R.id.review_card_text_heading1, R.id.review_card_text_heading2, R.id.review_card_text_heading3,
            R.id.review_card_text_heading4, R.id.review_card_text_heading5, R.id.review_card_text_heading6,
            R.id.review_card_text_heading7, R.id.review_card_text_heading8
    };
    TextView[] titleTextView = new TextView[MAX_CARDS];

    private final int[] contentTextId = {
            R.id.review_card_content1, R.id.review_card_content2, R.id.review_card_content3,
            R.id.review_card_content4, R.id.review_card_content5, R.id.review_card_content6,
            R.id.review_card_content7, R.id.review_card_content8
    };
    TextView[] contentTextView = new TextView[MAX_CARDS];

    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        for (int index = 0; index <MAX_CARDS; index++){
            cardView[index] = (CardView) findViewById(cardViewId[index]);
            titleTextView[index] = (TextView) findViewById(titleTextId[index]);
            contentTextView[index] = (TextView) findViewById(contentTextId[index]);

            cardView[index].setVisibility(View.INVISIBLE);
            titleTextView[index].setText("");
            contentTextView[index].setText("");
        }
        mMovie = new Movie();
        reviewArrayList = new ArrayList<Review>();

        if (getIntent() != null) {
            Bundle args = getIntent().getExtras();

            if (args != null) {
                mMovie.setMovieId(args.getString(KEY_MOVIE_ID));
                mMovie.setTitle(args.getString(KEY_MOVIE_TITLE));
                mMovie.setPoster(args.getString(KEY_MOVIE_POSTER));
                Log.d(LOG_TAG, "Values read from bundleArgs are " + mMovie.getMovieId() + " / " +
                        mMovie.getTitle() + " / " + mMovie.getPoster());

                if (savedInstanceState != null) {
                    try{
                        reviewArrayList = savedInstanceState.getParcelableArrayList(KEY_REVIEW_ARRAY_LIST);
                        Log.d(LOG_TAG, "Values read from savedInstanceState is " +
                                " size - " + reviewArrayList.size());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "onCreate : NullPointerException when accessing reviewArrayList.size()");
                        e.printStackTrace();
                    }
                }
            }
        }else if (savedInstanceState != null){
            mMovie.setMovieId(savedInstanceState.getString(KEY_MOVIE_ID));
            mMovie.setTitle(savedInstanceState.getString(KEY_MOVIE_TITLE));
            mMovie.setPoster(savedInstanceState.getString(KEY_MOVIE_POSTER));
            reviewArrayList = savedInstanceState.getParcelableArrayList(KEY_REVIEW_ARRAY_LIST);

            Log.d(LOG_TAG, "Values read from savedInstanceState are " +
                    mMovie.getMovieId() + " / " + mMovie.getTitle() + " / " +
                    mMovie.getPoster() +  " / size - " + reviewArrayList.size());
        }

        collapsingToolbar.setTitle(mMovie.getTitle());
        if (Utility.isNetworkAvailable(mContext)) {
            collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        }else{
            collapsingToolbar.setExpandedTitleColor(Color.WHITE);
            collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        }

        Movie movie = new Movie();
        movie.setPoster(mMovie.getPoster());
        Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                movie, movieBackDrop);

        if (Utility.isNetworkAvailable(mContext)) {
            if (reviewArrayList.isEmpty()) {
                Log.d(LOG_TAG, "onCreate : reviewArrayList is Empty! So, access internet!");

                TMDBUrlBuilder tmdbUrlBuilder = new TMDBUrlBuilder();
                tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_REVIEWS);
                tmdbUrlBuilder.setMovieId(Integer.parseInt(mMovie.getMovieId()));

                FetchMovieReviewTask movieReviewTask = new FetchMovieReviewTask(tmdbUrlBuilder);
                movieReviewTask.execute();
            }else{
                Log.d(LOG_TAG, "onCreate : reviewArrayList is Full! So, just do painting!");
                updateReviewDetails();
            }
        }else{
            Log.d(LOG_TAG, "onCreate : You are Offline! So, inform this to user!");
            updateReviewDetails();
            showUpSnackBar();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_MOVIE_ID, mMovie.getMovieId());
        outState.putString(KEY_MOVIE_TITLE, mMovie.getTitle());
        outState.putString(KEY_MOVIE_POSTER, mMovie.getPoster());

        outState.putParcelableArrayList(KEY_REVIEW_ARRAY_LIST, reviewArrayList);
    }

    private void updateReviewDetails(){

        for (int index = 0; index < MAX_CARDS; index++){
            if (reviewArrayList.size() >= (index + 1)){
                cardView[index].setVisibility(View.VISIBLE);
                titleTextView[index].setText(reviewArrayList.get(index).getAuthor());
                contentTextView[index].setText(reviewArrayList.get(index).getContent());
            }else{
                if ((reviewArrayList.size() == 0) && (index == 0)){
                    cardView[0].setVisibility(View.VISIBLE);
                    titleTextView[0].setText(INFO_NO_REVIEWS);
                    ((LinearLayout)contentTextView[0].getParent()).removeView(contentTextView[0]);
                }else {
                    if (index != 0) {
                        ((LinearLayout) cardView[index].getParent()).removeView(cardView[index]);
                    }
                }
            }
        }
    }

    private void showUpSnackBar(){
        snackbar = Snackbar.make(coordinatorLayout,
                INFO_OFFLINE, Snackbar.LENGTH_LONG)
                .setAction(INFO_OK_BUTTON, this);
        snackbar.setActionTextColor(Color.RED);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);

        TextView snackBarText = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackBarText.setTextColor(Color.WHITE);

        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        finish();
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

    public class FetchMovieReviewTask extends AsyncTask<Void, Void, String> {

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
//                    Toast.makeText(mContext,
//                            "Total Reviews in MovieReviews is - " + movieReviewsData.getReviewCount(),
//                            Toast.LENGTH_SHORT)
//                            .show();
                    updateReviewDetails();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieReviewsData returned null");
                }
            }
        }
    }
}
