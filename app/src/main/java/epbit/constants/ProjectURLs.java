package epbit.constants;

public class ProjectURLs {

	public static String BASE_URL = "http://cab.luminativesolutions.com/cabbooking/ws/";

	//Pre Login Urls
	public static String REGISTER_URL = BASE_URL + "user_register.php";
	public static String LOGIN_URL_STRING = BASE_URL + "checklogin1.php";

	public static String FORGOT_PASSWORD_URL = BASE_URL + "forgot_password.php";

	public static String PROFILE_ACTIVITY_URL = BASE_URL + "user_profile.php";

	public static String HELP_PROJECT_URL = BASE_URL + "help.php";

	public static String UPDATE_PROFILE_PIC_URL = BASE_URL
			+ "upload_user_pic.php";

	public static String getProfileUrl(String username, String user_type) {
		return PROFILE_ACTIVITY_URL + "?email=" + username + "&user_type="
				+ user_type.toLowerCase();
	}

	

}