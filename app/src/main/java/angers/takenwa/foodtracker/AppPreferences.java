package angers.takenwa.foodtracker;

import android.content.Context;
import android.content.SharedPreferences;
public class AppPreferences extends MainActivity{
    private static final String PREF_NAME = "app_preferences";
    private static final String KEY_DB_CREATED = "db_created";

    public static void setDatabaseCreated(Context context, boolean created) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_DB_CREATED, created);
        editor.apply();
    }

    public static boolean isDatabaseCreated(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DB_CREATED, false);
    }
}