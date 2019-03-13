package com.project.photoshoot.models;

public class ImageFile {
    String mCategoryname;
    String mDisplayImage;

    public ImageFile(String mCategoryname, String mDisplayImage) {
        this.mCategoryname = mCategoryname;
        this.mDisplayImage = mDisplayImage;
    }

    public String getCategoryname() {
        return mCategoryname;
    }

    public void setCategoryname(String mCategoryname) {
        this.mCategoryname = mCategoryname;
    }

    public String getDisplayImage() {
        return mDisplayImage;
    }

    public void setDisplayImage(String mDisplayImage) {
        this.mDisplayImage = mDisplayImage;
    }

}
