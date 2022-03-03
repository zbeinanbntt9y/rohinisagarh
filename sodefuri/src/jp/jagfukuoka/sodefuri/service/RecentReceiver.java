package jp.jagfukuoka.sodefuri.service;

import android.bluetooth.BluetoothAdapter;
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
		Toast.makeText(context, "Bluetooth������", Toast.LENGTH_LONG).show();
		
		// bluetooth����
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		bluetoothAdapter.startDiscovery();
	}

}
