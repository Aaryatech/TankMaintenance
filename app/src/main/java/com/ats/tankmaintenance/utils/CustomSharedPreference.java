package com.ats.tankmaintenance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by prashant on २७/७/१७.
 */

public class CustomSharedPreference
{

    public static String LANGUAGE_ENG = "en";
    public static String LANGUAGE_MAR = "ma";
    public static String LANGUAGE_ENG_ID = "1"; //--- for english
    public static String LANGUAGE_MAR_ID = "2"; //--- for marathi
    public static String LANGUAGE_SELECTED = "1"; //--- for marathi
    public static String KEY_USER = "User";

    private static String PREFERENCE_NAME = "UserInfo";
    public static String PREFERENCE_TOKEN = "appToken";


    public static List<String> lstAppData = new ArrayList<>();

    public static void putString(Context activity, String key, String value)
    {
        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = shared.edit();
        edt.putString(key, value);
        edt.commit();
    }


    /*public static RegistrationModel getUserProfile(Activity activity, String key)
    {

        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return new Gson().fromJson(shared.getString(key, null), RegistrationModel.class);
    }*/

    public static String getString(Context activity, String key)
    {
        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String val = shared.getString(key, null);
        return shared.getString(key, null);
    }

    public static int getInt(Activity activity, String key)
    {
        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return shared.getInt(key, 1);
    }


    public static void deletePreference(Context context)
    {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = shared.edit().clear();
        edt.commit();
    }

    public static void deletePreferenceByKey(Context context, String key )
    {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(key);
        editor.apply();
    }

    //FirstTime
    public static void setForFirstTime(Context c, String userId)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FirstTime", userId);
        editor.commit();
    }

    public static boolean saveArray(Context context, List<String> sKey)
    {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        /* sKey is an array */
        editor.putInt("Status_size", sKey.size());

        for(int i=0;i<sKey.size();i++)
        {
            editor.remove("Status_" + i);
            lstAppData.add(sKey.get(i));
            editor.putString("Status_" + i, lstAppData.get(i));
        }

        return editor.commit();
    }

    public static void loadArray(Context context, List<String> sKey)
    {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        //sKey.clear();
        int size = shared.getInt("Status_size", 0);

        for(int i=0;i<size;i++)
        {
            //sKey.add(shared.getString("Status_" + i, null));
            lstAppData.add(shared.getString("Status_" + i, null));
        }

    }
}