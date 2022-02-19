package jp.jagfukuoka.sodefuri.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * ����I��bluetooth����������T�[�r�X
 * @author shikajiro
 *
 */
public class RecentService extends Service {
	public static final String MEET = "meet";
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * �P��������bluetooth����������
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				sendBroadcast(new Intent(MEET));
			}
		};
		timer.schedule(task, 30 * 1000);
	}

}
