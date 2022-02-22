package jp.android.fukuoka.gpstomail;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

public class Main extends Activity implements OnClickListener {

	private Button btn1;
	private Button btn2;
	private Button btn3;
	private TimePicker timePicker;
	private Intent intentgps;
	private Intent intent_Service;
	private IntentFilter filter_Service;
//	private AlarmManager am;
//	private PendingIntent sender;
	
	private String TAG = "GPS2MAIL";
	private boolean DebugMode = true;
	private CheckBox mCheckBox;

	
	//�ݒ莞�ԂŌĂяo����鏈��
	private class KitchenTimerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				
				if(DebugMode){
					Log.i(TAG, "Timer Restar");
				}
				
				//�ʒu���擾����
				intentgps = new Intent(context ,LocationGet.class);
				intentgps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentgps);

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
        
		timePicker = (TimePicker)findViewById(R.id.TimePicker01);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(0);
		timePicker.setCurrentMinute(1);
		
		btn1 = (Button)findViewById(R.id.Button_SET);
		btn1.setOnClickListener(this);
		btn2 = (Button)findViewById(R.id.Button_END);
		btn2.setOnClickListener(this);
		btn3 =(Button)findViewById(R.id.Button_PREF);
		btn3.setOnClickListener(this);
		mCheckBox=(CheckBox) findViewById(R.id.CheckBox01);
		
		// �T�[�r�X���J�n
		intent_Service = new Intent(this, KitchenTimerService.class);
		startService(intent_Service);
		//���V�[�o�[�̓o�^
		filter_Service = new IntentFilter(KitchenTimerService.ACTION);
		registerReceiver(receiver, filter_Service);
		
		// �T�[�r�X�Ƀo�C���h
		bindService(intent_Service, serviceConnection, Context.BIND_AUTO_CREATE);

		// ��������A���o�C���h���Ă���ēx�o�C���h
		unbindService(serviceConnection);
		bindService(intent_Service, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.Button_END:
			//�A�v���I��
			finish();
			break;
			
		case R.id.Button_SET:
			//AlarmManager�Ŏ����H
//			Intent intent = new Intent(this, KitchenTimerReceiver.class);
//			intent.setAction(ACTION);        
//			    
//			sender = PendingIntent.getBroadcast(this, 0, intent, 0);
//			am = (AlarmManager)(this.getSystemService(ALARM_SERVICE));
//			
//			long hour = timePicker.getCurrentHour();
//			long min = timePicker.getCurrentMinute();
//			
//			long interval = ((hour * 60 + min) * 60 * 1000);
//			long firstTime = SystemClock.elapsedRealtime();
//			if (!mCheckBox.isChecked()){
//				firstTime =+ interval;
//			}
//			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime , interval, sender);
			
			//�^�C�}�[�ݒ菈��
			long hour = timePicker.getCurrentHour();
			long min = timePicker.getCurrentMinute();
			
			//Timer set
			kitchenTimerService.schedule((hour * 60 + min) * 60 * 1000);
			
			//SendMail Immediate��true�Ȃ瑗�M
			if (mCheckBox.isChecked()){
				//�ʒu���擾����
				intentgps = new Intent(this ,LocationGet.class);
				intentgps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentgps);
			}

			//Activity ���\���փZ�b�g
//			moveTaskToBack(true);
			break;
			
		case R.id.Button_PREF:
			// �ݒ��ʂ̕\��
        	Intent i = new Intent(view.getContext(),setting.class);
        	startActivity(i);
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		am.cancel(sender);
		unbindService(serviceConnection); // �o�C���h����
		unregisterReceiver(receiver); // �o�^����
		kitchenTimerService.stopSelf(); // �T�[�r�X�͕K�v�Ȃ��̂ŏI��������B
	}
	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();
//		am.cancel(sender);
	}
	
}