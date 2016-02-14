package luminative.cab;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import epbit.Login.LoginDetails;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "Notification";
    private DefaultHttpClient httpclient;
    private HttpPost httppost;
    private ArrayList<NameValuePair> nameValuePairs;
    private Utils utils;
    String regid;
    String SENDER_ID = "224163385438";
    String API_KEY = "AIzaSyCL3REK_ONEgLdhcP8giso_5P6xWE3gUvA";
    private ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        utils = new Utils(this);
    }

    public void Accept(View view){

        getregid();
        finish();
    }

    public void Reject(View view){
        finish();
    }


    void getregid() {

        //hidePB();
        //showPB("Getting People to Chat...");

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String response = "";

                try {

                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://cab.luminativesolutions.com/cabbooking/ws/gcm/get_people_list.php");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("username",
                            utils.getdata("username")));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    Log.i(TAG, "USER :" + utils.getdata("username"));
                    Log.i(TAG, "Response : " + response);

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
                            System.out.println("pp:============" + LoginDetails.Pass);
                            JSONObject jsonObj = jArray.getJSONObject(j);
                            String username = jsonObj.getString("username");
                            String re = jsonObj.getString("reg_id");
                            System.out.println("user:============"+username);

                            if (LoginDetails.Pass.equals(username)) {
                                regid = re;
                                break;
                                //sendMessage();
                                //return;
                            }

                            /*PeopleObject p = new PeopleObject();
                            p.setPersonName(username);
                            p.setRegId(reg_id);
                            peopleObjList.add(p);*/

                        }
                        sendMessage();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            // Define the Handler that receives messages from the thread and
            // update the progress

        }.execute(null, null, null);
    }



    public void sendMessage() {

        final String messageToSend = "Driver Accepted your request";

        if (messageToSend.length() > 0) {

            Log.i(TAG, "sendMessage");

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        httpclient = new DefaultHttpClient();
                        httppost = new
                                HttpPost("http://cab.luminativesolutions.com/cabbooking/ws/gcm/gcm_engine.php");
                        nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("message",
                                messageToSend));
                        nameValuePairs.add(new BasicNameValuePair(
                                "registrationIDs", regid));
                        nameValuePairs.add(new BasicNameValuePair("apiKey",
                                API_KEY));

                        httppost.setEntity(new UrlEncodedFormEntity(
                                nameValuePairs));
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response = httpclient.execute(httppost,
                                responseHandler);
                        Log.i(TAG, "Response : " + response);
                        if (response.trim().isEmpty()) {
                            Log.d(TAG, "Message Not Sent");
                        }

                    } catch (Exception e) {
                        Log.d(TAG, "Exception : " + e.getMessage());
                    }
                }
            };

            thread.start();

        }
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(NotificationActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
