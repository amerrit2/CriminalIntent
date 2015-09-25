package com.electricsheep.criminalintent;

import android.net.Uri;
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
    private static final String JSON_PHOTO  = "photo";
    private static final String JSON_SUSPECT = "suspect";
    private static final String JSON_PHONE_NUMBER     = "phone_number";


    private UUID    mId;
    private String  mTitle;
    private Date    mDate;
    private Boolean mSolved;
    private Photo   mPhoto;
    private String  mSuspect;
    private String  mPhoneNumber;

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
        if(json.has(JSON_SUSPECT)){
            mSuspect = json.getString(JSON_SUSPECT);
        }
        if(json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }
        if(json.has(JSON_PHONE_NUMBER)){
            mPhoneNumber = json.getString(JSON_PHONE_NUMBER);
        }

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

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        this.mPhoto = photo;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_SUSPECT, mSuspect);
        json.put(JSON_PHONE_NUMBER, mPhoneNumber);
        if(mPhoto != null){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }

        return json;



    }

}
