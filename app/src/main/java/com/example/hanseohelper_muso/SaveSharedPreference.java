package com.example.hanseohelper_muso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    //로그인 상태 유지를 위한 클래스

    static final String PREF_USER_EMAIL = "userEmail";

    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //사용자 정보 set
    public static void setUserEmail(Context ctx, String userEmail){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, userEmail);
        editor.commit();
    }

    //사용자 정보 get
    public static String getUserEmail(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    //사용자 정보 삭제(logout)
    public static void clearUserEmail(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
