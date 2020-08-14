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


public Crime() {
    this(UUID.randomUUID());
//        mDate = DateUtils.getRandomDate(2000, 2020); //random date between 2000 to 2020
}
    public Crime(UUID uuid) {
        mId = uuid;
        mDate= DateUtils.getRandomDate(2000, 2020);
//        mTitle="set crime";
    }


    public Crime(UUID id, String title, Date date, boolean solved) {
        mId = id;
        mTitle = title;
        mDate = date;
        mSolved = solved;
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
