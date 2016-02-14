package epbit.constants;

public interface IWebConstant {

	/**
	 * Google API Key for Maps
	 * 
	 */
	String Browser_GOOGLEAPI_KEY = "AIzaSyDVGUZdHWnDfUnid9uivAaQSP38NmV4LC4";
	
	String SERVER_KEY_FORGCM="AIzaSyCBI_c2izVrEPcJ509uZGVLdfWUAW-rg48";
	/**
	 * GCM PROJECT ID used fro notifications
	 * ]myan cab
	 */
	public static final String SENDER_ID = "243750827028";
	/**
	 * SPLASH TIME OUT
	 */

	int SPLASH_TIMEOUT = 3000;
	int SPLASH_PROGRESS_TIME = 2000;

	/**
	 * STRINGS FOR UI
	 */
	String GCM_INTERNET_ERROR = "Internet Connection Error";
	String GCM_INTERNET_ERROR_MESSAGE = "Please connect to working Internet connection";

	String EMAIL_NOT_ENTERED = "Please Enter Email-Id";
	String EMAIL_NOT_VALID = "Please Enter Valid Email-Id";
	String ENTER_PASSWORD = "Please Enter password";
	String CONFIRM = "confirm";
	String REJECT = "reject";

	// WEB SERVICE RESULT CODE
	int RESULT_SUCCESS = 1;
	int RESULT_ERROR_EMAIL_ID_EXIST = 20;
	int RESULT_ERROR_EMAIL_ID_DOESNT_EXIST = 30;
	int RESULT_ERROR_WRONG_CREDENTIALS = 40;
	int RESULT_ERROR_BLANK_GCM_ID = 50;
	int RESULT_ERROR_INVALID_AUTH_TOKEN = 100;
	String sharedpreferencesname = "PREF";

	// WEB SERVICE KEYS

	/**
	 * STRING FOR WEB SERVICES
	 */

	String Driver_autoupdate_email = "d_email";
	String Driver_autoupdate_lat = "d_lat";
	String Driver_autoupdate_long = "d_long";
	String NAME_VALUE_PAIR_KEY_CAB_TYPE = "cab_type";
	String NAME_VALUE_PAIR_KEY_EMAIL = "email";
	String NAME_VALUE_PAIR_KEY_UPDATEPIC = "updatepic";
	String NAME_VALUE_PAIR_KEY_DRIVER_SEND_USER_EMAIL = "noti_user_email";
	String NAME_VALUE_PAIR_KEY_DRIVER_EMAIL = "d_email";
	String NAME_VALUE_PAIR_KEY_DRIVER_STATUS = "driver_status";
	String NAME_VALUE_PAIR_KEY_DRIVER_CAB_TYPE = "d_cabtype";
	String NAME_VALUE_PAIR_KEY_DRIVER_NAME = "d_name";
	String NAME_VALUE_PAIR_KEY_PICK_DATE = "pick_date";
	String NAME_VALUE_PAIR_KEY_PICK_TIME = "pick_time";
	String NAME_VALUE_PAIR_KEY_PICK_ADDRESS = "pick_address";
	String NAME_VALUE_PAIR_KEY_DEST_ADDRESS = "dest_address";
	String NAME_VALUE_PAIR_KEY_CAB_NUMBER = "cab_number";
	String NAME_VALUE_PAIR_KEY_COUPON_USED = "coupon_used";
	String Name_VALUE_PAIR_KEY_DISTANCE = "distance";
	String NAME_VALUE_PAIR_KEY_CONFIRM_CAB = "confirm_reject_cab";
	String NAME_VALUE_PAIR_KEY_ID = "unique_id";
	String NAME_VALUE_PAIR_USER_TYPE = "user_type";
	String Name_VALUE_PAIR_KEY_TRANS_ID = "trans_id";
	String NAME_VALUE_PAIR_KEY_TRANS_STATE = "trans_state";
	String NAME_VALUE_PAIR_KEY_PAYMENT_ID = "payment_id";
	String NAME_VALUE_PAIR_REFER_COUNT = "count";
	String NAME_VALUE_PAIR_REFER_IDS = "friends_ids";

	// base Keys
	String REQUEST_PARAMETER_KEY_NAME = "name";
	String REQUEST_PARAMETER_KEY_EMAIL = "email";
	String REQUEST_PARAMETER_KEY_PASSWORD = "password";
	String REQUEST_PARAMETER_KEY_CONTACT = "contact";
	String REQUEST_PARAMETER_KEY_UDID = "udid";
	String REQUEST_PARAMETER_KEY_OTP = "otp";
	String REQUEST_PARAMETER_KEY_IMEI = "imei_num";
	String REQUEST_PARAMETER_KEY_SMS_DATA = "smsdata";
	String REQUEST_PARAMETER_KEY_MANUFACTURER_NAME = "device_brand";
	String REQUEST_PARAMETER_KEY_MODEL_NUM = "device_model";
	String REQUEST_PARAMETER_KEY_ANDROID_VERSION = "android_version";
	String REQUEST_PARAMETER_KEY_AUTH_TOKEN = "auth_token";
	String REQUEST_PARAMETER_KEY_DATA = "data";
	String REQUEST_PARAMETER_KEY_GCM_ID = "gcm_id";
	String REQUEST_PARAMETER_KEY_LAT = "lat";
	String REQUEST_PARAMETER_KEY_LONG = "long";
	String REQUEST_PARAMETER_KEY_TYPE = "type";
	String REQUEST_PARAMETER_KEY_BODY = "body";
	String REQUEST_PARAMETER_KEY_SIZE = "size";
	String REQUEST_PARAMETER_KEY_INCOMING_SMS_FIRST_SYNCH = "incomingfirstsmssynch";
	String REQUEST_PARAMETER_KEY_OUTGOING_SMS_FIRST_SYNCH = "outgoingfirstsmssynch";

	String REQUEST_PARAMETER_KEY_VERIFIED = "is_verified";
	String REQUEST_PARAMETER_KEY_TODAY_TASK = "T";
	String REQUEST_PARAMETER_KEY_ALL_TASK = "A";

	// LOG TAGS
	String TAG_REGISTER = "REGISTERATION";
	String TAG_LOGIN = "LOGIN";
	String TAG_TASK_LIST = "TASK_LIST";
	String TAG_LOCATION_UPDATE = "LOCATION_UPDATE_TAG";
	String TAG_FORGOT = "FORGOT ACTIVITY";

	// RESPONSE KEYS
	String RESPONSE_KEY_RESPONSE = "response";
	String RESPONSE_KEY_USERID = "userid";
	String RESPONSE_KEY_RESULT = "result";
	String RESPONSE_KEY_DESC = "description";
	String RESPONSE_KEY_MESSAGE = "message";
	String RESPONSE_KEY_AUTH_TOKEN = "auth_token";
	String RESPONSE_KEY_TASK_ID = "task_id";
	String RESPONSE_KEY_TASK_SUBJECT = "subject_line";
	String RESPONSE_KEY_TASK_BODY = "task_description";
	String RESPONSE_KEY_DATE = "date";
	String RESPONSE_KEY_TIME = "time";
	String RESPONSE_KEY_TASK_STATUS = "task_status";
	String RESPONSE_KEY_STATUS = "status";
	String RESPONSE_KEY_TASK_DATA = "task_data";
	String GCM_ALREADY_REGISTER_MESSAGE = "ALREADY REGISTERED";
	String NOTI_KEY_NEW_TASK = "new_task";
	String NOTI_KEY_LOC_INTERVAL_UPDATE = "loc_interval";
	String NOTI_KEY_POST_CURRENT_LOC = "current_loc";
	String NOTIFICATION_TASK_TITLE = "New Task Assigned";
	int RESPONSE_CODE_FORBIDDEN = 403;
	int RESPONSE_CODE_RESOURCE_NOT_FOUND = 404;
	int RESPONSE_CODE_SERVER_ERROR = 500;
	int ACTIVITY_MARK_TASK = 110;
	String APP_ACTION = "com.vinay.alarm.ACTION";
	String DISTANCE_UNITS = "imperial";
}
