package com.wssholmes.stark.zyla.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stark on 27/03/17.
 */

public class DataHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "songs.db";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //SQL command to create song_table.
        final String CREATE_SONG_TABLE = "CREATE TABLE "+ DataContract.SongEntry.TABLE_NAME + " ("+
                DataContract.SongEntry._ID + " INTEGER PRIMARY KEY, "+
                DataContract.SongEntry.NAME + " TEXT NOT NULL, " +
                DataContract.SongEntry.ARTIST + " TEXT NOT NULL, "+
                DataContract.SongEntry.ALBUM + " TEXT NOT NULL"+
                ");";

        sqLiteDatabase.execSQL(CREATE_SONG_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //Droping existing table, this can be replaced by any suitable action.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DataContract.SongEntry.TABLE_NAME);

    }
}
