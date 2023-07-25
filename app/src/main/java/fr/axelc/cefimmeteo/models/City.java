package fr.axelc.cefimmeteo.models;

import static fr.axelc.cefimmeteo.utils.Util.setWeatherIcon;

import org.json.JSONException;
import org.json.JSONObject;

import fr.axelc.cefimmeteo.utils.Util;

public class City {
    private String mName;
    private String mDescription;
    private String mTemperature;
    private int mWeatherIcon;
    private int midCity;
    private double mLatitude;
    private double mLongitude;
    private int mWeatherResIconWhite;
    private int mWeatherResIconGrey;


    public City(String mName, String mDescription, String mTemperature, int mWeatherIcon) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mTemperature = mTemperature;
        this.mWeatherIcon = mWeatherIcon;
    }

    public City(String stringJson) throws JSONException {
        JSONObject json = new JSONObject(stringJson);
        JSONObject main = json.getJSONObject("main");
        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
        int actualId = json.getJSONArray("weather").getJSONObject(0).getInt("id");
        long sunrise = json.getJSONObject("sys").getLong("sunrise");
        long sunset = json.getJSONObject("sys").getLong("sunset");
        this.mWeatherIcon = setWeatherIcon(actualId,sunrise,sunset);
        mName = json.getString("name");
        mDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");
        mTemperature = String.format("%.0f", main.getDouble("temp")) + " ℃";
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmTemperature() {
        return mTemperature;
    }

    public int getmWeatherIcon() {
        return mWeatherIcon;
    }
}
