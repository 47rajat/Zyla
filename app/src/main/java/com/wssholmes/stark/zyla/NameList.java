package com.wssholmes.stark.zyla;

/**
 * Created by stark on 29/03/17.
 */

//Binder object for texts.
public class NameList {

    private String[] mNames;

    public NameList(String[] names){
        mNames = names;
    }

    public String getNameAt(int i){
        return mNames[i];
    }
}
