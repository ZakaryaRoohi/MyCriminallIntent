package com.example.mycriminallintent.controller.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.controller.activity.CrimeDetailActivity;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeRepository;
import com.example.mycriminallintent.repository.RepositoryInterface;

import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String BUNDLE_CRIME = "BundleCrime";
    private EditText mEditTextCrimeTitle;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;

    private RepositoryInterface<Crime> mRepository;
    private Crime mCrime;

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
mRepository = CrimeRepository.getInstance();
        // get extra crimeId that sent from CrimeListFragment
        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeDetailActivity.EXTRA_CRIME_ID);
        mCrime=mRepository.get(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_crime_detail, container, false);
        findViews(view);
        initViews();
    return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_CRIME,mCrime);
    }

    private void findViews(View view){
        mEditTextCrimeTitle=view.findViewById(R.id.crime_title);
        mButtonDate =  view.findViewById(R.id.crime_date);
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);
    }
    private void initViews(){
        mEditTextCrimeTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText(mCrime.getDate().toString());
        mButtonDate.setEnabled(false);
    }
}