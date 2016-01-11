package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VideosActivity extends AppCompatActivity {

    private static final String LOG_TAG = VideosActivity.class.getSimpleName();

    public static final String VIDEO_URI = "URI";
    public static final String KEY_MOVIE_ID = "movieId";
    public static final String KEY_MOVIE_TITLE = "movieTitle";
    public static final String KEY_MOVIE_POSTER = "moviePoster";

    private String movieId = null;
    public static Context mContext;
    public static ArrayList<Video> videoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_videos);
        videoArrayList = new ArrayList<Video>();

        Bundle args = getIntent().getExtras();
        movieId = args.getString(KEY_MOVIE_ID);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Movie Name");
        collapsingToolbar.setExpandedTitleColor(Color.BLACK);
        collapsingToolbar.setCollapsedTitleTextColor(Color.YELLOW);

        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Movie movie = new Movie();
        movie.setPoster("/fYzpM9GmpBlIC893fNjoWCwE24H.jpg");
        Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                movie, imageView);

//        Toast.makeText(this, "You have clicked Video Button for Movie " + movieId,
//                Toast.LENGTH_SHORT).show();

        TMDBUrlBuilder tmdbUrlBuilder = new TMDBUrlBuilder();
        tmdbUrlBuilder.setUrlEndPoint(tmdbUrlBuilder.MOVIE_VIDEOS);
        tmdbUrlBuilder.setMovieId(Integer.parseInt(movieId));

        FetchMovieVideoTask movieVideosTask = new FetchMovieVideoTask(tmdbUrlBuilder);
        movieVideosTask.execute();
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

        public FetchMovieVideoTask(TMDBUrlBuilder tmdbUrlBuilder){
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
                MovieVideosData movieVideosData = new MovieVideosData(jsonString);
                if (movieVideosData.parseMovieVideosData()){
                    videoArrayList = movieVideosData.videoList;
                    Toast.makeText(mContext,
                            "Total Videos in MovieVideos is - " + movieVideosData.getVideoCount(),
                            Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Log.e(LOG_TAG_I, "onPostExecute : parseMovieVideosData returned null");
                }
            }
        }
    }
}
