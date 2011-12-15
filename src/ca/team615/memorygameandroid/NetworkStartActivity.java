package ca.team615.memorygameandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NetworkStartActivity extends Activity implements OnClickListener {
	
	RadioButton hostButton;
	RadioButton joinButton;
	
	EditText networkPort;
	EditText networkHost;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_start_layout);

		
		hostButton = (RadioButton)findViewById(R.id.radio_host);
		hostButton.setEnabled(false);
		joinButton = (RadioButton)findViewById(R.id.radio_join);
		joinButton.setEnabled(true);
		joinButton.setSelected(true);
		
		networkHost = (EditText) findViewById(R.id.network_host);
		networkPort = (EditText) findViewById(R.id.network_port);
	
		Button startButton = (Button)findViewById(R.id.network_start_button);
		startButton.setOnClickListener(this);


		
	}
	
	
	@Override
	public void onClick(View v) {
		Intent i = new Intent(NetworkStartActivity.this, NetworkGameActivity.class);
		if(hostButton.isChecked()){
			i.putExtra("address", "127.0.0.1");
			i.putExtra("port", 9999);
		}else{
			//TODO ERROR CHECK THIS SHIT
			i.putExtra("address", networkHost.getText().toString());
			i.putExtra("port", Integer.parseInt(networkPort.getText().toString()));
		}
		NetworkStartActivity.this.startActivity(i);
		
	}
}
