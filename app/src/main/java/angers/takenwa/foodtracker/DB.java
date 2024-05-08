package angers.takenwa.foodtracker;


import static android.app.DownloadManager.COLUMN_ID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "open_food_facts.db";
    private static final int DATABASE_VERSION = 1;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code_bare,"+
                "product_name TEXT," +
                "grade TEXT," +
                "expiration_date TEXT," +  // Ajout de la date de péremption
                "days_until_expiry INTEGER," +  // Ajout du nombre de jours restants
                "energy REAL," +
                "energy_kcal REAL," +
                "energy_unit TEXT," +
                "fat_100g REAL," +
                "fat REAL," +
                "fat_unit TEXT," +
                "proteins REAL," +
                "proteins_unit TEXT," +
                "salt REAL," +
                "salt_unit TEXT," +
                "sugars REAL," +
                "sugars_unit TEXT," +
                "allergens_tags TEXT," +
                "status TEXT," +
                "status_verbose TEXT," +
                "image_uri TEXT)";
        sqLiteDatabase.execSQL(createTableQuery);


    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void clearProductsTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM products");
        db.isOpen();
        //db.close();
    }




}

