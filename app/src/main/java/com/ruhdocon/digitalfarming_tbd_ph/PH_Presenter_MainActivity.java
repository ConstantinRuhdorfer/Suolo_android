package com.ruhdocon.digitalfarming_tbd_ph;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PH_Presenter_MainActivity extends AppCompatActivity implements Analysis.OnFragmentInteractionListener, Recent.OnFragmentInteractionListener, Tips.OnFragmentInteractionListener {

    private ActionBar toolbar;

    public void getPhValue() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                List<Double> ph_Values = new ArrayList<Double>();

                URL phValues_EndPoint = null;
                try {
                    phValues_EndPoint = new URL("http://192.168.33.155:8080/PHvalues");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    assert phValues_EndPoint != null;
                    HttpURLConnection phValues_connection = (HttpURLConnection) phValues_EndPoint.openConnection();
                    phValues_connection.setRequestProperty("Accept", "application/JSON");
                    phValues_connection.setRequestMethod("GET");

                    if (phValues_connection.getResponseCode() == 200) {
                        InputStream responseBody = phValues_connection.getInputStream();
                        InputStreamReader phValues_responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(phValues_responseBodyReader);

                        jsonReader.beginObject();

                        Log.i("JSON PH", jsonReader.toString());

                        /*
                        while (jsonReader.hasNext()) {
                            String key = jsonReader.nextName();
                            if (key.equals("_embedded")) {
                                jsonReader.beginObject();
                                key = jsonReader.nextName();
                                if (key.equals("PHvalues")) {
                                    jsonReader.beginArray();
                                    while (jsonReader.hasNext()) {
                                        ph_Values.add(jsonReader.nextDouble());
                                    }
                                    jsonReader.endArray();

                                    for( Double d: ph_Values ) {
                                        Log.i("VALUES", d.toString());
                                    }
                                    Log.i("length_phValues", "" + ph_Values.size() );

                            }
                                break;
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        */

                        jsonReader.close();
                        phValues_connection.disconnect();
                    } else {
                        Log.i("PHVALUE", "ERROR");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ph__presenter__main);

        loadFragment(new Recent());

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        getPhValue();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.tab_analysis:
                    fragment = new Analysis();
                    loadFragment(fragment);
                    return true;
                case R.id.tab_info:
                    fragment = new Tips();
                    loadFragment(fragment);
                    return true;
                case R.id.tab_recent:
                    fragment = new Recent();
                    loadFragment(fragment);
                    return true;
                }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
