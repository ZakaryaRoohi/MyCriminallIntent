package com.example.mycriminallintent.controller.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeRepository;
import com.example.mycriminallintent.repository.RepositoryInterface;

public class CrimeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RepositoryInterface<Crime> mRepository;

    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository= CrimeRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_crime_list, container, false);
        findViews(view);
        return view;
    }
    private void findViews(View view){
        mRecyclerView=view.findViewById(R.id.recycler_view_crimes);
    }
    private class CrimeHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;
        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle=itemView.findViewById(R.id.list_row_crime_title);
            mTextViewDate=itemView.findViewById((R.id.list_row_crime_date));
            mImageViewSolved=itemView.findViewById(R.id.imgview_solved);
        }
    }
}