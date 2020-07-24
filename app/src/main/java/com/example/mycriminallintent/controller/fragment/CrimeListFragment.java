package com.example.mycriminallintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        findViews(view);
        mRecyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));
        updateUI();
        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crimes);
    }

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
                    Intent intent = new Intent(getActivity(), CrimeDetailActivity.class);
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

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_row_crime, parent, false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private void updateUI() {
        List<Crime> crimes = mRepository.getList();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();
        }
    }
}