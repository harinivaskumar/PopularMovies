package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
class MovieReviewsData extends JSONParser {

    private final String LOG_TAG_MR = MovieReviewsData.class.getSimpleName();

    public ArrayList<Review> reviewList = null;
    private Review[] reviews = null;

    private static final String TAG_AUTHOR = "author";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_URL_STR = "url";

    public MovieReviewsData(String jsonData) {
        super(jsonData);
        setJsonArray(createJSONArray());
        setReviewList(createReviewArrayList());
    }

    private ArrayList<Review> createReviewArrayList(){
        return new ArrayList<Review>();
    }

    public ArrayList<Review> getReviewList() {
        return reviewList;
    }

    private void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public int getReviewCount(){
        return reviewList.size();
    }

    private Review[] createReviews(int size){
        return new Review[size];
    }

    private Review[] getReviews(){
        return reviews;
    }

    private void setReviews(Review[] reviews){
        this.reviews = reviews;
    }

    public Review getReview(int position){
        return reviews[position];
    }

    private void setReview(int position, JSONObject jsonObject) throws JSONException{
        reviews[position] = new Review();

        reviews[position].setReviewId(jsonObject.getString(JP_TAG_ID));
        reviews[position].setAuthor(jsonObject.getString(TAG_AUTHOR));
        reviews[position].setContent(jsonObject.getString(TAG_CONTENT));
        reviews[position].setReviewUrlStr(jsonObject.getString(TAG_URL_STR));
    }

    private void populateReviewList(){
        reviewList = null;
        reviewList = new ArrayList<Review>(Arrays.asList(getReviews()));
    }

    public boolean parseMovieReviewsData() {
        if ((getJsonData() != null) && (getJsonObject() != null)) {
            try {
                setReviews(createReviews(getJSONArrayLength()));

                for (int index = 0; index < getJSONArrayLength(); index++) {
                    jsonResults = getJsonArray().getJSONObject(index);
                    setReview(index, jsonResults);
                }
                populateReviewList();
                Log.d(LOG_TAG_MR, "parseMovieReviewsData : Total Read Reviews - " + reviews.length);
            } catch (JSONException e) {
                Log.e(LOG_TAG_MR, "parseMovieReviewsData : JSONException");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
