package jp.jagfukuoka.sodefuri.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * ���ӂ�bluetooth���������āADB�ɓo�^���郌�V�[�o�[
 * 
 * @author shikajiro
 * 
 */
public class RecentReceiver extends BroadcastReceiver {
	/**
	 * ���ӂ�bluetooth����������
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Time over!", Toast.LENGTH_LONG).show();
		// TODO bluetooth����
		
		// TODO DB�o�^
	}

}
