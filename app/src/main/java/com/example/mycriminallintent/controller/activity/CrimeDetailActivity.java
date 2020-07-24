package com.example.mycriminallintent.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.mycriminallintent.controller.fragment.CrimeDetailFragment;

public class CrimeDetailActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return  new CrimeDetailFragment();
    }
}