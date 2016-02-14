package luminative.cab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import epbit.constants.IWebConstant;
import epbit.helper.GpsUtil;
import epbit.utils.AlertUtil;
import epbit.utils.AlertUtil.ConnectionDialogClickListener;

public class SplashActivity extends ActionBarActivity {

	ProgressBar splash_progress;
	TextView errortext;
	Context context;
	private AlertDialog.Builder alertDialog;
	public boolean spalshflag = true;
	final int GPS_DIALOG = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		errortext = (TextView) findViewById(R.id.errortetview);
		startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
		context = SplashActivity.this;

		if (getIntent().getBooleanExtra("EXIT", false)) {
			Log.e("SPLASH", "FINISHING");

			android.os.Process.killProcess(android.os.Process.myPid());
		}

		getSupportActionBar().hide();
		splash_progress = (ProgressBar) findViewById(R.id.splash_progressBar);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				splash_progress.setVisibility(View.VISIBLE);

			}
		}, IWebConstant.SPLASH_PROGRESS_TIME);

		Log.e("VERSION", "" + Build.VERSION.SDK_INT);

	}

	public void loginProcess() {
		splash_progress.setVisibility(View.VISIBLE);
		if (!new GpsUtil(context, context).checkGpsStatus()) {

			AlertUtil.showAlertDialogwithListener(context,
					R.string.gpsdisabledtitle, R.string.gpsdisabledmessage,
					R.string.gpsposbutton, R.string.gpsnegativebutton,
					dialog_listener, GPS_DIALOG).show();

			splash_progress.setVisibility(View.INVISIBLE);
		} else {
			startActivity(new Intent(SplashActivity.this, MainActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
							"issplash", true));
			finish();
		}

	}

	/*protected void doLogin() {

		if (SharedPreferencesUtility.loadUsername(context)
				.equalsIgnoreCase("0")) {
			startActivity(new Intent(SplashActivity.this, MainActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
							"issplash", true));
			finish();
		} else {
			LoginDetails.Username = SharedPreferencesUtility
					.loadUsername(context);
			LoginDetails.Password = SharedPreferencesUtility
					.loadPassword(context);
			new MyAsynchtask((Activity) context).execute("");

		}

	}*/

	@Override
	protected void onResume() {

		super.onResume();

		if (Build.VERSION.SDK_INT < 12) {
			errortext.setVisibility(View.VISIBLE);
			Log.e("VERSION", "" + Build.VERSION.SDK_INT);
			splash_progress.setVisibility(View.INVISIBLE);
		}

		else {

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					loginProcess();

				}
			}, IWebConstant.SPLASH_TIMEOUT);

		}

	}

	ConnectionDialogClickListener dialog_listener = new ConnectionDialogClickListener() {

		@Override
		public void dialogClicklistener(int dialog_id, int button) {
			switch (dialog_id) {
			case GPS_DIALOG:
				if (button == DialogInterface.BUTTON_POSITIVE)
					try {
						new GpsUtil(context, context).toogle();
					} catch (Exception e) {
						Toast.makeText(context, R.string.gps_error,
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				else {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
				break;

			default:
				break;
			}

		}
	};

}