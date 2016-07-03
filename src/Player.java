
public class Player {
	/*
	  Player class
	  value is the representation of 'X' or 'O'
	  1 == 'X'
	  2 == 'O'
	 */

	protected int value;
	protected String playerName;
	protected boolean isHumanPlayer;
	
	public Player(String playerName) {
		this.value = 0;
		this.playerName = playerName;
		isHumanPlayer = false;
	}

	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}
	
	public String getPlayerName(){
		return playerName;
	}
	
	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}
	
	public void setIsHumanPlayer(boolean isHumanPlayer){
		this.isHumanPlayer = isHumanPlayer;
	}
	
	public boolean getIsHumanPlayer(){
		return this.isHumanPlayer;
	}
}
