package jp.android.fukuoka.AndroJanken;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AndroJanken extends Activity implements View.OnClickListener {

    /** Called when the activity is first created. */

    //���b�Z�[�W�萔
    public static final int MSG_STATE_CHANGE=1;
    public static final int MSG_READ        =2;
	
	//Bluetooth
    private BluetoothAdapter     btAdapter;
    private BluetoothChatService chatService;

    //UI
    private TextView lblReceive;//��M���x��
 
	   //���N�G�X�g�萔
    private static final int RQ_CONNECT_DEVICE=1;
    private static final int RQ_ENABLE_BT     =2;

	private Button   btnSend;   //���M�{�^��
	   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        LinearLayout layout = (LinearLayout)this.findViewById(R.id.Layout01);
        btnSend=(Button) this.findViewById(R.id.Button01);
        //btnSend.setText("���M");
        btnSend.setOnClickListener(this);
        
        //Bluetooth�A�_�v�^
        btAdapter=BluetoothAdapter.getDefaultAdapter();

        //��M���x���̐���
        lblReceive=new TextView(this);
        lblReceive.setId(1);
        lblReceive.setText("");
        lblReceive.setTextSize(16.0f);
        lblReceive.setTextColor(Color.rgb(0,0,0));
        setLLParams(lblReceive,
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);  
        layout.addView(lblReceive);    
    }
    
    
    //���C�i�[���C�A�E�g�̃p�����[�^�w��
    private static void setLLParams(View view,int w,int h) {
        view.setLayoutParams(new LinearLayout.LayoutParams(w,h));
    }

	@Override
	public void onClick(View v) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
        Intent serverIntent=new Intent(this,DeviceListActivity.class);
        startActivityForResult(serverIntent,RQ_CONNECT_DEVICE);
        	
	}
	
    //�A�v���J�n���ɌĂ΂��
    @Override
    public void onStart() {
        super.onStart();
        if (!btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,RQ_ENABLE_BT);
        } else {
            if (chatService==null) chatService=
                new BluetoothChatService(this,handler);
        }
    }

    //�A�v�����W���[�����ɌĂ΂��
    @Override
    public synchronized void onResume() {
        super.onResume();
        if (chatService!=null) {
            if (chatService.getState()==BluetoothChatService.STATE_NONE) {
                //Bluetooth�̐ڑ��҂�(�T�[�o)
                chatService.start();
            }
        }
    }
	
    //�A�v�����A���ɌĂ΂��
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
  
    	
    	switch (requestCode) {
        //�[������
        case RQ_CONNECT_DEVICE:
            if (resultCode==Activity.RESULT_OK) {
            	
            	String address=data.getExtras().
                    getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                
                //Bluetooth�̐ڑ��v��(�N���C�A���g)
                BluetoothDevice device=btAdapter.getRemoteDevice(address);
                chatService.connect(device);
                
                Intent intent = new Intent(AndroJanken.this ,BluetoothEx.class);
                startActivity(intent);

                
            }
            break;
        //�����L��
        case RQ_ENABLE_BT:
            if (resultCode==Activity.RESULT_OK) {
                chatService=new BluetoothChatService(this,handler);
            } else {
                Toast.makeText(this,"Bluetooth���L���ł͂���܂���",
                	Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    //�`���b�g�T�[�o��������擾����n���h��
    private final Handler handler=new Handler() {
        //�n���h�����b�Z�[�W
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            //��ԕύX
            case MSG_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    addText("�ڑ�����");break;
                case BluetoothChatService.STATE_CONNECTING:
                    addText("�ڑ���");break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    addText("���ڑ�");break;
                }
                break;
            //���b�Z�[�W��M
            case MSG_READ:
                byte[] readBuf=(byte[])msg.obj;
                addText(new String(readBuf,0,msg.arg1));
                break;
            }
        }
    };
    
    //��M�e�L�X�g�̒ǉ�
    private void addText(final String text) {
        //�n���h���ɂ�郆�[�U�[�C���^�t�F�[�X����
        handler.post(new Runnable(){
            public void run() {
                lblReceive.setText(text+
                    System.getProperty("line.separator")+
                    lblReceive.getText());
                
                
            }
        });
    }
    

}