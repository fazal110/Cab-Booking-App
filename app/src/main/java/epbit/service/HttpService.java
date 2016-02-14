package epbit.service;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import epbit.exception.NetworkException;
import epbit.exception.ServerException;


public class HttpService
{
	public static String  response;
	
	public static String httpPostService(String URL,List<NameValuePair> namevaluepair) throws ClientProtocolException, IOException
	{
		response="";
	
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL);
		httppost.setEntity(new UrlEncodedFormEntity(namevaluepair));
		httppost.setHeader("Content-type",
				"application/x-www-form-urlencoded");
		httppost.setHeader("Accept", "application/json");
	HttpResponse	serviceresponse = httpclient.execute(httppost);
	if (serviceresponse != null) {
		InputStream is = serviceresponse.getEntity().getContent();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {

			sb.append(line + "\n");
		response=sb.toString();

		}

	}
		
		return response;
		
	}
	
	public static String httpPostService(String URL
			) throws ClientProtocolException,
			IOException, NetworkException, ServerException {

		// 
		response = "";
		// Log.i("Url", URL);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL);
		Log.e("Url http post", "" + httppost.getEntity());
		HttpResponse serviceresponse = httpclient.execute(httppost);
		int responseCode = serviceresponse.getStatusLine().getStatusCode();
		if (responseCode == 200) {
			if (serviceresponse != null) {
				InputStream is = serviceresponse.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {

					sb.append(line + "\n");
					response = sb.toString();

				}

			}
		} else if (responseCode == 404 || responseCode == 500
				|| responseCode == 403) {
			Log.i("Server Exception", "Response Code:" + responseCode);
			throw new ServerException(responseCode);
		}

		return response;

	}
	public static HttpResponse httpPostServiceReturnHttpResponse(String URL,List<NameValuePair> namevaluepair) throws ClientProtocolException, IOException
	{
		response="";
	
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL);
		httppost.setEntity(new UrlEncodedFormEntity(namevaluepair));
		httppost.setHeader("Content-type",
				"application/x-www-form-urlencoded");
		httppost.setHeader("Accept", "application/json");
	HttpResponse	serviceresponse = httpclient.execute(httppost);
	
		
		return serviceresponse;
		
	}
	
}
