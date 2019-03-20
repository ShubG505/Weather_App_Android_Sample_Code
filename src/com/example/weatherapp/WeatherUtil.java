package com.example.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * Class to Send the request to the URL and parse the received response
 *
 */
public class WeatherUtil {
	private static final String WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

	// This key has to generate and replace with own key ID
	private static final String WEATHER_MAP_API = "4a2b4e79b12037a4a9e1fbc4c4e88b90";
	
	private static final String WEATHER= "weather";

	private static final String NAME = "name";

	private static final String SYS = "sys";

	private static final String MAIN = "main";

	private static final String COUNTRY = "country";

	private static final String DESCRIPTION = "description";

	private static final String TEMP = "temp";

	private static final String HUMIDITY = "humidity";

	private static final String PRESSURE = "pressure";

	private static final String SUNRISE = "sunrise";

	private static final String SUNSET = "sunset";
	
	private static final String ID = "id";

	public static String setWeatherIcon(int actualId, long sunrise, long sunset) {
		int id = actualId / 100;
		String icon = "";
		if (actualId == 800) {
			long currentTime = new Date().getTime();
			if (currentTime >= sunrise && currentTime < sunset) {
				icon = "&#xf00d;";
			} else {
				icon = "&#xf02e;";
			}
		} else {
			switch (id) {
			case 2:
				icon = "&#xf01e;";
				break;
			case 3:
				icon = "&#xf01c;";
				break;
			case 7:
				icon = "&#xf014;";
				break;
			case 8:
				icon = "&#xf013;";
				break;
			case 6:
				icon = "&#xf01b;";
				break;
			case 5:
				icon = "&#xf019;";
				break;
			}
		}
		return icon;
	}

	public interface JSOnASyncResponse {

		void processFinish(String output1, String output2, String output3, String output4, String output5,
				String output6, String output7, String output8);
	}

	public static class jSONParseTask extends AsyncTask<String, Void, JSONObject> {

		public JSOnASyncResponse delegate = null;

		public jSONParseTask(JSOnASyncResponse asyncResponse) {
			delegate = asyncResponse;
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			JSONObject jsonWeather = null;
			try {
				jsonWeather = getWeatherJSON(params[0], params[1]);
			} catch (Exception e) {
				Log.d("Error", "Cannot process JSON results", e);
			}

			return jsonWeather;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null) {
					JSONObject details = json.getJSONArray(WEATHER).getJSONObject(0);
					JSONObject main = json.getJSONObject(MAIN);
					DateFormat df = DateFormat.getDateTimeInstance();

					String city = json.getString(NAME).toUpperCase(Locale.US) + ", "
							+ json.getJSONObject(SYS).getString(COUNTRY);
					String description = details.getString(DESCRIPTION).toUpperCase(Locale.US);
					String temperature = String.format("%.2f", main.getDouble(TEMP)) + "°";
					String humidity = main.getString(HUMIDITY) + "%";
					String pressure = main.getString(PRESSURE) + " hPa";
					String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
					String iconText = setWeatherIcon(details.getInt(ID),
							json.getJSONObject(SYS).getLong(SUNRISE) * 1000,
							json.getJSONObject(SYS).getLong(SUNSET) * 1000);

					delegate.processFinish(city, description, temperature, humidity, pressure, updatedOn, iconText,
							"" + (json.getJSONObject(SYS).getLong(SUNRISE) * 1000));

				}
			} catch (JSONException e) {
				// Log.e(LOG_TAG, "Cannot process JSON results", e);
			}
		}
	}

	private static JSONObject getWeatherJSON(String pLatitude, String pLongitude) {
		try {
			URL url = new URL(String.format(WEATHER_MAP_URL, pLatitude, pLongitude));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.addRequestProperty("x-api-key", WEATHER_MAP_API);

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuffer json = new StringBuffer(1024);
			String tmp = "";
			while ((tmp = reader.readLine()) != null)
				json.append(tmp).append("\n");
			reader.close();

			JSONObject data = new JSONObject(json.toString());

			if (data.getInt("cod") != 200) {
				return null;
			}

			return data;
		} catch (Exception e) {
			return null;
		}
	}

}
