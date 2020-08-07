package com.example.mycriminallintent.repository;

import com.example.mycriminallintent.model.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeRepository implements IRepository<Crime> {
    private static final int NUMBER_OF_CRIMES = 5;
    private static CrimeRepository sCrimeRepository;
    private List<Crime> mCrimes;
    private int position;

    public static CrimeRepository getInstance() {
        if (sCrimeRepository == null)
            sCrimeRepository = new CrimeRepository();
        return sCrimeRepository;
    }

    private CrimeRepository() {
        mCrimes = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CRIMES; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime#" + (i + 1));
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }

    @Override
    public List<Crime> getList() {
        return mCrimes;
    }

    @Override
    public Crime get(UUID uuid) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(uuid))
                return crime;
        }
        return null;
    }

    @Override
    public void setList(List<Crime> crimes) {
        mCrimes = crimes;
    }

    @Override
    public synchronized void update(Crime crime) {//synchronized tell s you that if two component want to update repository this process can't do at the same time > threat subject
        Crime updateCrime = get(crime.getId());
        updateCrime.setTitle(crime.getTitle());
        updateCrime.setDate(crime.getDate());
        updateCrime.setSolved(crime.isSolved());

    }

    @Override
    public void delete(Crime crime) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crime.getId())) {
                mCrimes.remove(i);
                return;
            }
        }

    }

    @Override
    public void insert(Crime crime) {
        mCrimes.add(crime);
    }

    @Override
    public void insertList(List<Crime> crimes) {
        mCrimes.addAll(crimes);
    }

    @Override
    public int getPosition(UUID uuid) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(uuid))
                return i;
        }
        return -1;
    }
}
