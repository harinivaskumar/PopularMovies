package com.harinivaskumarrp.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class ReviewsActivity extends AppCompatActivity {

    public static final String REVIEW_URI = "URI";
    public static final String KEY_MOVIE_ID = "movieId";

    private String movieId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Bundle args = getIntent().getExtras();
        movieId = args.getString(KEY_MOVIE_ID);

        Toast.makeText(this, "You have clicked Review Button for Movie " + movieId,
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
