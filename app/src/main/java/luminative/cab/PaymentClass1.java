package luminative.cab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

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

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import epbit.Login.LoginDetails;

/**
 * Created by Fazal on 26-Jan-16.
 */
public class PaymentClass1 extends Activity {

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AVufKYUI0KMITj6Jwb3Reg2_i1aenbTud35sJLWub1RGXs-X54sieLx1n_IkQ97iBDwBUvesF4hzBGR9";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
    Button payPal;
    String TAG = "paypalactivity";
    HttpPost httppost;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    JSONArray array;
    InputStream is=null;
    String result=null;
    String line=null;
    int code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        onBuyPressed();
    }

    private static PaymentClass1 paymentClass1 = new PaymentClass1();
    public static PaymentClass1 getinstance(){
        return paymentClass1;
    }

    public void onBuyPressed() {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(PaymentClass1.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    public PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(LoginDetails.Priced), "USD", "Driver Payment",
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e("Show============", confirm.toJSONObject().toString(4));
String na = confirm.toJSONObject().toString();
System.out.println("tahir=====" + na);

                        Log.e("Show0000000000000000", confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         */
                   inerst();
                        Toast.makeText(getApplicationContext(), "PaymentConfirmation info received" +
                                " from PayPal", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "an extremely unlikely failure" +
                                " occurred:", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "The user canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(getApplicationContext(), "An invalid Payment or PayPalConfiguration" +
                        " was submitted. Please see the docs.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void senddataintodatabase() {

        Log.i(TAG, "senddataintodatabase");

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    httpclient = new DefaultHttpClient();

                    // yahan reg id ki server webserivcice dalegi
                    System.out.println("tahanansdad");
                    httppost = new HttpPost("http://cab.luminativesolutions.com/cabbooking/ws/insert_invoice.php?");
                    nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("invoiceno",
                            null
                    ));
                    nameValuePairs.add(new BasicNameValuePair("pessangername","aa"));
                    nameValuePairs.add(new BasicNameValuePair("address", null));
                    nameValuePairs.add(new BasicNameValuePair("amount", "ss"));
                    nameValuePairs.add(new BasicNameValuePair("drivername", "dd"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

    public void inerst(){

        Log.i(TAG, "Update location");

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {


                    httpclient = new DefaultHttpClient();

                    String link = "http://cab.luminativesolutions.com/cabbooking/ws/insert_invoice.php?invoiceno="+null+"&pessangername="+LoginDetails.Username+"&address="+null+"&amount="+LoginDetails.Priced+"&drivername="+LoginDetails.selecteddirver;
                    httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i(TAG, "Response : " + response);

                    Intent in = new Intent(getBaseContext(),rating.class);
                    startActivity(in);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }




    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
