package luminative.cab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import epbit.Login.LoginDetails;
import epbit.utils.SharedPreferencesUtility;

public class SignOut extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signout);
		LoginDetails.Username = "";
		SharedPreferencesUtility
				.resetSharedPreferences(getApplicationContext());
		startActivity(new Intent(getApplicationContext(), MainActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();

	}

}