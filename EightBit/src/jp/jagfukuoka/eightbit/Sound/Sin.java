package jp.jagfukuoka.eightbit.Sound;

public class Sin implements Sound {

	private short[] audio;
	@Override
	public void createAudio(int time,int interval,int volume){
		
		short[] sinWave = new short[SoundCommon.getSampleRate() * time];
		double freq = interval;
		double t = 0.0;
		double dt = 1.0 / SoundCommon.getSampleRate();
		short shortVolume = (short) ((Short.MAX_VALUE)/100 * volume);

		for (int i = 0; i < sinWave.length; i++, t += dt) {
		    sinWave[i] = (short) (shortVolume * Math.sin(2.0 * Math.PI * t * freq));
		}
		this.audio = sinWave;
	}
	@Override
	public short[] getAudio(){
		return this.audio;
	}

}
