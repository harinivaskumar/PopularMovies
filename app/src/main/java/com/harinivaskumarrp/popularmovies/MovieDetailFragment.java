package com.harinivaskumarrp.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.harinivaskumarrp.popularmovies.data.MovieColumns;
import com.harinivaskumarrp.popularmovies.data.MovieProvider;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment
        implements AdapterView.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    public static final String MOVIE_DETAIL_FRAG_TAG = "MDFTAG";
    public static final String KEY_MOVIE_ITEM_POSITION = "movieItemPosition";
    public static final String MOVIE_URI = "URI";
    private static final int CURSOR_LOADER_ID = 0;
    private final int FAV_COLOR_SET_CYAN = Color.CYAN;
    private final int FAV_COLOR_RESET_WHITE = Color.WHITE;

    private final String KEY_MOVIE_LIST = "movieList";
    private final String KEY_MOVIE_POSITION = "moviePosition";

    private ArrayList<Movie> movieList = null;
    private static Movie mMovie = null;
    private String mMoviePosition = null;
    private static Uri mUri = null;
    private static boolean markMovieAsFavourite = false;

    int[] textId = {
            R.id.movie_title,
            R.id.movie_overview,
            R.id.movie_rating,
            R.id.movie_release_date
    };
    TextView[] textView = null;
    ImageView moviePosterImageView = null;
    ImageButton favouriteButton = null;
    ImageButton reviewButton = null;
    ImageButton videoButton = null;

    public MovieDetailFragment() {
        movieList = MovieListFragment.movieArrayList;
        textView = new TextView[6];
    }

    public interface Callback {
        /**
         * MovieReviewFragmentCallback for when an movie item has been selected.
         */
        public void onReviewSelected(String movieId, Uri movieUri);
        public void onVideoSelected(String movieId, Uri movieUri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        Intent intent = getActivity().getIntent();

        if (intent!= null && intent.hasExtra(KEY_MOVIE_ITEM_POSITION)){
            arguments = intent.getExtras();
            mUri = arguments.getParcelable(MOVIE_URI);
            mMoviePosition = arguments.getString(KEY_MOVIE_ITEM_POSITION);
        }else if (arguments != null){
            mUri = arguments.getParcelable(MOVIE_URI);
            mMoviePosition = arguments.getString(KEY_MOVIE_ITEM_POSITION);
        }

        if (arguments != null) {
            Log.d(LOG_TAG, "onCreateView : MoviePosition - " +
                    arguments.getString(KEY_MOVIE_ITEM_POSITION) +
                    "\n Uri - " + arguments.getParcelable(MOVIE_URI));
        }

/*
            mMovie = new Movie();
            mMovie.setTitle("The Wrestler");
            mMovie.setOverview("Aging wrestler Randy \"The Ram\" Robinson is long past his prime but still ready");
            mMovie.setRating("6.94");
            mMovie.setReleaseDate("2008-09-07");
            mMovie.setPoster("/huooRmB7yksJyVVSkqOgitxlCec.jpg");
*/
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        for (int index = 0; index < 4; index++) {
            textView[index] = (TextView) rootView.findViewById(textId[index]);
        }
        moviePosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);

        favouriteButton = (ImageButton) rootView.findViewById(R.id.favourite_icon);
        reviewButton = (ImageButton) rootView.findViewById(R.id.review_icon);
        videoButton = (ImageButton) rootView.findViewById(R.id.video_icon);

        favouriteButton.setOnClickListener(this);
        reviewButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);

        if (mUri != null) {
            getLoaderManager().initLoader(CURSOR_LOADER_ID, arguments, this);
        }else {
            loadInvalidData();
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (mUri != null && mMovie != null) {
            Bundle args = new Bundle();
            Class intentClass = null;

            switch (v.getId()) {
                case R.id.favourite_icon:{
                    Log.d(LOG_TAG, "144: markMovieAsFavourite - " + markMovieAsFavourite);
                    if (markMovieAsFavourite) {
                        favouriteButton.setBackgroundColor(FAV_COLOR_RESET_WHITE);
                        markMovieAsFavourite = false;
                        Log.d(LOG_TAG, "147: Reset to White! - markMovieAsFavourite - " + markMovieAsFavourite);
                    } else{
                        favouriteButton.setBackgroundColor(FAV_COLOR_SET_CYAN);
                        markMovieAsFavourite = true;
                        Log.d(LOG_TAG, "151: Marked as Cyan! - markMovieAsFavourite - " + markMovieAsFavourite);
                    }
                    //markMovieAsFavourite = !markMovieAsFavourite;
                    break;
                }
                case R.id.review_icon: {
                    args.putParcelable(ReviewsActivity.REVIEW_URI, mUri);
                    args.putString(ReviewsActivity.KEY_MOVIE_ID, mMovie.getMovieId());
                    args.putString(ReviewsActivity.KEY_MOVIE_TITLE, mMovie.getTitle());
                    args.putString(ReviewsActivity.KEY_MOVIE_POSTER, mMovie.getPoster());
                    intentClass = ReviewsActivity.class;
                    break;
                }
                case R.id.video_icon: {
                    args.putParcelable(VideosActivity.VIDEO_URI, mUri);
                    args.putString(VideosActivity.KEY_MOVIE_ID, mMovie.getMovieId());
                    args.putString(VideosActivity.KEY_MOVIE_TITLE, mMovie.getTitle());
                    args.putString(VideosActivity.KEY_MOVIE_POSTER, mMovie.getPoster());
                    intentClass = VideosActivity.class;
                    break;
                }
            }
            if (intentClass != null) {
                Intent intent = new Intent(getActivity(), intentClass);
                intent.putExtras(args);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
            mMoviePosition = savedInstanceState.getString(KEY_MOVIE_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIE_LIST, movieList);
        outState.putString(KEY_MOVIE_POSITION, mMoviePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((mMoviePosition != null) &&
                (mMovie != null) && (mMovie.getMovieId() != null)) {
            if (markMovieAsFavourite) {
                addToFavourites();
            } else {
                removeFromFavourites();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void addToFavourites(){
        Cursor movieCursor = null;
        Log.d(LOG_TAG, mMovie.getMovieId() + "" + mMovie.getTitle());
        try {
            movieCursor = getContext().getContentResolver().query(
                    MovieProvider.Movies.CONTENT_URI,
                    null,
                    MovieColumns.MOVIE_ID + " = ?",
                    new String[]{mMovie.getMovieId()},
                    null);
            if (movieCursor != null && movieCursor.moveToFirst()) {
                Log.d(LOG_TAG, "addToFavourites : Entry[" + mMovie.getTitle() + "] already present in DB!");
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
                Log.d(LOG_TAG, "addToFavourites : New Entry[" + mMovie.getTitle() + "] added to DB!");
            }
        }catch (NullPointerException e){
            Log.e(LOG_TAG, "addToFavourites : NullPointerException@try for Cursor!");
            e.printStackTrace();
        }finally {
            try {
                if (movieCursor != null)
                    movieCursor.close();
            }catch (NullPointerException e){
                Log.e(LOG_TAG, "addToFavourites : NullPointerException@finally for Cursor!");
                e.printStackTrace();
            }
        }
    }

    public void removeFromFavourites(){
        Cursor movieCursor = null;
        Log.d(LOG_TAG, mMovie.getMovieId() + "" + mMovie.getTitle());
        try {
            movieCursor = getContext().getContentResolver().query(
                    MovieProvider.Movies.CONTENT_URI,
                    null,
                    MovieColumns.MOVIE_ID + " = ?",
                    new String[]{mMovie.getMovieId()},
                    null);
            if (movieCursor != null && movieCursor.moveToFirst()) {
                getActivity().getContentResolver()
                        .delete(MovieProvider.Movies.CONTENT_URI.buildUpon()
                                .appendPath(mMovie.getMovieId()).build(), null, null);
                Log.d(LOG_TAG, "removeFromFavourites : Entry[" + mMovie.getTitle() + "] removed from DB!");
            } else {
                Log.d(LOG_TAG, "removeFromFavourites : No Entry[" + mMovie.getTitle() + "] present in DB!");
            }
        }catch (NullPointerException e){
            Log.e(LOG_TAG, "removeFromFavourites : NullPointerException@try for Cursor!");
            e.printStackTrace();
        }finally {
            try {
                if (movieCursor != null)
                    movieCursor.close();
            }catch (NullPointerException e){
                Log.e(LOG_TAG, "removeFromFavourites : NullPointerException@finally for Cursor!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, mUri + "");
        return new CursorLoader(getActivity(), mUri,
                null, null, null, null);
    }

    private void loadInvalidData(){
        Movie movie = new Movie();
        textView[0].setText(movie.getTitle());
        textView[1].setText(movie.getOverview());
        textView[2].setText(movie.getRating());
        textView[3].setText(movie.getReleaseDate());
        movie.setPoster(null);
        Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                movie, moviePosterImageView);
    }

    private void loadInvalidImage(){
        Movie movie = new Movie();
        movie.setPoster(null);
        Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                movie, moviePosterImageView);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor movieCursor) {
        boolean fragmentStartUp = false;

        if (movieCursor.moveToFirst()) {
        //    DatabaseUtils.dumpCursor(movieCursor);

            mMovie = new Movie();
            mMovie.setMovieId(movieCursor.getString(
                    movieCursor.getColumnIndex(MovieColumns.MOVIE_ID)));
            mMovie.setTitle(movieCursor.getString(
                    movieCursor.getColumnIndex(MovieColumns.TITLE)));
            mMovie.setPoster(movieCursor.getString(
                    movieCursor.getColumnIndex(MovieColumns.POSTER)));
            mMovie.setOverview(movieCursor.getString(
                    movieCursor.getColumnIndex(MovieColumns.OVERVIEW)));
            mMovie.setRating(movieCursor.getString(
                    movieCursor.getColumnIndex(MovieColumns.RATING)));
            mMovie.setReleaseDate(movieCursor.getString(
                    movieCursor.getColumnIndex(MovieColumns.RELEASE_DATE)));
            Log.d(LOG_TAG, "[" + movieCursor.getPosition() + "] -- " + mMovie.getMovieId() +
                    "--" + mMovie.getTitle() );
            Log.d(LOG_TAG, "341: markMovieAsFavourite - " + markMovieAsFavourite);
            markMovieAsFavourite = true;
            favouriteButton.setBackgroundColor(FAV_COLOR_SET_CYAN);
            Log.d(LOG_TAG, "344: Marked as Cyan! - markMovieAsFavourite - " + markMovieAsFavourite);
        }else if (!movieList.isEmpty()){
            mMovie = movieList.get(Integer.parseInt(mMoviePosition));
            Log.d(LOG_TAG, "347: markMovieAsFavourite - " + markMovieAsFavourite);
            markMovieAsFavourite = false;
            favouriteButton.setBackgroundColor(FAV_COLOR_RESET_WHITE);
            Log.d(LOG_TAG, "350: Reset to White! - markMovieAsFavourite - " + markMovieAsFavourite);
        }else{
            fragmentStartUp = true;
        }

        if (fragmentStartUp){
            loadInvalidImage();
        }else{
            textView[0].setText(mMovie.getTitle());
            textView[1].setText(mMovie.getOverview());
            textView[2].setText(mMovie.getRating());
            textView[3].setText(mMovie.getReleaseDate());

            if (Utility.isNetworkAvailable(getContext())) {
                Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                        mMovie, moviePosterImageView);
            }else{
                loadInvalidImage();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
