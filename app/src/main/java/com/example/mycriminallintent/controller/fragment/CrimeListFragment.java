package com.example.mycriminallintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.controller.activity.CrimeDetailActivity;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeRepository;
import com.example.mycriminallintent.repository.RepositoryInterface;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RepositoryInterface<Crime> mRepository;
    private CrimeAdapter mCrimeAdapter;

    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = CrimeRepository.getInstance();
    }

    public static CrimeListFragment newInstance() {

        Bundle args = new Bundle();

        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        findViews(view);
        //RecyclerView Responsibility : Positioning . set recyclerView to listFragment layout
        mRecyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));
        updateUI();
        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crimes);
    }

    //hold Views References
    private class CrimeHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;

        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.list_row_crime_title);
            mTextViewDate = itemView.findViewById((R.id.list_row_crime_date));
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), CrimeDetailActivity.class);
                    //intent.putExtra("CrimeId",mCrime.getId());//send uuid to CrimeDetailActivity and read information from repository in crimeDetailActivity
                   //there is an android convention that  write a method in activity to get extra's
                    Intent intent = CrimeDetailActivity.newIntent(getActivity(),mCrime.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        public static final String TAG = "CLF";
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");
            // first I write 107 line but I found I need a view so I write line 106,105
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_row_crime, parent, false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {//this viewHolder is returned from onCreateViewHolder
            Log.d(TAG, "onBindViewHolder " + position);
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private void updateUI() { //connect adapter to RecyclerView
        List<Crime> crimes = mRepository.getList();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);//connect recyclerView to adapter
        } else {
            mCrimeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}