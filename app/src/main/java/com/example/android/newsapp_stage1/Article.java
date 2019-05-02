package com.example.android.newsapp_stage1;

public class Article {

    private String mTitle;
    private String mSection;
    private String mDate;
    private String mUrl;

    /**
     * @param mTitle   title of the article
     * @param mSection section of the article
     * @param mDate    date of the article
     * @param mUrl     URL of the article
     */
    public Article(String mTitle, String mSection, String mDate, String mUrl) {
        this.mTitle = mTitle;
        this.mSection = mSection;
        this.mDate = mDate;
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    @Override
    public String toString() {
        return "Article{" +
                "mTitle='" + mTitle + '\'' +
                ", mSection='" + mSection + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}