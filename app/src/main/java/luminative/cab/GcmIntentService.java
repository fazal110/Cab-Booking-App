package luminative.cab;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;

import epbit.Login.LoginDetails;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	public static final String TAG = "GcmIntentService";
	private PendingIntent pIntent;
	private PaymentClass paymentclass;
	private PayPalPayment thingToBuy;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);
		System.out.println("yahan tak araha haa bahar ");
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			System.out.println("yahan tak araha haa andar ");
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(extras.getString("message"));
				//alertView(extras.getString("message"));
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
		System.out.println("yahan tak araha haa wakelook ");
		
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.


	private void sendNotification(String msg) {




		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// intent triggered, you can add other intent for other actions
		//Intent intent = new Intent(this, NotificationReceiver.class);
		int requestCode = ("someString" + System.currentTimeMillis()).hashCode();
		if(msg.equals("Driver Accepted your request")) {
Intent n = new Intent(this,PaymentClass1.class);
		n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(n);

			/*paymentclass = PaymentClass.getinstance();
			startPaymentService();
			PayPalPayment thingToBuy = paymentclass.getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
			Intent intent = new Intent(this, PaymentActivity.class);

			// send the same configuration for restart resiliency
			intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PaymentClass.config);

			intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

			//paymentclass.startPaypalActivity();
			pIntent = PendingIntent.getActivity(this, requestCode,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			//PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
			*/
		}
		else {
			String [] parts = msg.split(": ");

			String numberPart = parts[0];
			String username = parts[1];
			LoginDetails.Pass = username;
			pIntent = PendingIntent.getActivity(this, requestCode,
					new Intent(this, NotificationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		}
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.cab);
		//alertView(msg);


		Notification mNotification = new Notification.Builder(this)

				.setContentTitle("Message Received")
				.setContentText(msg)
				///////////////////////////////
				.setTicker("New Notification From Cab Booking App")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentIntent(pIntent)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cab))
				.setStyle(new Notification.BigPictureStyle().bigPicture(b).setBigContentTitle(msg))
				.setSound(soundUri)
				.build();




		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// If you want to hide the notification after it was selected, do the code below
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify((int) System.currentTimeMillis(), mNotification);



		/*

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, ChatActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("GCM Notification")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		*/
	}

	/*private void alertView( String message ) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle("New Msg")
				.setIcon(R.mipmap.ic_launcher)
				.setMessage(message)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						dialoginterface.cancel();
					}})
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}
*/
	public void startPaymentService(){
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PaymentClass.config);
		startService(intent);
	}

	private void alertView( String message ) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle("POPUP")
				.setIcon(R.mipmap.ic_launcher)
				.setMessage(message)
//  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}






}