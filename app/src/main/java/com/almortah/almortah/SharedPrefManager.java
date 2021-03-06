package com.almortah.almortah;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ziyadalkhonein on 11/28/17.
 */

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME="fcmshareddemo";
    private static final String KEY_ACCESS_TOKEN="token";

    private static Context context;
    private static SharedPrefManager mInstance;

    private SharedPrefManager(Context context){
        this.context=context;
    }
    public static synchronized SharedPrefManager getmInstance(Context context){
        if(mInstance==null){
            mInstance=new SharedPrefManager(context);
        }
        return mInstance;
    }
    public boolean storeToken(String token){
        SharedPreferences sharedPreferences =context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        return true;
    }
    public String getToken(){
        SharedPreferences sharedPreferences =context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);
    }
}
