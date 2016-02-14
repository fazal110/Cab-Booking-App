package luminative.cab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends Activity {

    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    Utils utils;
    Intent i;
    static String TAG = "GCM DEMO";
    String user_name;
    String regid;

    String SENDER_ID = "224163385438";
    String API_KEY = "AIzaSyCL3REK_ONEgLdhcP8giso_5P6xWE3gUvA";
    private GCM gcmreg;
    private ProgressDialog pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        i = getIntent();
        sendMessage();
       // registerReceiver(broadcastReceiver, new IntentFilter(
         //       "CHAT_MESSAGE_RECEIVED"));

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();

            String message = b.getString("message");

          //  Log.i(TAG, " Received in Activity " + message + ", NAME = "
           //         + i.getStringExtra("username"));


        }
    };

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(BookingActivity.this);
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

    public void sendMessage() {

        showPB("Notification is sending to a specified driver, Please wait");
        final String messageToSend = "Driver you are now booked by: "+i.getStringExtra("username");


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
                                "registrationIDs", i.getStringExtra("regid")));
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
                        startActivity(new Intent(BookingActivity.this,Passengeractivity.class));
                    } catch (Exception e) {
                        Log.d(TAG, "Exception : " + e.getMessage());
                    }
                }
            };

            thread.start();

        }

    }

}
