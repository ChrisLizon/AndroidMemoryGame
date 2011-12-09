package ca.team615.memorygameandroid;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

/* 
 * Source adapted from
 * http://www.droidnova.com/creating-sound-effects-in-android-part-2,695.html
 */

public class SoundManager {

	private static SoundPool mSoundPool; 
	private static HashMap<Integer, Integer> mSoundPoolMap; 
	private static HashMap<Integer, Integer> loopedSoundMap;
	private static AudioManager  mAudioManager;
	private static Context mContext;

	private static float effectVolume = 1.0f;
	private static float backgroundVolume = 0.4f;

	private static SoundManager _instance = null;

	private SoundManager()
	{

	}

	public static synchronized SoundManager getInstance() 
	{
		if (_instance == null) 
			_instance = new SoundManager();
		return _instance;
	}

	public static void initSounds(Context theContext) { 
		mContext = theContext;
		mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0); 
		mSoundPoolMap = new HashMap<Integer, Integer>(); 
		loopedSoundMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 	     
	} 

	public static void addSound(int index,int soundID)
	{
		mSoundPoolMap.put(index, mSoundPool.load(mContext, soundID, 1));
	}

	public static void playSound(int index) { 

		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolume / streamMaxVolume * effectVolume;
		int i = mSoundPoolMap.get(index);
		System.out.println("Found " + i);
		mSoundPool.play(i, volume, volume, 1, 0, 1f); 
	}
	
	public static int playLoopedSound(int index) { 
		int loopedSound;
		try{
			loopedSound = loopedSoundMap.get(index);
		}catch(NullPointerException e){
			loopedSound = 0;
		}
		
		if(loopedSound == 0){
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = (float)(streamVolume / streamMaxVolume * backgroundVolume);
			loopedSound = mSoundPool.play(mSoundPoolMap.get(index), volume, volume, 1, -1, 1f);
			System.out.println("Looped Sound is " + loopedSound);
			loopedSoundMap.put(index, loopedSound);
		}else{
			mSoundPool.resume(loopedSound);
		}
		return loopedSound;
	}

	public static void pauseLoopedSound(int soundId){
		
		int loopedSound;
		try{loopedSound = loopedSoundMap.get(soundId);
		}catch(NullPointerException e){
			loopedSound = 0;
		}
		System.out.println("Stopping " + loopedSound);
		if(loopedSound != 0){
			mSoundPool.pause(loopedSound);
		}
	}

	
}