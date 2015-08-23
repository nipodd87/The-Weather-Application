package com.example.nitinpoddar.theweatherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Util.Utils;
import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;


public class MainActivity extends ActionBarActivity {

    private TextView cityName;
    private TextView temperature;
    private ImageView iconView;
    private TextView wind;
    private TextView cloud;
    private TextView pressure;
    private TextView humidity;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        temperature = (TextView) findViewById(R.id.tempText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        wind = (TextView) findViewById(R.id.windText);
        cloud = (TextView) findViewById(R.id.cloudText);
        pressure = (TextView) findViewById(R.id.pressureText);
        humidity = (TextView) findViewById(R.id.humidText);
        sunrise = (TextView) findViewById(R.id.sunriseText);
        sunset = (TextView) findViewById(R.id.sunsetText);
        updated = (TextView) findViewById(R.id.updated);

        CityPreference cityPreference = new CityPreference(MainActivity.this);
        getWeatherData(cityPreference.getCity());
    }

    public void getWeatherData(String city){

        String[] var = {city};
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(var);
    }

    private class DownloadImageAsycnTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            return downloadImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
        }

        private Bitmap downloadImage(String code){
            final DefaultHttpClient client = new DefaultHttpClient();
            //final HttpGet request = new HttpGet(Utils.ICON_URL + code + ".png");
            final HttpGet request = new HttpGet("http://icons.wxug.com/graphics/wu2/logo_130x80.png");
            try {
                final HttpResponse response = client.execute(request);

                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK){
                    Log.e("Download Error", "Error: " + statusCode);
                    return null;
                }

                final HttpEntity entity = response.getEntity();

                if (entity != null){
                    InputStream inputStream = null;

                    inputStream = entity.getContent();

                    //decode the response
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class WeatherTask extends AsyncTask<String, Void, Weather>{

        @Override
        protected Weather doInBackground(String... params) {
            String data = (new WeatherHttpClient().getWeatherData(params[0]));
            weather = JSONWeatherParser.getWeather(data);
            weather.iconData = weather.currentCondition.getIcon();

            new DownloadImageAsycnTask().execute(weather.iconData);

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            Date sunriseDate = new Date(weather.place.getSunrise() * 1000L);
            Date sunsetDate = new Date(weather.place.getSunset()*1000L);
            Date updateDate = new Date(weather.place.getLastUpdate()*1000L);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());


            cityName.setText(weather.place.getCity());
            temperature.setText(""+tempFormat+"°C");
            wind.setText("Wind: " + weather.wind.getSpeed()+" mps");
            cloud.setText("Description: "+weather.currentCondition.getDescription());
            pressure.setText("Pressure: " + weather.currentCondition.getPressure()+" hPA");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity()+ " %");
            sunrise.setText("Sunrise: " + sdf.format(sunriseDate));
            sunset.setText("Sunset: " + sdf.format(sunsetDate));
            updated.setText("Last Update: "+sdf.format(updateDate));

    //            iconView.setImageResource();

        }
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");
        final EditText cityText = new EditText(MainActivity.this);
        cityText.setInputType(InputType.TYPE_CLASS_TEXT);
        cityText.setHint("Atlanta,US");
        builder.setView(cityText);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              CityPreference pref = new CityPreference(MainActivity.this);
                pref.setCity(cityText.getText().toString());

                String newCity = pref.getCity();
                getWeatherData(newCity);
            }
        });
        builder.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_city) {
         showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }
}
