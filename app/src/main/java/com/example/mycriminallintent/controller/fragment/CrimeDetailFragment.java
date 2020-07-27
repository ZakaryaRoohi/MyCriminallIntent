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
    public static final String ARG_CRIME_ID = "CrimeId";
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
        /**
         get extra crimeId that sent from CrimeListFragment
         this is very very bad Wrong we didn't observe(Regrading-comply) Single responsibility
         this fragment maybe use another activity . it should not dependent on CrimeDetailActivity
         fragment never should be dependent on one Activity
         this fragment isn't reusable
         activity should read extras  and send to fragment
         */
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeDetailActivity.EXTRA_CRIME_ID);


        UUID crimeId = (UUID) getArguments().getSerializable("CrimeId");
        mCrime = mRepository.get(crimeId);
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
}