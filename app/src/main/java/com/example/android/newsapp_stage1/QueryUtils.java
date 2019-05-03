package com.example.android.newsapp_stage1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp_stage1.Constants.*;

/**
 * Helper methods related to requesting and receiving articles data from guardian_apis.
 */
public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Article> fetchArticleData(String requestUrl) {

        //create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem creating URL");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if the url is null, return empty string
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Article> extractFeatureFromJson(String articleJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Article> articles = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(articleJson);
            JSONObject jsonObject = baseJsonResponse.getJSONObject(KEY_RESPONSE);

            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleArticle = jsonArray.getJSONObject(i);

                String title = singleArticle.getString(KEY_WEB_TITLE);
                String section = singleArticle.getString(KEY_SECTION_NAME);
                String date = singleArticle.getString(KEY_WEB_PUBLICATION_DATE).substring(0, 10);
                String url = singleArticle.getString(KEY_WEB_URL);

                JSONArray tagsArray = singleArticle.getJSONArray(KEY_TAGS);
                String author = tagsArray.getJSONObject(0).getString(KEY_AUTHOR);

                Article article = new Article(title, section, author, date, url);
                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the article JSON results", e);
        }

        return articles;
    }
}
