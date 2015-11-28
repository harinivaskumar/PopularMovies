package com.harinivaskumarrp.popularmovies;

import android.util.Log;

import org.json.JSONException;

/**
 * Created by Hari Nivas Kumar R P on 11/28/2015.
 */
class APIStatusCodeData extends JSONParser{

    private final String LOG_TAG_ESP = APIStatusCodeData.class.getSimpleName();

    public APIStatusCodes apiStatusCodes = null;

    private static final String TAG_STATUS_CODE = "status_code";
    private static final String TAG_STATUS_MESSAGE = "status_message";

    public APIStatusCodeData(){
        setApiStatusCodes(createAPIStatusCodesInstance());
    }

    public APIStatusCodeData(String jsonData) {
        super(jsonData);
        setApiStatusCodes(createAPIStatusCodesInstance());
    }

    private APIStatusCodes createAPIStatusCodesInstance(){
        return new APIStatusCodes();
    }

    public APIStatusCodes getApiStatusCodes() {
        return apiStatusCodes;
    }

    private void setApiStatusCodes(APIStatusCodes apiStatusCodes) {
        this.apiStatusCodes = apiStatusCodes;
    }

    public boolean parseAPIStatusCodeData() {
        if ((getJsonData() != null) && (getJsonObject() != null)) {
            try {
                int statusCode = Integer.parseInt(jsonObject.getString(TAG_STATUS_CODE));
                apiStatusCodes.setStatusCode(statusCode);

                String statusMessage = jsonObject.getString(TAG_STATUS_MESSAGE);
                apiStatusCodes.setStatusMessage(statusMessage);

                Log.d(LOG_TAG_ESP, "parseAPIStatusCodeData : StatusM/C - " +
                        apiStatusCodes.getStatusMessage() + "/" + apiStatusCodes.getStatusCode());
            } catch (JSONException e) {
                Log.e(LOG_TAG_ESP, "parseAPIStatusCodeData : JSONException");
                e.printStackTrace();
            }
            return true;
        }else {

            return false;
        }
    }
}
