package com.example.weatherapp;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

/**
 * 
 * Main class to UI and retrieving the GPS coordinate from GPS or Network
 *
 */
 
public class WeatherMainActivity extends Activity implements LocationListener {

	TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon,
			updatedField;
	private LocationManager locationManager;
	private double longitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cityField = (TextView) findViewById(R.id.city_text);
		updatedField = (TextView) findViewById(R.id.updated_text);
		detailsField = (TextView) findViewById(R.id.details_text);
		currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
		humidity_field = (TextView) findViewById(R.id.humidity_text);
		pressure_field = (TextView) findViewById(R.id.pressure_text);
		weatherIcon = (TextView) findViewById(R.id.weather_icon);

		getLocation();

		WeatherUtil.jSONParseTask asyncTask = new WeatherUtil.jSONParseTask(
				new WeatherUtil.JSOnASyncResponse() {
					public void processFinish(String weather_city, String weather_description,
							String weather_temperature, String weather_humidity, String weather_pressure,
							String weather_updatedOn, String weather_iconText, String sun_rise) {

						cityField.setText(weather_city);
						updatedField.setText(weather_updatedOn);
						detailsField.setText(weather_description);
						currentTemperatureField.setText(weather_temperature);
						humidity_field.setText("Humidity: " + weather_humidity);
						pressure_field.setText("Pressure: " + weather_pressure);
						weatherIcon.setText(Html.fromHtml(weather_iconText));

					}
				});

		asyncTask.execute(String.valueOf(latitude), String.valueOf(longitude));
	}

	/**
	 * Method to initialized the location parameter and get the
	 * latitude/longitude
	 */
	private void getLocation() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);

		String bestprovider = locationManager.getBestProvider(criteria, false);

		Location lastknownlocation = null;
		if (isGpsLocationProviderEnabled()) {
			lastknownlocation = locationManager.getLastKnownLocation(bestprovider);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		} else {
			Log.w("WeatherAPP", "GPS is off.Please Enable GPS");
		}
		if (isNetworkLocationProviderEnabled()) {
			lastknownlocation = locationManager.getLastKnownLocation(bestprovider);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		} else {
			Log.w("WeatherAPP", "Internet is off.Please Enable Internet");
		}

		if (lastknownlocation != null) {
			longitude = lastknownlocation.getLongitude();
			latitude = lastknownlocation.getLatitude();
		} else {
			Log.w("WeatherAPP", "GPS is off.Please Enable GPS");
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * Method to check the Enable/Disable of GPS
	 * 
	 * @return true if GPS is Enable else false
	 */
	private boolean isGpsLocationProviderEnabled() {
		try {
			return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Method to check the Enable/Disable of Internet
	 * 
	 * @return true if Internet is Enable else false
	 */

	private boolean isNetworkLocationProviderEnabled() {
		try {
			return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
		}

		return false;
	}
}
