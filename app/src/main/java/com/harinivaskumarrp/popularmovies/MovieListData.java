package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
class MovieListData extends JSONParser{

    private final String LOG_TAG_ML = MovieListData.class.getSimpleName();

    public ArrayList<Movie> movieList = null;
    private Movie[] movies = null;

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

    private Movie[] createMovies(int size){
        return new Movie[size];
    }

    private Movie[] getMovies(){
        return movies;
    }

    private void setMovies(Movie[] movies){
        this.movies = movies;
    }

    public Movie getMovie(int position){
        return movies[position];
    }

    private void setMovie(int position, JSONObject jsonObject) throws JSONException{
        movies[position] = new Movie();

        movies[position].setMovieId(jsonObject.getString(JP_TAG_ID));
        movies[position].setTitle(jsonObject.getString(TAG_ORIGINAL_TITLE));
        movies[position].setPoster(jsonObject.getString(TAG_POSTER_PATH));
        movies[position].setOverview(jsonObject.getString(TAG_OVERVIEW));
        movies[position].setRating(jsonObject.getString(TAG_USER_RATING));
        movies[position].setReleaseDate(jsonObject.getString(TAG_RELEASE_DATE));
    }

    private void populateMovieList(){
        movieList = null;
        movieList = new ArrayList<Movie>(Arrays.asList(getMovies()));
    }

    public boolean parseMovieListData() {
        if ((getJsonData() != null) && (getJsonObject() != null)) {
            try {
                setMovies(createMovies(getJSONArrayLength()));

                for (int index = 0; index < getJSONArrayLength(); index++) {
                    jsonResults = getJsonArray().getJSONObject(index);
                    setMovie(index, jsonResults);
                }
                populateMovieList();
                Log.d(LOG_TAG_ML, "parseMovieListData : Total Read Movies - " + movies.length);
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
