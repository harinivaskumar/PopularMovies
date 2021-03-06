package com.harinivaskumarrp.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MovieDetailActivity extends AppCompatActivity{

    private static final String KEY_MOVIE_POSITION = "movieItemPosition";

    private String mMovieItemPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null){
            Intent intent = new Intent();
            mMovieItemPosition = intent.getStringExtra(MovieDetailFragment.KEY_MOVIE_ITEM_POSITION);

            Bundle args = new Bundle();
            args.putString(MovieDetailFragment.KEY_MOVIE_ITEM_POSITION, "" + mMovieItemPosition);

            Fragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movie_detail_container,
                            movieDetailFragment,
                            MovieDetailFragment.MOVIE_DETAIL_FRAG_TAG)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_MOVIE_POSITION, mMovieItemPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            mMovieItemPosition = savedInstanceState.getString(KEY_MOVIE_POSITION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle args = new Bundle();
        args.putString(MovieDetailFragment.KEY_MOVIE_ITEM_POSITION, "" + mMovieItemPosition);

        Fragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_detail_container,
                        movieDetailFragment,
                        MovieDetailFragment.MOVIE_DETAIL_FRAG_TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else if (id == android.R.id.home){     // Respond to the action bar's Up/Home button
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
