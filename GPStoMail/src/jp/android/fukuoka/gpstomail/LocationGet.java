package jp.android.fukuoka.gpstomail;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;

public class LocationGet extends Activity implements LocationListener{
	private boolean DebugMode = false;

	private LocationManager lm;
	private String TAG = "GPS2MAIL";
	private TextView mText1;
	private TextView mText2;
	private TextView mText3;
	private TextView mText4;
	private Date mTime;
	private StringBuilder mBody;

	private Button btn1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(DebugMode){ 
			setContentView(R.layout.gps);

			mText1 = (TextView)findViewById(R.id.TextView01);
			mText2 = (TextView)findViewById(R.id.TextView02);
			mText3 = (TextView)findViewById(R.id.TextView03);
			mText4 = (TextView)findViewById(R.id.TextView04);
			mText4.setAutoLinkMask(Linkify.WEB_URLS);

			btn1 = (Button) findViewById(R.id.Button02);
			btn1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
					// �N���b�N���̏���
					Intent i = new Intent(v.getContext(),setting.class);
					startActivity(i);
					onProviderDisabled(null);
				}
			});

		}

		mTime =new  Date();

		// LocationManager��GPS�̒l���擾���邽�߂̐ݒ�
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    

		// �ʒu���̍X�V���󂯎��悤�ɐݒ�
		this.onProviderEnabled(LocationManager.GPS_PROVIDER);
		this.onProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	// GPS�̒l���X�V�����ƌĂяo�����
	public void onLocationChanged(Location location) {
		if(DebugMode){
			Log.i(TAG,""+location.getLatitude());
			Log.i(TAG,""+location.getLongitude());
			mText1.setText(""+location.getLatitude());
			mText2.setText(""+location.getLongitude());
			mTime.setTime(location.getTime());
			if (mTime !=null ){
				mText3.setText(mTime.toLocaleString());
				Log.i(TAG,mTime.toLocaleString());
			}
			mText4.setText("http://maps.google.co.jp/maps?geocode=&sll="+location.getLatitude()+","+location.getLongitude()+"&q="+location.getLatitude()+","+location.getLongitude());
		}

		mBody = new StringBuilder();
		mBody.append("�o�x�F"+location.getLongitude()+"\n");
		mBody.append("�ܓx�F"+location.getLatitude()+"\n");
		mBody.append("�W���F"+location.getAltitude()+"\n");
		mBody.append("�v�������F"+mTime.toLocaleString()+"\n");
		mBody.append("�v�����x�F"+location.getAccuracy()+"m(s)\n");

		mBody.append("Google Maps�ňʒu���m�F�ł��܂�\n");
		mBody.append("http://maps.google.co.jp/maps?geocode=&sll="+location.getLatitude()+","+location.getLongitude()+"&q="+location.getLatitude()+","+location.getLongitude());
		Log.i(TAG,mBody.toString());

		//Mail�쐬
		Intent intent = new Intent(this,SendMail.class);
		CharSequence text = mBody.toString();
		intent.putExtra("mBody", text);
		startActivity(intent);
		onProviderDisabled(null);
		finish();
		//Activity ���\���փZ�b�g
//		moveTaskToBack(true);
	}

	public void onProviderDisabled(String provider) {
		// �ʒu���̍X�V���~�߂�
		lm.removeUpdates(this);
	}

	public void onProviderEnabled(String provider) {
		lm.requestLocationUpdates(provider, 0, 0, this);
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/*
    public class GPSView extends View {
    	public GPSView(Context context) {
    		super(context);
    		setBackgroundColor(Color.WHITE);
    	}
    	@Override
    	protected void onDraw(Canvas canvas) {

    		Paint paint=new Paint();
    		paint.setAntiAlias(true);
    		paint.setTextSize(32);
    		canvas.drawText("LocationGet",320,200, paint);
    	}
    }
	 */
}