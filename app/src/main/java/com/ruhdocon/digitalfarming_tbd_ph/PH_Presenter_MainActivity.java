package com.ruhdocon.digitalfarming_tbd_ph;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PH_Presenter_MainActivity extends AppCompatActivity {

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

        Toolbar toolbar = findViewById(R.id.navigation_supportBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getPhValue();
    }

}
