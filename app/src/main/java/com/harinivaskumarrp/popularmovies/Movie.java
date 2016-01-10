package com.harinivaskumarrp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class Movie implements Parcelable{

    private static final String LOG_TAG = Movie.class.getSimpleName();

    private final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private final String POSTER_IMAGE_THUMBNAIL = "w185";
    private final String POSTER_IMAGE_XLARGE = "w500";

    public final static int POSTER_IMAGE_SIZE1 = 0;
    public final static int POSTER_IMAGE_SIZE2 = 1;
    private final static int POSTER_IMAGE_MAX = 2;

    private String movieId;
    private String title;
    private String poster;
    private String overview;
    private String rating;
    private String releaseDate;
    private ArrayList<Review> reviewList;
    private ArrayList<Video> videoList;

    private boolean isPosterAvailable;

    private String[] mMoviePosterUrl = new String[POSTER_IMAGE_MAX];
    private int mMoviePosterResId;

    public Movie() {
    }

    public Movie(String title, String poster, String overview, String rating, String releaseDate, String movieId) {
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    public Movie(Parcel parcel){
        setMovieId(parcel.readString());
        setTitle(parcel.readString());
        setPoster(parcel.readString());
        setOverview(parcel.readString());
        setRating(parcel.readString());
        setReleaseDate(parcel.readString());
        //parcel.readArrayList(Review.class.getClassLoader());
        //parcel.readArrayList(Video.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getMovieId());
        parcel.writeString(getTitle());
        parcel.writeString(getPoster());
        parcel.writeString(getOverview());
        parcel.writeString(getRating());
        parcel.writeString(getReleaseDate());
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int index) {
            return new Movie[index];
        }

    };

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        if (releaseDate == null || "null".equals(releaseDate)){
            this.releaseDate = "Not Available!";
        }else {
            this.releaseDate = releaseDate;
        }
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        if (rating == null || "null".equals(rating)){
            this.rating = "NA";
        }else{
            this.rating = rating;
        }
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        if (overview == null || "null".equals(overview)){
            this.overview = "Sorry, Overview not Available!";
        }else{
            this.overview = overview;
        }
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        if (poster == null || "null".equals(poster)){
            setPosterAvailable(false);
            setMoviePosterResId(R.drawable.movie_poster_coming_soon);
        }else {
            setPosterAvailable(true);
            this.poster = poster;
            createMoviePosterUrl();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public ArrayList<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<Video> videoList) {
        this.videoList = videoList;
    }

    public int getReviewCount(){
        return reviewList.size();
    }

    public int getVideoCount(){
        return videoList.size();
    }

    public boolean getPosterAvailable(){
        return isPosterAvailable;
    }

    private void setPosterAvailable(boolean isPosterAvailable){
        this.isPosterAvailable = isPosterAvailable;
    }

    private void createMoviePosterUrl() {
        setMoviePosterUrl(POSTER_IMAGE_SIZE1);
        setMoviePosterUrl(POSTER_IMAGE_SIZE2);
    }

    public String getMoviePosterUrl(int imageSize) {
        return mMoviePosterUrl[imageSize];
    }

    private void setMoviePosterUrl(int imageSize) {
        String poster = "/" + getPoster();

        if (imageSize == POSTER_IMAGE_SIZE1) {
            mMoviePosterUrl[POSTER_IMAGE_SIZE1] =
                    (POSTER_BASE_URL + POSTER_IMAGE_THUMBNAIL + poster);
        }else if(imageSize == POSTER_IMAGE_SIZE2){
            mMoviePosterUrl[POSTER_IMAGE_SIZE2] =
                    (POSTER_BASE_URL + POSTER_IMAGE_XLARGE + poster);
        }
    }

    public int getMoviePosterResId (){
        return mMoviePosterResId;
    }

    private void setMoviePosterResId (int moviePosterResId){
        this.mMoviePosterResId = moviePosterResId;
    }

    public static void loadImageFromPicasso(int imageSize, final Movie movie, ImageView imageView){
        if (movie.getPosterAvailable()) {
            //Log.v(LOG_TAG, "loadImageFromPicasso : Poster MovieURL is - " + movie.getPoster());
            Picasso.with(MovieListFragment.mContext)
                    .load(movie.getMoviePosterUrl(imageSize))
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Log.d(LOG_TAG, "loadImageFromPicasso : " +
                                    "Failed to Load image from MovieURL - " + movie.getPoster());
                        }
                    });
        }else{
            //Log.v(LOG_TAG, "loadImageFromPicasso : Poster Resource is - R.drawable.movie_poster_coming_soon!");
            Picasso.with(MovieListFragment.mContext)
                    .load(movie.getMoviePosterResId())
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Log.d(LOG_TAG, "loadImageFromPicasso : " +
                                    "Failed to Load image from Resource - R.drawable.movie_poster_coming_soon");
                        }
                    });
        }
    }

}