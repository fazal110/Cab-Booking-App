package luminative.cab;

/**
 * Created by Muhammad Tahir on 12/26/2015.
 */

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
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import epbit.Login.LoginDetails;
import epbit.helper.CheckUserType;
import epbit.latlong.GPSTracker22;
import epbit.latlong.LatLongDetails;

public class Passengeractivity extends ActionBarActivity implements
        GoogleMap.OnMarkerClickListener {

    public ListView mDrawerList;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;
    public String mActivityTitle;
    double latitude,longitude ;
    // GPSTracker class
    GPSTracker22 gps;
    JSONArray array;
    String name;
    String awen;
    functions obj = new functions();
    EditText username;

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
    private Context context = Passengeractivity.this;
    static String t,r;
    private ProgressDialog pb;

    private GoogleMap googleMap;
    private float x1,x2,y1,y2;
    private BookingActivity o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        utils = new Utils(this);
        startRegistration();
        o = new BookingActivity();
        registerReceiver(o.broadcastReceiver, new IntentFilter(
                     "CHAT_MESSAGE_RECEIVED"));
        getloc();
        updatelocation();
        addGoogleMap();
        mDrawerList = (ListView)findViewById(R.id.List);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        jj();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    public void onClickre(View v){
        Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_SHORT).show();
        CheckUserType.intentservice(Passengeractivity.this);

    }

    //for custom list
    void addDrawerItems() {
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

                    CheckUserType.intentservice(Passengeractivity.this);
                } else if (Slecteditem.equals("Profile")) {

                    Passengeractivity.this.startActivity(new Intent(Passengeractivity.this, Profile.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if (Slecteditem.equals("Help")) {

                    Passengeractivity.this.startActivity(new Intent(Passengeractivity.this, Help.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if (Slecteditem.equals("Sign Out")) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Passengeractivity.this);
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
                                                unregIntent.putExtra("app", PendingIntent.getBroadcast(Passengeractivity.this, 0, new Intent(), 0));
                                                startService(unregIntent);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            Passengeractivity.this.startActivity(new Intent(Passengeractivity.this, SignOut.class)
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


    private void  addGoogleMap() {
        // check if we have got the googleMap already
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            // googleMap.setOnMarkerClickListener(this);
            LatLng passenger = new LatLng(latitude,longitude);
            googleMap.addMarker(new MarkerOptions().position(passenger).title("" + LoginDetails.Username)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.userm))).showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(passenger));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }

    }

    public void getloc() {
        // create class object
        gps = new GPSTracker22(Passengeractivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            LatLongDetails.user_latitude= latitude;
            LatLongDetails.user_longitude=longitude;

        } else
        {
            gps.showSettingsAlert();
        }

    }
    public void addMarkers(String email,Double lt,Double lg) {
        googleMap.setOnMarkerClickListener(this);
        LatLng taxi = new LatLng(lt, lg);
        googleMap.addMarker(new MarkerOptions()

                .position(taxi)
                .title("" + email)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.taxim)));


    }



    private void showDialog(String n) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialog inputNameDialog = new dialog(n);
        LoginDetails.selecteddirver = n;
        inputNameDialog.setCancelable(false);
        inputNameDialog.setDialogTitle("Driver Info");
        inputNameDialog.show(fragmentManager, "Driver Info");
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("GoogleMapActivity", "onMarkerClick");
        String name=marker.getTitle();
        showDialog(name);
        utils.savedata("driver", LoginDetails.selecteddirver);
        return false;
    }



    public void jj() {
        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost("http://cab.luminativesolutions.com/cabbooking/ws/get_user_drivers.php");
// Depends on your web service
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            awen = sb.toString();
            System.out.println("contactlist---->" + awen);
            array = new JSONArray(awen);
            JSONObject object;
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject json = array.getJSONObject(i);
                    String id = json.getString("id");
                    name = json.getString("fullname");
                    String email = json.getString("email");
                    String image = json.getString("image");
                    String password = json.getString("password");
                    String mobile = json.getString("mobile");
                    String name_on_card = json.getString("name_on_card");
                    String card_num = json.getString("card_num");
                    String exp_date = json.getString("exp_date");
                    String cvv_num = json.getString("cvv_num");
                    String balance = json.getString("balance");
                    String paid_yet = json.getString("paid_yet");
                    String add_date = json.getString("add_date");
                    String usertype = json.getString("usertype");
                    String latitude = json.getString("latitude");
                    String longitude = json.getString("longitude");
                    String cab_type = json.getString("cab_type");
                    String cab_no = json.getString("cab_no");
                    String coupon_code = json.getString("coupon_code");
                    String refer_count = json.getString("refer_count");
                    String status = json.getString("status");

                    LatLongDetails.driver_latitude = Double.valueOf(latitude).doubleValue();
                    LatLongDetails.driver_longitude = Double.valueOf(longitude).doubleValue();
                    System.out.println("contactlist---->" + name);
                    System.out.println("contactlist---->" + email);
                    System.out.println("contactlist---->" + LatLongDetails.driver_latitude);
                    System.out.println("contactlist---->" + LatLongDetails.driver_longitude);

                    addMarkers(email, LatLongDetails.driver_latitude, LatLongDetails.driver_longitude);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception squish) {
            }
        }
    }

    @Override
    public void onBackPressed() {

        obj.recorddelete();

        try {
            Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
            unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
            startService(unregIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Passengeractivity.this.startActivity(new Intent(Passengeractivity.this, SignOut.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
       /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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


    void startRegistration() {

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            Log.i(TAG, "Google Play Services OK");
            gcm = GoogleCloudMessaging.getInstance(this);
            utils = new Utils(this);
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