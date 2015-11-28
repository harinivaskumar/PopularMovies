package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
class MovieVideosData extends JSONParser {

    private final String LOG_TAG_MV = MovieVideosData.class.getSimpleName();

    public ArrayList<Video> videoList = null;

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

    public boolean parseMovieVideosData() {
        if ((getJsonData() != null) && (getJsonObject() != null)) {
            try {
                Video video = null;

                for (int index = 0; index < getJSONArrayLength(); index++) {
                    jsonResults = getJsonArray().getJSONObject(index);

                    if (video == null) {
                        video = new Video();
                    }

                    video.setVideoId(jsonResults.getString(JP_TAG_ID));
                    video.setKey(jsonResults.getString(TAG_KEY));
                    video.setName(jsonResults.getString(TAG_NAME));
                    video.setPublishedSite(jsonResults.getString(TAG_SITE));
                    video.setType(jsonResults.getString(TAG_TYPE));

                    videoList.add(video);
                }
                Log.d(LOG_TAG_MV, "parseMovieVideosData : Total Read Videos - " + getVideoCount());
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
