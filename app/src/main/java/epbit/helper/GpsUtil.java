package epbit.helper;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class GpsUtil {

	private Context ctx,activity;
	boolean flag;
	private LocationManager lm;
	

	
	public GpsUtil(Context ctx ,Context act) {
		super();
		this.ctx = ctx;
		lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		this.activity=act;
	}


	
	/*
	 * return true if success
	 * false if failed
	 * 
	 */
	
	public boolean toogle() throws Exception {
		
		
		 Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
         activity.startActivity(intent);
		
		
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
	}
	
	/*
	 * return true if GPS is enabled
	 * return false if Gps is Disabled
	 */
	
	public boolean checkGpsStatus()
	{
	
		 boolean isGPS = lm.isProviderEnabled (LocationManager.GPS_PROVIDER);
		 if(!isGPS)
			 isGPS=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return isGPS;
	}
	

}
