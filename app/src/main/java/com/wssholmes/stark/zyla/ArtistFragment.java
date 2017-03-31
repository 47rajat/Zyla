package com.wssholmes.stark.zyla;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
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
import com.wssholmes.stark.zyla.databinding.FragmentArtistBinding;


public class ArtistFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = ArtistFragment.class.getSimpleName();

    //Fields for fetching data from database.
    private static final int CURSOR_LOADER_ID = 0;
    private static final String[] PROJECTION = {
            "DISTINCT " + DataContract.SongEntry.ARTIST
    };

    private static final String SORT_ORDER = DataContract.SongEntry.ARTIST;

    //Fields for binding, loading and displaying data in fragment.
    FragmentArtistBinding mFragmentArtistBinding;
    private AdapterViewHolder mAdapterViewHolder;
    private ListViewHolder mListViewHolder;





    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing loader to fetch data.
        getLoaderManager().initLoader(CURSOR_LOADER_ID,null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Binding the layout for this fragment
        mFragmentArtistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist,
                container, false);

        //View holder for the recycler views in the fragment.
        mListViewHolder = new ListViewHolder();

        mListViewHolder.list0 = (RecyclerView) mFragmentArtistBinding.getRoot().findViewById(R.id.list_0);
        mListViewHolder.list1 = (RecyclerView) mFragmentArtistBinding.getRoot().findViewById(R.id.list_1);
        mListViewHolder.list2 = (RecyclerView) mFragmentArtistBinding.getRoot().findViewById(R.id.list_2);
        mListViewHolder.list3 = (RecyclerView) mFragmentArtistBinding.getRoot().findViewById(R.id.list_3);
        mListViewHolder.list4 = (RecyclerView) mFragmentArtistBinding.getRoot().findViewById(R.id.list_4);

        //ViewHolder for the adapters for the recycler views.
        mAdapterViewHolder = new AdapterViewHolder();

        mAdapterViewHolder.adapter0 = new NameListAdapter(getActivity(), false);
        mAdapterViewHolder.adapter1 = new NameListAdapter(getActivity(), false);
        mAdapterViewHolder.adapter2 = new NameListAdapter(getActivity(), false);
        mAdapterViewHolder.adapter3 = new NameListAdapter(getActivity(), false);
        mAdapterViewHolder.adapter4 = new NameListAdapter(getActivity(), false);


        mListViewHolder.list0.setAdapter(mAdapterViewHolder.adapter0);
        mListViewHolder.list1.setAdapter(mAdapterViewHolder.adapter1);
        mListViewHolder.list2.setAdapter(mAdapterViewHolder.adapter2);
        mListViewHolder.list3.setAdapter(mAdapterViewHolder.adapter3);
        mListViewHolder.list4.setAdapter(mAdapterViewHolder.adapter4);

        return mFragmentArtistBinding.getRoot();
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
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

        if( cursor != null && cursor.getCount() > 0){
            Log.v(LOG_TAG, "total distinct nameList = " + cursor.getCount());

            cursor.moveToFirst();

            //fetching names of unique artists.
            String[] names = new String[cursor.getCount()];
            int i = 0;
            do {
                names[i++] = cursor.getString(0);
            } while (cursor.moveToNext());

            mAdapterViewHolder.adapter0.setName(names[0]);
            mAdapterViewHolder.adapter1.setName(names[1]);
            mAdapterViewHolder.adapter2.setName(names[2]);
            mAdapterViewHolder.adapter3.setName(names[3]);
            mAdapterViewHolder.adapter4.setName(names[4]);

            //Namelist object to be used for binding data.
            NameList nameList = new NameList(names);

            if (mFragmentArtistBinding != null) {
                mFragmentArtistBinding.setNameList(nameList);
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
    }

    //ViewHolder class for recycler view adapter.
    private static class AdapterViewHolder{
        NameListAdapter adapter0;
        NameListAdapter adapter1;
        NameListAdapter adapter2;
        NameListAdapter adapter3;
        NameListAdapter adapter4;
    }
}
