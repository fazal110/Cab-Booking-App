package luminative.cab;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import epbit.Login.LoginDetails;

/**
 * Created by Muhammad Tahir on 2/4/2016.
 */
public class functions {

    HttpPost httppost;
    HttpClient httpclient;
    String TAG = "MainActivity";


    public void recorddelete(){

        Log.i(TAG, "Update location");

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    httpclient = new DefaultHttpClient();

                    String link = "http://cab.luminativesolutions.com/cabbooking/ws/delete_record_RegisteredDevices.php?username="+ LoginDetails.Username;
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
