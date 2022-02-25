package jp.android.fukuoka.gpstomail;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.util.Log;

public class SendMail extends Activity {
	private boolean DebugMode = false;
	private String mBody;
	private NotificationManager mNM;
	private Notification notification;
	private String mUser;
	private String mPassword;
	private MediaPlayer mp;
	private Context context;
	private ProgressDialog progressDialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        progressDialog = new ProgressDialog(this);
        // �v���O���X�_�C�A���O�̃^�C�g����ݒ肵�܂�
        progressDialog.setTitle(R.string.app_name);
        // �v���O���X�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
        progressDialog.setMessage(getText(R.string.Sending_Message));
        // �v���O���X�_�C�A���O�̊m��ifalse�j�^�s�m��itrue�j��ݒ肵�܂�
        progressDialog.setIndeterminate(false);
        // �v���O���X�_�C�A���O�̃X�^�C�����~�X�^�C���ɐݒ肵�܂�
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // �v���O���X�_�C�A���O�̍ő�l��ݒ肵�܂�
        progressDialog.setMax(100);
        // �v���O���X�_�C�A���O�̒l��ݒ肵�܂�
        progressDialog.incrementProgressBy(30);
        // �v���O���X�_�C�A���O�̃Z�J���_���l��ݒ肵�܂�
//        progressDialog.incrementSecondaryProgressBy(70);
        // �v���O���X�_�C�A���O�̃L�����Z�����\���ǂ�����ݒ肵�܂�
        progressDialog.setCancelable(false);
        // �v���O���X�_�C�A���O��\�����܂�
        progressDialog.show();
		
        //Notification�̏���
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Bundle extras = getIntent().getExtras();
		mBody =new String();
		if (extras != null) {
			mBody = (String) extras.getCharSequence("mBody");
		}
		//�����Đ�
		mp = MediaPlayer.create(this, R.raw.alarm);
		mp.start();
		context = this;

		//Mail Sending
		if(DebugMode){
			Log.i("GPStoMail","Mail Sending");
			Log.i("GPStoMail", setting.getTextbyKey("Gmail_account", context));
			Log.i("GPStoMail", setting.getTextbyKey("Gmail_password", context));
			Log.i("GPStoMail", setting.getTextbyKey("Mail_Subject", context).toString());
			Log.i("GPStoMail", setting.getTextbyKey("fromMail_adr", context));
			Log.i("GPStoMail", setting.getTextbyKey("toMail_adr", context));
			Log.i("GPStoMail", mBody.toString());
		}
		mUser = setting.getTextbyKey("Gmail_account", context);
		mPassword =setting.getTextbyKey("Gmail_password", context);
		
    	SendMail_mod(setting.getTextbyKey("Mail_Subject", context),
    			mBody.toString(),
    			setting.getTextbyKey("fromMail_adr", context),
    			setting.getTextbyKey("toMail_adr", context));
    	finish();
    	//Activity ���\���փZ�b�g
//		moveTaskToBack(true);
	};
	
	protected void SendMail_mod(String subject, String body, String sender, String recipient){
		GMailSender G_sender = new GMailSender(mUser,mPassword);

		try {
			G_sender.sendMail(subject,
					body,
					sender,
					recipient);
			// Notification�̕\��
			notification = new Notification(R.drawable.icon, getText(R.string.app_name),System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, jp.android.fukuoka.gpstomail.Main.class),
					Intent.FLAG_ACTIVITY_NEW_TASK);
			notification.setLatestEventInfo(this,
					getText(R.string.app_name), 
					getText(R.string.Succeed_Message_Gmail_msg), contentIntent);
			mNM.notify(R.string.app_name, notification);

		} catch (Exception e) {
			// Notification�̕\��
			notification = new Notification(R.drawable.icon_no, getText(R.string.app_name),System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, jp.android.fukuoka.gpstomail.Main.class),
					Intent.FLAG_ACTIVITY_NEW_TASK);
			notification.setLatestEventInfo(this,
					getText(R.string.app_name), 
					getText(R.string.Error_Message_Gmail_msg), contentIntent);
			mNM.notify(R.string.app_name, notification);
			
			Log.e("SendMail", e.getMessage(), e);
		}
//		progressDialog.dismiss();
	}
}
