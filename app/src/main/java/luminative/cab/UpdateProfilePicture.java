package luminative.cab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import epbit.Login.LoginDetails;
import epbit.constants.IWebConstant;
import epbit.constants.ProjectURLs;
import epbit.helper.CheckUserType;

public class UpdateProfilePicture extends ActionBarActivity {

	public ListView mDrawerList;
	public DrawerLayout mDrawerLayout;
	public ArrayAdapter<String> mAdapter;
	public ActionBarDrawerToggle mDrawerToggle;
	public String mActivityTitle;
	ImageButton select_profile;
	ImageButton update_to_server;
	private static final int TAKE_REQUEST = 1888;
	private static final int SELECT_REQUEST = 1;
	ImageView pic_imageview;
	Bitmap pic;

	ImageButton takepicture;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateprofilepicture);

		// FrameLayout FL=(FrameLayout)findViewById(R.id.framelayout);
		// FL.
		context = UpdateProfilePicture.this;

		mDrawerList = (ListView)findViewById(R.id.List);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mActivityTitle = getTitle().toString();

		addDrawerItems();
		setupDrawer();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		pic_imageview = (ImageView) findViewById(R.id.update_profile_preview);
		select_profile = (ImageButton) findViewById(R.id.select_profile_pic_button);
		select_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, SELECT_REQUEST);
			}
		});

		takepicture = (ImageButton) findViewById(R.id.take_profile_picture);
		takepicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE),
						TAKE_REQUEST);

			}
		});

		// will send Image to the Server
		update_to_server = (ImageButton) findViewById(R.id.update_to_Server);
		update_to_server.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pic_imageview.getDrawable() == null) {
					Toast.makeText(getApplicationContext(),
							"Please Select/Capture a Image", Toast.LENGTH_SHORT);
				} else {

					Drawable d = pic_imageview.getDrawable();
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					byte[] ba;
					do {
						ByteArrayOutputStream bao = new ByteArrayOutputStream();

						Log.e("BEFORE REDUCING",
								bitmap.getHeight() + " " + bitmap.getWidth()
										+ " " + bitmap.getRowBytes()
										* bitmap.getHeight());

						Log.e("After REDUCING",
								bitmap.getHeight() + " " + bitmap.getWidth()
										+ " " + bitmap.getRowBytes()
										* bitmap.getHeight());
						bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);

						ba = bao.toByteArray();
						if ((ba.length / 1024) >= 650) {
							bitmap = Bitmap.createScaledBitmap(bitmap,
									(int) (bitmap.getWidth() * 0.95),
									(int) (bitmap.getHeight() * 0.95), true);

						}

						Log.e("BYTE LENGTH", "" + ba.length / 1024);

					} while ((ba.length / 1024) >= 650);

					String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

					Toast.makeText(getApplicationContext(),
							"Updating Your Pic....	", Toast.LENGTH_LONG).show();
					ArrayList<NameValuePair> nameValuePairs = new

					ArrayList<NameValuePair>();

					nameValuePairs.add(new BasicNameValuePair(
							IWebConstant.NAME_VALUE_PAIR_KEY_UPDATEPIC, ba1));
					nameValuePairs.add(new BasicNameValuePair(
							IWebConstant.NAME_VALUE_PAIR_KEY_EMAIL,
							LoginDetails.Username));
					new updatepictask(getApplicationContext())
							.execute(nameValuePairs);

				}
			}
		});

	}

	public UpdateProfilePicture() {
		super();
	}

	private class updatepictask extends
			AsyncTask<List<? extends NameValuePair>, String, String> {

		Context context;
		int result_code = 0;

		public updatepictask(Context applicationContext) {
			this.context = applicationContext;
		}

		@Override
		protected String doInBackground(List<? extends NameValuePair>... params) {
			try {
				StringBuilder stringBuilder = new StringBuilder();

				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new

				HttpPost(ProjectURLs.UPDATE_PROFILE_PIC_URL);

				httppost.setEntity(new UrlEncodedFormEntity(params[0]));

				HttpResponse response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
				JSONObject jsonObject = new JSONObject();
				try {
					Log.e("Check String", stringBuilder.toString());
					jsonObject = new JSONObject(stringBuilder.toString());

					Log.e("Check", "" + jsonObject.getInt("success"));
					result_code = jsonObject.getInt("success");

				} catch (JSONException e) {

					Log.e("Exception", "Json");
					e.printStackTrace();
				}

				Log.e("UPDATE TASK", "Successfully Done");

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {

				Log.e("log_tag", "Error in http connection " + e.toString());

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			if (result_code == 1) {
				Toast.makeText(context, "Profile pic Updated Successfully",
						Toast.LENGTH_SHORT).show();
				startActivity(new Intent(context, Profile.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			} else {
				Toast.makeText(context, "Failed to Update Profile Pic..",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TAKE_REQUEST && resultCode == RESULT_OK) {
			Log.i("Update Profile", "ONACTIVITY RESULT");
			pic = (Bitmap) data.getExtras().get("data");
			update_to_server = (ImageButton) findViewById(R.id.update_to_Server);
			update_to_server.setVisibility(View.VISIBLE);
			Log.i("Update Profile", "ONACTIVITY RESULT" + pic.toString());
			pic_imageview.setImageBitmap(pic);

		}
		if (requestCode == SELECT_REQUEST && resultCode == Activity.RESULT_OK)
			try {

				if (pic != null) {
					pic.recycle();
				}
				InputStream stream = getContentResolver().openInputStream(
						data.getData());
				pic = BitmapFactory.decodeStream(stream);
				stream.close();
				update_to_server = (ImageButton) findViewById(R.id.update_to_Server);
				update_to_server.setVisibility(View.VISIBLE);
				pic_imageview.setImageBitmap(pic);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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

					CheckUserType.intentservice(UpdateProfilePicture.this);
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

				} else if (Slecteditem.equals("Rate Card")) {
					//	activity.startActivity(new Intent(activity, RateCard.class)
					//	.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					//	activity.finish();

				} else if (Slecteditem.equals("Refer to Friend")) {

					//	context.startActivity(new Intent(context, ReferActivity.class)
					//			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					//finish();
				} else if (Slecteditem.equals("Payment")) {

					//context.startActivity(new Intent(context, PaymentActivity.class)
//					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//					finish();
				} else if (Slecteditem.equals("Help")) {

					context.startActivity(new Intent(context, Help.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				} else if (Slecteditem.equals("Sign Out")) {

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
			CheckUserType.intentservice(UpdateProfilePicture.this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}