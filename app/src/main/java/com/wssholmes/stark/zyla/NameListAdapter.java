package com.wssholmes.stark.zyla;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wssholmes.stark.zyla.data.DataContract;
import com.wssholmes.stark.zyla.databinding.ItemListBinding;

import java.util.Vector;

/**
 * Created by stark on 30/03/17.
 */

public class NameListAdapter extends RecyclerView.Adapter<NameListAdapter.ViewHolder>{
    private static final String LOG_TAG = NameListAdapter.class.getSimpleName();

    private final Context mContext;
    private final String SELECTION;
    private String mName = "";
    private Vector<String> mListItems;
    private static final String[] PROJECTION = new String[]{
            DataContract.SongEntry.NAME
    };

    private static final String SORT_ORDER = DataContract.SongEntry.NAME;



    public NameListAdapter(Context context, boolean isAlbum){
        mContext = context;

        if (isAlbum){
            SELECTION = DataContract.SongEntry.ALBUM + " = ?";
        } else {
            SELECTION = DataContract.SongEntry.ARTIST + " = ?";
        }

    }

    @Override
    public NameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemListBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list, parent, false);
            View view = itemListBinding.getRoot();
            view.setFocusable(true);
            return new ViewHolder(itemListBinding);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(NameListAdapter.ViewHolder holder, int position) {

        if(mListItems != null){
            holder.bind(mListItems.elementAt(position));
        }

    }

    @Override
    public int getItemCount() {
        return mListItems == null?0:mListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListBinding mItemListBinding;

        public ViewHolder(ItemListBinding itemListBinding) {
            super(itemListBinding.getRoot());
            mItemListBinding = itemListBinding;
        }

        private void bind(String name){
            mItemListBinding.setItem(name);
            mItemListBinding.executePendingBindings();

        }
    }


    public void swapList(Vector<String> list){
        if(list != null){
            mListItems = list;

            notifyDataSetChanged();
        }
    }

    public void setName(String name){
        if(name != null && !mName.equals(name)){

            mName = name;

            QueryHandler queryHandler = new QueryHandler(mContext.getContentResolver(), this);
            queryHandler.startQuery(0, null,
                    DataContract.SongEntry.CONTENT_URI,
                    PROJECTION,
                    SELECTION,
                    new String[]{mName},
                    SORT_ORDER);

            notifyDataSetChanged();
        }

    }

}
