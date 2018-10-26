  package com.example.bharat.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

  public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;
    Button  findWeatherButton;

    public void findWeather(View view){
        Log.i("city Name: ", cityName.getText().toString());

        DownloadTask task = new DownloadTask();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + ",uk&appid=b6907d289e10d714a6e88b30761fae22");

    }

    public class DownloadTask extends AsyncTask<String, Void, String>{

          @Override
          protected String doInBackground(String... urls) {

              String result = "";
              URL url ;
              HttpURLConnection urlConnection = null;

              try {
                  url = new URL(urls[0]);

                  urlConnection = (HttpURLConnection) url.openConnection();

                  InputStream inputStream = urlConnection.getInputStream();

                  InputStreamReader reader = new InputStreamReader(inputStream);

                  int data = reader.read();

                  while (data != -1){
                      char current = (char) data;

                      result += current;
                      data=reader.read();
                  }
                  return result;

              } catch (MalformedURLException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              return "";
          }

          @Override
          protected void onPostExecute(String result) {
              super.onPostExecute(result);

              try {

                  String message = "";

                  if(result != null){

                  JSONObject jsonObject = new JSONObject(result);
                  String weatherInfo = jsonObject.getString("weather");

                  Log.i("weather content: ", weatherInfo);

                  JSONArray arr = new JSONArray(weatherInfo);

                  for (int i=0 ; i<arr.length(); i++){

                      JSONObject jsonPart = arr.getJSONObject(i);

                      String main="";
                      String description="";

                      main =jsonPart.getString("main");
                      description = jsonPart.getString("description");

                      if(main != "" && description != ""){

                          message += main+ ": " + description + "\r\n";

                      }
                  }
                  if( message != ""){
                      resultTextView.setText(message);
                  }

                  }
                  else
                  {
                      Toast.makeText(getApplicationContext(), "Wrong City name Entered or Empty City Name",Toast.LENGTH_SHORT).show();
                  }

              } catch (JSONException e) {
                  e.printStackTrace();
              }


          }
      }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText)findViewById(R.id.cityName);

        resultTextView = (TextView)findViewById(R.id.resultTextView);

        findWeatherButton = (Button)findViewById(R.id.findWeatherButton);
    }



  }
