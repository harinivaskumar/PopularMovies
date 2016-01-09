package com.harinivaskumarrp.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    public static final String MOVIE_DETAIL_FRAG_TAG = "MDFTAG";
    public static final String KEY_MOVIE_ITEM_POSITION = "movieItemPosition";

    private ArrayList<Movie> movieList = null;

    public MovieDetailFragment() {
        movieList = MovieListFragment.movieArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        Intent intent = getActivity().getIntent();
        String mMoviePosition = null;
        Movie movie = null;

        if (intent!= null && intent.hasExtra(KEY_MOVIE_ITEM_POSITION)){
//            mMoviePosition = intent.getStringExtra(Intent.EXTRA_TEXT);
            mMoviePosition = intent.getStringExtra(KEY_MOVIE_ITEM_POSITION);
            movie = movieList.get(Integer.parseInt(mMoviePosition));
        }else if (arguments != null){
            Log.d(LOG_TAG, "Hello! I am inside else if");
            mMoviePosition = arguments.getString(KEY_MOVIE_ITEM_POSITION);
            movie = movieList.get(Integer.parseInt(mMoviePosition));
        }else {
            Log.d(LOG_TAG, "Hello! I am inside else");
//            if (PopularMoviesMainActivity.mPosition != null) {
//                mMoviePosition = PopularMoviesMainActivity.mPosition;
//            }else {
//                mMoviePosition = 1 + "";
//            }
            movie = new Movie();
            movie.setTitle("The Wrestler");
            movie.setOverview("Aging wrestler Randy \"The Ram\" Robinson is long past his prime but still ready");
            movie.setRating("6.94");
            movie.setReleaseDate("2008-09-07");
            movie.setPoster("/huooRmB7yksJyVVSkqOgitxlCec.jpg");
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
        textView[0].setText(movie.getTitle());
        textView[1].setText(movie.getOverview());
        textView[2].setText(movie.getRating());
        textView[3].setText(movie.getReleaseDate());

        ImageView moviePosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                movie, moviePosterImageView);

        return rootView;
    }
}
