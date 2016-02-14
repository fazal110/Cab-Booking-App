package epbit.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import luminative.cab.MainActivity;
import luminative.cab.SplashActivity;
import luminative.cab.R;


public class MyWebViewClient extends WebViewClient {
	public Context mContext;
	private ProgressDialog mDialog;

	public MyWebViewClient(Context context) {
		mContext = context;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		mDialog = ProgressDialog.show(mContext, "", "Loading...", true, true);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		mDialog.dismiss();
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		Log.e("ERROR", "" + errorCode);
		Log.e("ERROR", "" + failingUrl);
		view.setVisibility(View.INVISIBLE);
		mDialog.dismiss();

		new AlertDialog.Builder(mContext).setTitle("Connectivity Problem !!!!")
				.setMessage("Please Check Your Internet Connection !!!!")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						Intent intent = new Intent((Activity) mContext,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("EXIT", true);
						mContext.startActivity(intent);
						// ProfileActivity1.this.finish();
						Log.e("CHECK", "GOING TO LOGIN");
						((Activity) mContext).finish();

					}
				}).show();

	}

	public static void enableWebViewSettings(WebView web) {
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setSavePassword(false);
		web.getSettings().setSaveFormData(false);
		web.getSettings().setSupportZoom(false);
		web.getSettings().setAppCacheEnabled(true);

	}

	public static void setActionBar(ActionBar actionbar, Context context,
			boolean display_action_back) {

		actionbar.setDisplayHomeAsUpEnabled(display_action_back);
		// actionbar.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#f4e81b")));
		actionbar.setBackgroundDrawable(new ColorDrawable(context
				.getResources().getColor(R.color.action_titlebar_color)));
		// actionbar.setBackgroundDrawable(
		// new ColorDrawable(R.color.action_titlebar_color));
		actionbar.setTitle(context.getString(R.string.header_name));
		actionbar.setIcon(R.drawable.menu);

	}

	public static void setActionBar(ActionBar actionbar,Context context)
	{


		actionbar.setBackgroundDrawable(
				new ColorDrawable(context.getResources().getColor(R.color.action_titlebar_color)));
		actionbar.setTitle(context.getString(R.string.header_name));
		actionbar.setIcon(R.drawable.menu);

	}

	public static void handleIncomingIntent(Context context, Class<?> classobj) {

		Intent intent = new Intent(context, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NO_HISTORY);

		intent.putExtra("EXIT", true);
		context.startActivity(intent);
		((Activity) context).finish();

	}
	
	public static MyWebChromeClient getWebChromeInstance()
	{
		return new MyWebChromeClient();
	}
	
	public static class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Log.d("WebViewDemo", message);
			result.confirm();
			return true;
		}
	}

}