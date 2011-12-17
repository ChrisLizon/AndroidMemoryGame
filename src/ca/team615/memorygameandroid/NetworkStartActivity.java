package ca.team615.memorygameandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class NetworkStartActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	
	RadioButton hostButton;
	RadioButton joinButton;
	
	Button startButton;
	
	EditText networkPort;
	EditText networkHost;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_start_layout);

		startButton = (Button)findViewById(R.id.network_start_button);
		startButton.setOnClickListener(this);
		
		networkHost = (EditText) findViewById(R.id.network_host);
		networkPort = (EditText) findViewById(R.id.network_port);
		
		hostButton = (RadioButton)findViewById(R.id.radio_host);
		//hostButton.setEnabled(false);
		hostButton.setOnCheckedChangeListener(this);
		joinButton = (RadioButton)findViewById(R.id.radio_join);
		
		joinButton.setOnCheckedChangeListener(this);
		joinButton.setEnabled(true);
		joinButton.setChecked(true);
		
		
	}
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		int port = 0;
		String host = "";
		
		port = ConfigActivity.getSavedPort(this);
		host = ConfigActivity.getSavedHost(this);
		
		networkHost.setText(host);
		if(port == 0){
			networkPort.setText("");
		}else{
			networkPort.setText(Integer.toString(port));
		}
	}




	@Override
	public void onClick(View v) {
		Intent i = new Intent(NetworkStartActivity.this, NetworkGameActivity.class);
		if(hostButton.isChecked()){
			
			Intent service = new Intent(this, GameHostService.class);
			this.startService(service);
			
			i.putExtra("address", "127.0.0.1");
			i.putExtra("port", 9999);
		}else{
			//TODO ERROR CHECK THIS SHIT
			int port = Integer.parseInt(networkPort.getText().toString());
			String host = networkHost.getText().toString();
			
			if(port > 65535 || port < 0){
				Toast toast = Toast.makeText(this, R.string.badport, Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			
			i.putExtra("address", host);
			i.putExtra("port", port);
			
			ConfigActivity.saveHost(this, host);
			ConfigActivity.savePort(this, port);
			
		}
		NetworkStartActivity.this.startActivity(i);
		
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(!isChecked){
			return;
		}
		if(buttonView == joinButton){
			networkPort.setVisibility(View.VISIBLE);
			networkHost.setVisibility(View.VISIBLE);
			startButton.setText(R.string.multiplayer_join);
		}else{
			networkPort.setVisibility(View.GONE);
			networkHost.setVisibility(View.GONE);
			startButton.setText(R.string.multiplayer_host);
		}
		
	}
}
