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
import android.view.MenuItem;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ph__presenter__main);

        loadFragment(new Recent());

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fillViewWithData();
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
    public void onFragmentInteraction(Uri uri) { }

    private class ph_valuesTask extends AsyncTask<Void, Void, List<Float>> {
        @Override
        protected List<Float> doInBackground(Void... params) {

            List<Float> ph_Values = new ArrayList<>();

            URL phValues_EndPoint = null;
            try {
                phValues_EndPoint = new URL("http://192.168.33.155:8080/Ph_value");
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

                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("_embedded")) {
                            jsonReader.beginObject();
                            key = jsonReader.nextName();
                            if (key.equals("Ph_value")) {
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        key = jsonReader.nextName();
                                        if (key.equals("toxic")) {
                                            ph_Values.add((float) jsonReader.nextDouble());
                                        } else if (key.equals("_links")) {

                                            jsonReader.beginObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.endObject();

                                        } else if (key.equals("date")) {
                                            jsonReader.nextString();
                                            // TODO USE THE DATE
                                        }
                                    }
                                    jsonReader.endObject();
                                }
                                jsonReader.endArray();
                            }
                            break;
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.close();
                    phValues_connection.disconnect();
                } else {
                    // TODO TELL FRAGMENT THERE IS NO PH VALUE
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ph_Values;
        }

        @Override
        protected void onPostExecute(List<Float> result) {
            super.onPostExecute(result);
            processPh_values(result);
        }

    }

    private class temperature_valuesTask extends AsyncTask<Void, Void, List<Long>> {
        @Override
        protected List<Long> doInBackground(Void... params) {

            List<Long> temperature_values = new ArrayList<Long>();

            URL phValues_EndPoint = null;
            try {
                phValues_EndPoint = new URL("http://192.168.33.155:8080/Temperature_value");
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

                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("_embedded")) {
                            jsonReader.beginObject();
                            key = jsonReader.nextName();
                            if (key.equals("Temperature_value")) {
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {


                                    jsonReader.beginObject();
                                    while(jsonReader.hasNext()) {
                                        key = jsonReader.nextName();
                                        if (key.equals("temperature")) {
                                            temperature_values.add(jsonReader.nextLong());
                                        } else if (key.equals("_links")) {
                                            jsonReader.beginObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.endObject();
                                        } else if (key.equals("date")) {
                                            // TODO DEAL WITH THE DATE
                                            jsonReader.nextString();
                                        }
                                    }
                                    jsonReader.endObject();


                                }
                                jsonReader.endArray();
                            }
                            break;
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.close();
                    phValues_connection.disconnect();
                } else {
                    // TODO TELL THE FRAGMENT THERE IS NO INFO
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return temperature_values;
        }

        @Override
        protected void onPostExecute(List<Long> result) {
            super.onPostExecute(result);
            processTemperature_values(result);
        }

    }

    private class airpressure_valuesTask extends AsyncTask<Void, Void, List<Long>> {
        @Override
        protected List<Long> doInBackground(Void... params) {

            List<Long> airpressure_values = new ArrayList<Long>();

            URL phValues_EndPoint = null;
            try {
                phValues_EndPoint = new URL("http://192.168.33.155:8080/Airpressure_value");
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

                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("_embedded")) {
                            jsonReader.beginObject();
                            key = jsonReader.nextName();
                            if (key.equals("Airpressure_value")) {
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {


                                    jsonReader.beginObject();
                                    while(jsonReader.hasNext()) {
                                        key = jsonReader.nextName();
                                        if (key.equals("pressure")) {
                                            airpressure_values.add(jsonReader.nextLong());
                                        } else if (key.equals("_links")) {
                                            jsonReader.beginObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.endObject();
                                        } else if (key.equals("date")) {
                                            // TODO DEAL WITH THE DATE
                                            jsonReader.nextString();
                                        }
                                    }
                                    jsonReader.endObject();


                                }
                                jsonReader.endArray();
                            }
                            break;
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.close();
                    phValues_connection.disconnect();
                } else {
                    // TODO TELL THE FRAGMENT THERE IS NO INFO
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return airpressure_values;
        }

        @Override
        protected void onPostExecute(List<Long> result) {
            super.onPostExecute(result);
            processAirpressure_values(result);
        }

    }

    private class humidity_valuesTask extends AsyncTask<Void, Void, List<Long>> {
        @Override
        protected List<Long> doInBackground(Void... params) {

            List<Long> humidity_values = new ArrayList<Long>();

            URL phValues_EndPoint = null;
            try {
                phValues_EndPoint = new URL("http://192.168.33.155:8080/Humidity_value");
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

                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("_embedded")) {
                            jsonReader.beginObject();
                            key = jsonReader.nextName();
                            if (key.equals("Humidity_value")) {
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {


                                    jsonReader.beginObject();
                                    while(jsonReader.hasNext()) {
                                        key = jsonReader.nextName();
                                        if (key.equals("humidity")) {
                                            humidity_values.add(jsonReader.nextLong());
                                        } else if (key.equals("_links")) {
                                            jsonReader.beginObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.endObject();
                                        } else if (key.equals("date")) {
                                            // TODO DEAL WITH THE DATE
                                            jsonReader.nextString();
                                        }
                                    }
                                    jsonReader.endObject();


                                }
                                jsonReader.endArray();
                            }
                            break;
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.close();
                    phValues_connection.disconnect();
                } else {
                    // TODO TELL THE FRAGMENT THERE IS NO INFO
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return humidity_values;
        }

        @Override
        protected void onPostExecute(List<Long> result) {
            super.onPostExecute(result);
            processHumidity_values(result);
        }

    }

    private class light_valuesTask extends AsyncTask<Void, Void, List<Long>> {
        @Override
        protected List<Long> doInBackground(Void... params) {

            List<Long> light_values = new ArrayList<Long>();

            URL phValues_EndPoint = null;
            try {
                phValues_EndPoint = new URL("http://192.168.33.155:8080/Light_value");
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

                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("_embedded")) {
                            jsonReader.beginObject();
                            key = jsonReader.nextName();
                            if (key.equals("Humidity_value")) {
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {


                                    jsonReader.beginObject();
                                    while(jsonReader.hasNext()) {
                                        key = jsonReader.nextName();
                                        if (key.equals("lux")) {
                                            light_values.add(jsonReader.nextLong());
                                        } else if (key.equals("_links")) {
                                            jsonReader.beginObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.nextName();
                                            jsonReader.beginObject();
                                            jsonReader.nextName();
                                            jsonReader.nextString();
                                            jsonReader.endObject();

                                            jsonReader.endObject();
                                        } else if (key.equals("date")) {
                                            // TODO DEAL WITH THE DATE
                                            jsonReader.nextString();
                                        }
                                    }
                                    jsonReader.endObject();


                                }
                                jsonReader.endArray();
                            }
                            break;
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.close();
                    phValues_connection.disconnect();
                } else {
                    // TODO TELL THE FRAGMENT THERE IS NO INFO
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return light_values;
        }

        @Override
        protected void onPostExecute(List<Long> result) {
            super.onPostExecute(result);
            processLight_values(result);
        }

    }

    private void processLight_values(List<Long> result) {
        TextView light_Value_TextView = findViewById(R.id.temp_info);
        light_Value_TextView.setText( result.get(result.size() - 1).toString() );
    }

    private void processHumidity_values(List<Long> result) {
        TextView humidity_Value_TextView = findViewById(R.id.temp_info);
        humidity_Value_TextView.setText( result.get(result.size() - 1).toString() );
    }

    private void processAirpressure_values(List<Long> result) {
        TextView airpressure_Value_TextView = findViewById(R.id.temp_info);
        airpressure_Value_TextView.setText( result.get(result.size() - 1).toString() );
    }

    private void processTemperature_values(List<Long> result) {
        TextView temperature_Value_TextView = findViewById(R.id.temp_info);
        temperature_Value_TextView.setText( result.get(result.size() - 1).toString() );
    }

    private void processPh_values(List<Float> result) {
        // TODO CHECK THAT
        TextView PH_Value_TextView = (TextView)findViewById(R.id.Ph_Value);
        PH_Value_TextView.setText( result.get(result.size() - 1).toString() );
    }

    private void fillViewWithData() {
        new ph_valuesTask().execute();
        new temperature_valuesTask().execute();
        new airpressure_valuesTask().execute();
        new humidity_valuesTask().execute();
        new light_valuesTask().execute();
    }
}
