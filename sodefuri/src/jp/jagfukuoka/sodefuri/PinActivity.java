package jp.jagfukuoka.sodefuri;

import jp.jagfukuoka.sodefuri.preference.TwitterPreferenceManager;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class PinActivity extends Activity {
	private TwitterPreferenceManager tpm = new TwitterPreferenceManager(this);  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_input);
		
		View pinButton = findViewById(R.id.PinButton);
		pinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(TwitterPreferenceManager.CONSUMER_KEY, TwitterPreferenceManager.CONSUMER_SERCRET);
				RequestToken requestToken = new RequestToken(tpm.getRequestToken(), tpm.getRequestTokenSercret());
				try {
					// pin�R�[�h���g�p���ĔF�؂���
					String pin = ((EditText) findViewById(R.id.PinText)).getText().toString();
					AccessToken oAuthAccessToken = twitter.getOAuthAccessToken(requestToken, pin);
					tpm.storeAccessToken(oAuthAccessToken.getToken(), oAuthAccessToken.getTokenSecret());
					tpm.storeScreenName(twitter.getScreenName());
					
					//�����̃^�C�����C����\��
					ResponseList<Status> homeTimeline = twitter.getHomeTimeline();
					for(Status status : homeTimeline){
						String text = status.getText();
						Log.d("hometimeline", text);
					}

//					checkBluetooth();
					startActivity(new Intent(getApplicationContext(),RecentListViewActivity.class));
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}
}
