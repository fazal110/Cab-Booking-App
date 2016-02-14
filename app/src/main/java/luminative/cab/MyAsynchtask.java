package luminative.cab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;

import epbit.Login.LoginDetails;
import epbit.constants.IWebConstant;
import epbit.constants.ProjectURLs;
import epbit.exception.NetworkException;
import epbit.exception.ParsingException;
import epbit.exception.ServerException;
import epbit.exception.TimeOutException;
import epbit.service.AppAsynchTask;
import epbit.service.HttpService;
import epbit.service.NameValueCreator;
import epbit.utils.SharedPreferencesUtility;

//import com.cab.project.DriverActivity;
//import com.example.cabbookinghome.ProfileActivity1;

public class MyAsynchtask extends AppAsynchTask<String, String, String> {
	HttpResponse response;
	JSONObject loginresultjson, usertypejson;
	Context mycontext;
	Context dialogcontext;
	int result_codes = -1;
	Method dataMtd = null;

	public MyAsynchtask(Activity context) {
		super(context);
		this.mycontext = context;
		this.dialogcontext = dialogcontext;
		setShowdialog(false);
	}

	@Override
	protected String customDoInBackground(String... params)
			throws NetworkException, ServerException, ParsingException,
			TimeOutException, IOException, JSONException {
		int success = 0;
		String response = HttpService.httpPostService(
				ProjectURLs.LOGIN_URL_STRING, NameValueCreator
						.createNameValuePair("username", LoginDetails.Username,
								"password", LoginDetails.Password));
		loginresultjson = new JSONObject(response);
		success = loginresultjson.getInt("success");
		if (success == 1) {
			LoginDetails.usertype = loginresultjson
					.getString(IWebConstant.NAME_VALUE_PAIR_USER_TYPE);
		}
		return "" + success;
	}

	@Override
	protected void customOnPostExecute(String result) {

		if (result.equals("1")) {

			SharedPreferencesUtility.saveUsername(mycontext,
					LoginDetails.Username);
			SharedPreferencesUtility.savePassword(mycontext,
					LoginDetails.Password);

			if (LoginDetails.usertype.equals("passenger")) {

				Toast.makeText(mycontext, "Usertype passenger",
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent(mycontext, Passengeractivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mycontext.startActivity(i);

			} else if (LoginDetails.usertype.equalsIgnoreCase("driver")) {

				Intent main_to_driver_intent = new Intent(mycontext,
						driveactivity.class);
				main_to_driver_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 mycontext.startActivity(main_to_driver_intent);

			} else {
				Toast.makeText(mycontext, "Usertype not found",
						Toast.LENGTH_SHORT).show();

			}

		} else {
			Toast.makeText(mycontext, "Login Failed", Toast.LENGTH_SHORT)
					.show();
			mycontext.startActivity(new Intent(mycontext, MainActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP));
			SharedPreferencesUtility.resetSharedPreferences(mycontext);
			LoginDetails.Username = "";
		}

	}

	protected void retryLogin() {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				new MyAsynchtask((Activity) mycontext).execute();
			}
		}, 1500);
	}

}