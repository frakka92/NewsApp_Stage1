package com.example.android.newsapp_stage1;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String REQUEST_URL = "https://content.guardianapis.com/search?api-key=12b62687-af53-4c14-a6c9-93260e94e4b0";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /**
     * Adapter for the list of earthquakes
     */
    private ArticleAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (!isConnected) {
            ProgressBar progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.GONE);

            mEmptyStateTextView = findViewById(R.id.empty);
            mEmptyStateTextView.setText(getString(R.string.no_internet_connection));

        } else {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = findViewById(R.id.list_article);

            mEmptyStateTextView = findViewById(R.id.empty);
            earthquakeListView.setEmptyView(mEmptyStateTextView);

            // Create a new {@link ArrayAdapter} of earthquakes
            mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(mAdapter);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Article currentEarthquake = mAdapter.getItem(position);

                    String url = currentEarthquake.getmUrl();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        mEmptyStateTextView.setText(getString(R.string.no_articles_found));

        mAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}
