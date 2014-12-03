package com.zacharysweigart.phunweather.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.DialogInterface;
import android.widget.Toast;

import com.zacharysweigart.phunweather.R;
import com.zacharysweigart.phunweather.adapter.ZipCodeListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class represents the main list activity which will display zip codes for which teh user can
 * get weather details
 *
 * @author Zachary Sweigart
 */
public class ZipCodeListActivity extends ListActivity {
    private ArrayList<String> zipcodeList;
    private SharedPreferences sharedPreferences;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_code_list);

        zipcodeList = new ArrayList<String>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String zipCodeJson = sharedPreferences.getString(getString(R.string.zipcodelist), null);
        if(zipCodeJson == null) {
            zipcodeList = createDefaultZipCodeList();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(zipCodeJson);
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.zipcodelist));
                for(int i = 0; i < jsonArray.length(); i++) {
                    zipcodeList.add(jsonArray.getString(i));
                }
            } catch (JSONException ex) {
                zipcodeList = createDefaultZipCodeList();
            }
        }

        setListAdapter(new ZipCodeListAdapter(this, zipcodeList.toArray(new String[zipcodeList.size()])));

        setupButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(getString(R.string.zipcodelist), createZipCodeListString(zipcodeList));
        sharedPrefEditor.commit();
    }

    private void setupButtons() {
        addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(ZipCodeListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                new AlertDialog.Builder(ZipCodeListActivity.this)
                        .setTitle("Add Zip Code")
                        .setMessage("Add a new zip code to your list")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Editable value = input.getText();
                                if (value.length() > 5 || value.length() < 5) {
                                    Toast.makeText(ZipCodeListActivity.this, "Enter a 5-digit zip code", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        Integer.parseInt(value.toString());
                                        zipcodeList.add(value.toString());
                                        setListAdapter(new ZipCodeListAdapter(ZipCodeListActivity.this,
                                                zipcodeList.toArray(new String[zipcodeList.size()])));
                                        ((ArrayAdapter) ZipCodeListActivity.this.getListView().getAdapter()).notifyDataSetChanged();
                                    } catch (NumberFormatException ex) {
                                        Toast.makeText(ZipCodeListActivity.this, "Enter a 5-digit zip code", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
            }
        });
    }

    private ArrayList<String> createDefaultZipCodeList() {
        ArrayList<String> defaultZipCodes = new ArrayList<String>();
        defaultZipCodes.add("78757");
        defaultZipCodes.add("78630");
        defaultZipCodes.add("78641");
        return defaultZipCodes;
    }

    private String createZipCodeListString(ArrayList<String> zipcodeList) {
        JSONArray jsonArray = new JSONArray();
        for(String zipCode : zipcodeList) {
            jsonArray.put(zipCode);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(getString(R.string.zipcodelist), jsonArray);
        } catch (JSONException ex) {
            return null;
        }

        return jsonObject.toString();
    }
}
