package com.example.lab_dailiy_selfi.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lab_dailiy_selfi.R;

//SINGLETON CLASS
public class SharedPreferenceHelper {
    private static SharedPreferenceHelper INSTANCE;
    private static SharedPreferences preferences;

    synchronized public static SharedPreferenceHelper getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferenceHelper();
            preferences = context.getSharedPreferences(String.valueOf((R.string.share_preferenses_name)), Context.MODE_PRIVATE);
        }

        return INSTANCE;
    }

    public void writeDATA(String key,String value){
        preferences.edit().putString(key, value).apply();
    }
    public String readDATA(String key){
        String result = preferences.getString(key,"");
        Log.d("READSTRINGDATA", result);
        return result;
    }

}
