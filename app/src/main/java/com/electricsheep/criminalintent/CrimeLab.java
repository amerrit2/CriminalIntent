package com.electricsheep.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Adam on 10/11/2014.
 */
public class CrimeLab {

    private static CrimeLab         sCrimeLab;
    private        Context          mAppContext;
    private        ArrayList<Crime> mCrimes;
    private        CriminalIntentJSONSerializer mJSONSerializer;

    private static final String LOG_CATEGORY = "CrimeLab";
    private static final String FILENAME     = "crimes.json";



    private CrimeLab(Context appContext) {
        mAppContext = appContext;

        Log.d(LOG_CATEGORY, "This is the constructor");

        mJSONSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);

        try{
            mCrimes = mJSONSerializer.loadCrimes();
        }catch(Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.e(LOG_CATEGORY, "Error loading crimes: ", e);
        }


    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }
    public static CrimeLab get(Context c) {
         if(sCrimeLab == null){
             sCrimeLab = new CrimeLab(c.getApplicationContext());
         }

        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {

        return mCrimes;
    }

    public boolean saveCrimes(){
        try{
            mJSONSerializer.saveCrimes(mCrimes);
            Log.d(LOG_CATEGORY, "crimes saved to file");
            return true;
        } catch(Exception e){
            Log.e(LOG_CATEGORY, "Error saving crimes: ", e);
            return false;
        }
    }

    public Crime getCrime(UUID id){
        for(Crime aCrime : mCrimes){
            if(aCrime.getId().equals(id)){
                return aCrime;
            }
        }

        return null;
    }


}
