package epbit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtility
{
	 static SharedPreferences mySharedPreferences;
	
	
	public static void saveUsername(Context context,String Value)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putString("login_username", Value);
	    sharedpreferenceeditor.commit();
	}
	public static void savePassword(Context context,String Value)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putString("login_password", Value);
	    sharedpreferenceeditor.commit();
	}
	public static String loadUsername(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString("login_username", "0");
		
		
	}
	public static String loadPassword(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString("login_password", "0");
		
		
	}
	
	public static void resetSharedPreferences(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putString("login_password", "0");
		sharedpreferenceeditor.putString("login_username", "0");
	    sharedpreferenceeditor.commit();
	}
	public static void saveCabType(Context context,int Value)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putInt("cab_type", Value);
	    sharedpreferenceeditor.commit();
	}
	public static int loadCabType(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getInt("cab_type", 2);
		
		
	}
	
	
}