package org.balote.geocodingapp.json;

import org.balote.geocodingapp.models.LatLngModel;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class GeoCodeJsonParser {

	private static final String TAG = "GeoCodeJsonParser";

	public static LatLngModel parseLatLongFromJsonString(String jsonString) {

		LatLngModel myLatLngModel = new LatLngModel();

		try {

			JSONObject myJsonObj = new JSONObject(jsonString);

			String status = myJsonObj.getString("status");
			Log.i(TAG, "status: " + status);

			JSONArray resultsJsonArr = myJsonObj.getJSONArray("results");
			JSONObject resultsJsonObj = resultsJsonArr.getJSONObject(0);
			JSONObject geometryJsonObj = resultsJsonObj
					.getJSONObject("geometry");
			JSONObject locationJsonObj = geometryJsonObj
					.getJSONObject("location");

			long lat = locationJsonObj.getLong("lat");
			long lng = locationJsonObj.getLong("lng");

			Log.i(TAG, "lat: " + lat);
			Log.i(TAG, "lng: " + lng);

			myLatLngModel.setLatitude(lat);
			myLatLngModel.setLongitude(lng);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return myLatLngModel;
	}
}
