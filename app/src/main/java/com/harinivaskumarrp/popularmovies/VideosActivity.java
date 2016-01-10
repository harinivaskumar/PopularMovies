package com.harinivaskumarrp.popularmovies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class VideosActivity extends AppCompatActivity {

    public static final String VIDEO_URI = "URI";
    public static final String KEY_MOVIE_ID = "movieId";

    private String movieId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Movie Name");
        collapsingToolbar.setExpandedTitleColor(Color.MAGENTA);
        collapsingToolbar.setCollapsedTitleTextColor(Color.YELLOW);

        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Movie movie = new Movie();
        movie.setPoster("/fYzpM9GmpBlIC893fNjoWCwE24H.jpg");
        Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                movie, imageView);

        Bundle args = getIntent().getExtras();
        movieId = args.getString(KEY_MOVIE_ID);

        Toast.makeText(this, "You have clicked Video Button for Movie " + movieId,
                Toast.LENGTH_SHORT).show();
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
}
