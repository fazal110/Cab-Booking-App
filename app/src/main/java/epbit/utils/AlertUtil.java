package epbit.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Class to define show alert dialog and toast.
 * 
 * @author Vinay.
 * 
 */
public class AlertUtil implements OnClickListener {

	private int dialog_id;

	public AlertUtil(int dialog_id) {
		this.dialog_id = dialog_id;
	}

	public static void showToastMessage(Context context, String message) {

		Toast.makeText(context, message, Toast.LENGTH_LONG).show();

	}

	/**
	 * This method is used to show alert dialog.
	 * 
	 * @param context
	 * @param alertMessage
	 * @param isGoBack
	 */
	// @SuppressWarnings("deprecation")
	// public static void showAlertDialogNotification(String alertMessage) {
	// final Context context = BaseActivity.currentVisibleActivity;
	// AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// alertDialog.setMessage(alertMessage);
	// alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// return;
	// }
	// });
	//
	// alertDialog.show();
	// }

	/**
	 * This method is used to show alert dialog.
	 * 
	 * @param context
	 * @param alertMessage
	 */
	@SuppressWarnings("deprecation")
	public static void showAlertDialog(final Context context,
			String alertMessage) {
		// final Context context =
		// MobileSaverBaseActivity.currentVisibleActivity;
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});

		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	public static void showAlertDialogFinishActivity(final Context context,
			String alertMessage) {
		// final Context context =
		// MobileSaverBaseActivity.currentVisibleActivity;
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();

			}
		});

		alertDialog.show();
	}

	/**
	 * This method is used to show alert dialog.
	 * 
	 * @param context
	 * @param alertMessage
	 * @param isGoBack
	 */
	// @SuppressWarnings("deprecation")
	// public static void showAlertDialog(String alertMessage) {
	// final Context context = BaseActivity.currentVisibleActivity;
	// AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// alertDialog.setMessage(alertMessage);
	// alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// BaseActivity.currentVisibleActivity.finish();
	//
	// }
	// });
	//
	// alertDialog.show();
	// }

	static ConnectionDialogClickListener dialoglistener;

	public static AlertDialog.Builder showDialogwithNeutralButton(
			Context context, String title, String errormessage, String button,
			ConnectionDialogClickListener dialogclicklistener, int dialog_id) {
		AlertDialog.Builder alert;
		AlertUtil.dialoglistener = dialogclicklistener;
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Holo_Light_Dialog));
		} else {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Dialog));

		}

		alert.setTitle(title);
		alert.setMessage(errormessage);
		alert.setNeutralButton(button, new AlertUtil(dialog_id));

		return alert;

	}

	public static AlertDialog.Builder showAlertDialogwithListener(
			Context context, int title, int errormessage, int postivebutton,
			int negative_button, ConnectionDialogClickListener listener,
			int dialog_id) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		dialoglistener = listener;
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Holo_Light_Dialog));
		} else {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Dialog));

		}

		alert.setTitle(context.getString(title));
		alert.setMessage(context.getString(errormessage));
		alert.setPositiveButton(context.getString(postivebutton),
				new AlertUtil(dialog_id));
		alert.setNegativeButton(context.getString(negative_button),
				new AlertUtil(dialog_id));

		return alert;
	}

	public static AlertDialog.Builder showDialogwithNeutralButton(
			Context context, int title, int errormessage, int button,
			ConnectionDialogClickListener dialogclicklistener, int dialog_id) {
		AlertDialog.Builder alert;
		AlertUtil.dialoglistener = dialogclicklistener;
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Holo_Light_Dialog));
		} else {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Dialog));

		}

		alert.setTitle(context.getString(title));
		alert.setMessage(context.getString(errormessage));
		alert.setNeutralButton(context.getString(button), new AlertUtil(
				dialog_id));

		return alert;

	}

	public static AlertDialog.Builder showInputDialogwithNeutralButton(
			Context context, int title, int hint, int button,
			ConnectionDialogClickListener dialogclicklistener, int dialog_id,
			EditText input) {
		AlertDialog.Builder alert;
		AlertUtil.dialoglistener = dialogclicklistener;
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Holo_Light_Dialog));
		} else {
			alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Dialog));

		}

		alert.setTitle(context.getString(title));
		alert.setNeutralButton(context.getString(button), new AlertUtil(
				dialog_id));
//		input.setTextColor(Color.WHITE);
		alert.setCancelable(false);
		// input.setHintTextColor(context.getResources().getColor(R.color.white_opacity35));
		input.setHint(hint);
		input.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_SIGNED);
		alert.setView(input);
		return alert;

	}
	

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			AlertUtil.dialoglistener.dialogClicklistener(dialog_id,
					DialogInterface.BUTTON_POSITIVE);
			

			break;
		case DialogInterface.BUTTON_NEGATIVE:
			AlertUtil.dialoglistener.dialogClicklistener(dialog_id,
					DialogInterface.BUTTON_NEGATIVE);
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			AlertUtil.dialoglistener.dialogClicklistener(dialog_id,
					DialogInterface.BUTTON_NEUTRAL);
			break;

		default:
			break;
		}

	}

	/**
	 * 
	 * @params this interface is for tracking of dialog button int button will
	 *         be result either DialogInterface.BUTTON_POSITIVE or
	 *         DialogInterface.BUTTON_NEGATIVE
	 * 
	 */

	public interface ConnectionDialogClickListener {

		public void dialogClicklistener(int dialog_id, int button);
	}

}
