package com.example.mycriminallintent.model;

import com.example.mycriminallintent.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Crime implements Serializable {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    public UUID getId(){return mId;}

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
    public Crime(){
        mId=UUID.randomUUID();
        mDate= DateUtils.getRandomDate(2000, 2020);

    }

    @Override
    public String toString() {
        return "Crime{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mDate=" + mDate +
                ", mSolved=" + mSolved +
                '}';
    }
}
