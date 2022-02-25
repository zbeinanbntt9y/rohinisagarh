package jp.android.fukuoka.gpstomail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
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
	
	private String TAG = "GPS2MAIL";
	private boolean DebugMode = true;
	private CheckBox mCheckBox;
	private LocationManager lm;

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
				startService(intentgps);
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
		timePicker.setCurrentHour(1);
		timePicker.setCurrentMinute(0);
		
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
		
		// LocationManager��GPS�̒l���擾���邽�߂̐ݒ�
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		//GPS��On���`�F�b�N
		chkGpsService();
	}

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.Button_END:
			//�A�v���I��
			finish();
			break;
			
		case R.id.Button_SET:
			//�^�C�}�[�ݒ菈��
			long hour = timePicker.getCurrentHour();
			long min = timePicker.getCurrentMinute();
			
			//Timer set
			kitchenTimerService.schedule((hour * 60 + min) * 60 * 1000,mCheckBox.isChecked());

			//Activity ���\���փZ�b�g
			moveTaskToBack(true);
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
	}

	// GPS���L����Check
	// �L���ɂȂ��Ă��Ȃ���΁A�ݖʂ̕\���m�F�_�C�A���O
	private void chkGpsService() {
		

		//GPS�Z���T�[�����p�\���H
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setMessage(R.string.Ch_GPS_Message);
			alertDialogBuilder.setCancelable(false);

			//GPS�ݒ��ʋN���p�{�^���ƃC�x���g�̒�`
			alertDialogBuilder.setPositiveButton(R.string.Ch_GPS_Positive_btn,
					new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					Intent callGPSSettingIntent = new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					//					callGPSSettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(callGPSSettingIntent);
				}
			});
			//�L�����Z���{�^������
			alertDialogBuilder.setNegativeButton(R.string.Ch_GPS_Negative_btn,
					new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					dialog.cancel();
				}
			});
			AlertDialog alert = alertDialogBuilder.create();
			// �ݒ��ʂֈړ����邩�̖₢���킹�_�C�A���O��\��
			alert.show();
		}
	}
}