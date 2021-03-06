package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/27/2015.
 */

public class ImageViewAdapter extends CursorAdapter {

    private final String LOG_TAG = ImageViewAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Movie> mMovieList = null;

    public ImageViewAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mContext = context;
        mMovieList = new ArrayList<>();
    }

    private ArrayList<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(ArrayList<Movie> mMovieList) {
        this.mMovieList = mMovieList;
    }

    private Movie getMovie(int position) {
        return mMovieList.get(position);
    }

    private int getMovieCount() {
        return mMovieList.size();
    }

    private ImageView getNewImageView() {
        ImageView imageView = new ImageView(mContext);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setPadding(0,0,0,0);
        return imageView;
    }

    private TextView getNewTextView(int movieNum){
        int randomColor = (Color.CYAN * 100 * movieNum);

        TextView textView = new TextView(mContext);
        textView.setAllCaps(true);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setPadding(2,2,2,2);
        textView.setHeight(365);
        textView.setWidth(100);
        textView.setBackgroundColor(randomColor);

        return textView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_popular_movies, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor movieCursor) {
        movieCursor = super.getCursor();
        ImageView imageView = getNewImageView();
        Movie movie = new Movie();
        movie.setPoster(null);
        movieCursor.moveToFirst();
        do {
            Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                    movie, imageView);
        }while(movieCursor.moveToNext());
        Log.d(LOG_TAG, "Inside bindView");
    }

    public int getCount() {
        return getMovieCount();
    }

    public String getItem(int position) {
        return getMovie(position).getMoviePosterUrl(Movie.POSTER_IMAGE_SIZE1);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (Utility.isNetworkAvailable(mContext)) {
            ImageView imageView = getNewImageView();

            Movie.loadImageFromPicasso(Movie.POSTER_IMAGE_SIZE2,
                    getMovie(position), imageView);

            return imageView;
        }else{
            int movieId = Integer.parseInt(getMovie(position).getMovieId());
            TextView textView = getNewTextView(movieId);
            textView.setText(getMovie(position).getTitle());
            return textView;
        }
    }
}
