package com.example.mycriminallintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.controller.activity.CrimeDetailActivity;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeRepository;
import com.example.mycriminallintent.repository.RepositoryInterface;

import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String BUNDLE_CRIME = "BundleCrime";
    public static final String ARG_CRIME_ID = "CrimeId";
    private EditText mEditTextCrimeTitle;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;
    private static int mCrimePosition;
    private RepositoryInterface<Crime> mRepository;
    private Crime mCrime;

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
        mCrimePosition = mRepository.getPosition(mCrime);
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
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);
    }

    private void initViews() {
        mEditTextCrimeTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText(mCrime.getDate().toString());
        mButtonDate.setEnabled(false);
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
    }
}