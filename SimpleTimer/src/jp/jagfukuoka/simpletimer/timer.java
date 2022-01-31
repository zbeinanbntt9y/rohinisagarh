package jp.jagfukuoka.simpletimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class timer extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//XML����View���擾
        
        //XML��ɒ�`�����I�u�W�F�N�g���擾
        countText = (TextView)findViewById(R.id.Counter);
        final Button startButton = (Button)findViewById(R.id.StartButton);
        startButton.setOnClickListener(new OnClickListener() {
	    	@Override
			public void onClick(View v) {
	    	    startDate =System.currentTimeMillis();
	    	    loopEngine.start();
	    	}
	    });
        final Button stopButton = (Button)findViewById(R.id.StopButton);
        stopButton.setOnClickListener(new OnClickListener() {
	    	@Override
			public void onClick(View v) {
	    		loopEngine.stop();
	    	}
	    });
        
    }
    private TextView countText; // �J�E���g��\������e�L�X�g�{�b�N�X
    private long startDate;

    
    public void update(){
    	countText.setText(String.valueOf(System.currentTimeMillis()-startDate));//�v���������Ԃƍ��̎��Ԃ̍����J�E���^�[�ɕ\��
    }
    //��莞�Ԍ��update���ĂԂ��߂̃I�u�W�F�N�g
    class LoopEngine extends Handler {
    	private boolean isUpdate;
    	public void start(){
    		this.isUpdate = true;
    		handleMessage(new Message());
    	}
    	public void stop(){
    		this.isUpdate = false;
    	}
    	@Override
    	public void handleMessage(Message msg) {
    		this.removeMessages(0);//�����̃��b�Z�[�W�͍폜
    		if(this.isUpdate){
        		timer.this.update();//���M�����������b�Z�[�W���擾����update�����s
    			sendMessageDelayed(obtainMessage(0), 10);//10�~���b��Ƀ��b�Z�[�W���o��
    		}
    	} 
    };
    private LoopEngine loopEngine = new LoopEngine();
    
}