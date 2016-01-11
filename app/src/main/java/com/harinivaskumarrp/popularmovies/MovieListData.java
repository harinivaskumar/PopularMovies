package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.harinivaskumarrp.popularmovies.data.MovieColumns;

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
    private Context mContext = null;
    private Cursor cursorData = null;

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

    public MovieListData (Context context, Cursor cursorData){
        mContext = context;
        setCursorData(cursorData);
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

    private void setMovie(int position, Cursor cursorObject){
        movies[position] = new Movie();

        movies[position].setMovieId(cursorObject.getString(
                cursorObject.getColumnIndex(MovieColumns.MOVIE_ID)));
        movies[position].setTitle(cursorObject.getString(
                cursorObject.getColumnIndex(MovieColumns.TITLE)));
        if (Utility.isNetworkAvailable(mContext)) {
            movies[position].setPoster(cursorObject.getString(
                    cursorObject.getColumnIndex(MovieColumns.POSTER)));
        }else{
            movies[position].setPoster(null);
        }
        movies[position].setOverview(cursorObject.getString(
                cursorObject.getColumnIndex(MovieColumns.OVERVIEW)));
        movies[position].setRating(cursorObject.getString(
                cursorObject.getColumnIndex(MovieColumns.RATING)));
        movies[position].setReleaseDate(cursorObject.getString(
                cursorObject.getColumnIndex(MovieColumns.RELEASE_DATE)));

    }

    private Cursor getCursorData() {
        return cursorData;
    }

    private void setCursorData(Cursor cursorData) {
        this.cursorData = cursorData;
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

    public boolean parseMovieCursorData(){
        int position = 0;
        if (getCursorData() != null && cursorData.getCount() != 0){
            setMovies(createMovies(cursorData.getCount()));
            //DatabaseUtils.dumpCursor(cursorData);
            cursorData.moveToFirst();
            do{
                setMovie(position, cursorData);
                if (position%5 == 0) {
                    Log.d(LOG_TAG_ML, "[" + position + "] -- " + movies[position].getMovieId() +
                            "--" + movies[position].getTitle() +
                            "--" + movies[position].getMoviePosterUrl(Movie.POSTER_IMAGE_SIZE2));
                }
                position++;
            }while (cursorData.moveToNext());
            cursorData.close();
            populateMovieList();
            Log.d(LOG_TAG_ML, "parseMovieCursorData : Total Read Movies - " + movies.length);
            return true;
        }else{
            Log.d(LOG_TAG_ML, "parseMovieCursorData : Total Read Movies is Zero as Cursor returned zero!");
        }
        return false;
    }
}
