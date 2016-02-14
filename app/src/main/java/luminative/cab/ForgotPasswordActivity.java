package luminative.cab;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import epbit.constants.ProjectURLs;
import epbit.helper.MyWebViewClient;

public class ForgotPasswordActivity extends ActionBarActivity {
	WebView forgot_WebView;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword);
		getSupportActionBar().setTitle(
				getResources().getString(R.string.header_name));
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#0190B8")));
		forgot_WebView = (WebView) findViewById(R.id.forgot_password_webview_id);

		MyWebViewClient.enableWebViewSettings(forgot_WebView);
		forgot_WebView.setWebChromeClient(new MyWebChromeClient());
		forgot_WebView.setWebViewClient(new MyWebViewClient(this));
		forgot_WebView.addJavascriptInterface(new DemoJavaScriptInterface(),
				"android");
		forgot_WebView.loadUrl(ProjectURLs.FORGOT_PASSWORD_URL);

	}

	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface() {

		}

		@JavascriptInterface
		public void clickOnAndroidLogin() {
			mHandler.post(new Runnable() {
				public void run() {
				
					startActivity(new Intent(ForgotPasswordActivity.this,
							luminative.cab.MainActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

				}
			});

		}
	}

	final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {

			result.confirm();
			return true;
		}
	}

}