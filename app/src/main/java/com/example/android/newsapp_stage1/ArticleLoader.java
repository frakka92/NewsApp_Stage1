package com.example.android.newsapp_stage1;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /**
     * Query Url
     */
    private String mUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param url    to load data from
     */

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null)
            return null;

        //Perform network request,parse the response, and extract a list of articles
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);

        return articles;
    }
}
