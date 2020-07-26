package com.example.mycriminallintent.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.controller.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context , CrimeListActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return  new CrimeListFragment();
    }
}