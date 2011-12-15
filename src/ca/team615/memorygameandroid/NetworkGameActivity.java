package ca.team615.memorygameandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NetworkGameActivity extends Activity implements OnClickListener {

	private int[] viewIds = {R.id.card_0, R.id.card_1, R.id.card_2, R.id.card_3,
			R.id.card_4, R.id.card_5, R.id.card_6, R.id.card_7,
			R.id.card_8, R.id.card_9, R.id.card_10, R.id.card_11,
			R.id.card_12, R.id.card_13, R.id.card_14, R.id.card_15,};

	private int[] drawableIds = {R.drawable.card_0, R.drawable.card_1, R.drawable.card_2, R.drawable.card_3,
			R.drawable.card_4, R.drawable.card_5, R.drawable.card_6, R.drawable.card_7, R.drawable.card_back};

	private int[] assignments;	//Holds the assigned positions of the cards

	private ImageView[] imageviews;

	private static final int SOUND_FLIP = 1;
	private static final int SOUND_FLOP = 2;
	static final int SOUND_BACKGROUND = 3;
	private static final int SOUND_WINNER = 4;

	/** the number of cards currently face up */

	private TextView playerPairsLabel;
	private TextView opponentPairsLabel;

	Handler handler;

	OutputStream oStream = null;
	PrintWriter writer = null;

	InputStream iStream = null;
	BufferedReader reader = null;

	int flippedCards = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen_layout);

		SoundManager.addSound(SOUND_FLIP, R.raw.flip);
		SoundManager.addSound(SOUND_FLOP, R.raw.flop);
		SoundManager.addSound(SOUND_WINNER, R.raw.test2);

		handler = new Handler();
		
		String connectAddress = "";
		int connectPort = 0;
		try{
			Intent i = this.getIntent();
			connectAddress = i.getStringExtra("address");
			connectPort = i.getIntExtra("port", 0);
		}catch(Exception e){
			e.printStackTrace();
		}

		Socket socket = null;
		try {
			//TODO This probably has to go into a thread
			System.out.println("Trying to connect to socket");
			socket = new Socket(connectAddress, connectPort);
			System.out.println("Connected to socket");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//TODO THIS NEEDS TO BE A STRING RESOURCE
		((TextView)this.findViewById(R.id.found_pairs_label)).setText("Your Score");
		playerPairsLabel=(TextView)NetworkGameActivity.this.findViewById(R.id.pairs_counter);

		//TODO THIS NEEDS TO BE A STRING RESOURCE
		((TextView)this.findViewById(R.id.turns_label)).setText("Opponent Score");
		opponentPairsLabel=(TextView)NetworkGameActivity.this.findViewById(R.id.turns);

		//create a new array to hold the card positions
		assignments = new int[16];

		try {
			iStream = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(iStream));

			oStream = socket.getOutputStream();
			writer = new PrintWriter(oStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//initialize the positions
		try {
			System.out.println("getting assignments");
			String buffer = reader.readLine();
			System.out.println("got assignments");
			String[] values = buffer.split(" ");
			for(int i = 1; i < values.length; i++){
				assignments[i-1] = Integer.parseInt(values[i]);
				System.out.println("Card " + i + " is " + assignments[i-1]);
			}
			System.out.println("Saved assignments");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//get the imageviews
		imageviews = new ImageView[viewIds.length];
		for(int i = 0; i < viewIds.length; i++){
			imageviews[i] = (ImageView)findViewById(viewIds[i]);
		}

		//set click listeners for each veiw
		for(int i = 0; i < 16; i++){
			((ImageView)findViewById(viewIds[i])).setOnClickListener(this);
			((ImageView)findViewById(viewIds[i])).setTag(new Integer(i));
		}

		//set each image to blank
		for(int i = 0; i < 16; i++){
			((ImageView)findViewById(viewIds[i])).setImageResource(R.drawable.card_back);
		}

		new Thread(new InputHandler()).start();

	}

	@Override
	public void onBackPressed() {
		//TODO Confirm quit
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		
		int index = 0;
		for(int i = 0; i < 16; i++){
			if(v.getId() == viewIds[i]){
				index = i;
				break;
			}
		}
		System.out.println("Clicked " + index);
		try{
			writer.println("select " + index);
			flippedCards++;
			v.setClickable(false);
			v.setFocusable(false);
			if(flippedCards == 2){
				disableCards();
			}
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Flip the card the server wants us to flip.
	 * @param index	The index of the card to be flipped.
	 */
	void flipCard(int index){

		System.out.println("Flipping card " + index);

		SoundManager.playSound(SOUND_FLIP);

		((ImageView)findViewById(viewIds[index])).setImageResource(drawableIds[assignments[index]]);
		imageviews[index].setFocusable(false);
		imageviews[index].setClickable(false);

	}

	/**
	 * Disable all the cards when the server tells us to
	 */
	void disableCards(){
		for(ImageView view:imageviews){
			view.setFocusable(false);
			view.setClickable(false);
		}
		((LinearLayout)findViewById(R.id.bottomtoolbarcontainer)).setBackgroundResource(R.drawable.toolbar_bg);
		((TextView)findViewById(R.id.turn_indicator)).setText("");
	}

	/**
	 * Enable all the cards when the server tells us to
	 */
	void enableCards(){
		for(ImageView view: imageviews){
			view.setFocusable(true);
			view.setClickable(true);
		}
		((LinearLayout)findViewById(R.id.bottomtoolbarcontainer)).setBackgroundResource(R.drawable.toolbar_playerturn);
		((TextView)findViewById(R.id.turn_indicator)).setText(R.string.player_turn);
		flippedCards = 0;
	}

	/**
	 * Either set the cards back to the back image or remove them
	 * @param currentIndex	Card index 1
	 * @param lastIndex		Card index 2
	 * @param found			if true, hide the cards, if false, show the back
	 */
	void flipCardsBack(int currentIndex, int lastIndex, boolean found) { 
		SoundManager.playSound(SOUND_FLOP);

		if(found){
			((ImageView)findViewById(viewIds[lastIndex])).setVisibility(View.INVISIBLE);
			((ImageView)findViewById(viewIds[currentIndex])).setVisibility(View.INVISIBLE);

		}else{
			((ImageView)findViewById(viewIds[currentIndex])).setImageResource(R.drawable.card_back);
			((ImageView)findViewById(viewIds[lastIndex])).setImageResource(R.drawable.card_back);

		}

	} 

	/**
	 * Update the scores on the screen.
	 * @param player
	 * @param opponent
	 */
	private void updateScores(int player, int opponent){
		opponentPairsLabel.setText(Integer.toString(opponent));
		playerPairsLabel.setText(Integer.toString(player));
	}


	@Override
	protected void onResume() {
		//TODO let the server know that the game has gained foreground
		super.onResume();
	}

	@Override
	protected void onPause() {
		//TODO let the server know that the game has lost foreground
		super.onPause();
	}

	private class InputHandler implements Runnable{

		@Override
		public void run() {
			while(true){
				System.out.println("Waiting for a command");
				String command;
				try {
					command = reader.readLine();
					if(command.startsWith("quit")){
						break;
						//TODO quit?
					}else if(command.startsWith("flip")){
						final int index = Integer.parseInt(command.substring(5, command.length()));
						handler.post(new Runnable(){
							@Override
							public void run() {
								flipCard(index);

							}});
					}else if(command.startsWith("disableall")){
						handler.post(new Runnable(){
							@Override
							public void run() {
								disableCards();

							}});
					}else if(command.startsWith("enableall")){
						handler.post(new Runnable(){
							@Override
							public void run() {
								enableCards();

							}});
					}else if(command.startsWith("flop")){
						final String[] parts = command.split(" ");
						handler.post(new Runnable(){
							@Override
							public void run() {
								flipCardsBack(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), false);
							}});
					}else if(command.startsWith("remove")){
						final String[] parts = command.split(" ");
						handler.post(new Runnable(){
							@Override
							public void run() {
								flipCardsBack(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), true);
							}});
					}else if(command.startsWith("score")){
						final String[] parts = command.split(" ");
						handler.post(new Runnable(){
							@Override
							public void run() {
								updateScores(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
							}});
					}
					

				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Server Closed Connection.");
					break;
					//e.printStackTrace();
				}

			}

		}

	}


}

