package com.mc1.dev.goapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// ----------------------------------------------------------------------
// class GameListArrayAdapter
// This class provides an implementation of an ArrayAdapter suited for
// displaying two rows of text and an image in a list view.
// ----------------------------------------------------------------------
public class GameListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values1stLine;
    private final ArrayList<String> values2ndLine;
    private final Bitmap[] images;

    public GameListArrayAdapter(Context context, ArrayList<String> values1stLine, ArrayList<String> values2ndLine, Bitmap[] img) {
        super(context, R.layout.listview_layout, values1stLine);
        this.context = context;
        this.values1stLine = values1stLine;
        this.values2ndLine = values2ndLine;
        this.images = img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // This implementation is potentially very inefficient and needs to be replaced by the
        // view holder pattern in the future.
        View rowView = inflater.inflate(R.layout.listview_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.gameImage);
        textView.setText(values1stLine.get(position));
        textView2.setText(values2ndLine.get(position));

        imageView.setImageBitmap(images[position]);

        return rowView;
    }
}