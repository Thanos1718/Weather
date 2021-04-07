package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
TextView textView;
TextView textView2;
EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        editText=findViewById(R.id.city);
    }
    public void getWeather(View view)
    {
        if(editText.getText().toString()==null)
        {
            textView.setText("Enter a city");
        }
        else {
            try {
                if(editText.getText().toString()==null)
                {
                    textView.setText("Enter a city");
                }
                else {
                    Log.e("city",editText.getText().toString());
                    download task = new download();
                    task.execute("http://api.weatherapi.com/v1/current.json?key=a8eefde89f1d448fb29143339210704&q=" + editText.getText().toString() + "&aqi=no");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class download extends AsyncTask<String,Void,String>
    {
        String result="";
        HttpURLConnection connection;
        URL url;
        @Override
        protected String doInBackground(String... urls) {
            try {
                if(urls.length==0)
                {
                    textView.setText("Enter a Valid city please");
                    return  null;
                }
                else {
                    url = new URL(urls[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = reader.read();
                    }
                    return result;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(s==null)
                {
                    textView.setText("It is not valid");
                }
                else {
                    JSONObject jsonObject = new JSONObject(s);
                    String location = jsonObject.getString("location");
                    String current = jsonObject.getString("current");

                    JSONObject jsonObject1 = new JSONObject(location);
                    JSONObject jsonObject2 = new JSONObject(current);


                    String name = jsonObject1.getString("name");
                    String localTime = jsonObject1.getString("localtime");
                    String temp = jsonObject2.getString("temp_c");
                    String wind = jsonObject2.getString("wind_mph");
                    String humidity = jsonObject2.getString("humidity");
                    String cloud = jsonObject2.getString("cloud");

                    String condition = jsonObject2.getString("condition");
                    JSONObject jsonObject3 = new JSONObject(condition);
                    String state = jsonObject3.getString("text");

                    String ans = "\nWeather Info: " + "  \nname:" + name + "   localtime:" + localTime + "\ntemperature:" + temp + "\nhumidity:" + humidity + "\nState of weather:" + state + "\nclouds:" + cloud + "\nwind speed:" + wind;
                    textView.setText(name);
                    textView2.setText(ans);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}