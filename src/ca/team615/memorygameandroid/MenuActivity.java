package ca.team615.memorygameandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);

		SoundManager.getInstance();
		SoundManager.initSounds(getBaseContext());
		SoundManager.addSound(MemoryGameActivity.SOUND_BACKGROUND, R.raw.test2);
		
		Button singleButton = (Button)findViewById(R.id.single_player_button);
		singleButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuActivity.this, MemoryGameActivity.class);
				MenuActivity.this.startActivity(i);
				
			}
			
		});
		
		Button configButton = (Button)findViewById(R.id.menu_settings_button);
		configButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuActivity.this, ConfigActivity.class);
				MenuActivity.this.startActivity(i);
				
			}
			
		});
		

	}
}
