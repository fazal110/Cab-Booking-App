package epbit.helper;

import android.content.Context;
import android.content.Intent;

import luminative.cab.Passengeractivity;
import luminative.cab.driveactivity;
import epbit.Login.LoginDetails;

public class CheckUserType
{
	
	//1 for user
	//2 for driver
	public static int checkuser()
	{
		if(LoginDetails.usertype.equalsIgnoreCase("passenger"))
		{
			return 1;
		}
		else {
			return 2;
			
		}
	}
	
	public static void intentservice(Context context)
	{
		if (CheckUserType.checkuser() == 1)
			context.startActivity(new Intent(context, Passengeractivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
		else {
			context.startActivity(new Intent(context, driveactivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
		}
	}
	
}