package jp.android.fukuoka.AndroJanken;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothEx extends Activity    
    implements View.OnClickListener {
    //���b�Z�[�W�萔
    public static final int MSG_STATE_CHANGE=1;
    public static final int MSG_READ        =2;

    //���N�G�X�g�萔
    private static final int RQ_CONNECT_DEVICE=1;
    private static final int RQ_ENABLE_BT     =2;

    //Bluetooth
    private BluetoothAdapter     btAdapter;
    private BluetoothChatService chatService;

    //UI
    private TextView lblReceive;//��M���x��
    private EditText edtSend;   //���M�G�f�B�b�g�e�L�X�g
    private Button   btnSend;   //���M�{�^��
    
    //�A�v���������ɌĂ΂��
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //���C�A�E�g�̐���
        LinearLayout layout=new LinearLayout(this);
        layout.setBackgroundColor(Color.rgb(255,255,255));
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout); 
        
        //���M�G�f�B�b�g�e�L�X�g�̐���
        edtSend=new EditText(this);
        edtSend.setId(2);
        edtSend.setText("",TextView.BufferType.NORMAL);
        setLLParams(edtSend,
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);  
        layout.addView(edtSend);
        
        //���M�{�^���̐���
        btnSend=new Button(this);
        btnSend.setText("���M");
        btnSend.setOnClickListener(this);
        setLLParams(btnSend);  
        layout.addView(btnSend);

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
        
        //Bluetooth�A�_�v�^
        btAdapter=BluetoothAdapter.getDefaultAdapter();
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

    //�A�v���j�����ɌĂ΂��
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatService!=null) chatService.stop();
    }

    //����Bluetooth�[������̔�����L����(4)
    private void ensureDiscoverable() {
        if (btAdapter.getScanMode()!=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent=new Intent(
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
            startActivity(discoverableIntent);
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

    //�I�v�V�������j���[�������ɌĂ΂��
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item0=menu.add(0,0,0,"�[������");
        item0.setIcon(android.R.drawable.ic_search_category_default);
        MenuItem item1=menu.add(0,1,0,"�����L��");
        item1.setIcon(android.R.drawable.ic_menu_call);
        return true;
    }

    //�I�v�V�������j���[�I�����ɌĂ΂��
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        //����
        case 0:
            Intent serverIntent=new Intent(this,DeviceListActivity.class);
            startActivityForResult(serverIntent,RQ_CONNECT_DEVICE);
            return true;
        //�����L��
        case 1:
            ensureDiscoverable();
            return true;
        }
        return false;
    }

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
    
    //�{�^���N���b�N�C�x���g�̏���
    public void onClick(View v) {
        if (v==btnSend) {
            try {
                //���b�Z�[�W�̑��M
                String message=edtSend.getText().toString();
                if (message.length()>0) {
                    chatService.write(message.getBytes());
                }
                addText(message);
                edtSend.setText("",TextView.BufferType.NORMAL);
            } catch (Exception e) {
                addText("�ʐM���s���܂���");
            }           
        }
    }  
    
    //���C�i�[���C�A�E�g�̃p�����[�^�w��
    private static void setLLParams(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
    }  

    //���C�i�[���C�A�E�g�̃p�����[�^�w��
    private static void setLLParams(View view,int w,int h) {
        view.setLayoutParams(new LinearLayout.LayoutParams(w,h));
    }
}
