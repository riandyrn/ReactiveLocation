package com.haraj.mersal.reactivelocation.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by riandyrn on 3/28/16.
 */
public class PreferenceProvider {

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences("com.haraj.mersal.reactivelocation", Context.MODE_PRIVATE);
    };

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreference(context).edit();
    }

    public static void setIsOnForeground(Context context, boolean isOnForeground) {
        getEditor(context).putBoolean("isOnForeground", isOnForeground).apply();
    }

    public static boolean isOnForeground(Context context) {
        return getSharedPreference(context).getBoolean("isOnForeground", false);
    }

}
