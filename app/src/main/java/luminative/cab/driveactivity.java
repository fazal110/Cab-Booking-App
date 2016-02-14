package luminative.cab;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import epbit.Login.LoginDetails;
import epbit.helper.CheckUserType;
import epbit.latlong.GPSTracker22;

/**
 * Created by Muhammad Tahir on 12/10/2015.
 */
public class driveactivity extends ActionBarActivity implements OnMapReadyCallback {

    public ListView mDrawerList;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;
    public String mActivityTitle;
    double latitude,longitude ;
    private GoogleMap mMap;
    GPSTracker22 gps;
    functions obj = new functions();
    private GoogleCloudMessaging gcm;
    String regid;
    HttpPost httppost;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    String user_name = "";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String TAG = "MainActivity";
    String SENDER_ID = "224163385438";
    String API_KEY = "AIzaSyCL3REK_ONEgLdhcP8giso_5P6xWE3gUvA";
    Utils utils;
    private Context context = driveactivity.this;
    static String t,r;
    private ProgressDialog pb;
    private BookingActivity o;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        utils  = new Utils(this);
        startRegistration();
        getloc();
        updatelocation();
        o = new BookingActivity();
        registerReceiver(o.broadcastReceiver, new IntentFilter(
                "CHAT_MESSAGE_RECEIVED"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mDrawerList = (ListView)findViewById(R.id.List);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    //for custom list
    void  addDrawerItems() {
        ListView list;
        final String[] item = { "Home", "Profile","Help", "Sign Out"};
        Integer[] imgid={R.drawable.home, R.drawable.profile, R.drawable.help, R.drawable.signout};
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

                    CheckUserType.intentservice(driveactivity.this);
                } else if (Slecteditem.equals("Profile")) {

                    driveactivity.this.startActivity(new Intent(driveactivity.this, Profile.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if (Slecteditem.equals("Help")) {

                    driveactivity.this.startActivity(new Intent(driveactivity.this, Help.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if (Slecteditem.equals("Sign Out")) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(driveactivity.this);
                    alertDialogBuilder.setTitle("Exit Application?");
                    alertDialogBuilder
                            .setMessage("Click yes to exit!")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                obj.recorddelete();
                                                Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
                                                unregIntent.putExtra("app", PendingIntent.getBroadcast(driveactivity.this, 0, new Intent(), 0));
                                                startService(unregIntent);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            driveactivity.this.startActivity(new Intent(driveactivity.this, SignOut.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            finish();
                                        }
                                    })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            }
        });
    }
    public void onClickre(View v){

    }
    protected void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(LoginDetails.Username);
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


    public void getloc() {
        // create class object
        gps = new GPSTracker22(driveactivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                obj.recorddelete();
                                Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
                                unregIntent.putExtra("app", PendingIntent.getBroadcast(driveactivity.this, 0, new Intent(), 0));
                                startService(unregIntent);
                                driveactivity.this.startActivity(new Intent(driveactivity.this, SignOut.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void startRegistration() {

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            Log.i(TAG, "Google Play Services OK");
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = utils.getRegistrationId();
            System.out.println("refdfsasdasdasdada================ "+regid);
            registerInBackground();
             if (regid.equals(null)) {
                 registerInBackground();
             } else {
               Log.i(TAG, "Reg ID Not Empty");
             }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, "No Google Play Services...Get it from the store.");
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void registerInBackground() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                } catch (IOException ex) {
                    msg = ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, "onPostExecute : " + msg);

                if (!msg.equalsIgnoreCase("SERVICE_NOT_AVAILABLE")) {

                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("server_response", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);

                } else {

                    utils.showToast("Error : " + msg
                            + "\nPlease check your internet connection");

                    hidePB();

                }
            }

            // Define the Handler that receives messages from the thread and
            // update the progress
            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {

                    String aResponse = msg.getData().getString(
                            "server_response");

                    if ((null != aResponse)) {

                        Log.i(TAG, "	sendRegistrationIdToBackend();");

                        sendRegistrationIdToBackend();

                    } else {

                    }

                }
            };
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use
     * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
     * since the device sends upstream messages to a server that echoes back the
     * message using the 'from' address in the message.
     */
    public void sendRegistrationIdToBackend() {

        Log.i(TAG, "sendRegistrationIdToBackend");

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    httpclient = new DefaultHttpClient();

                    // yahan reg id ki server webserivcice dalegi

                    httppost = new HttpPost("http://cab.luminativesolutions.com/cabbooking/ws/gcm/save_reg_id.php");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("username",
                            LoginDetails.Username));
                    nameValuePairs.add(new BasicNameValuePair("reg_id", regid));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);
                    Log.i(TAG, "Response : " + response);

                    if (response != null) {

                        if (response
                                .equalsIgnoreCase("Username already registered")) {

                            utils.showToast("Username already registered");


                            hidePB();



                        } else {
                            if (response
                                    .equalsIgnoreCase("New Device Registered successfully")) {

                                utils.savePreferences(Utils.UserName, user_name);
                                // Persist the regID - no need to register
                                // again.
                                utils.storeRegistrationId(regid);

                                utils.showToast("Device registration successful");


                            }
                        }

                    }

                } catch (Exception e) {

                    hidePB();
                    Log.d(TAG, "Exception : " + e.getMessage());
                }
            }
        };

        thread.start();

    }
    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(driveactivity.this);
                pb.setMessage(message);
                pb.show();
            }
        });

    }

    void hidePB() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pb != null && pb.isShowing())
                    pb.dismiss();
            }
        });

    }

    public void updatelocation(){

        Log.i(TAG, "Update location");

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    httpclient = new DefaultHttpClient();

                    String link = "http://cab.luminativesolutions.com/cabbooking/ws/update_user_latlong.php?lat="+String.valueOf(latitude)+"&long="+String.valueOf(longitude)+"&email="+LoginDetails.Username;
                    httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i(TAG, "Response : " + response);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}