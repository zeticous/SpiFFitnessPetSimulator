package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class JSONSerializer {

    private String mFilename;
    private Context mContext;

    public JSONSerializer(String fn, Context con) {
        mFilename = fn;
        mContext = con;
    }

    public void save(Spirits userSpirit) throws IOException, JSONException {
        JSONArray jArray = new JSONArray();
        Gson gson = new Gson();
        jArray.put(gson.toJson(userSpirit));

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, mContext.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
            Log.e("JSON", "Save successfully");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public Spirits load() throws IOException, JSONException {
        Spirits userSpirit = new Spirits();
        BufferedReader reader = null;
        Gson gson = new Gson();

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            JSONObject obj = jArray.getJSONObject(0);
            userSpirit = gson.fromJson(obj.toString(), Spirits.class);
            Log.e("JSON", "JSON to object load successful");

        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND");
        } finally {
            if (reader != null)
                reader.close();
        }
        return userSpirit;
    }
}