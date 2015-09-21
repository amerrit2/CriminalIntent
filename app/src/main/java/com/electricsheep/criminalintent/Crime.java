package com.electricsheep.criminalintent;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Adam on 10/8/2014.
 */
public class Crime {

    private static final String JSON_ID     = "id";
    private static final String JSON_TITLE  = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE   = "date";


    private UUID    mId;
    private String  mTitle;
    private Date    mDate;
    private Boolean mSolved;

    private static final String LOG_CATEGORY = "Crime";



    public Crime(){
        //Generate UUID
        mId   = UUID.randomUUID();
        mDate = new Date();
        mSolved = false;


        Log.d(LOG_CATEGORY, "Crime constructor");
    }
    public Crime(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        }
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));

    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {

        return mTitle;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Boolean getSolved() {
        return mSolved;
    }

    public void setSolved(Boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_SOLVED, mSolved);
        return json;



    }
}
