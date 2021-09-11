package jp.android.fukuoka.AndroJanken;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

//Bluetooth�`���b�g�T�[�o
public class BluetoothChatService {
    //�ݒ�萔
    private static final String NAME="BluetoothEx";
    private static final UUID   MY_UUID=
        UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    //��Ԓ萔
    public static final int STATE_NONE      =0;
    public static final int STATE_LISTEN    =1;
    public static final int STATE_CONNECTING=2;
    public static final int STATE_CONNECTED =3;

    //�ϐ�
    private BluetoothAdapter adapter;
    private Handler          handler;
    private AcceptThread     acceptThread;
    private ConnectThread    connectThread;
    private ConnectedThread  connectedThread;
    private int              state;
    
    //�R���X�g���N�^
    public BluetoothChatService(Context context,Handler handler) {
        this.adapter=BluetoothAdapter.getDefaultAdapter();
        this.state  =STATE_NONE;
        this.handler=handler;
    }

    //��Ԃ̎w��
    private synchronized void setState(int state) {
        this.state=state;
        handler.obtainMessage(
            BluetoothEx.MSG_STATE_CHANGE,state,-1).sendToTarget();
    }

    //��Ԃ̎擾
    public synchronized int getState() {
        return state;
    }

    //Bluetooth�̐ڑ��҂�(�T�[�o)
    public synchronized void start() {
        if (connectThread!=null) {
            connectThread.cancel();connectThread=null;}
        if (connectedThread!=null) {
            connectedThread.cancel();connectedThread=null;}
        if (acceptThread==null) {
            acceptThread=new AcceptThread();
            acceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    //Bluetooth�̐ڑ��v��(�N���C�A���g)
    public synchronized void connect(BluetoothDevice device) {
        if (state==STATE_CONNECTING) {
            if (connectThread!=null) {
                connectThread.cancel();connectThread=null;}
        }
        if (connectedThread!=null) {
            connectedThread.cancel();connectedThread=null;}
        connectThread=new ConnectThread(device);
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    //Bluetooth�ڑ�������̏���
    public synchronized void connected(BluetoothSocket socket,
        BluetoothDevice device) {
        if (connectThread!=null) {
            connectThread.cancel();connectThread=null;}
        if (connectedThread!=null) {
            connectedThread.cancel();connectedThread=null;}
        if (acceptThread!=null) {
            acceptThread.cancel();acceptThread=null;}
        connectedThread=new ConnectedThread(socket);
        connectedThread.start();
        setState(STATE_CONNECTED);
    }

    //Bluetooth�̐ؒf
    public synchronized void stop() {
        if (connectThread!=null) {
            connectThread.cancel();connectThread=null;}
        if (connectedThread!=null) {
            connectedThread.cancel();connectedThread=null;}
        if (acceptThread!=null) {
            acceptThread.cancel();acceptThread=null;}
        setState(STATE_NONE);
    }

    //��������
    public void write(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (state!=STATE_CONNECTED) return;
            r=connectedThread;
        }
        r.write(out);
    }

    //Bluetooth�̐ڑ��҂�(�T�[�o)(5)
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
        
        //�R���X�g���N�^
        public AcceptThread() {
            try {
                serverSocket=adapter.
                    listenUsingRfcommWithServiceRecord(NAME,MY_UUID);
            } catch (IOException e) {
            }
        }

        //����
        public void run() {
            BluetoothSocket socket=null;
            while (state!=STATE_CONNECTED) {
                try {
                    socket=serverSocket.accept();
                } catch (IOException e) {
                    break;
                }
                if (socket!=null) {
                    synchronized (BluetoothChatService.this) {
                        switch (state) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            connected(socket,socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            try {
                                socket.close();
                            } catch (IOException e) {
                            }
                            break;
                        }
                    }
                }
            }
        }

        //�L�����Z��
        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    //Bluetooth�̐ڑ��v��(�N���C�A���g)(6)
    private class ConnectThread extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        //�R���X�g���N�^
        public ConnectThread(BluetoothDevice device) {
            try {
                this.device=device;
                this.socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
        }

        //����
        public void run() {
            adapter.cancelDiscovery();
            try {
                socket.connect();
            } catch (IOException e) {
                setState(STATE_LISTEN);
                try {
                    socket.close();
                } catch (IOException e2) {
                }
                BluetoothChatService.this.start();
                return;
            }
            synchronized (BluetoothChatService.this) {
                connectThread=null;
            }
            connected(socket,device);
        }

        //�L�����Z��
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    //Bluetooth�ڑ�������̏���(7)
    private class ConnectedThread extends Thread {
        private BluetoothSocket socket;
        private InputStream     input;
        private OutputStream    output;

        //�R���X�g���N�^
        public ConnectedThread(BluetoothSocket socket) {
            try {
                this.socket=socket;
                this.input =socket.getInputStream();
                this.output=socket.getOutputStream();
            } catch (IOException e) {
            }
        }

        //����
        public void run() {
            byte[] buf=new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes=input.read(buf);
                    handler.obtainMessage(BluetoothEx.MSG_READ,
                        bytes,-1,buf).sendToTarget();
                } catch (IOException e) {
                    setState(STATE_LISTEN);
                    break;
                }
            }
        }

        //��������
        public void write(byte[] buf) {
            try {
                output.write(buf);
            } catch (IOException e) {
            }
        }

        //�L�����Z��
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
