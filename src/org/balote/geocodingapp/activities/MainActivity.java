package org.balote.geocodingapp.activities;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.balote.geocodingapp.R;
import org.balote.geocodingapp.json.GeoCodeJsonParser;
import org.balote.geocodingapp.models.LatLngModel;
import org.balote.geocodingapp.utils.InputStreamConverterUtil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

	private EditText addressField;
	private Button geoCodeButton;
	private TextView latText;
	private TextView longText;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		addressField = (EditText) findViewById(R.id.address_text_field);
		geoCodeButton = (Button) findViewById(R.id.geo_code_button);
		latText = (TextView) findViewById(R.id.lat_text);
		longText = (TextView) findViewById(R.id.long_text);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		geoCodeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String address = addressField.getText().toString();

				if (address != null && !address.trim().isEmpty()) {

					String formatedAddress = addressField.getText().toString()
							.replaceAll(" ", "+");

					GeoCodingAsyncTask myGeoCodingAsyncTask = new GeoCodingAsyncTask();
					myGeoCodingAsyncTask.execute(formatedAddress);
				}

			}
		});
	}

	private class GeoCodingAsyncTask extends
			AsyncTask<String, Void, LatLngModel> {

		private static final String TAG = "GeoCodingAsyncTask";
		private static final String route = "http://maps.googleapis.com/maps/api/geocode/json?";

		@Override
		protected LatLngModel doInBackground(String... params) {

			HttpClient myHttpClient = new DefaultHttpClient();
			HttpResponse myHttpResponse;
			int responseCode = 0;
			String jsonString = "";
			LatLngModel myLatLngModel = null;

			try {

				StringBuffer sb = new StringBuffer();
				sb.append(route);
				sb.append("address=");
				sb.append(URLEncoder.encode(params[0], "UTF-8"));
				sb.append("&sensor=false");

				Log.i(TAG, "url : " + sb.toString());

				HttpGet myHttpGet = new HttpGet(sb.toString());

				myHttpResponse = myHttpClient.execute(myHttpGet);
				responseCode = myHttpResponse.getStatusLine().getStatusCode();

				if (responseCode == 200) {

					jsonString = InputStreamConverterUtil
							.convert(myHttpResponse.getEntity().getContent());

					Log.i(TAG, "json string: " + jsonString);

					myLatLngModel = GeoCodeJsonParser
							.parseLatLongFromJsonString(jsonString);

				} else {
					Log.w(TAG, "response code: " + responseCode);
				}

			} catch (Exception e) {

				e.printStackTrace();

			} finally {

				myHttpClient.getConnectionManager().shutdown();

			}

			return myLatLngModel;
		}

		@Override
		protected void onPostExecute(LatLngModel myLatLngModel) {

			if (myLatLngModel != null) {

				latText.setText("Latitude: " + myLatLngModel.getLatitude());
				longText.setText("Longitude: " + myLatLngModel.getLongitude());

				LatLng latLng = new LatLng(myLatLngModel.getLatitude(),
						myLatLngModel.getLongitude());
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
						latLng, 10);

				map.animateCamera(cameraUpdate);
				map.clear();
				map.addMarker(new MarkerOptions().position(new LatLng(
						myLatLngModel.getLatitude(), myLatLngModel
								.getLongitude())));
			}
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
