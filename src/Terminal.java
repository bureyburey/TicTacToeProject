


public class Terminal extends GPNode{
	/*
	 * Terminal class
	 * represent a terminal of the form Make Move board[index] = (player_piece) if possible
	 */
	private int MAX_RAND_VALUE = 11; // max randomly generated value for RandVal terminal
	private int value = -1; // generated value for RandMax terminal
	private int friendOrEnemy = -1; // generated randomly 1 (1 for FRIEND, 2 for ENEMY)
	private String terminalIdentity = "UNINITIALIZED";
	private Individual individual;
	//private static String[] terminalList;
	// list of the allowed terminals
	private static final String[] allowedTerminalList = {
		"CountNeightbors",
		"CountRow",
		"RowStreak",
		"CountColumn",
		"ColumnStreak",
		"CountDiagMain",
		"DiagMainStreak",
		"CountDiagSec",
		"DiagSecStreak",
		"CornerCount",
		"RandVal",
		"IsRandIndex",
	"WinOrBlock"};

	public Terminal(Board board, String terminalIdentity, Individual individual){
		super(false, board);
		this.numChildren = 0;
		this.individual = individual;
		this.terminalIdentity = terminalIdentity;
		this.setIdentity(terminalIdentity);;
		
	}

	public Terminal(Terminal terminal){
		// copy constructor
		super(false, terminal.board);
		this.numChildren = 0;
		this.individual = terminal.individual;
		this.terminalIdentity = terminal.terminalIdentity;
		this.friendOrEnemy = terminal.friendOrEnemy;
		this.value = terminal.value;
		this.height = terminal.height;
	}

	public Terminal copy(){
		return new Terminal(this);
	}

	public int Eval(int index){
		/*
		 * evaluation of a terminal
		 * calls the proper method according to the terminal type (methods are in Board class)
		 */
		int grade = 0;
		int playerNum = 0;
		if(friendOrEnemy == 1){
			// if the terminal checks for friendly pieces
			// check if the individual value is 1 ('X') and assign 1 to playerNum, else assign 2
			if(individual.getValue() == 1)
				playerNum = 1;
			else
				playerNum = 2;
		}
		else{
			// if the terminal checks for enemy pieces
			// check if the individual value is ('X') and assign 2 to playerNum, else assign 1
			if(individual.getValue() == 1)
				playerNum = 2;
			else
				playerNum = 1;
		}
		// call the proper method (stored as a string in terminalIdentity variable)
		// all methods but the countCorners receives 2 arguments:
		// index argument and playerNum argument which represent looking for a friend or an enemy
			
		if(this.terminalIdentity.equalsIgnoreCase("CountNeightbors"))
			grade = board.countNeighbors(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("CountRow"))
			grade = board.countRow(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("RowStreak"))
			grade = board.countRowStreak(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("CountColumn"))
			grade = board.countColumn(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("ColumnStreak"))
			grade = board.countColumnStreak(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("CountDiagMain"))
			grade = board.countDiagMain(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("DiagMainStreak"))
			grade = board.countDiagMainStreak(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("CountDiagSec"))
			grade = board.countDiagSec(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("DiagSecStreak"))
			grade = board.countDiagSecStreak(index, playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("CornerCount"))
			grade = board.countCorners(playerNum);
		if(this.terminalIdentity.equalsIgnoreCase("RandVal"))
			grade = value;
		if(this.terminalIdentity.equalsIgnoreCase("IsRandIndex"))
			grade = (value == index)? 1:0;
		if(this.terminalIdentity.equalsIgnoreCase("WinOrBlock"))
			grade = (board.willWinBlock(index, playerNum))? Integer.MAX_VALUE:0;
		return grade;
	}

	public void setIdentity(String terminalIdentity){
		boolean exists = false;
		for(String id: allowedTerminalList){
			if(id.equalsIgnoreCase(terminalIdentity))
				exists = true;
		}
		if(!exists)
			return;
		this.terminalIdentity = terminalIdentity;
		if(terminalIdentity.equalsIgnoreCase("RandVal"))
			value = (int) (Math.random()*MAX_RAND_VALUE);
		else if(terminalIdentity.equalsIgnoreCase("IsRandIndex"))
			value = (int) (Math.random()*board.getBoardSize());
		else
			friendOrEnemy = (int) (Math.random()*2 + 1);
	}

	public void setFriendOrEnemy(int friendOrEnemy){
		if(friendOrEnemy == 1 || friendOrEnemy == 2)
			this.friendOrEnemy = friendOrEnemy;
	}

	public void setRandTerminal(){
		/*
		 * set the terminal randomly by randomly selecting index from the terminalList
		 * and assigning the terminalIdentity with it
		 * ******************
		 * if the terminal that was selected is RandVal, also assign a random value
		 * randomly select whether the terminal refers to friend or enemy when evaluating
		 * ******************
		 * if the terminal that was selected is IsRandIndex, also assign a random index value
		 * this terminal will return if the inspected index equals to the generated index value (1/0)
		 * ******************
		 * all terminals besides ..Rand.. returns amount of friendly/enemy pieces at various scenarios
		 */
		terminalIdentity = individual.getRandomTerminal();
		if(terminalIdentity == null){
			terminalIdentity = allowedTerminalList[(int) (Math.random()*allowedTerminalList.length)];
		}
		if(terminalIdentity.equalsIgnoreCase("RandVal"))
			value = (int) (Math.random()*MAX_RAND_VALUE);
		else if(terminalIdentity.equalsIgnoreCase("IsRandIndex"))
			value = (int) (Math.random()*board.getBoardSize());
		else
			friendOrEnemy = (int) (Math.random()*2 + 1);
	}

	public int getTerminalValue(){
		/*
		 * return the random value/index (depends on terminalIdentity) of RandVal terminal
		 */
		return value;
	}

//	public static void setTerminalList(String[] terminalList){
//		Terminal.terminalList = terminalList;
//	}

//	public static String[] getTerminalList(){
//		return Terminal.terminalList;
//	}
	
	public String toString(){
		/*
		 * returns a string representation of the terminal
		 * if the terminal is RandVal return RandVal 'VALUE'
		 * else
		 * return <terminalName> <friendOrEnemy>
		 */
		if(terminalIdentity.equalsIgnoreCase("RandVal") || terminalIdentity.equalsIgnoreCase("IsRandIndex"))
			return terminalIdentity + " " + value;
		return terminalIdentity + " " + ((friendOrEnemy==1)? "f":"e");
	}
}
