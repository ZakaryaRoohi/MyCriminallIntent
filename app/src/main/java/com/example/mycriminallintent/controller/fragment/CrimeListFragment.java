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
    private int mPosition;

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

    private class CrimeHolder2 extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;

        private Crime mCrime;
        public CrimeHolder2(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.list_row_crime_title);
            mTextViewDate = itemView.findViewById((R.id.list_row_crime_date));
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPosition = getAdapterPosition();
//                    Intent intent = new Intent(getActivity(), CrimeDetailActivity.class);
                    //intent.putExtra("CrimeId",mCrime.getId());//send uuid to CrimeDetailActivity and read information from repository in crimeDetailActivity
                    //there is an android convention that  write a method in activity to get extra's
                    Intent intent = CrimeDetailActivity.newIntent(getActivity(), mCrime.getId());
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
                    mPosition = getAdapterPosition();
//                    Intent intent = new Intent(getActivity(), CrimeDetailActivity.class);
                    //intent.putExtra("CrimeId",mCrime.getId());//send uuid to CrimeDetailActivity and read information from repository in crimeDetailActivity
                    //there is an android convention that  write a method in activity to get extra's
                    Intent intent = CrimeDetailActivity.newIntent(getActivity(), mCrime.getId());
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

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final String TAG = "CLF";
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
//            return super.getItemViewType(position);
            if (mCrimes.get(position).isSolved()) {
                return 1;
            }
            return 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");
            // first I write 107 line but I found I need a view so I write line 106,105
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View view;
            switch (viewType) {
                case 1:{
                    view = inflater.inflate(R.layout.list_row_crime, parent, false);
                    return new CrimeHolder(view);
                }
                case 0:{
                    view = inflater.inflate(R.layout.list_row_crime_second, parent, false);
                    return new CrimeHolder2(view);

                }
            }
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {//this viewHolder is returned from onCreateViewHolder
            Log.d(TAG, "onBindViewHolder " + position);
            Crime crime = mCrimes.get(position);
            if (crime.isSolved()) {
                CrimeHolder crimeHold = (CrimeHolder) holder;
                crimeHold.bindCrime(crime);
            }
            else{
                CrimeHolder2 crimeHolder2 = (CrimeHolder2) holder;
                crimeHolder2.bindCrime(crime);
            }

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }


//    private void updateUI() { //connect adapter to RecyclerView
//        List<Crime> crimes = mRepository.getList();
//        if (mCrimeAdapter == null) {
//            mCrimeAdapter = new CrimeAdapter(crimes);
//            mRecyclerView.setAdapter(mCrimeAdapter);//connect recyclerView to adapter
//        } else {
//            mCrimeAdapter.notifyDataSetChanged();
//        }
//    }

    private void updateUI() { //connect adapter to RecyclerView
        List<Crime> crimes = mRepository.getList();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);//connect recyclerView to adapter
        } else {
            mCrimeAdapter.notifyItemChanged(mPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * this has  very bad performance because every time that come back from
         * CrimeDetailFragment drop that Ui and new another one.
         * magas ro ba katusha zadan
         * what should I do?????????????//
         * don't new Adapter.make adapter as field.create one adapter and only bind its view holders
         * before change updateUI method was:
         *
         * List<Crime> crimes = mRepository.getList;
         * CrimeAdapter adapter = new CrimeAdapter(crimes);
         * mRecyclerView.setAdapter(adapter);
         *
         * after Change use: .notifyDataSetChanged();
         *  List<Crime> crimes = mRepository.getList();
         *         if (mCrimeAdapter == null) {
         *             mCrimeAdapter = new CrimeAdapter(crimes);
         *             mRecyclerView.setAdapter(mCrimeAdapter);//connect recyclerView to adapter
         *         } else {
         *             mCrimeAdapter.notifyDataSetChanged();}
         */
        updateUI();
    }
}