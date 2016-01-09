package com.harinivaskumarrp.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

public class PopularMoviesMainActivity extends AppCompatActivity implements MovieListFragment.Callback {

    public static boolean mTwoPane = false;
//    public static String mPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.fragment_movie_detail_container) != null){
            mTwoPane = true;
            if (savedInstanceState == null){
                Fragment movieDetailFragment = new MovieDetailFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_movie_detail_container,
                                movieDetailFragment,
                                MovieDetailFragment.MOVIE_DETAIL_FRAG_TAG)
                        .commit();
            }
        }else {
            mTwoPane = false;
        }
        if (savedInstanceState == null){
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MovieListFragment movieListFragment =
                (MovieListFragment) getSupportFragmentManager().
                        findFragmentByTag(MovieListFragment.MOVIE_LIST_FRAG_TAG);
        if (movieListFragment != null){
            //Notify List Fragment to update the latest data
        }

        MovieDetailFragment movieDetailFragment =
                (MovieDetailFragment) getSupportFragmentManager().
                        findFragmentByTag(MovieDetailFragment.MOVIE_DETAIL_FRAG_TAG);
        if (movieDetailFragment != null){
            //Notify Detail Fragment to update the latest data
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieItemSelected(int movieItemPosition) {
        if (mTwoPane){
            Bundle args = new Bundle();
            args.putString(MovieDetailFragment.KEY_MOVIE_ITEM_POSITION, "" + movieItemPosition);

            Fragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movie_detail_container,
                            movieDetailFragment,
                            MovieDetailFragment.MOVIE_DETAIL_FRAG_TAG)
                    .commit();
        }else{
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.KEY_MOVIE_ITEM_POSITION, "" + movieItemPosition);
            startActivity(intent);
        }
    }
}
