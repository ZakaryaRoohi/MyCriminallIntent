package com.example.mycriminallintent.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.controller.fragment.CrimeDetailFragment;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeRepository;
import com.example.mycriminallintent.repository.IRepository;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID ="com.example.mycriminallintent.controller.activity.crimeId" ;
    private ViewPager2 mCrimeViewPager;
    private IRepository mRepository;
    private Button mButtonFirst;
    private Button mButtonPrevious;
    private Button mButtonNext;
    private Button mButtonLast;
public static Intent newIntent(Context context, UUID crimeId){
    Intent intent  = new Intent(context, CrimePagerActivity.class);
    intent.putExtra(EXTRA_CRIME_ID,crimeId);
    return intent;
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mRepository = CrimeRepository.getInstance();
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        int position = mRepository.getPosition(crimeId);
        findViews();
        setUI(position);
        setClickListeners();
    }
    private void setUI(int position){
    FragmentStateAdapter fragmentStateAdapter =  new CrimeViewPagerAdapter(this,mRepository.getList());
    mCrimeViewPager.setAdapter(fragmentStateAdapter);
    //after set adapter
    mCrimeViewPager.setCurrentItem(position);
}
    private void findViews(){
    mCrimeViewPager = findViewById(R.id.crime_view_pager);
    mButtonFirst = findViewById(R.id.button_first);
    mButtonPrevious = findViewById(R.id.button_previous);
    mButtonNext = findViewById(R.id.button_next);
    mButtonLast = findViewById(R.id.button_last);
    }
    private  class CrimeViewPagerAdapter extends FragmentStateAdapter{
        private List<Crime> mCrimes;
        public CrimeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Crime> crimes) {
            super(fragmentActivity);
            mCrimes=crimes;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return CrimeDetailFragment.newInstance((mCrimes.get(position)).getId());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
    private void setClickListeners(){

    mButtonFirst.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setUI(0);
        }
    });
    mButtonPrevious.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    });
    mButtonNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    });
    mButtonLast.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setUI(mRepository.getList().size());
        }
    });
    }
}