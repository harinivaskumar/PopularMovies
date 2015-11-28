package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
class MovieReviewsData extends JSONParser {

    private final String LOG_TAG_MR = MovieReviewsData.class.getSimpleName();

    public ArrayList<Review> reviewList = null;

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

    public boolean parseMovieReviewsData() {
        if ((getJsonData() != null) && (getJsonObject() != null)) {
            try {
                Review review = null;

                for (int index = 0; index < getJSONArrayLength(); index++) {
                    jsonResults = getJsonArray().getJSONObject(index);

                    if (review == null) {
                        review = new Review();
                    }

                    review.setReviewId(jsonResults.getString(JP_TAG_ID));
                    review.setAuthor(jsonResults.getString(TAG_AUTHOR));
                    review.setContent(jsonResults.getString(TAG_CONTENT));
                    review.setReviewUrlStr(jsonResults.getString(TAG_URL_STR));

                    reviewList.add(review);
                }
                Log.d(LOG_TAG_MR, "parseMovieReviewsData : Total Read Reviews - " + getReviewCount());
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
