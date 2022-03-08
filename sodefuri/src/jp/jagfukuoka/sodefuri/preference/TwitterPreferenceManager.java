package jp.jagfukuoka.sodefuri.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * �S�Ă�activity�̊��N���X
 * 
 * @author shikajiro
 * 
 */
public class TwitterPreferenceManager {
	private Context context;

	private TwitterPreferenceManager() {
		super();
	}

	public TwitterPreferenceManager(Context context) {
		this();
		this.context = context;
	}

	// twitter consumer key & sercret
	// TODO �O���t�@�C����
	public static final String CONSUMER_KEY = "OJLV3hMh1EAbkNIzjB3IqA";
	public static final String CONSUMER_SERCRET = "Go0XfTVbHjtbxo2Asl3fMCLoXH7idRkQmjMv7quaMcI";

	public static final String REQUEST_TOKEN = "request_token";
	public static final String REQUEST_TOKEN_SERCRET = "request_token_sercret";

	public static final String ACCESS_TOKEN = "access_token";
	public static final String ACCESS_TOKEN_SERCRET = "access_token_sercret";

	/**
	 * pin�L�[����͂��邽�߂�requestToken��ێ�����
	 * 
	 * @param token
	 * @param tokenSecret
	 */
	public void storeRequestToken(String token, String tokenSecret) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(REQUEST_TOKEN, token);
		editor.putString(REQUEST_TOKEN_SERCRET, tokenSecret);
		editor.commit();
	}

	/**
	 * �ꎞ�ۑ�����requestToken���擾����B
	 * 
	 * @return
	 */
	public String getRequestToken() {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(REQUEST_TOKEN, "");
	}

	/**
	 * �ꎞ�ۑ�����requestTokenSercret���擾����B
	 * 
	 * @return
	 */
	public String getRequestTokenSercret() {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(REQUEST_TOKEN_SERCRET, "");
	}

	/**
	 * twitter��requestToken�̗L�����`�F�b�N����B
	 * 
	 * @return
	 */
	public boolean isRequestToken() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String requestToken = preferences.getString(REQUEST_TOKEN, "");
		String requestTokenSercret = preferences.getString(
				REQUEST_TOKEN_SERCRET, "");
		if (requestToken != null && requestToken.length() > 0) {
			if (requestTokenSercret != null && requestTokenSercret.length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �i��������Ă���access_token���擾����B
	 * 
	 * @return
	 */
	public String getAccessToken() {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(ACCESS_TOKEN, "");
	}

	/**
	 * �i��������Ă���access_token���擾����B
	 * 
	 * @return
	 */
	public String getAccessTokenSercret() {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(ACCESS_TOKEN_SERCRET, "");
	}

	/**
	 * twitter��token��ݒ肷��B
	 * 
	 * @param accessToken
	 * @param accessTokenSercret
	 */
	public void storeAccessToken(String accessToken,String accessTokenSercret) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(ACCESS_TOKEN, accessToken);
		editor.putString(ACCESS_TOKEN_SERCRET, accessTokenSercret);
		editor.commit();
	}

	/**
	 * twitter��token�̗L�����`�F�b�N����B
	 * 
	 * @return
	 */
	public boolean isAccessToken() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String accessToken = preferences.getString(ACCESS_TOKEN, "");
		String accessTokenSercret = preferences.getString(ACCESS_TOKEN_SERCRET,
				"");
		if (accessToken != null && accessToken.length() > 0) {
			if (accessTokenSercret != null && accessTokenSercret.length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * twitter�̃X�N���[���l�[����preference�ɓo�^����B
	 * 
	 * @param screen_name
	 */
	public void storeScreenName(String screen_name) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString("pre_twitter_name", screen_name);
		editor.commit();
	}
}
