package com.example.mycriminallintent.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.controller.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return  new CrimeListFragment();
    }
}