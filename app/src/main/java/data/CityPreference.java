package data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nitinpoddar on 8/21/15.
 */
public class CityPreference {
    SharedPreferences prefs;
    public CityPreference(Activity activity){
         prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return prefs.getString("city", "Kolkata,IN");
    }

    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
