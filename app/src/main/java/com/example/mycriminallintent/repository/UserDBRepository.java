package com.example.mycriminallintent.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.mycriminallintent.database.CrimeBaseHelper;

public class UserDBRepository {

    private static UserDBRepository sUserDBRepository;
    private SQLiteDatabase mDatabase;
    private static Context mContext;

    public static UserDBRepository getInstance(Context context){
        mContext = context.getApplicationContext();
        if(sUserDBRepository==null)
            sUserDBRepository = new UserDBRepository();
        return sUserDBRepository;
    }
    private UserDBRepository(){
        CrimeBaseHelper crimeBaseHelper = new CrimeBaseHelper(mContext);
        mDatabase=crimeBaseHelper.getWritableDatabase();
    }


}
