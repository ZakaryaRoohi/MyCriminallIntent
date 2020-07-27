package com.example.mycriminallintent.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.mycriminallintent.controller.fragment.CrimeDetailFragment;

import java.util.UUID;

public class CrimeDetailActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.example.mycriminallintent.controller.activity.extraCrimeId";

    /**
     * every component that want to start activity must
     *     create It's intent from this method
     *      this activity tells that everyOne want to start
     *       me should start with my role
     *
     * @param context Context of src
     * @param crimeId this activity needs a crime Id to work
     * @return
     */
    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimeDetailActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);//UUID is serializable and parsable
        return intent;
    }


    @Override
    public Fragment createFragment() {
        return new CrimeDetailFragment();
    }
}