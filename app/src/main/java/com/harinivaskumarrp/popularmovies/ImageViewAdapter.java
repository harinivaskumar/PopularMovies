package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/27/2015.
 */

public class ImageViewAdapter extends BaseAdapter {

    private final String LOG_TAG = ImageViewAdapter.class.getSimpleName();

    private final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private final String POSTER_IMAGE_SIZE = "w185";

    private Context mContext;
    private ArrayList<Movie> mMovieList = null;
    private String mMoviePosterUrl = null;

    public ImageViewAdapter(Context context) {
        mContext = context;
        mMovieList = new ArrayList<>();
    }

    private ArrayList<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(ArrayList<Movie> mMovieList) {
        this.mMovieList = mMovieList;
    }

    private String getMoviePosterUrl() {
        return mMoviePosterUrl;
    }

    private void setMoviePosterUrl(int position) {
        String poster = "/" + getMovie(position).getPoster();
        mMoviePosterUrl = (POSTER_BASE_URL + POSTER_IMAGE_SIZE + poster);
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

    public int getCount() {
        return getMovieCount();
    }

    public String getItem(int position) {
        setMoviePosterUrl(position);
        return getMoviePosterUrl();
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = getNewImageView();
        } else {
            imageView = (ImageView) convertView;
        }

        setMoviePosterUrl(position);

        Picasso.with(mContext)
                .load(getMoviePosterUrl())
                .into(imageView);
        return imageView;
    }
}
