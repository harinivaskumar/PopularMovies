package com.harinivaskumarrp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class Movie implements Parcelable{

    String movieId;
    String title;
    String poster;
    String overview;
    String rating;
    String releaseDate;
    ArrayList<Review> reviewList;
    ArrayList<Video> videoList;

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
        this.releaseDate = releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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
}