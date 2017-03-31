package com.wssholmes.stark.zyla;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import java.util.Vector;

/**
 * Created by stark on 30/03/17.
 */
//Async query handler for loading list items.
public class QueryHandler extends AsyncQueryHandler {
    private Vector<String> mList;
    private NameListAdapter mNameListAdapter;

    public QueryHandler(ContentResolver cr, NameListAdapter nameListAdapter) {
        super(cr);
        mList  = new Vector<>();
        mNameListAdapter = nameListAdapter;
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if(cursor!=null && cursor.getCount() > 0){
            cursor.moveToFirst();

            do {
                mList.add(cursor.getString(0));
            } while (cursor.moveToNext());

            mNameListAdapter.swapList(mList);

        }

        if (cursor!=null) cursor.close();
    }
}
