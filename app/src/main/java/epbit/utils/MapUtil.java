package epbit.utils;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import luminative.cab.R;
import epbit.Login.LoginDetails;
import epbit.exception.NetworkException;
import epbit.exception.ServerException;
import epbit.service.HttpService;

public class MapUtil {

	/**
	 * 
	 * Method to Drop Map Pointer to Current Location
	 * 
	 * @param google_map
	 *            Google Map object
	 * @param lat
	 *            latitude of current location
	 * @param longi
	 *            longitude of current location icon_color of Icon to be Pointed
	 *            will be Blue
	 */

	public static void dropPin(GoogleMap google_map, double lat, double longi) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(lat, longi));
		options.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.userm));
		options.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

		google_map.addMarker(options);

	}

	/**
	 * 
	 * Method to Drop Map Pointer to Current Location
	 * 
	 * @param google_map
	 *            Google Map object
	 * @param lat
	 *            latitude of current location
	 * @param longi
	 *            longitude of current location
	 * @param icon
	 *            color of Icon to be Pointed
	 * @return
	 */

	public static Marker dropPin(GoogleMap google_map, double lat,
			double longi, int icon, String title) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(lat, longi));
		options.icon(BitmapDescriptorFactory.fromResource(icon));
		if (title != null)
			options.title(title);

		return google_map.addMarker(options);

	}

	/**
	 * Method to Clear the All the Markers Pointers from the map
	 * 
	 * @param google_map
	 */
	public static void clearMarkers(GoogleMap google_map) {
		google_map.clear();
	}

	/**
	 * Method to Change Camera Focus to Current Location
	 * 
	 * @param google_map
	 *            Google Map object
	 * @param lat
	 *            latitude of current location
	 * @param longitude
	 *            longitude of current location
	 * @param zoomlevel
	 *            ZoomLevel to which focus had be done
	 */
	public static void changeCameraFocus(GoogleMap google_map, double lat,
			double longitude, int zoomlevel) {

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(lat, longitude)).zoom(zoomlevel).build();
		google_map.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

	public static String getLatLongToAddressResponse(double lat, double longi,
			String google_api_key) throws ClientProtocolException, IOException,
			NetworkException, ServerException, JSONException {

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ lat + "," + longi + "&sensor=true";
		Log.e("url	", url);
		String res = HttpService.httpPostService(url);
		Log.e("response	", res);
		JSONObject json = new JSONObject(res);
		if (json.has("results")) {

			JSONArray jArray = json.getJSONArray("results");
			JSONObject json1 = jArray.getJSONObject(0);
			if (json1.has("formatted_address")) {
				res = json1.getString("formatted_address");
			} else {
				throw new JSONException(json.toString());
			}
		}

		return res;
	}

	public static List<String> getAddressSuggestions(String api_key,
			String keyword, String lat, String longi)
			throws ClientProtocolException, IOException, NetworkException,
			ServerException, JSONException {
		String search_text[] = keyword.replace(" ", "").split(",");

		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
				+ search_text[0]
				+ "&location="
				+ lat
				+ ","
				+ longi
				+ "&radius=49000&sensor=true&key=" + api_key;

		List<String> list = new ArrayList<String>();
		String res = HttpService.httpPostService(url);
		JSONObject json = new JSONObject(res);
		if (json != null && json.has("predictions")) {

			JSONArray contacts = json.getJSONArray("predictions");

			for (int i = 0; i < contacts.length(); i++) {
				JSONObject c = contacts.getJSONObject(i);
				if (c.has("description")) {
					String description = c.getString("description");

					list.add(description);
				}

			}
		}
		return list;

	}
	public static String getLatLongFromAddress(String address)
			throws ClientProtocolException, IOException, NetworkException,
			ServerException, JSONException {

		String uri = "http://maps.google.com/maps/api/geocode/json?address="
				+ address.replace(" ", "") + "&sensor=false";

		String res = HttpService.httpPostService(uri);
		String location = "";

		JSONObject jsonObject = new JSONObject(res);
		if (jsonObject != null && jsonObject.has("results")) {
			JSONArray jarray = jsonObject.getJSONArray("results");
			if (jarray != null && jarray.length() > 0) {
				JSONObject json = jarray.getJSONObject(0);
				if (json != null && json.has("geometry")) {
					JSONObject loca = json.getJSONObject("geometry");
					if(loca!=null&&loca.has("location"))
					{
						JSONObject loc=loca.getJSONObject("location");
						if (loc.has("lat")) {
							location+=loc.getDouble("lat");
						}
						location+=",";
						if (loc.has("lng")) {
							location+=loc.getDouble("lng");
						}
					}
					}
					
					
			}

		}
		
		return location;
	}
	

	public static HashMap<String, String> getTimeDistanceBetweenMapPoints(
			String source_lat, String source_long, String dest_lat,
			String dest_long, String units) throws ClientProtocolException,
			IOException, NetworkException, ServerException, JSONException {
		HashMap<String, String> response = new HashMap<String, String>();

		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ "json" + "?" + "&origin=" + source_lat
				+ "," + source_long + "&destination=" + dest_lat + ","
				+ dest_long + "&" + "sensor=false" + "&" + "units=" + units;
//		
//          String url="http://maps.googleapis.com/maps/api/directions/json?origin=28.675861870844997,77.32144810259342&destination=28.6805906,77.3747908&sensor=true&mode=driving";
		String res = "";
		res = HttpService.httpPostService(url);
		response.put("response", res);
		JSONObject jsonobj = new JSONObject(res);
		if (jsonobj != null && jsonobj.has("routes")) {
			JSONArray jsonarray = jsonobj.getJSONArray("routes");

			if (jsonarray != null && jsonarray.length() > 0) {
				JSONObject jsonobj2 = jsonarray.getJSONObject(0);
				if (jsonobj2 != null && jsonobj2.has("legs")) {
					JSONArray jsonArray2 = jsonobj2.getJSONArray("legs");
					if (jsonArray2 != null && jsonArray2.length() > 0) {

						JSONObject jsonobj3 = jsonArray2.getJSONObject(0);
						if (jsonobj3 != null && jsonobj3.has("distance")) {
							JSONObject jsonobj4 = jsonobj3
									.getJSONObject("distance");
							if (jsonobj4 != null && jsonobj4.has("text")) {
								LoginDetails.S_D_Distance = jsonobj4
										.getString("text");
								response.put("distance",
										LoginDetails.S_D_Distance);
							}
						}
						if (jsonobj3 != null && jsonobj3.has("duration")) {
							JSONObject jsonobj4 = jsonobj3
									.getJSONObject("duration");
							if (jsonobj4 != null && jsonobj4.has("text")) {
								LoginDetails.S_D_Time = jsonobj4
										.getString("text");
								response.put("duration", LoginDetails.S_D_Time);
							}
						}

					}

				}

			}

		}

		return response;
	}

	public static List<List<HashMap<String, String>>> getRoutesBetweenMapPoints(
			String res) throws JSONException {
		List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();

		JSONArray jLegs;
		JSONObject jsonobj = new JSONObject(res);
		if (jsonobj != null && jsonobj.has("routes")) {
			JSONArray jsonarray = jsonobj.getJSONArray("routes");

			if (jsonarray != null && jsonarray.length() > 0) {

				for (int i = 0; i < jsonarray.length(); i++) {
					jLegs = ((JSONObject) jsonarray.get(i))
							.getJSONArray("legs");
					List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

					/** Traversing all legs */
					for (int j = 0; j < jLegs.length(); j++) {
						JSONArray jSteps = ((JSONObject) jLegs.get(j))
								.getJSONArray("steps");

						/** Traversing all steps */
						for (int k = 0; k < jSteps.length(); k++) {
							String polyline = "";
							polyline = (String) ((JSONObject) ((JSONObject) jSteps
									.get(k)).get("polyline")).get("points");
							List<LatLng> list = decodePoly(polyline);

							/** Traversing all points */
							for (int l = 0; l < list.size(); l++) {
								HashMap<String, String> hm = new HashMap<String, String>();
								hm.put("lat", Double.toString(((LatLng) list
										.get(l)).latitude));
								hm.put("lng", Double.toString(((LatLng) list
										.get(l)).longitude));
								path.add(hm);
							}
						}
						routes.add(path);
					}

				}
			}
		}
		return routes;
	}

	private static List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}
		return poly;
	}

	
}
