package com.example.mycriminallintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.mycriminallintent.R;
import com.example.mycriminallintent.model.Crime;
import com.example.mycriminallintent.repository.CrimeDBRepository;
import com.example.mycriminallintent.repository.IRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String BUNDLE_CRIME = "BundleCrime";
    public static final String ARG_CRIME_ID = "CrimeId";
    public static final String DATE_DIALOG_FRAGMENT_TAG = "DialogDate";
    public static final String TIMER_DIALOG_FRAGMENT_TAG = "DialogTimer";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    public static final int REQUEST_CODE_SELECT_CONTACT = 2;

    private EditText mEditTextCrimeTitle;
    private Button mButtonDate;
    private Button mButtonTime;
    private CheckBox mCheckBoxSolved;
    private Button mButtonSuspect;
    private Button mButtonShareReport;
    private Button mButtonDialSuspect;
    private Button mButtonCallSuspect;

    private IRepository<Crime> mRepository;
    private Crime mCrime;

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = CrimeDBRepository.getInstance(getActivity());
//        mRepository = CrimeRepository.getInstance();
        /**
         get extra crimeId that sent from CrimeListFragment
         this is very very bad Wrong we didn't observe(Regrading-comply) Single responsibility
         this fragment maybe use another activity . it should not dependent on CrimeDetailActivity
         fragment never should be dependent on one Activity
         this fragment isn't reusable
         activity should read extras  and send to fragment
         */
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeDetailActivity.EXTRA_CRIME_ID);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
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
        setListeners();

        initViews();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_CRIME, mCrime);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_crime_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                deleteCrime(mCrime);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCrime(Crime crime) {
//        IRepository repository = CrimeRepository.getInstance();
        IRepository repository = CrimeDBRepository.getInstance(getActivity());

        repository.delete(crime);
        getActivity().finish();


    }

    private void findViews(View view) {
        mEditTextCrimeTitle = view.findViewById(R.id.crime_title);
        mButtonDate = view.findViewById(R.id.crime_date);
        mButtonTime = view.findViewById(R.id.crime_time);
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);
        mButtonSuspect = view.findViewById(R.id.select_suspect);
        mButtonShareReport = view.findViewById(R.id.share_report);
        mButtonDialSuspect = view.findViewById(R.id.dial_suspect);
        mButtonCallSuspect = view.findViewById(R.id.call_report);
    }

    private void initViews() {
        mEditTextCrimeTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(mCrime.getDate()));
        mButtonTime.setText(new SimpleDateFormat("HH:mm:ss").format(mCrime.getDate()));

        if (mCrime.getSuspect() != null)
            mButtonSuspect.setText(mCrime.getSuspect());
        if (mCrime.getSuspectPhoneNumber() != null){
            mButtonDialSuspect.setText("Dial "+mCrime.getSuspectPhoneNumber());
            mButtonCallSuspect.setText("Call "+mCrime.getSuspectPhoneNumber());
        }


    }


    /**
     * One the best way to save object Automatically is "OnPause" 100%  safe
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
                mCrime.setTitle(mEditTextCrimeTitle.getText().toString());
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
                datePickerFragment.setTargetFragment(CrimeDetailFragment.this, REQUEST_CODE_DATE_PICKER);

                datePickerFragment.show(getFragmentManager(), DATE_DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeDetailFragment.this, REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getFragmentManager(), TIMER_DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonShareReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, getReportText());
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                sendIntent.setType("text/plain");
//
//                Intent shareIntent = Intent.createChooser(sendIntent, null);
//                if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null)
//                    startActivity(shareIntent);

                    String mimeType = "text/plain";
                    Intent shareIntent =   ShareCompat.IntentBuilder.from(getActivity())
                            .setType(mimeType)
                            .setText(getReportText())
                            .getIntent();
                    if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null){
                        startActivity(shareIntent);
                    }

            }
        });

        mButtonSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK);
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (pickContactIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivityForResult(pickContactIntent, REQUEST_CODE_SELECT_CONTACT);
            }
        });
        mButtonCallSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String uri = "tel:" + mCrime.getSuspectPhoneNumber().trim() ;
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse(uri));

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(mCrime.getSuspectPhoneNumber().trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(callIntent);

                Intent shareIntent = Intent.createChooser(callIntent, null);
                if (callIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(shareIntent);
//                startActivity(intent);
            }
        });
        mButtonDialSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + mCrime.getSuspectPhoneNumber().trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                Intent shareIntent = Intent.createChooser(intent, null);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(shareIntent);
//                startActivity(intent);
            }

        });
    }
    private String getReportText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = simpleDateFormat.format(mCrime.getDate()) ;

        String solvedString = mCrime.isSolved() ?
                getString(R.string.crime_report_solved) :
                getString(R.string.crime_report_unsolved);

        String suspectString = mCrime.getSuspect() == null ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect, mCrime.getSuspect());

        String report = getString(R.string.crime_report,
                mCrime.getTitle(),
                dateString,
                solvedString,
                suspectString);

        return report;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            //get response from intent extra, which is user selected date
            Date userSelectedDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            mCrime.setDate(userSelectedDate);
            mButtonDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(mCrime.getDate()));

            updateCrime();
        }
        else if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Date userSelectedDate = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            mCrime.setDate(userSelectedDate);
            mButtonTime.setText(new SimpleDateFormat("HH:mm:ss").format(mCrime.getDate()));

            updateCrime();
        }else if (requestCode == REQUEST_CODE_SELECT_CONTACT) {
            Uri contactUri = data.getData();

            String[] columns = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getActivity().getContentResolver().query(contactUri,
                    columns,
                    null,
                    null,
                    null);

            if (cursor == null || cursor.getCount() == 0)
                return;

            try {
                cursor.moveToFirst();

                String suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String suspectPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                mCrime.setSuspectPhoneNumber(suspectPhoneNumber);
                mCrime.setSuspect(suspect);
                updateCrime();
                initViews();

                mButtonSuspect.setText(mCrime.getSuspect());
            } finally {
                cursor.close();
            }
        }


    }
}