package luminative.cab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import epbit.Login.LoginDetails;

/**
 * Created by Muhammad Tahir on 1/18/2016.
 */
public class GCM extends Activity {


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
    Utils utils = new Utils(this);
    private Context context = GCM.this;
    static String t,r;
    private ProgressDialog pb;



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

    void getPeopleList() {

        hidePB();
        showPB("Getting People to Chat...");

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String response = "";

                try {

                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://cab.luminativesolutions.com/cabbooking/ws/gcm/get_people_list.php");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("username",
                            utils.getFromPreferences(Utils.UserName)));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    Log.i(TAG, "USER :" + utils.getFromPreferences(Utils.UserName));
                    Log.i(TAG, "Response : " + response);
                    System.out.println("dfdfdf ===============" + response);
                } catch (Exception ex) {

                    Log.d(TAG, "Error : " + ex.getMessage());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            utils.showToast("Server Not responding, Please check whether your server is running or not");
                        }
                    });

                }

                return response;
            }

            @Override
            protected void onPostExecute(String response) {

                if (!response.equalsIgnoreCase("No People")) {
                    //peopleObjList = new ArrayList<PeopleObject>();
                    // parse JSON here

                    try {
                        JSONArray jArray = new JSONArray(response);

                        for (int j = 0; j < jArray.length(); j++) {

                            JSONObject jsonObj = jArray.getJSONObject(j);
                            String username = jsonObj.getString("username");
                            String reg_id = jsonObj.getString("reg_id");

                            if(username.equals(LoginDetails.selecteddirver)) {
                                // PeopleObject p = new PeopleObject();
                                //   p.setPersonName(username);
                                GCM.t= username;
                                GCM.r = reg_id;
                                //   p.setRegId(reg_id);
                                //  peopleObjList.add(p);
                                // j = jArray.length();
                            }

                        }
                        System.out.println("selected ===============" + t);
                        hidePB();
                        Intent i = new Intent(GCM.this,BookingActivity.class);
                        i.putExtra("username",utils.getFromPreferences(Utils.UserName));
                        i.putExtra("regid", GCM.r);
                        startActivity(i);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //  if (peopleObjList.size() > 0) {

                    //    Message msgObj = handler.obtainMessage();
                    //    Bundle b = new Bundle();
                    //     b.putSerializable("people_list",
                    //            (Serializable) peopleObjList);
                    //    msgObj.setData(b);
                    //   handler.sendMessage(msgObj);

                }
                // } else {
                //     hidePB();
                //      utils.showToast("No People registered for Chat...");
                //  }

            }

            // Define the Handler that receives messages from the thread and
            // update the progress
            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {

                    //     @SuppressWarnings("unchecked")
                    //  ArrayList<PeopleObject> peopleList = (ArrayList<PeopleObject>) msg
                    //         .getData().getSerializable("people_list");

                    //  if (peopleList.size() > 0) {

                    //     ArrayAdapter<PeopleObject> adapter = new ListAdapter(
                    //            MainActivity.this, peopleList);
                    //     list.setAdapter(adapter);

                    // } else {

                    //  }


                    hidePB();
                }
            };
        }.execute(null, null, null);
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
            if (regid.isEmpty()) {
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


    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(GCM.this);
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


}
