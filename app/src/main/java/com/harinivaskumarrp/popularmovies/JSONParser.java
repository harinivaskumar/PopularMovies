package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
public class JSONParser {

    private final String LOG_TAG_JP = JSONParser.class.getSimpleName();

    String jsonData = null;
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    JSONObject jsonResults = null;

    protected static final String JP_TAG_RESULTS = "results";
    protected static final String JP_TAG_ID = "id";

    protected JSONParser(){
    }

    protected JSONParser(String jsonData) {
        setJsonData(jsonData);
        setJsonObject(createJSONObject());
    }

    protected String getJsonData() {
        return jsonData;
    }

    protected void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    protected JSONObject getJsonObject() {
        return jsonObject;
    }

    protected void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    protected JSONArray getJsonArray() {
        return jsonArray;
    }

    protected void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    protected JSONObject createJSONObject() {
        if (getJsonData() != null) {
            try {
                return new JSONObject(getJsonData());
            } catch (JSONException e) {
                Log.e(LOG_TAG_JP, "createJSONObject : JSONException");
                e.printStackTrace();
            }
        }
        Log.e(LOG_TAG_JP, "createJSONObject : jsonData is null");
        return null;
    }

    protected JSONArray createJSONArray(){
        if (getJsonObject() != null) {
            try {
                return jsonObject.getJSONArray(JP_TAG_RESULTS);
            } catch (JSONException e) {
                Log.e(LOG_TAG_JP, "createJSONArray : JSONException");
                e.printStackTrace();
            }
        }
        Log.e(LOG_TAG_JP, "createJSONArray : jsonObject is null");
        return null;
    }

    protected int getJSONArrayLength(){
        return jsonArray.length();
    }
}
