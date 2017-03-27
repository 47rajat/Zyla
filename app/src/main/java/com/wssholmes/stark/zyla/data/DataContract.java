package com.wssholmes.stark.zyla.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by stark on 27/03/17.
 */

public class DataContract  {

    public static final String CONTENT_AUTHORITY = "com.wssholmes.stark.zyla";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Path for storing song data.
    public static final String PATH_SONG = "song_path";

    public static class SongEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SONG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG;

        //Columns for table:
        public static final String TABLE_NAME = "songs_table",
        NAME = "song_name",
        ARTIST = "song_artist",
        ALBUM = "song_album";

        public static Uri buildSongUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
