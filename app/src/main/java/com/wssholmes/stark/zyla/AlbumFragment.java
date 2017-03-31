package com.wssholmes.stark.zyla;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wssholmes.stark.zyla.data.DataContract;
import com.wssholmes.stark.zyla.databinding.FragmentAlbumBinding;


public class AlbumFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = AlbumFragment.class.getSimpleName();

    //Fields for fetching data from database.
    private static final int CURSOR_LOADER_ID = 0;
    private static final String[] PROJECTION = new String[]{
            "DISTINCT " + DataContract.SongEntry.ALBUM
    };

    private static final String SORT_ORDER = DataContract.SongEntry.ALBUM;


    //Fields for binding, loading and displaying data in fragment.
    private FragmentAlbumBinding mFragmentAlbumBinding;
    private AdapterViewHolder mAdapterViewHolder;
    private ListViewHolder mListViewHolder;


    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing loader to fetch data.
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Binding the layout for this fragment
        mFragmentAlbumBinding  = DataBindingUtil.inflate(inflater,
                R.layout.fragment_album, container, false);

        //View holder for the recycler views in the fragment.
        mListViewHolder = new ListViewHolder();

        mListViewHolder.list0 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_0);
        mListViewHolder.list1 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_1);
        mListViewHolder.list2 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_2);
        mListViewHolder.list3 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_3);
        mListViewHolder.list4 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_4);
        mListViewHolder.list5 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_5);
        mListViewHolder.list6 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_6);
        mListViewHolder.list7 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_7);
        mListViewHolder.list8 = (RecyclerView) mFragmentAlbumBinding.getRoot().findViewById(R.id.list_8);


        //ViewHolder for the adapters for the recycler views.
        mAdapterViewHolder = new AdapterViewHolder();

        mAdapterViewHolder.adapter0 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter1 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter2 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter3 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter4 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter5 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter6 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter7 = new NameListAdapter(getActivity(), true);
        mAdapterViewHolder.adapter8 = new NameListAdapter(getActivity(), true);


        mListViewHolder.list0.setAdapter(mAdapterViewHolder.adapter0);
        mListViewHolder.list1.setAdapter(mAdapterViewHolder.adapter1);
        mListViewHolder.list2.setAdapter(mAdapterViewHolder.adapter2);
        mListViewHolder.list3.setAdapter(mAdapterViewHolder.adapter3);
        mListViewHolder.list4.setAdapter(mAdapterViewHolder.adapter4);
        mListViewHolder.list5.setAdapter(mAdapterViewHolder.adapter5);
        mListViewHolder.list6.setAdapter(mAdapterViewHolder.adapter6);
        mListViewHolder.list7.setAdapter(mAdapterViewHolder.adapter7);
        mListViewHolder.list8.setAdapter(mAdapterViewHolder.adapter8);

        return mFragmentAlbumBinding.getRoot();
    }


    //this method is used to change the layout of the recycler views according to user input via.
    //the spinner in MainActivity.
    public void setLayoutSpan(int span){
        if (mListViewHolder != null){

            mListViewHolder.list0.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list1.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list2.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list3.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list4.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list5.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list6.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list7.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
            mListViewHolder.list8.setLayoutManager(new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                DataContract.SongEntry.CONTENT_URI,
                PROJECTION,
                null,
                null,
                SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null && cursor.getCount() > 0){
            Log.v(LOG_TAG, "total distinct album " + cursor.getCount());

            String[] names = new String[cursor.getCount()];

            //fetching names of unique albums.
            cursor.moveToFirst();
            do {
                names[cursor.getPosition()] = cursor.getString(0);
            } while (cursor.moveToNext());

            //Updating the adapter to get list of songs for each album.
            mAdapterViewHolder.adapter0.setName(names[0]);
            mAdapterViewHolder.adapter1.setName(names[1]);
            mAdapterViewHolder.adapter2.setName(names[2]);
            mAdapterViewHolder.adapter3.setName(names[3]);
            mAdapterViewHolder.adapter4.setName(names[4]);
            mAdapterViewHolder.adapter5.setName(names[5]);
            mAdapterViewHolder.adapter6.setName(names[6]);
            mAdapterViewHolder.adapter7.setName(names[7]);
            mAdapterViewHolder.adapter8.setName(names[8]);

            //Namelist object to be used for binding data.
            NameList nameList = new NameList(names);

            if (mFragmentAlbumBinding != null){
                mFragmentAlbumBinding.setNameList(nameList);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    //ViewHodler class for recycler views.
    private static class ListViewHolder{
        RecyclerView list0;
        RecyclerView list1;
        RecyclerView list2;
        RecyclerView list3;
        RecyclerView list4;
        RecyclerView list5;
        RecyclerView list6;
        RecyclerView list7;
        RecyclerView list8;
    }

    //ViewHolder class for recycler view adapter.
    private static class AdapterViewHolder{
        NameListAdapter adapter0;
        NameListAdapter adapter1;
        NameListAdapter adapter2;
        NameListAdapter adapter3;
        NameListAdapter adapter4;
        NameListAdapter adapter5;
        NameListAdapter adapter6;
        NameListAdapter adapter7;
        NameListAdapter adapter8;
    }
}
