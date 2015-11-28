package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
class MovieListData extends JSONParser{

    private final String LOG_TAG_ML = MovieListData.class.getSimpleName();

    public ArrayList<Movie> movieList = null;

    private static final String TAG_ORIGINAL_TITLE = "original_title";
    private static final String TAG_POSTER_PATH = "poster_path";
    private static final String TAG_OVERVIEW = "overview";
    private static final String TAG_USER_RATING = "vote_average";
    private static final String TAG_RELEASE_DATE = "release_date";

    public MovieListData(String jsonData) {
        super(jsonData);
        setJsonArray(createJSONArray());
        setMovieList(createMovieArrayList());
    }

    private ArrayList<Movie> createMovieArrayList(){
        return new ArrayList<Movie>();
    }

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    private void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    public int getMovieCount(){
        return movieList.size();
    }

    public boolean parseMovieListData() {
        if ((getJsonData() != null) && (getJsonObject() != null)) {
            try {
                Movie movie = null;

                for (int index = 0; index < getJSONArrayLength(); index++) {
                    jsonResults = getJsonArray().getJSONObject(index);

                    if (movie == null) {
                        movie = new Movie();
                    }

                    movie.setMovieId(jsonResults.getString(JP_TAG_ID));
                    movie.setTitle(jsonResults.getString(TAG_ORIGINAL_TITLE));
                    movie.setPoster(jsonResults.getString(TAG_POSTER_PATH));
                    movie.setOverview(jsonResults.getString(TAG_OVERVIEW));
                    movie.setRating(jsonResults.getString(TAG_USER_RATING));
                    movie.setReleaseDate(jsonResults.getString(TAG_RELEASE_DATE));

                    movieList.add(movie);
                }
                Log.d(LOG_TAG_ML, "parseMovieListData : Total Read Movies - " + getMovieCount());
            } catch (JSONException e) {
                Log.e(LOG_TAG_ML, "parseMovieListData : JSONException");
                e.printStackTrace();
            }
            return true;
        }else {
            return false;
        }
    }
}
