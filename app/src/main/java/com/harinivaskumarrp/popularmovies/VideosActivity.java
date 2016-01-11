package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
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

public class VideosActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = VideosActivity.class.getSimpleName();

    private static final String YOUTUBE_BASE_URL = "youtu.be/";
    private static final String POPULAR_MOVIES_SHARE_HASHTAG = " #PopularMoviesApp";

    public static final String VIDEO_URI = "URI";
    public static final String KEY_MOVIE_ID = "movieId";
    public static final String KEY_MOVIE_TITLE = "title";
    public static final String KEY_MOVIE_POSTER = "poster";
    private static final String KEY_VIDEO_ARRAY_LIST = "videoArrayList";

    private static final String INFO_NO_VIDEOS = "No Videos";
    private static final String INFO_OFFLINE = "You are Offline!";
    private static final String INFO_OK_BUTTON = "Ok";

    private static Movie mMovie;
    private static Context mContext;
    private static ArrayList<Video> videoArrayList;

    @Bind(R.id.video_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.video_main_content)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.video_backdrop)
    ImageView movieBackDrop;

    private final int MAX_CARDS = 8;

    private final int[] cardViewId = {
            R.id.video_card1, R.id.video_card2, R.id.video_card3,
            R.id.video_card4, R.id.video_card5, R.id.video_card6,
            R.id.video_card7, R.id.video_card8
    };
    CardView[] cardView = new CardView[MAX_CARDS];

    private final int[] titleTextId = {
            R.id.video_card_text_heading1, R.id.video_card_text_heading2, R.id.video_card_text_heading3,
            R.id.video_card_text_heading4, R.id.video_card_text_heading5, R.id.video_card_text_heading6,
            R.id.video_card_text_heading7, R.id.video_card_text_heading8
    };
    TextView[] titleTextView = new TextView[MAX_CARDS];

    private final int[] contentTextId = {
            R.id.video_card_content1, R.id.video_card_content2, R.id.video_card_content3,
            R.id.video_card_content4, R.id.video_card_content5, R.id.video_card_content6,
            R.id.video_card_content7, R.id.video_card_content8
    };
    TextView[] contentTextView = new TextView[MAX_CARDS];

    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        setContentView(R.layout.activity_videos);
        ButterKnife.bind(this);

        for (int index = 0; index < MAX_CARDS; index++) {
            cardView[index] = (CardView) findViewById(cardViewId[index]);
            titleTextView[index] = (TextView) findViewById(titleTextId[index]);
            contentTextView[index] = (TextView) findViewById(contentTextId[index]);

            cardView[index].setVisibility(View.INVISIBLE);
            titleTextView[index].setText("");
            contentTextView[index].setText("");

            titleTextView[index].setOnClickListener(this);
        }

        mMovie = new Movie();
        videoArrayList = new ArrayList<Video>();

        if (getIntent() != null) {
            Bundle args = getIntent().getExtras();

            if (args != null) {
                mMovie.setMovieId(args.getString(KEY_MOVIE_ID));
                mMovie.setTitle(args.getString(KEY_MOVIE_TITLE));
                mMovie.setPoster(args.getString(KEY_MOVIE_POSTER));
                Log.d(LOG_TAG, "Values read from bundleArgs are " + mMovie.getMovieId() + " / " +
                        mMovie.getTitle() + " / " + mMovie.getPoster());

                if (savedInstanceState != null) {
                    try {
                        videoArrayList = savedInstanceState.getParcelableArrayList(KEY_VIDEO_ARRAY_LIST);
                        Log.d(LOG_TAG, "Values read from savedInstanceState is " +
                                " size - " + videoArrayList.size());
                    } catch (NullPointerException e) {
                        Log.d(LOG_TAG, "onCreate : NullPointerException when accessing videoArrayList.size()");
                        e.printStackTrace();
                    }
                }
            }
        } else if (savedInstanceState != null) {
            mMovie.setMovieId(savedInstanceState.getString(KEY_MOVIE_ID));
            mMovie.setTitle(savedInstanceState.getString(KEY_MOVIE_TITLE));
            mMovie.setPoster(savedInstanceState.getString(KEY_MOVIE_POSTER));
            videoArrayList = savedInstanceState.getParcelableArrayList(KEY_VIDEO_ARRAY_LIST);

            Log.d(LOG_TAG, "Values read from savedInstanceState are " +
                    mMovie.getMovieId() + " / " + mMovie.getTitle() + " / " +
                    mMovie.getPoster() + " / size - " + videoArrayList.size());
        }

        collapsingToolbar.setTitle(mMovie.getTitle());
        if (Utility.isNetworkAvailable(mContext)) {
            collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        } else {
            collapsingToolbar.setExpandedTitleColor(Color.WHITE);
            collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        }

        Movie movie = new Movie();
        movie.setPoster(mMovie.getPoster());
        Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                movie, movieBackDrop);

        if (Utility.isNetworkAvailable(mContext)) {
            if (videoArrayList.isEmpty()) {
                Log.d(LOG_TAG, "onCreate : videoArrayList is Empty! So, access internet!");

                TMDBUrlBuilder tmdbUrlBuilder = new TMDBUrlBuilder();
                tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_VIDEOS);
                tmdbUrlBuilder.setMovieId(Integer.parseInt(mMovie.getMovieId()));

                FetchMovieVideoTask movieVideoTask = new FetchMovieVideoTask(tmdbUrlBuilder);
                movieVideoTask.execute();
            } else {
                Log.d(LOG_TAG, "onCreate : videoArrayList is Full! So, just do painting!");
                updateVideoDetails();
            }
        } else {
            Log.d(LOG_TAG, "onCreate : You are Offline! So, inform this to user!");
            updateVideoDetails();
            showUpSnackBar();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_MOVIE_ID, mMovie.getMovieId());
        outState.putString(KEY_MOVIE_TITLE, mMovie.getTitle());
        outState.putString(KEY_MOVIE_POSTER, mMovie.getPoster());

        outState.putParcelableArrayList(KEY_VIDEO_ARRAY_LIST, videoArrayList);
    }

    private void updateVideoDetails() {
        for (int index = 0; index < MAX_CARDS; index++) {
            if (videoArrayList.size() >= (index + 1)) {
                String title = videoArrayList.get(index).getPublishedSite()
                        + " - " + videoArrayList.get(index).getName();
                String videoKey = videoArrayList.get(index).getKey();
                String content = videoKey + "\n " +
                        "http://img.youtube.com/vi/" + videoKey + "/0.jpg";

                cardView[index].setVisibility(View.VISIBLE);
                titleTextView[index].setText(title);
                contentTextView[index].setText(content);
            } else {
                if ((videoArrayList.size() == 0) && (index == 0)) {
                    cardView[0].setVisibility(View.VISIBLE);
                    titleTextView[0].setText(INFO_NO_VIDEOS);
                    ((LinearLayout) contentTextView[0].getParent()).removeView(contentTextView[0]);
                } else {
                    if (index != 0) {
                        ((LinearLayout) cardView[index].getParent()).removeView(cardView[index]);
                    }
                }
            }
        }
    }

    private void showUpSnackBar() {
        snackbar = Snackbar.make(coordinatorLayout, INFO_OFFLINE, Snackbar.LENGTH_LONG)
                .setAction(INFO_OK_BUTTON, this);
        snackbar.setActionTextColor(Color.RED);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);

        TextView snackBarText = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackBarText.setTextColor(Color.WHITE);

        snackbar.show();
    }

    @Override
    public void onClick(View view) {
        for (int index = 0; index < MAX_CARDS; index++) {
            if (view.getId() == titleTextId[index]) {
                String urlPrefix = "http://" + YOUTUBE_BASE_URL;
                String actualUrl = urlPrefix + videoArrayList.get(index).getKey();

                Uri uri = Uri.parse(actualUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                Log.d(LOG_TAG, videoArrayList.get(index).getName() + " started for " +
                        mMovie.getTitle() + " @ " + actualUrl);
                return;
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_videos, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (Utility.isNetworkAvailable(mContext)) {
            if (videoArrayList.size() != 0) {
                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareForecastIntent());
                } else {
                    Log.d(LOG_TAG, "Share Action Provider is null?");
                }
            }
        }else{
            showUpSnackBar();
        }
        return true;
    }

    private Intent createShareForecastIntent() {
        String shareStr = mMovie.getTitle() + " - Watch the Trailer here - " +
                YOUTUBE_BASE_URL + videoArrayList.get(0).getKey() +
                POPULAR_MOVIES_SHARE_HASHTAG;
        Log.d(LOG_TAG, "Share Text is - \n" + shareStr);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareStr);

        return shareIntent;
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

    public class FetchMovieVideoTask extends AsyncTask<Void, Void, String> {

        private final String LOG_TAG_I = FetchMovieVideoTask.class.getSimpleName();

        TMDBUrlBuilder tmdbUrlBuilder;

        public FetchMovieVideoTask(TMDBUrlBuilder tmdbUrlBuilder) {
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

            if (!tmdbUrlBuilder.validateHttpResponseCode()) {
                Toast.makeText(mContext,
                        "Invalid API key!\nYou must be granted a valid key.",
                        Toast.LENGTH_SHORT)
                        .show();
            } else if (tmdbUrlBuilder.validateHttpResponseCode()) {
                MovieVideosData movieVideosData = new MovieVideosData(jsonString);
                if (movieVideosData.parseMovieVideosData()) {
                    videoArrayList = movieVideosData.videoList;
//                    Toast.makeText(mContext,
//                            "Total Videos in MovieVideos is - " + movieVideosData.getVideoCount(),
//                            Toast.LENGTH_SHORT)
//                            .show();
                    updateVideoDetails();
                } else {
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieVideosData returned null");
                }
            }
        }
    }
}