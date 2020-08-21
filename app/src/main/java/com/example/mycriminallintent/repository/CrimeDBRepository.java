package com.example.mycriminallintent.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mycriminallintent.database.CrimeBaseHelper;
import com.example.mycriminallintent.database.CrimeDBSchema;
import com.example.mycriminallintent.database.cursorwrapper.CrimeCursorWrapper;
import com.example.mycriminallintent.model.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements IRepository<Crime> {

    private static CrimeDBRepository sCrimeRepository;

    //future referenced: memory leaks
    private static Context mContext;

    private SQLiteDatabase mDatabase;

    public static CrimeDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sCrimeRepository == null)
            sCrimeRepository = new CrimeDBRepository();

        return sCrimeRepository;
    }

    private CrimeDBRepository() {
        CrimeBaseHelper crimeBaseHelper = new CrimeBaseHelper(mContext);
        mDatabase = crimeBaseHelper.getWritableDatabase();
    }

    //Read all
    @Override
    public List<Crime> getList() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    /*@Override
    public void setList(List<Crime> crimes) {
        mCrimes = crimes;
    }*/

    //Read one
    @Override
    public Crime get(UUID uuid) {
        /*for (Crime crime: mCrimes) {
            if (crime.getId().equals(uuid))
                return crime;
        }*/

        String selection = CrimeDBSchema.CrimeTable.COLS.UUID + "=?";
        String[] selectionArgs = new String[]{uuid.toString()};

        CrimeCursorWrapper cursor = queryCrimes(selection, selectionArgs);
        try {
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    @Override
    public void setList(List<Crime> list) {

    }

    private CrimeCursorWrapper queryCrimes(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(CrimeDBSchema.CrimeTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        CrimeCursorWrapper crimeCursorWrapper = new CrimeCursorWrapper(cursor);
        return crimeCursorWrapper;
    }

    //Update one
    @Override
    public void update(Crime crime) {
        ContentValues values = getCrimeContentValue(crime);
        String where = CrimeDBSchema.CrimeTable.COLS.UUID + "=?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.update(CrimeDBSchema.CrimeTable.NAME, values, where, whereArgs);
    }

    //Delete
    @Override
    public void delete(Crime crime) {
        String where = CrimeDBSchema.CrimeTable.COLS.UUID + "=?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.delete(CrimeDBSchema.CrimeTable.NAME, where, whereArgs);
    }

    //Create: Insert
    @Override
    public void insert(Crime crime) {
        ContentValues values = getCrimeContentValue(crime);
        mDatabase.insert(CrimeDBSchema.CrimeTable.NAME, null, values);
    }

    //Create: Insert
    @Override
    public void insertList(List<Crime> crimes) {
    }

    @Override
    public int getPosition(UUID uuid) {
        List<Crime> crimes = getList();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(uuid))
                return i;
        }

        return -1;
    }

    /**
     * Convert crime pojo to ContentValue
     * @param crime
     * @return
     */
    private ContentValues getCrimeContentValue(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDBSchema.CrimeTable.COLS.UUID, crime.getId().toString());
        values.put(CrimeDBSchema.CrimeTable.COLS.TITLE, crime.getTitle());
        values.put(CrimeDBSchema.CrimeTable.COLS.DATE, crime.getDate().getTime());
        values.put(CrimeDBSchema.CrimeTable.COLS.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeDBSchema.CrimeTable.COLS.SUSPECT, crime.getSuspect());
        values.put(CrimeDBSchema.CrimeTable.COLS.SuspectPhoneNumber,crime.getSuspectPhoneNumber());
        return values;
    }
}
