package com.zacharysweigart.phunweather.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zacharysweigart.phunweather.R;
import com.zacharysweigart.phunweather.activity.WeatherDetailsActivity;

import java.util.ArrayList;

/**
 * This class provides an adapter which will create views for a list of zip code strings
 *
 * @author Zachary Sweigart
 */
public class ZipCodeListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> zipCodes;

    public ZipCodeListAdapter(Context context, ArrayList<String> zipCodes) {
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
            textView.setText(zipCodes.get(position));

            ViewHolder holder = new ViewHolder();
            holder.text = (TextView) rowView.findViewById(R.id.zipCodeTextView);
            rowView.setTag(holder);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WeatherDetailsActivity.class);
                intent.putExtra(context.getString(R.string.zipcode), zipCodes.get(position));
                context.startActivity(intent);
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Zip Code")
                        .setMessage("Are you sure you want to delete " + zipCodes.get(position))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                zipCodes.remove(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
                return true;
            }
        });

        return rowView;
    }

    static class ViewHolder {
        TextView text;
    }
}
