package com.example.stoycho.traveling.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stoycho on 12/18/2016.
 */

public class Hotel implements Parcelable{

    private int         mId;
    private String      mName;
    private String      mDescription;
    private String      mPhone;
    private String      mEmail;
    private String      mWebsite;
    private String      mAdress;
    private double      mLatitude;
    private double      mLongitude;
    private double      mDistance;
    private String[]    mImages;

    public Hotel(){}

    public Hotel(int mId, String mName, String mDescription, String mPhone, String mEmail, String mWebsite, String mAdress, double mLatitude, double mLongitude, String[] mImages) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mPhone = mPhone;
        this.mEmail = mEmail;
        this.mWebsite = mWebsite;
        this.mAdress = mAdress;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mImages = mImages;
    }

    public Hotel(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.mId = Integer.parseInt(data[0]);
        this.mName = data[1];
        this.mDescription = data[2];
        this.mPhone = data[3];
        this.mEmail = data[4];
        this.mWebsite = data[5];
        this.mAdress = data[6];
        this.mLatitude = Double.parseDouble(data[7]);
        this.mLongitude = Double.parseDouble(data[8]);
        this.mDistance = Double.parseDouble(data[9]);
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmWebsite() {
        return mWebsite;
    }

    public void setmWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }

    public String getmAdress() {
        return mAdress;
    }

    public void setmAdress(String mAdress) {
        this.mAdress = mAdress;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String[] getmImages() {
        return mImages;
    }

    public void setmImages(String[] mImages) {
        this.mImages = mImages;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(mId),mName, mDescription,
        mPhone,mEmail, mWebsite,mAdress, String.valueOf(mLatitude), String.valueOf(mLongitude), String.valueOf(mDistance)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };
}
