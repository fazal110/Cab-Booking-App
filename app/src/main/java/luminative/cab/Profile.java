package luminative.cab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import epbit.Login.LoginDetails;
import epbit.constants.ProjectURLs;
import epbit.helper.CheckUserType;
import epbit.helper.MyWebViewClient;
import epbit.utils.SharedPreferencesUtility;

public class Profile extends ActionBarActivity {


	public ListView mDrawerList;
	public DrawerLayout mDrawerLayout;
	public ArrayAdapter<String> mAdapter;
	public ActionBarDrawerToggle mDrawerToggle;
	public String mActivityTitle;

	WebView profile_web_view;
	private Handler mHandler = new Handler();
	ProgressBar profileProgressupdateProfile;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		context = Profile.this;

		mDrawerList = (ListView)findViewById(R.id.List);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mActivityTitle = getTitle().toString();

		addDrawerItems();
		setupDrawer();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		profileProgressupdateProfile = (ProgressBar) findViewById(R.id.progressBarupdateprofile);
		profile_web_view = (WebView) findViewById(R.id.profile_webview);
		MyWebViewClient.enableWebViewSettings(profile_web_view);
		//MyWebViewClient.setActionBar(getSupportActionBar(), context, false);
		profile_web_view.addJavascriptInterface(new DemoJavaScriptInterface(
				this), "uploadpic");
		profileProgressupdateProfile.setVisibility(View.GONE);
		profile_web_view.setVisibility(View.VISIBLE);
		profile_web_view.setWebViewClient(new MyWebViewClient(this));
		profile_web_view.loadUrl(ProjectURLs.getProfileUrl(
				LoginDetails.Username, LoginDetails.usertype));

	}

	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface(Context c) {

		}

		@JavascriptInterface
		public void clickOnUploadPic() {
			mHandler.post(new Runnable() {
				public void run() {

					startActivity(new Intent(Profile.this,
							UpdateProfilePicture.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

				}
			});

		}

		@JavascriptInterface
		public void clickOnDriver(String fullname, String contact,
				String cab_type, String cab_number) {
			LoginDetails.CabType = Integer.parseInt(cab_number);
			SharedPreferencesUtility.saveCabType(context, LoginDetails.CabType);
			mHandler.post(new Runnable() {
				public void run() {
				}
			});

		}

	}

	//for custom list
	void  addDrawerItems() {
		ListView list;
		final String[] item = { "Home", "Profile","Help", "Sign Out"};
		Integer[] imgid={R.drawable.home,R.drawable.profile,R.drawable.help,R.drawable.signout};
		customlist adapter=new customlist(this, item, imgid);
		list=(ListView)findViewById(R.id.List);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				String Slecteditem = item[+position];
				//Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
				if (Slecteditem.equals("Home")) {

					CheckUserType.intentservice(Profile.this);
				} else if (Slecteditem.equals("Profile")) {

					context.startActivity(new Intent(context,Profile.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} else if (Slecteditem.equals("Rides")) {

					Toast.makeText(getBaseContext(), "Enter in Rides", Toast.LENGTH_SHORT).show();
					//context.startActivity(new Intent(context, Rides.class)
					//		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					//	finish();
				} else if (Slecteditem.equals("Cab Money")) {
					//	context.startActivity(new Intent(context, CabMoney.class)
					//			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					//	finish();

				}else if (Slecteditem.equals("Rate Card")) {
					//	activity.startActivity(new Intent(activity, RateCard.class)
					//	.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					//	activity.finish();

				}else if (Slecteditem.equals("Refer to Friend")) {

					//	context.startActivity(new Intent(context, ReferActivity.class)
					//			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					//finish();
				}else if (Slecteditem.equals("Payment")) {

					//context.startActivity(new Intent(context, PaymentActivity.class)
//					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//					finish();
				}else if (Slecteditem.equals("Help")) {

					context.startActivity(new Intent(context, Help.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				}else if (Slecteditem.equals("Sign Out")) {

					context.startActivity(new Intent(context, SignOut.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				}

			}
		});
	}
	protected void setupDrawer() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle("your name");
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mActivityTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CheckUserType.intentservice(getApplicationContext());
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			CheckUserType.intentservice(Profile.this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}