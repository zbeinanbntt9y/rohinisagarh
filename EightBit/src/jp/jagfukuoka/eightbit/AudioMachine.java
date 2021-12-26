package jp.jagfukuoka.eightbit;

import java.util.Random;

import jp.jagfukuoka.eightbit.Sound.Sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioMachine {
	boolean stop = true;
	
	public AudioMachine(){
		this.init();
	}
	AudioTrack track;
	private void init(){		
		this.track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_DEFAULT, AudioFormat.ENCODING_DEFAULT, 8000, AudioTrack.MODE_STREAM);		
		this.track.play();		
	}
	
	public void Play(Sound sound){
		this.stop = false;		
		short[] audio = sound.getAudio();
		track.write(audio, 0, audio.length);		
	}
	
	public void Stop(){
		if (!stop){
			this.track.stop();
			this.track.release();
			stop = true;
		}
	}
}
