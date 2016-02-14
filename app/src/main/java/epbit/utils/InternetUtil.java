package epbit.utils;

//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.util.Log;
//
//public class InternetUtil {
//
//	private static final String TAG = InternetUtil.class.getName();
//
//	/**
//	 * Check Internet connectivity
//	 *     
//	 * @return
//	 */
//	public static boolean isInternetConnectivityAvailable(Context ctx) {
//
//		ConnectivityManager cm = (ConnectivityManager) ctx
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo netInfo = cm.getActiveNetworkInfo();
//
//		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean isConnectionAvailable(Context ctx) {
//
//		try {
//			ConnectivityManager connec = (ConnectivityManager) ctx
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//			if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
//					|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
//					|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
//					|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
//
//				return true;
//
//			} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
//					|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
//
//				return false;
//			}
//		} catch (Exception e) {
//			Log.e(TAG, "Exception @ isConnectionAvailable: " + e.toString());
//		}
//
//		return false;
//	}
//}

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;

public class InternetUtil {

	// CHECK FOR INTERNET METHOD

	public static boolean isInternetOn(Context context) {

		// Log.d(TAG, "In isInternetOn method");

		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// ARE WE CONNECTED TO THE NET

		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED

		||

		connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING

		||

		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING

		||

		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

			return true;

		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED

				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

			return false;
		}
		return false;
	}

	/**
	 * Display error dialog
	 * 
	 * @param context
	 * @param isGoBack
	 *            - if true go to the previous screen when clicked on OK
	 */
	@SuppressWarnings("deprecation")
	public static void showErrorDialog(final Context context,
			final boolean isGoBack) {

		String alertMessage = "Unable to get data from Network. Check your Internet connectivity.";

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setTitle("Network Error");
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				if (isGoBack) {
					dialog.cancel();
				}
				return;
			}
		});

		alertDialog.show();
	}

}
