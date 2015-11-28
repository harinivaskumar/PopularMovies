package com.harinivaskumarrp.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class TMDBUrlBuilder {

    // please, add in your movie database APP ID from www.themoviedb.org
    private final String MY_MOVIE_APP_ID = "Your_APP_ID";

    private final String LOG_TAG = TMDBUrlBuilder.class.getSimpleName();

    private final String DISCOVER_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private final String MOVIE_VIDEO_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final String MOVIE_REVIEW_BASE_URL = "http://api.themoviedb.org/3/movie/";

    //Parameters for DISCOVER_MOVIE_BASE_URL
    private final String PAGE_PARAM = "page";
    private final String VOTE_COUNT_PARAM = "vote_count.gte";
    private final String SORT_PARAM = "sort_by";
    private final String APPID_PARAM = "api_key";

    private final String POPULARITY_DESC = "popularity.desc";
    private final String HIGHEST_RATING = "vote_average.desc";

    //Parameters for Video & Review EndPoint
    private final String MOVIE_VIDEO_ENDPOINT = "/videos";
    private final String MOVIE_REVIEW_ENDPOINT = "/reviews";

    //Parameters for Sorting Movies
    private final int POPULARITY = 1;
    private final int RATING = 2;
    private final int FAVOURITES = 3;

    private final int HTTP_REQUEST_FAILURE = 0;
    private final int HTTP_SUCCESS_HTTP_OK = 200;
    private final int HTTP_CLIENT_ERROR_BAD_REQUEST = 400;
    private final int HTTP_CLIENT_UNAUTHORIZED = 401;
    private final int HTTP_CLIENT_ERROR_NOT_FOUND = 404;

    //EndPoint Types
    public int DISCOVER_MOVIE = 1;
    public int MOVIE_VIDEOS = 2;
    public int MOVIE_REVIEWS = 3;

    private int urlEndPoint, pageNumber, sortType, minVoteCount, movieId;
    public int httpStatusCode = 0;
    public APIStatusCodeData apiStatusCodeData = null;

    public TMDBUrlBuilder(int urlEndPoint, int pageNumber, int sortByType, int minVoteCount, int movieId) {
        setUrlEndPoint(urlEndPoint);
        setPageNumber(pageNumber);
        setSortByType(sortByType);
        setMinVoteCount(minVoteCount);
        setMovieId(movieId);
    }

    private int getUrlEndPoint() {
        return urlEndPoint;
    }

    public void setUrlEndPoint(int urlEndPoint) {
        this.urlEndPoint = urlEndPoint;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getSortByType() {
        return sortType;
    }

    public void setSortByType(int sortByType) {
        this.sortType = sortByType;
    }

    public int getMinVoteCount() {
        return minVoteCount;
    }

    public void setMinVoteCount(int minVoteCount) {
        this.minVoteCount = minVoteCount;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    private Uri getTmdbUri() {
        String baseURLStr = null;

        if (getUrlEndPoint() == DISCOVER_MOVIE) {
            baseURLStr = DISCOVER_MOVIE_BASE_URL;

            if (getSortByType() == POPULARITY) {
                return (Uri.parse(baseURLStr).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, getPageNumber() + "")
                        .appendQueryParameter(SORT_PARAM, POPULARITY_DESC)
                        .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                        .build());
            } else if (getSortByType() == RATING) {
                return (Uri.parse(baseURLStr).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, getPageNumber() + "")
                        .appendQueryParameter(VOTE_COUNT_PARAM, getMinVoteCount() + "")
                        .appendQueryParameter(SORT_PARAM, HIGHEST_RATING)
                        .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                        .build());
            }
        }else if (getUrlEndPoint() == MOVIE_VIDEOS) {
            baseURLStr = MOVIE_VIDEO_BASE_URL + getMovieId() + MOVIE_VIDEO_ENDPOINT;

            return (Uri.parse(baseURLStr).buildUpon()
                    .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                    .build());
        }else if (getUrlEndPoint() == MOVIE_REVIEWS){
            baseURLStr = MOVIE_REVIEW_BASE_URL + getMovieId() + MOVIE_REVIEW_ENDPOINT;

            return (Uri.parse(baseURLStr).buildUpon()
                    .appendQueryParameter(APPID_PARAM, MY_MOVIE_APP_ID)
                    .build());
        }
        return null;
    }

    public URL getTmdbURLInstance() {

        URL tmdbURL = null;
        try {
            Uri tmdbUri = getTmdbUri();

            if (tmdbUri == null) {
                Log.e(LOG_TAG, "getTmdbURLInstance : URI build Failed");
            } else {
                String tmdbURLStr = URLDecoder.decode(tmdbUri.toString(), "UTF-8");
                Log.d(LOG_TAG, "getTmdbURLInstance : TMDB URL - " + tmdbURLStr);

                tmdbURL = new URL(tmdbURLStr);

                //tmdbURL = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=10&api_key=" + MY_MOVIE_APP_ID);
                //tmdbURL = new URL("http://api.themoviedb.org/3/movie/1028990/videos?api_key=" + MY_MOVIE_APP_ID);
                //tmdbURL = new URL("http://api.themoviedb.org/3/movie/1028990/reviews?api_key=" + MY_MOVIE_APP_ID);
            }
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            Log.e(LOG_TAG, "getTmdbURLInstance : UnsupportedEncodingException/MalformedURLException");
            e.printStackTrace();
        }
        return tmdbURL;
    }

    public boolean validateHttpResponseCode(){
        switch (getHttpStatusCode()){
            case HTTP_CLIENT_ERROR_BAD_REQUEST:
                return false;
            case HTTP_CLIENT_UNAUTHORIZED:
                return false;
            case HTTP_CLIENT_ERROR_NOT_FOUND:
                return false;
            case HTTP_SUCCESS_HTTP_OK:
                return true;
        }
        return false;
    }

    public boolean checkResponseCode(InputStream inputErrorStream)
            throws IOException {

        //Log.d(LOG_TAG, "checkResponseCode : Response - " + mResponseMessage + "/" + mResponseCode);
        Log.d(LOG_TAG, "checkResponseCode : Response - " +
                apiStatusCodeData.apiStatusCodes.getHttpStatusMessage() + "/" +
                apiStatusCodeData.apiStatusCodes.getHttpStatusCode());

        setHttpStatusCode(apiStatusCodeData.apiStatusCodes.getHttpStatusCode());

        if (!validateHttpResponseCode()){
            String jsonErrorStr =
                    readJSONStringFromIOStream(inputErrorStream);

            if (jsonErrorStr != null) {
                apiStatusCodeData.setJsonData(jsonErrorStr);
                apiStatusCodeData.setJsonObject(apiStatusCodeData.createJSONObject());
                if (apiStatusCodeData.parseAPIStatusCodeData()) {
                    //Log.d(LOG_TAG, "Json Error String - " + jsonErrorStr);

                    Log.d(LOG_TAG, "checkResponseCode : Status - " +
                            apiStatusCodeData.apiStatusCodes.getStatusCode() + "/" +
                            apiStatusCodeData.apiStatusCodes.getStatusMessage());
                } else {
                    Log.d(LOG_TAG, "Json Error String is null");
                }
            }
        } else if (validateHttpResponseCode()) {
            return true;
        }
        return false;
    }

    public String readJSONStringFromIOStream(InputStream inputStream) {

        BufferedReader bufferedReader = null;
        String jsonStr = null;

        if (inputStream == null) {
            Log.e(LOG_TAG, "readJSONStringFromIOStream : InputStream is Null");
        } else {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer bufferStr = new StringBuffer();
                String lineStr = "";

                while ((lineStr = bufferedReader.readLine()) != null) {
                    bufferStr.append(lineStr);
                }

                jsonStr = bufferStr.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "readJSONStringFromIOStream : IOException");
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "readJSONStringFromIOStream : IOException");
                    e.printStackTrace();
                }
            }
        }
        return jsonStr;
    }
}
