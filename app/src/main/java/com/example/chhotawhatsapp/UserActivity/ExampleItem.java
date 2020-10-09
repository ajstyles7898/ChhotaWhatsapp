package com.example.chhotawhatsapp.UserActivity;

public class ExampleItem {
    private String mImageBitMap;
    private String mText1;
    private String mText2;
    private String mUid;

    public ExampleItem(String mImageBitMap, String mText1, String mText2, String mUid) {
        this.mImageBitMap = mImageBitMap;
        this.mText1 = mText1;
        this.mText2 = mText2;
        this.mUid = mUid;
    }

    public ExampleItem(){}

    public String getmImageBitMap() {
        return mImageBitMap;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public String getmUid(){ return mUid; }

    public void setmImageBitMap(String mImageBitMap) {
        this.mImageBitMap = mImageBitMap;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }
}


