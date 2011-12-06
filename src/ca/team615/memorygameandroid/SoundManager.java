package ca.team615.memorygameandroid;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;



public class SoundManager {

	private  SoundPool mSoundPool; 
	private  HashMap<Integer, Integer> mSoundPoolMap; 
	private  AudioManager  mAudioManager;
	private  Context mContext;


	public SoundManager()
	{

	}

	public void initSounds(Context theContext) { 
		mContext = theContext;
		mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0); 
		mSoundPoolMap = new HashMap<Integer, Integer>(); 
		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 	     
	} 

	public void addSound(int index,int soundID)
	{
		mSoundPoolMap.put(index, mSoundPool.load(mContext, soundID, 1));
	}

	public void playSound(int index) { 

		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = (float)streamVolume / streamMaxVolume;
		int i = mSoundPoolMap.get(index);
		System.out.println("Found " + i);
		mSoundPool.play(i, volume, volume, 1, 0, 1f); 
	}

	public int playLoopedSound(int index) { 

		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = (float)(streamVolume / streamMaxVolume * 0.3);
		loopedSound = mSoundPool.play(mSoundPoolMap.get(index), volume, volume, 1, -1, 1f);
		System.out.println("Looped Sound is " + loopedSound);
		return loopedSound;
	}
	int loopedSound = -1;

	public void stopLoopedSound(){
		System.out.println("Stopping " + loopedSound);
		if(loopedSound != 0){
			mSoundPool.stop(loopedSound);
		}
	}

	public void playSound_Delayed (final int soundId, final long millisec) {
		// TIMER
		final Handler mHandler = new Handler();
		final Runnable mDelayedTimeTask = new Runnable() {

			public void run() {

				playLoopedSound(soundId);		

			}
		};
		mHandler.postDelayed(mDelayedTimeTask, millisec); 
		mDelayedTimeTask.run();
	}

}