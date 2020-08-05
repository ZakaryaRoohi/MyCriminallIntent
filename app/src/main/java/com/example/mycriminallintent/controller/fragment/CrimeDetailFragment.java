package com.example.mycriminallintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeRepository;
import com.example.mycriminallintent.repository.IRepository;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String BUNDLE_CRIME = "BundleCrime";
    public static final String ARG_CRIME_ID = "CrimeId";
    public static final String DATE_DIALOG_FRAGMENT_TAG = "DialogDate";
    public static final String TIMER_DIALOG_FRAGMENT_TAG = "DialogTimer";
    public static final int DATE_PICKER_REQUEST_CODE = 0;
    public static final int TIME_PICKER_REQUEST_CODE = 1;

    private EditText mEditTextCrimeTitle;
    private Button mButtonDate;
    private Button mButtonTime;
    private CheckBox mCheckBoxSolved;
    private static int mCrimePosition;
    private IRepository<Crime> mRepository;
    private Crime mCrime;
    private Date mCrimeDate;
    private Time mCrimeTime;

    private DatePicker mDatePicker;

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = CrimeRepository.getInstance();
        /**
         get extra crimeId that sent from CrimeListFragment
         this is very very bad Wrong we didn't observe(Regrading-comply) Single responsibility
         this fragment maybe use another activity . it should not dependent on CrimeDetailActivity
         fragment never should be dependent on one Activity
         this fragment isn't reusable
         activity should read extras  and send to fragment
         */
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeDetailActivity.EXTRA_CRIME_ID);


        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = mRepository.get(crimeId);
//        mCrimePosition = mRepository.getPosition(mCrime);
    }

    /**
     * every want to start fragment should use this method and
     * nobody can't use fragment constractor
     * Using factory pattern to create this fragment. every class that want
     * to create this fragment should Always call this method "only"
     *
     * @param crimeId this fragment need CrimeId to work
     * @return new CrimeDetailFragment
     */
    public static CrimeDetailFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);
        findViews(view);
        setListeners();
        initViews();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_CRIME, mCrime);
    }



    private void findViews(View view) {
        mEditTextCrimeTitle = view.findViewById(R.id.crime_title);
        mButtonDate = view.findViewById(R.id.crime_date);
        mButtonTime=view.findViewById(R.id.crime_time);
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);
    }

    private void initViews() {
        mEditTextCrimeTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText( new SimpleDateFormat("MM/dd/yyyy").format(mCrime.getDate()));
        mButtonTime.setText(new SimpleDateFormat("HH:mm:ss").format(mCrime.getDate()));

    }


    /**
     * One the best way to save object Automaticaly is "OnPause" 100%  safe
     */
    @Override
    public void onPause() {
        super.onPause();
        updateCrime();

    }

    private void updateCrime() {
        mRepository.update(mCrime);
    }

    private void setListeners() {
        mEditTextCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                mCrime.setSolved(checked);
            }
        });
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());

                //create parent-child relations between CrimeDetailFragment-DatePickerFragment
                datePickerFragment.setTargetFragment(CrimeDetailFragment.this, DATE_PICKER_REQUEST_CODE);

                datePickerFragment.show(getFragmentManager(), DATE_DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeDetailFragment.this,TIME_PICKER_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), TIMER_DIALOG_FRAGMENT_TAG);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            //get response from intent extra, which is user selected date
            Date userSelectedDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            mCrime.setDate(userSelectedDate);
            mButtonDate.setText( new SimpleDateFormat("MM/dd/yyyy").format(mCrime.getDate()));

            updateCrime();
        }
        if(requestCode == TIME_PICKER_REQUEST_CODE){
            Date userSelectedDate  = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            mCrime.setDate(userSelectedDate);
            mButtonTime.setText(new SimpleDateFormat("HH:mm:ss").format(mCrime.getDate()));

            updateCrime();
        }
    }
}