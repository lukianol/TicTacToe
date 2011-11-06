package lukianol.tictactoe;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;


public class TicTacToeActivity extends Activity implements GameEventListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.main); 
        init();      
        
    }
    
    void init(){
    	
    	newGame = (Button)findViewById(R.id.newGame);
    	
    	newGame.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				new Game(_this, 3);
			}
		});
    	
    	output = (EditText)findViewById(R.id.output);
    	
    }
    
	public void GameStateChanged(final IGame game, GameState gameState) {
		
		switch(gameState)
		{
			case Playing:
				initializePlayGround(game);
				break;
				
			case Won:
				game.removeGameEventListener(_this);
				
				for(Field field : game.getWonFields()){
				
					_buttonMap.get(field.getPosition()).setBackgroundColor(Color.YELLOW);
				}				
				break;
			case Drawn:
				game.removeGameEventListener(_this);				
				break;
		}
		
		appendText("Game is " + gameState.toString().toLowerCase());
		
	}

	private void initializePlayGround(final IGame game) {
		playGround = (TableLayout)findViewById(R.id.playGround);    	
    	playGround.removeAllViews();
    	
    	int playGroundSize = game.getPlaygroundSize();
    	_buttonMap = new HashMap<Position, Button>(playGroundSize*playGroundSize);
    	
    	for(int r = 0; r < playGroundSize; r++){
    		
    		TableRow row = new TableRow(this);
    		
    		for(int c = 0; c < playGroundSize; c++){
    			
    			Button cell = new Button(this); 
    			Position position = new Position(c, r);
				cell.setTag(position);
    			cell.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						
						Button button = (Button)v;
						Position position = (Position)button.getTag();
					    try {
					    	StrokeKind stroke = game.getCurrentStroke();
							game.Stroke(position);
							button.setText(stroke.toString().toUpperCase());								
						} catch (TicTacToeException e) {
							appendText(e.getMessage());
						}
					    
					}});
    			row.addView(cell, c);
    			_buttonMap.put(position, cell);
    		}
    		
    		playGround.addView(row, r);
    	}
	}

	public void CurrentStrokeChanged(IGame game, StrokeKind stroke) {

		appendText("Stroke is changed to : " + stroke.toString().toUpperCase());
		
	}   
	
	private void appendText(String text){
		output.append(String.format("\n%s", text));
	}
		
	final private GameEventListener _this = this;
	private EditText output;
	private Button newGame;
	private TableLayout playGround;
	private Map<Position, Button> _buttonMap;
}