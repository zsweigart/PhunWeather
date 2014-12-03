package com.zacharysweigart.phunweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zacharysweigart.phunweather.R;
import com.zacharysweigart.phunweather.activity.WeatherDetailsActivity;

/**
 * This class provides an adapter which will create views for a list of zip code strings
 *
 * @author Zachary Sweigart
 */
public class ZipCodeListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] zipCodes;

    public ZipCodeListAdapter(Context context, String[] zipCodes) {
        super(context, R.layout.list_item_zip_code, zipCodes);
        this.context = context;
        this.zipCodes = zipCodes;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_zip_code, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.zipCodeTextView);
            textView.setText(zipCodes[position]);

            ViewHolder holder = new ViewHolder();
            holder.text = (TextView) rowView.findViewById(R.id.zipCodeTextView);
            rowView.setTag(holder);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WeatherDetailsActivity.class);
                intent.putExtra(context.getString(R.string.zipcode), zipCodes[position]);
                context.startActivity(intent);
            }
        });

        return rowView;
    }

    static class ViewHolder {
        TextView text;
    }
}
