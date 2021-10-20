package jp.android.fukuoka.AndroJanken;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothEx extends Activity
    implements OnClickListener {
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
    //private TextView lblReceive;//��M���x��
    private Button tyoki,gu,pa;
    private String message = null;

    //private Context context;
    //private EditText edtSend;   //���M�G�f�B�b�g�e�L�X�g
    // @private Button   btnSend;   //���M�{�^��

    //�A�v���������ɌĂ΂��
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.select);
        tyoki  = (Button) findViewById(R.id.Button01);
        tyoki.setOnClickListener(this);

        gu  = (Button) findViewById(R.id.Button02);
        gu.setOnClickListener(this);

        pa  = (Button) findViewById(R.id.Button03);
        pa.setOnClickListener(this);
        /*
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
        */
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
                    Toast.makeText(BluetoothEx.this, "�ڑ�����", Toast.LENGTH_LONG).show();break;
                case BluetoothChatService.STATE_CONNECTING:
                	Toast.makeText(BluetoothEx.this, "�ڑ���", Toast.LENGTH_LONG).show();;break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                	Toast.makeText(BluetoothEx.this, "���ڑ�", Toast.LENGTH_LONG).show();break;
                }
                break;
            //���b�Z�[�W��M
            case MSG_READ:
                byte[] readBuf=(byte[])msg.obj;
                Toast.makeText(BluetoothEx.this,new String(readBuf,0,msg.arg1), Toast.LENGTH_LONG).show();
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

    /*
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

    */
    //�{�^���N���b�N�C�x���g�̏���
    public void onClick(View v) {
        if (v==gu) {
        	Log.e("BluetoothEx.class","gu-");
            try {
                //���b�Z�[�W�̑��M
                message="0";
                if (message.length()>0) {
                    chatService.write(message.getBytes());
                }
                Toast.makeText(this, "�`���L", Toast.LENGTH_SHORT).show();
                //edtSend.setText("",TextView.BufferType.NORMAL);
            } catch (Exception e) {
                Toast.makeText(this, "�ʐM���s���܂���", Toast.LENGTH_SHORT).show();
            }
        }

        if (v==tyoki) {
        	Log.e("BluetoothEx.class","gu-");
            try {
                //���b�Z�[�W�̑��M
                message="1";
                if (message.length()>0) {
                    chatService.write(message.getBytes());
                }
                Toast.makeText(this, "�O�[", Toast.LENGTH_SHORT).show();
                //edtSend.setText("",TextView.BufferType.NORMAL);
            } catch (Exception e) {
                Toast.makeText(this, "�ʐM���s���܂���", Toast.LENGTH_SHORT).show();
            }
        }
        if (v==pa) {
        	Log.e("BluetoothEx.class","pa-");
            try {
                //���b�Z�[�W�̑��M
                message="2";
                if (message.length()>0) {
                    chatService.write(message.getBytes());
                }
                Toast.makeText(this, "�p�[", Toast.LENGTH_SHORT).show();
                //edtSend.setText("",TextView.BufferType.NORMAL);
            } catch (Exception e) {
                Toast.makeText(this, "�ʐM���s���܂���", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
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
    */
}
