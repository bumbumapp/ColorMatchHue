package com.bumbumapps.hue;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceCoin {
    private SharedPreferences sharedPreferences;

    public PreferenceCoin(Context context){
        sharedPreferences = context.getSharedPreferences("coins",Context.MODE_PRIVATE);
    }


    public void putInteger(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("coin", value);
        editor.apply();
    }

    public int getInteger(String key){
        return sharedPreferences.getInt("coin", 0);
    }
}
