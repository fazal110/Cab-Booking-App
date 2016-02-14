package epbit.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;

import org.json.JSONException;

import java.io.IOException;

import luminative.cab.R;
import epbit.constants.IWebConstant;
import epbit.exception.NetworkException;
import epbit.exception.ParsingException;
import epbit.exception.ServerException;
import epbit.exception.TimeOutException;
import epbit.utils.InternetUtil;

/**
 * Asyntask to provide common handling to all asyntask used.
 * 
 * @param <T1>
 * @param <T2>
 * @param <T3>
 */
public abstract class AppAsynchTask<T1, T2, T3> extends
		AsyncTask<T1, T2, T3> {

	protected ProgressDialog progressDialog;
	private Context activity;
	protected String dialogMessage = "Please wait....";
	private Exception exception;
	private boolean is_dialog_cancelable;
	private boolean showdialog=true;

	public AppAsynchTask(Activity activity) {
		this.activity = activity;
	}
	
	public AppAsynchTask(Activity activity, int dialog_id,boolean is_dialog_cancelable) {
		this.activity = activity;
		dialogMessage = activity.getResources().getString(dialog_id);
		this.is_dialog_cancelable=is_dialog_cancelable;
		

	}

	@Override
	protected void onCancelled() {

		if (isShowdialog()&&this.progressDialog != null) {
			this.progressDialog.dismiss();
		}
	}

	@Override
	protected void onPreExecute() {
		try {
			if (InternetUtil.isInternetOn(activity)) {
				if(isShowdialog()){
				progressDialog = new ProgressDialog(activity,
						AlertDialog.THEME_HOLO_LIGHT);
				progressDialog.setTitle("");

				progressDialog.setMessage(dialogMessage);

				progressDialog.setCancelable(is_dialog_cancelable);
				progressDialog.setIndeterminate(true);
				progressDialog.show();
				progressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								// cancel AsyncTask
								cancel(false);
							}
						});
				}

			} else {

				InternetUtil.showErrorDialog(activity, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(T3 result) {

		try {

			if (isShowdialog()&&progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (exception == null) {
				if (result != null)
					customOnPostExecute(result);
				else {
					showAlertDialog(
							activity,
							getStringFromResources(R.string.activity_base_alert_message_error));
				}

			} else {
				handleException(activity, exception);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected T3 doInBackground(T1... params) {
		T3 result = null;
		try {
			if (!isCancelled()) {
				result = customDoInBackground(params);
			}

		} catch (NetworkException e) {
			e.printStackTrace();
			this.exception = e;
		} catch (ServerException e) {
			e.printStackTrace();
			this.exception = e;
		} catch (ParsingException e) {
			e.printStackTrace();
			this.exception = e;
		} catch (TimeOutException e) {
			e.printStackTrace();
			this.exception = e;

		} catch (Exception e) {
			this.exception = e;
			e.printStackTrace();
		}

		return result;
	}

	abstract protected T3 customDoInBackground(T1... params)
			throws NetworkException, ServerException, ParsingException,
			TimeOutException, IOException, JSONException;

	abstract protected void customOnPostExecute(T3 result);

	/**
	 * METHOD TO HANDLE EXCEPTION AND TO SHOW ALERT DIALOG
	 * 
	 * @param context
	 * @param exception
	 */
	public void handleException(Context context, Exception exception) {
		if (exception instanceof NetworkException) {
			showAlertDialog(
					context,
					getStringFromResources(R.string.activity_base_alert_message_network_exception));
		}

		if (exception instanceof ParsingException) {
			showAlertDialog(
					context,
					getStringFromResources(R.string.activity_base_alert_message_parsing_exception));
		}
		if (exception instanceof TimeOutException) {
			showAlertDialog(
					context,
					getStringFromResources(R.string.activity_base_alert_message_timeout_exception));
		}
		if (exception instanceof ServerException) {
			showAlertDialog(context, getMessage(ServerException.errorCode));
		}
		

	}

	/**
	 * method to get string from resource
	 * 
	 * @param stringId
	 * @return
	 */
	protected String getStringFromResources(int stringId) {
		return activity.getResources().getString(stringId);
	}

	/**
	 * Method to get String from server error code
	 * 
	 * @param errorCode
	 * @return
	 */
	private String getMessage(int errorCode) {
		String message = getStringFromResources(R.string.server_exception_error_message_general);
		switch (errorCode) {
		case IWebConstant.RESPONSE_CODE_SERVER_ERROR:
			message = getStringFromResources(R.string.server_exception_error_message_server_error);
			break;
		case IWebConstant.RESPONSE_CODE_RESOURCE_NOT_FOUND:
			message = getStringFromResources(R.string.server_exception_error_message_resource_not_found);
			break;
		case IWebConstant.RESPONSE_CODE_FORBIDDEN:
			message = getStringFromResources(R.string.server_exception_error_message_forbidden);
			break;
		default:
			break;
		}
		return message;

	}

	/**
	 * Method to show alert dialog
	 */
	public static void showAlertDialog(final Context context, String message) {

		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			builder = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Holo_Dialog));
		} else {
			builder = new AlertDialog.Builder(new ContextThemeWrapper(context,
					android.R.style.Theme_Dialog));

		}
		builder.setTitle(R.string.error);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.okLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								// if
								// (TransactionProcessingActivity.dialog!=null
								// &&
								// TransactionProcessingActivity.dialog.isShowing())
								// {
								// TransactionProcessingActivity.dialog.cancel();
								//
								// }

							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public boolean isShowdialog() {
		return showdialog;
	}

	public void setShowdialog(boolean showdialog) {
		this.showdialog = showdialog;
	}

}
