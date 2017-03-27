package com.wssholmes.stark.zyla.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by stark on 27/03/17.
 */

public class DataProvider extends ContentProvider {
    private static final String LOG_TAG = DataProvider.class.getSimpleName();

    //Uri matcher to match the uri to a particular task.
    private static final UriMatcher sUrimatcher = buildUriMatcher();

    //Database helper used to perform CRUD operation on a SQL table.
    private DataHelper mDatabase;

    // Ids to identify the type of request being made.
    static final int SONG = 100;


    @Override
    public boolean onCreate() {
        mDatabase = new DataHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUrimatcher.match(uri)){
            case SONG:
            {
                Log.v(LOG_TAG, "songs queried");
                cursor = mDatabase.getReadableDatabase()
                        .query(DataContract.SongEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUrimatcher.match(uri);

        switch (match){
            case SONG:
                return DataContract.SongEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri : "+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final SQLiteDatabase database = mDatabase.getWritableDatabase();
        int match = sUrimatcher.match(uri);
        Uri returnUri;
        switch (match){
            case SONG:
            {
                Log.v(LOG_TAG, "inserting song " + contentValues.getAsString(DataContract.SongEntry.NAME));
                long _id = database.insert(DataContract.SongEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (_id > 0){
                    returnUri = DataContract.SongEntry.buildSongUri(_id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase database = mDatabase.getWritableDatabase();
        final int match = sUrimatcher.match(uri);
        int rowsDeleted;
        if(s == null) s = "1";

        switch (match){
            case SONG:
            {
                rowsDeleted = database.delete(DataContract.SongEntry.TABLE_NAME, s, strings);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknow uri : " + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase database = mDatabase.getWritableDatabase();
        final  int match = sUrimatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case SONG:
            {
                rowsUpdated = database.update(DataContract.SongEntry.TABLE_NAME,
                        contentValues,
                        s,
                        strings);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase database = mDatabase.getWritableDatabase();
        final int match = sUrimatcher.match(uri);

        switch (match){
            case SONG:
            {
                Log.v(LOG_TAG, "starting bulk insert for " + values.length + " items");

                database.beginTransaction();
                int returnCount = 0;

                try{
                    for(ContentValues contentValues : values){
                        long _id = database.insert(DataContract.SongEntry.TABLE_NAME,
                                null,
                                contentValues);

                        if(_id != -1){
                            returnCount++;
                        }
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri,values);
        }
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //adding various types of uris to be used for the app
        uriMatcher.addURI(DataContract.CONTENT_AUTHORITY,
                DataContract.PATH_SONG,
                SONG);

        return uriMatcher;
    }
}
