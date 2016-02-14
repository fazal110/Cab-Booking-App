package epbit.service;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class NameValueCreator
{
	public static  List<NameValuePair> nameValuePair=new ArrayList<NameValuePair>();
	
	public static List<NameValuePair> createNameValuePair(String... params)
	{
		if(params.length%2!=0)
		{
			Log.e("NameValuePair", "Either a Value or key is Missing");
		}
		else
		{
			for(int i=0;i<params.length;i+=2)
			{
				nameValuePair.add(new BasicNameValuePair(params[i], params[i+1]));
			}
		}
		return nameValuePair;
		
	}
	
	public static List<NameValuePair> addArrayToNameValuePair(List<NameValuePair> nvp,String key,String value)
	{
		
		nvp.add(new BasicNameValuePair(key, value));
		
		
		return nvp;
		
	}
	
	
}