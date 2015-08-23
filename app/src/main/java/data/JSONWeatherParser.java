package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by nitinpoddar on 8/20/15.
 */
public class JSONWeatherParser {

    public static Weather getWeather(String data){
        Weather weather = new Weather();

        //create json object data

        try {
            JSONObject jsonObject = new JSONObject(data);
            Place place = new Place();

            //get data for place class

            JSONObject coord = Utils.getObject("coord", jsonObject);
            place.setLat(Utils.getFloat("lat", coord));
            place.setLon(Utils.getFloat("lon", coord));

            JSONObject sys = Utils.getObject("sys", jsonObject);
            place.setSunrise(Utils.getLong("sunrise", sys));
            place.setSunset(Utils.getLong("sunset", sys));
            place.setCountry(Utils.getString("country", sys));
            place.setCity(Utils.getString("name", jsonObject));
            place.setLastUpdate(Utils.getLong("dt", jsonObject));
            weather.place = place;

            //get data for current condition

            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));

            JSONObject main = Utils.getObject("main", jsonObject);
            weather.currentCondition.setTemperature(Utils.getDouble("temp", main));
            weather.currentCondition.setPressure(Utils.getFloat("pressure", main));
            weather.currentCondition.setHumidity(Utils.getFloat("humidity", main));
            weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", main));
            weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", main));

            //get data for wind
            JSONObject wind = Utils.getObject("wind", jsonObject);
            weather.wind.setDeg(Utils.getFloat("deg", wind));
            weather.wind.setSpeed(Utils.getFloat("speed", wind));

            //get data for cloud
            JSONObject cloud = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPercipitaion(Utils.getInt("all", cloud));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
