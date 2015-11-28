package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
class MovieVideosData extends JSONParser {

    private final String LOG_TAG_MV = MovieVideosData.class.getSimpleName();

    public ArrayList<Video> videoList = null;
    private Video[] videos = null;

    private static final String TAG_KEY = "key";
    private static final String TAG_NAME = "name";
    private static final String TAG_SITE = "site";
    private static final String TAG_TYPE = "type";

    public MovieVideosData(String jsonData) {
        super(jsonData);
        setJsonArray(createJSONArray());
        setVideoList(createVideoArrayList());
    }

    private ArrayList<Video> createVideoArrayList(){
        return new ArrayList<Video>();
    }

    public ArrayList<Video> getVideoList() {
        return videoList;
    }

    private void setVideoList(ArrayList<Video> videoList) {
        this.videoList = videoList;
    }

    public int getVideoCount(){
        return videoList.size();
    }

    private Video[] createVideos(int size){
        return new Video[size];
    }

    private Video[] getVideos(){
        return videos;
    }

    private void setVideos(Video[] videos){
        this.videos = videos;
    }

    public Video getVideo(int position){
        return videos[position];
    }

    private void setVideo(int position, JSONObject jsonObject) throws JSONException{
        videos[position] = new Video();

        videos[position].setVideoId(jsonObject.getString(JP_TAG_ID));
        videos[position].setKey(jsonObject.getString(TAG_KEY));
        videos[position].setName(jsonObject.getString(TAG_NAME));
        videos[position].setPublishedSite(jsonObject.getString(TAG_SITE));
        videos[position].setType(jsonObject.getString(TAG_TYPE));
    }

    private void populateVideoList(){
        videoList = null;
        videoList = new ArrayList<Video>(Arrays.asList(getVideos()));
    }

    public boolean parseMovieVideosData() {
        if ((getJsonData() != null) && (getJsonObject() != null)) {
            try {
                setVideos(createVideos(getJSONArrayLength()));

                for (int index = 0; index < getJSONArrayLength(); index++) {
                    jsonResults = getJsonArray().getJSONObject(index);
                    setVideo(index, jsonResults);
                }
                populateVideoList();
                Log.d(LOG_TAG_MV, "parseMovieVideosData : Total Read Videos - " + videos.length);
            } catch (JSONException e) {
                Log.e(LOG_TAG_MV, "parseMovieVideosData : JSONException");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
