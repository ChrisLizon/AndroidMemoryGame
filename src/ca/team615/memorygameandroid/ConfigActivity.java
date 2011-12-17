package ca.team615.memorygameandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class ConfigActivity extends Activity {
	
	private static final String PREFS_NAME = "net.team615.memorygame.prefs";
	private static final String PREFS_BGMUSICON_KEY = "bmusicon";
	private static final String PREFS_SAVEHOST_KEY = "savedhost";
	private static final String PREFS_SAVEPORT_KEY = "saveport";
	
	SharedPreferences prefs;
	SharedPreferences.Editor prefsWriter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_layout);

		prefs= this.getSharedPreferences(PREFS_NAME, 0);
		prefsWriter = prefs.edit();
		
		
		((CheckBox)this.findViewById(R.id.config_bgmusic_enable)).setOnClickListener(bgMusicListener);
		((CheckBox)this.findViewById(R.id.config_bgmusic_enable)).setChecked(getBgMusicEnabled(this));
		

	}
	
	
	OnClickListener bgMusicListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			prefsWriter.putBoolean(PREFS_BGMUSICON_KEY, ((CheckBox)v).isChecked());
			prefsWriter.commit();
		}
		
	};
	
	public static boolean getBgMusicEnabled(Context context){
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getBoolean(PREFS_BGMUSICON_KEY, true);
	}

	public static int getSavedPort(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getInt(PREFS_SAVEPORT_KEY, 0);
		
	}

	public static String getSavedHost(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getString(PREFS_SAVEHOST_KEY, "");
	}
	
	public static void saveHost(Context context, String host){
		SharedPreferences.Editor prefsWriter = context.getSharedPreferences(PREFS_NAME, 0).edit();
		prefsWriter.putString(PREFS_SAVEHOST_KEY, host);
		prefsWriter.commit();
	}
	
	public static void savePort(Context context, int port){
		SharedPreferences.Editor prefsWriter = context.getSharedPreferences(PREFS_NAME, 0).edit();
		prefsWriter.putInt(PREFS_SAVEPORT_KEY, port);
		prefsWriter.commit();
	}
}

