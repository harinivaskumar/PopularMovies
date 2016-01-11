package com.harinivaskumarrp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class Video implements Parcelable {

    String videoId;
    String key;
    String name;
    String publishedSite;
    String type;

    public Video() {
    }

    public Video(String videoId, String key, String name, String publishedSite, String type) {
        this.videoId = videoId;
        this.key = key;
        this.name = name;
        this.publishedSite = publishedSite;
        this.type = type;
    }

    public Video(Parcel parcel){
        setVideoId(parcel.readString());
        setKey(parcel.readString());
        setName(parcel.readString());
        setPublishedSite(parcel.readString());
        setType(parcel.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getVideoId());
        parcel.writeString(getKey());
        parcel.writeString(getName());
        parcel.writeString(getPublishedSite());
        parcel.writeString(getType());
    }

    public final static Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        @Override
        public Video[] newArray(int index) {
            return new Video[index];
        }

    };

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishedSite() {
        return publishedSite;
    }

    public void setPublishedSite(String publishedSite) {
        this.publishedSite = publishedSite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
