package jp.android.fukuoka.AndroJanken;
import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//�[�������A�N�e�B�r�e�B
public class DeviceListActivity extends Activity 
    implements AdapterView.OnItemClickListener {
    public static String EXTRA_DEVICE_ADDRESS="device_address";

    public static BluetoothAdapter     btAdapter;//BT�A�_�v�^
    private ArrayAdapter<String> devices;  //�f�o�C�X�S

    //�A�v���������ɌĂ΂��
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setResult(Activity.RESULT_CANCELED);

        //���C�A�E�g�̐���
        LinearLayout layout=new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout); 

        
          
        
        //�f�o�C�X
        devices=new ArrayAdapter<String>(this,R.layout.rowdata);
        
        //���X�g�r���[�̐���
        ListView listView=new ListView(this);
        setLLParams(listView);
        listView.setAdapter(devices);
        layout.addView(listView);
        listView.setOnItemClickListener(this);
        
        //�u���[�h�L���X�g���V�[�o�[�̒ǉ�
        IntentFilter filter;
        filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter);
        filter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,filter);        
        
        //Bluetooth�[���̌����J�n(1)
        btAdapter=BluetoothAdapter.getDefaultAdapter();  
        Set<BluetoothDevice> pairedDevices=btAdapter.getBondedDevices();
        if (pairedDevices.size()>0) {
            for (BluetoothDevice device:pairedDevices) {
                devices.add(device.getName()+
                    System.getProperty("line.separator")+
                    device.getAddress());
            }
        }
        if (btAdapter.isDiscovering()) btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
    }

    //�A�v���j�����ɌĂ΂��
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btAdapter!=null) btAdapter.cancelDiscovery();
        this.unregisterReceiver(receiver);
    }

    //�N���b�N���ɌĂ΂��
    public void onItemClick(AdapterView<?>av,View v,int arg2,long arg3) {
        //Bluetooth�[���̌����L�����Z��
        btAdapter.cancelDiscovery();

        //�߂�l�̎w��
        String info   =((TextView) v).getText().toString();
        String address=info.substring(info.length()-17);
        Intent intent =new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS,address);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    //���C�i�[���C�A�E�g�̃p�����[�^�w��
    private static void setLLParams(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
    }
    
    //�u���[�h�L���X�g���V�[�o�[
    private final BroadcastReceiver receiver=new BroadcastReceiver() {
        //Bluetooth�[���̌������ʂ̎擾(3)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            
            //Bluetooth�[������
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device=intent.
                    getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState()!=BluetoothDevice.BOND_BONDED) {
                    devices.add(device.getName()+
                        System.getProperty("line.separator")+
                        device.getAddress());
                }
            } 
            //Bluetooth�[����������
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                android.util.Log.e("","Bluetooth�[����������");
            }
        }
    };
}