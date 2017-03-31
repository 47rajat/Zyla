package com.wssholmes.stark.zyla;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wssholmes.stark.zyla.data.DataContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Fields for finding and inflating fragments by tag.
    private static final String ALBUM_FRAGMENT_TAG = "ALBUM_FRAGMENT_TAG";
    private static final String ARTIST_FRAGMENT_TAG = "ARTIST_FRAGMENT_TAG";

    //Fields for updating page count on fragment change.
    private int mPageCount;

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

        Spinner mCategorySpinner = (Spinner) findViewById(R.id.spinner_category);
        Spinner mCountSpinner = (Spinner) findViewById(R.id.spinner_count);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_category, android.R.layout.simple_spinner_dropdown_item);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);
        mCategorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> countAdapter = ArrayAdapter.createFromResource(this,
                R.array.page_size, android.R.layout.simple_spinner_dropdown_item);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountSpinner.setAdapter(countAdapter);
        mCountSpinner.setOnItemSelectedListener(this);


    }

    //This method is used to update the database from the csv, upon first initialization.
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        switch (adapterView.getId()){
            case R.id.spinner_category:
            {
                switch (pos){
                    case 0:
                        //Album fragment selected.
                        if (getFragmentManager().findFragmentByTag(ALBUM_FRAGMENT_TAG) == null){

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, new AlbumFragment(), ALBUM_FRAGMENT_TAG)
                                    .commit();

                        }
                        break;
                    case 1:
                        //Artist fragment selected.
                        if (getFragmentManager().findFragmentByTag(ARTIST_FRAGMENT_TAG) == null) {

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, new ArtistFragment(), ARTIST_FRAGMENT_TAG)
                                    .commit();
                        }
                        break;
                }

                //This is important to execute pending transactions so that the fragments can be
                //accessed properly using tags.
                getFragmentManager().executePendingTransactions();

                //Setting the current pagecount (if >1) for the new fragment.
                if (getFragmentManager().findFragmentByTag(ALBUM_FRAGMENT_TAG) != null && mPageCount > 1){
                    AlbumFragment albumFragment = (AlbumFragment)getFragmentManager()
                            .findFragmentByTag(ALBUM_FRAGMENT_TAG);

                    albumFragment.setLayoutSpan(mPageCount);
                }
                if (getFragmentManager().findFragmentByTag(ARTIST_FRAGMENT_TAG) != null && mPageCount > 1){
                    ArtistFragment artistFragment = (ArtistFragment) getFragmentManager().
                            findFragmentByTag(ARTIST_FRAGMENT_TAG);

                    artistFragment.setLayoutSpan(mPageCount);
                }
                break;
            }
            case R.id.spinner_count:
            {
                int value = -1;
                switch (pos) {
                    case 0:
                        value = 1;
                        break;
                    case 1:
                        value = 2;
                        break;
                    case 2:
                        value = 3;
                        break;
                    case 3:
                        value = 4;
                        break;
                    case 4:
                        value = 5;
                        break;
                    }

                    if (value > 0){
                        mPageCount = value;

                        //This is important to execute pending transactions so that the fragments can be
                        //accessed properly using tags.
                        //This is here to ensure that you are not taking any chances.
                        getFragmentManager().executePendingTransactions();

                        if (getFragmentManager().findFragmentByTag(ALBUM_FRAGMENT_TAG) !=null){

                            AlbumFragment albumFragment = (AlbumFragment)getFragmentManager()
                                    .findFragmentByTag(ALBUM_FRAGMENT_TAG);

                            albumFragment.setLayoutSpan(value);
                        }
                        if (getFragmentManager().findFragmentByTag(ARTIST_FRAGMENT_TAG) != null){

                            ArtistFragment artistFragment = (ArtistFragment) getFragmentManager().
                                    findFragmentByTag(ARTIST_FRAGMENT_TAG);

                            artistFragment.setLayoutSpan(value);
                        }

                    }
                break;
            }
            default:
                Log.v(LOG_TAG, "unknown spinner selected");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {



    }
}
