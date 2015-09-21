package com.electricsheep.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Adam on 10/26/2014.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String  mFilename;

    public CriminalIntentJSONSerializer(Context context, String filename) {
        mFilename = filename;
        mContext = context;
    }

    public void saveCrimes(ArrayList<Crime> crimes)
                throws JSONException, IOException{

        //Build array in JSON
        JSONArray array = new JSONArray();
        for(Crime c : crimes){
            array.put(c.toJSON());
        }

        //Write file to disk
        Writer writer = null;
        try{
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }

    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException{
        ArrayList<Crime> crimes = new ArrayList<Crime>();

        BufferedReader reader = null;
        try{
            //Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                //Line breaks are omitted and irrelevent
                jsonString.append(line);
            }
            //Parse json using jsonTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //Build the array of crimes from JSONObjects
            for(int i = 0; i < array.length(); ++i){
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        }catch(FileNotFoundException e){
            //Ignore, this will happen when starting fresh
        } finally {
            if(reader != null){
                reader.close();
            }
        }
        return crimes;


    }
}
