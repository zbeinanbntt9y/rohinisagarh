package jp.android.fukuoka.gpstomail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class Main extends Activity {
	private Button btn1;
	private Button btn2;
	
	//�ݒ莞�ԂŌĂяo����鏈��
	private class KitchenTimerReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			//�����Đ�
			MediaPlayer mp = MediaPlayer.create(Main.this, R.raw.alarm);
	    	Log.i("GPStoMail","Mail Sending");
	    	try {
	    		//�^�X�N���t�H�A�O�����h�֖߂�����
	    		onBackPressed();
	    		//�ʒu���擾����
	    		Intent intentgps = new Intent(context ,LocationGet.class);
	    		intentgps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		
	    		startActivity(intentgps);
				
	    		mp.start();
	    		
			} catch (Exception e) {
				// ��O�͔������Ȃ�
			}
		}
	}
	
	private KitchenTimerService kitchenTimerService;
	private final KitchenTimerReceiver receiver = new KitchenTimerReceiver();
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			kitchenTimerService = ((KitchenTimerService.KitchenTimerBinder)service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			kitchenTimerService = null;
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		final TimePicker timePicker = (TimePicker)findViewById(R.id.TimePicker01);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(0);
		timePicker.setCurrentMinute(1);
		
		btn1 = (Button)findViewById(R.id.Button_SET);
		btn1.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View view) {
				long hour = timePicker.getCurrentHour();
				long min = timePicker.getCurrentMinute();
				
				//Timer set
				kitchenTimerService.schedule((hour * 60 + min) * 60 * 1000);
				
				//Activity ���\���փZ�b�g
				moveTaskToBack(true);
			}
			
		});
		btn2 = (Button)findViewById(R.id.Button_END);
		btn2.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View view) {
				//�A�v���I��
				finish();
			}
			
		});
		
		// �T�[�r�X���J�n
		Intent intent = new Intent(this, KitchenTimerService.class);
		startService(intent);
		IntentFilter filter = new IntentFilter(KitchenTimerService.ACTION);
		registerReceiver(receiver, filter);
		
		// �T�[�r�X�Ƀo�C���h
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

		// ��������A���o�C���h���Ă���ēx�o�C���h
		unbindService(serviceConnection);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection); // �o�C���h����
		unregisterReceiver(receiver); // �o�^����
		kitchenTimerService.stopSelf(); // �T�[�r�X�͕K�v�Ȃ��̂ŏI��������B
	}
}