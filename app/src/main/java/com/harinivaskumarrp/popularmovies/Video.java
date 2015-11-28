package com.harinivaskumarrp.popularmovies;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class Video {

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
