package com.example.mycriminallintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import static com.example.mycriminallintent.database.CrimeDBSchema.CrimeTable.*;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    public CrimeBaseHelper(@Nullable Context context) {
        super(context, NAME, null, CrimeDBSchema.VERSION);
    }

    /**
     * create the database with all tables, constraints, ...
     * it's like implementing the ERD.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NAME + "(" +
                CrimeDBSchema.CrimeTable.COLS.ID + " integer primary key autoincrement," +
                CrimeDBSchema.CrimeTable.COLS.UUID + " text," +
                CrimeDBSchema.CrimeTable.COLS.TITLE + " text," +
                CrimeDBSchema.CrimeTable.COLS.DATE + " long," +
                CrimeDBSchema.CrimeTable.COLS.SOLVED + " integer" +
                ");");
        db.execSQL("CREATE TABLE " + UserDBSchema.NAME + "(" +
                UserDBSchema.UserTable.COLS.USERNAME + " integer primary key autoincrement," +
                UserDBSchema.UserTable.COLS.PASSWORD + " text" +

                ");");
    }

    /**
     * update the database with all alters.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
