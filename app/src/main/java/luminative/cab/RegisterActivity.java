package luminative.cab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import epbit.constants.ProjectURLs;
import epbit.helper.MyWebViewClient;

public class RegisterActivity extends ActionBarActivity {

	WebView register_web_view;
	Context context;
	private Handler mHandler = new Handler();
	private Handler mHandler2 = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		context = RegisterActivity.this;
		MyWebViewClient.setActionBar(getSupportActionBar(), context, false);
		getSupportActionBar().setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
		register_web_view = (WebView) findViewById(R.id.register_webview_id);
		MyWebViewClient.enableWebViewSettings(register_web_view);

		register_web_view.setWebChromeClient(MyWebViewClient.getWebChromeInstance());
		register_web_view.setWebViewClient(new MyWebViewClient(this));

		register_web_view.addJavascriptInterface(new DemoJavaScriptInterface(
				this), "android");
		register_web_view.loadUrl(ProjectURLs.REGISTER_URL);

	}

	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface(Context c) {

		}

		// This Function is Triggered when user Click on Already Register Link
		// of Web Page
		@JavascriptInterface
		public void clickOnAlreadyRegister() {

			mHandler.post(new Runnable() {

				public void run() {

					// mWebView.loadUrl("javascript:wave()");
					// Toast.makeText(getApplicationContext(),
					// "Already registered", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(RegisterActivity.this,
							MainActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

				}
			});

		}

		// This Function is Triggered when user Click on Register Link of Web
		// Page
		// TODO Auto-generated constructor stub
		@JavascriptInterface
		public void clickOnRegister() {
			mHandler.post(new Runnable() {
				public void run() {
					// mWebView.loadUrl("javascript:wave()");
					// System.out.println("yes i am trigerred");
					Toast.makeText(getApplicationContext(),
							"You are successfully Registered",
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(RegisterActivity.this,
							MainActivity.class));

				}
			});

		}

	}

	

}