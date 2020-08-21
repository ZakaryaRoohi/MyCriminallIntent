package com.example.mycriminallintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.controller.activity.CrimeDetailActivity;
import com.example.mycriminallintent.controller.activity.CrimePagerActivity;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeDBRepository;
import com.example.mycriminallintent.repository.CrimeRepository;
import com.example.mycriminallintent.repository.IRepository;

import java.util.List;

public class CrimeListFragment extends Fragment {

    public static final String BUNDLE_IS_SUBTITLE_VISIBLE = "isSubtitleVisible";
    private RecyclerView mRecyclerView;
    private IRepository<Crime> mRepository;
    private CrimeAdapter mCrimeAdapter;
    private LinearLayout mLinearLayout1;
    private LinearLayout mLinearLayout2;
    private ImageButton mImageButtonAddCrime;

    private boolean mIsSubtitleVisible = false;

    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = CrimeDBRepository.getInstance(getActivity());
//        mRepository = CrimeRepository.getInstance();
        setHasOptionsMenu(true);
        if (savedInstanceState != null)
            mIsSubtitleVisible = savedInstanceState.getBoolean(BUNDLE_IS_SUBTITLE_VISIBLE, false);


    }
    private void setClickListener(){
        mImageButtonAddCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mLinearLayout1.setVisibility(View.VISIBLE);
//                mLinearLayout2.setVisibility(View.GONE);

                addCrime();

            }
        });
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
        mImageButtonAddCrime=view.findViewById(R.id.image_button_add_crime);

        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_crime_list, menu);
        updateMenuItemSubtitle(menu.findItem(R.id.menu_item_subtitle));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                addCrime();
                return true;
            case R.id.menu_item_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                updateSubtitle();
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMenuItemSubtitle(@NonNull MenuItem item) {
        if (mIsSubtitleVisible)
            item.setTitle("Hide SubTitle");
        else
            item.setTitle("Show subTitle");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_IS_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    private void addCrime() {
        IRepository repository = CrimeDBRepository.getInstance(getActivity());
//        IRepository repository = CrimeRepository.getInstance();
        Crime crime = new Crime();
        repository.insert(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
        startActivity(intent);
    }

    private void updateSubtitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        int numberOfCrimes = mRepository.getList().size();
        String crimesString = getString(R.string.subtitle_format, numberOfCrimes);

        if (!mIsSubtitleVisible)
            crimesString = null;
        activity.getSupportActionBar().setSubtitle(crimesString);
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crimes);
        mLinearLayout2 = view.findViewById(R.id.layout2);
        mLinearLayout1 = view.findViewById(R.id.layout1);
        mImageButtonAddCrime=view.findViewById(R.id.image_button_add_crime);

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
//                    mPosition = getAdapterPosition();
//                    Intent intent = new Intent(getActivity(), CrimeDetailActivity.class);
                    //intent.putExtra("CrimeId",mCrime.getId());//send uuid to CrimeDetailActivity and read information from repository in crimeDetailActivity
                    //there is an android convention that  write a method in activity to get extra's
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
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

        private  void  setCrimes(List<Crime> crimes){
            mCrimes= crimes;
        }

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
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");
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
            mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();
        }
        updateSubtitle();

        if (mRepository.getList().size() == 0) {
//            Toast.makeText(getActivity(), "repository is cleared "+mRepository.getList().size(), Toast.LENGTH_SHORT).show();
            mLinearLayout1.setVisibility(View.GONE);
            mLinearLayout2.setVisibility(View.VISIBLE);
        } else if (mRepository.getList().size() != 0) {
            mLinearLayout1.setVisibility(View.VISIBLE);
            mLinearLayout2.setVisibility(View.GONE);
        }
        setClickListener();
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