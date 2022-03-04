package jp.jagfukuoka.sodefuri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import jp.jagfukuoka.sodefuri.preference.MainPreferenceActivity;
import jp.jagfukuoka.sodefuri.provider.RecentContentProvider;
import jp.jagfukuoka.sodefuri.service.RecentReceiver;
import jp.jagfukuoka.sodefuri.service.RecentService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * �����������ԂɃX�N���[���l�[�����\�������Activity
 * @author shikajiro
 *
 */
public class RecentListViewActivity extends ListActivity {
	private static final String FIND_SCREEN_NAME_URL = "http://sodefuri.appspot.com/find_name";

	boolean debug = true;
	public static final String SCREEN_NAME = "SCREEN_NAME";


	
	String[] projection = new String[] { RecentContentProvider.MAC_ADDRESS, };
	List<String> screenNames;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//�ݒ肷��X�N���[���l�[�����擾����
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		preferences.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				// TODO screen_name �o�^����
				if(key.equals("pre_twitter_name")){

				}
			}
		});
		String screen_name = preferences.getString("pre_twitter_name", "");
		if(screen_name.length() < 1){
			//�X�N���[���l�[����o�^���Ă��Ȃ��ꍇ�́A�o�^��ʂ�
			startActivity(new Intent(this,NewAccountActivity.class));
			return;
		}
		
		// bluetooth����service�̋N��
		startService(new Intent(this, RecentService.class));
		registerReceiver(new RecentReceiver(), new IntentFilter(RecentService.SEARCH));
		
		// bluetooth�f�o�C�X��������������receiver�o�^
		registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		
		//�T�[�o�[��screen_name��mac_address��o�^����
		screenNames = getScreenName();
		
		//List�ւ̓o�^
		setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, screenNames));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// �I������screename��Intent�œn���B
				TextView textView = (TextView)view;
				CharSequence screen_name = textView.getText();
				
				Intent intent = new Intent(RecentListViewActivity.this,TimeLineActivity.class);
				intent.putExtra("screen_name", screen_name);
				startActivity(intent);
			}
		});

	}
	List<String> mArrayAdapter = new ArrayList<String>();

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "Bluetooth��������܂���", Toast.LENGTH_LONG).show();

	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	    		Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_LONG).show();

	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            screenNames.add(device.getName() + "\n" + device.getAddress());
	        }
			setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, screenNames));

	    }
	};
	private static final int GROUP_ID = 1;
	private static final int SETTING_ITEM_ID = 1;
	private static final int BLUETOOTH_ITEM_ID = 2;
	private static final String SETTING = "�ݒ�";
	private static final CharSequence BLUETOOTH = "���ӌ���";

	/**
	 * �I�v�V�������j���[
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(GROUP_ID, SETTING_ITEM_ID, 0, SETTING).setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(GROUP_ID, BLUETOOTH_ITEM_ID, 0, BLUETOOTH);//FIXME bluetooth�̃A�C�R��
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SETTING_ITEM_ID:
			startActivity(new Intent(this, MainPreferenceActivity.class));
			break;
		case BLUETOOTH_ITEM_ID:
			startActivity(new Intent(this, BluetoothFoundActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * server����mac_address�����Ƃ�screen_name���擾����
	 * 
	 * @return
	 */
	private List<String> getScreenName() {

		// json�쐬
		String json = this.getMacAddressJson();

		// json�ŃX�N���[���l�[����₢���킹��
		List<String> list = this.findScreenNames(json);
		return list;
	}

	/**
	 * �X�N���[���l�[���擾����
	 * 
	 * @param json
	 * @param handler
	 * @return
	 */
	public List<String> findScreenNames(String json) {
		List<String> result = new ArrayList<String>();
		try {
			// -----[POST���M]
			// -----[�N���C�A���g�ݒ�]
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("json", json));
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(FIND_SCREEN_NAME_URL);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			HttpResponse response = httpclient.execute(httpPost);

			// -----[�T�[�o�[����̉������擾]
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Toast.makeText(RecentListViewActivity.this, "�X�N���[���l�[�����擾���܂���", Toast.LENGTH_LONG).show();

				InputStream is = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String responsJson = "";
				String line;
				while ((line = br.readLine()) != null) {
					responsJson += line;
				}

				JSONArray jsonArrays = new JSONArray(responsJson);
				for (int i = 0; i < jsonArrays.length(); i++) {
					JSONObject jsonObj = jsonArrays.getJSONObject(i);
					String screen_name = jsonObj.getString("screen_name");
					result.add(screen_name);
				}

		} else {
			Toast.makeText(RecentListViewActivity.this, "[�X�N���[���l�[���擾error]: " + response.getStatusLine(),
					Toast.LENGTH_LONG).show();
		}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * json�^��mac_address�ꗗ���擾����
	 * @return
	 */
	private String getMacAddressJson() {
		Cursor managedCursor = managedQuery(RecentContentProvider.CONTENT_URI,
				projection, null, null, null);
		JSONArray array = new JSONArray();
		try {
			List<String> list = getColumnData(managedCursor);
			for (String mac_address : list) {
				JSONObject obj = new JSONObject();
				obj.put(RecentContentProvider.MAC_ADDRESS, mac_address);
				array.put(obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  array.toString();
	}

	/**
	 * DB����mac_address�̃f�[�^�𒊏o����
	 * 
	 * @param cur
	 * @return
	 */
	private List<String> getColumnData(Cursor cur) {
		List<String> list = new ArrayList<String>();
		if (cur.moveToFirst()) {
			int int_mac_address = cur
					.getColumnIndex(RecentContentProvider.MAC_ADDRESS);
			do {
				// Get the field values
				String mac_address = cur.getString(int_mac_address);
				// map.put(MAC_ADDRESS, mac_address);
				// map.put(SCREEN_NAME, screen_name);
				list.add(mac_address);
			} while (cur.moveToNext());
		}
		return list;
	}
}
