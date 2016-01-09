package com.harinivaskumarrp.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.harinivaskumarrp.popularmovies.data.MovieColumns;
import com.harinivaskumarrp.popularmovies.data.MovieProvider;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    public static final String MOVIE_DETAIL_FRAG_TAG = "MDFTAG";
    public static final String KEY_MOVIE_ITEM_POSITION = "movieItemPosition";

    private ArrayList<Movie> movieList = null;
    private Movie mMovie = null;
    private String mMoviePosition = null;

    public MovieDetailFragment() {
        movieList = MovieListFragment.movieArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        Intent intent = getActivity().getIntent();

        if (intent!= null && intent.hasExtra(KEY_MOVIE_ITEM_POSITION)){
//            mMoviePosition = intent.getStringExtra(Intent.EXTRA_TEXT);
            mMoviePosition = intent.getStringExtra(KEY_MOVIE_ITEM_POSITION);
            mMovie = movieList.get(Integer.parseInt(mMoviePosition));
        }else if (arguments != null){
            Log.d(LOG_TAG, "Hello! I am inside else if");
            mMoviePosition = arguments.getString(KEY_MOVIE_ITEM_POSITION);
            mMovie = movieList.get(Integer.parseInt(mMoviePosition));
        }else {
            Log.d(LOG_TAG, "Hello! I am inside else");
//            if (PopularMoviesMainActivity.mPosition != null) {
//                mMoviePosition = PopularMoviesMainActivity.mPosition;
//            }else {
//                mMoviePosition = 1 + "";
//            }
            mMovie = new Movie();
            mMovie.setTitle("The Wrestler");
            mMovie.setOverview("Aging wrestler Randy \"The Ram\" Robinson is long past his prime but still ready");
            mMovie.setRating("6.94");
            mMovie.setReleaseDate("2008-09-07");
            mMovie.setPoster("/huooRmB7yksJyVVSkqOgitxlCec.jpg");
        }

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        int[] textId = {
                R.id.movie_title,
                R.id.movie_overview,
                R.id.movie_rating,
                R.id.movie_release_date
        };

        TextView[] textView = new TextView[4];

        for (int index = 0; index < 4; index++) {
            textView[index] = (TextView) rootView.findViewById(textId[index]);
        }
        textView[0].setText(mMovie.getTitle());
        textView[1].setText(mMovie.getOverview());
        textView[2].setText(mMovie.getRating());
        textView[3].setText(mMovie.getReleaseDate());

        ImageView moviePosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        mMovie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                mMovie, moviePosterImageView);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMoviePosition != null){
            Cursor movieCursor = null;
            try {
                 movieCursor = getContext().getContentResolver().query(
                        MovieProvider.Movies.CONTENT_URI,
                        null,
                        MovieColumns.MOVIE_ID + " = ?",
                        new String[]{mMovie.getMovieId()},
                        null);
                if (movieCursor != null && movieCursor.moveToFirst()) {
                    Log.d(LOG_TAG, "onPause : Entry[" + mMovie.getTitle() + "] already present in DB!");
                } else {
                    ContentValues movieContentValues = new ContentValues();

                    movieContentValues.put(MovieColumns.MOVIE_ID, mMovie.getMovieId());
                    movieContentValues.put(MovieColumns.TITLE, mMovie.getTitle());
                    movieContentValues.put(MovieColumns.OVERVIEW, mMovie.getOverview());
                    movieContentValues.put(MovieColumns.RATING, mMovie.getRating());
                    movieContentValues.put(MovieColumns.RELEASE_DATE, mMovie.getReleaseDate());
                    movieContentValues.put(MovieColumns.POSTER, mMovie.getMoviePosterUrl(Movie.POSTER_IMAGE_SIZE2));

                    getActivity().getContentResolver()
                            .insert(MovieProvider.Movies.CONTENT_URI, movieContentValues);
                    Log.d(LOG_TAG, "onPause : New Entry[" + mMovie.getTitle() + "] added to DB!");
                }
            }catch (NullPointerException e){
                Log.e(LOG_TAG, "onPause : NullPointerException@try for Cursor!");
                e.printStackTrace();
            }finally {
                try {
                    if (movieCursor != null)
                        movieCursor.close();
                }catch (NullPointerException e){
                    Log.e(LOG_TAG, "onPause : NullPointerException@finally for Cursor!");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
