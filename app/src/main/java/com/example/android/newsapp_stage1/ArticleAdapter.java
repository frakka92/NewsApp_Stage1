package com.example.android.newsapp_stage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public static class ViewHolder {
        TextView titleViewHolder, sectionViewHolder, dateViewHolder;
    }

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context  The current context. Used to inflate the layout file.
     * @param articles A List of articles objects to display in a list
     */
    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.article_item_list, parent, false);
            ViewHolder holder = new ViewHolder();

            holder.titleViewHolder = listView.findViewById(R.id.article_title);
            holder.sectionViewHolder = listView.findViewById(R.id.article_section);
            holder.dateViewHolder = listView.findViewById(R.id.article_date);

            listView.setTag(holder);
        }
        //I get the current article

        Article currentArticle = getItem(position);

        ViewHolder viewHolder = (ViewHolder) listView.getTag();

        viewHolder.titleViewHolder.setText(currentArticle.getmTitle());
        viewHolder.sectionViewHolder.setText(currentArticle.getmSection());
        viewHolder.dateViewHolder.setText(currentArticle.getmDate());


        return listView;
    }
}
