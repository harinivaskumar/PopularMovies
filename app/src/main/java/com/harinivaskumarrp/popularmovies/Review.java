package com.harinivaskumarrp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class Review implements Parcelable{

    private final String LOG_TAG_R = Review.class.getSimpleName();

    String reviewId;
    String author;
    String content;
    String reviewUrlStr;
    URL reviewURL;

    public Review() {
    }

    public Review(String reviewId, String author, String content, String reviewUrlStr) {
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.reviewUrlStr = reviewUrlStr;
        setReviewURL(createReviewURLInstance());
    }

    public Review(Parcel parcel){
        setReviewId(parcel.readString());
        setAuthor(parcel.readString());
        setContent(parcel.readString());
        setReviewUrlStr(parcel.readString());
        setReviewURL(createReviewURLInstance());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getReviewId());
        parcel.writeString(getAuthor());
        parcel.writeString(getContent());
        parcel.writeString(getReviewUrlStr());
    }

    public final static Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int index) {
            return new Review[index];
        }

    };

    public URL createReviewURLInstance(){
        try {
            return new URL(getReviewUrlStr());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG_R, "createReviewURLInstance : MalformedURLException");
            e.printStackTrace();
        }
        return null;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewUrlStr() {
        return reviewUrlStr;
    }

    public void setReviewUrlStr(String reviewUrlStr) {
        this.reviewUrlStr = reviewUrlStr;
    }

    public URL getReviewURL() {
        return reviewURL;
    }

    public void setReviewURL(URL reviewURL) {
        this.reviewURL = reviewURL;
    }
}
