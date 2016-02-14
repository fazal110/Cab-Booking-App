package luminative.cab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import epbit.Login.LoginDetails;

/**
 * Created by Muhammad Tahir on 2/3/2016.
 */
public class rating extends Activity {

    private RatingBar ratingBar;
    private TextView name;
    EditText review;
    private Button btnSubmit;
    String revi , ratting;
    float rat;
    HttpPost httppost;
    HttpClient httpclient;
    String TAG = "MainActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        review = (EditText)findViewById(R.id.review);
        name = (TextView) findViewById(R.id.nn);
        name.setText(LoginDetails.selecteddirver);
        addListenerOnRatingBar();

    }

    public void addListenerOnRatingBar() {

        //if rating is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                rat = rating;
            }
        });
    }


            public void onClickreview(View v) {

                revi = review.getText().toString();
                ratting = String.valueOf(rat);
                insert();

                startActivity(new Intent(rating.this, Passengeractivity.class));

            }



    public void insert(){

        Log.i(TAG, "insert ratting");

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    httpclient = new DefaultHttpClient();

                    String link = "http://cab.luminativesolutions.com/cabbooking/ws/insert_review.php?stars="+ratting+"&review_name="+LoginDetails.selecteddirver+"&description="+revi;
                    httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,responseHandler);

                    f(response);
                    Log.i(TAG, "Response : " + response);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
void f(String msg){

    Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();

}

}
