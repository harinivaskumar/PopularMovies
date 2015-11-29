package com.harinivaskumarrp.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    private ArrayList<Movie> movieList = null;

    public MovieDetailActivityFragment() {
        movieList = PopularMoviesActivityFragment.movieArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        String mMoviePosition = intent.getStringExtra(Intent.EXTRA_TEXT);

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Movie movie = movieList.get(Integer.parseInt(mMoviePosition));

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
        textView[1].setText("Movie Overview : \n" + movie.getOverview());
        textView[2].setText("Rating : " + movie.getRating());
        textView[3].setText("Release Date : " + movie.getReleaseDate());

        Log.v(LOG_TAG, "onCreateView : PosterPath[" + mMoviePosition + "] is -- " + movie.getPoster());

        ImageView moviePosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        Picasso.with(PopularMoviesActivityFragment.mContext)
                .load(movie.getMoviePosterUrl(Movie.POSTER_IMAGE_SIZE2))
                .into(moviePosterImageView);

        return rootView;
    }
}
