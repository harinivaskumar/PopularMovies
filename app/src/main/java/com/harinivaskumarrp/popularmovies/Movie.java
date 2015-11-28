package com.harinivaskumarrp.popularmovies;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class Movie {

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