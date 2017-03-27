package com.wssholmes.stark.zyla;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wssholmes.stark.zyla.data.DataContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int CURSOR_LOADER_ID = 0;

    private static final String[] PROJECTION = new String[]{
            DataContract.SongEntry.NAME
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String IS_FIRST_TIME = "IsFirstTime";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean(IS_FIRST_TIME, true)){
            Log.v(LOG_TAG, "updating database");
            updateDatabase();
            sharedPreferences.edit().putBoolean(IS_FIRST_TIME,false).apply();

        }
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null ,this);
    }


    private void updateDatabase() {

        InputStream inStream;
        Vector<ContentValues> contentValuesVector = new Vector<>();

        try {
            inStream = getResources().getAssets().open("sample_music_data.csv");
        } catch (Exception e) {
            Log.v(LOG_TAG, "unable to read csv from assets");
            inStream = null;
            e.printStackTrace();
        }

        if (inStream != null) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line = "";
            try {
                while ((line = buffer.readLine()) != null) {
                    String[] colums = line.split(",");
                    if (colums.length != 3) {
                        Log.d(LOG_TAG, "Skipping Bad CSV Row");
                        continue;
                    }
                    Log.v(LOG_TAG, "extracting song " + colums[0].trim() + " from csv");
                    ContentValues contentValues = new ContentValues(3);
                    contentValues.put(DataContract.SongEntry.NAME, colums[0].trim());
                    contentValues.put(DataContract.SongEntry.ARTIST, colums[1].trim());
                    contentValues.put(DataContract.SongEntry.ALBUM, colums[2].trim());

                    contentValuesVector.add(contentValues);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (contentValuesVector.size() > 0){
                Log.v(LOG_TAG, "inserting data into database, total count " + contentValuesVector.size());
                ContentValues[] contentValuesArray = new ContentValues[contentValuesVector.size()];
                contentValuesVector.toArray(contentValuesArray);

                int total = getContentResolver().bulkInsert(DataContract.SongEntry.CONTENT_URI,
                        contentValuesArray);

                Log.v(LOG_TAG, "Total songs inserted = "+ total);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                DataContract.SongEntry.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(LOG_TAG, "The number of songs loaded from csv " +  data.getCount());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
