import java.util.Random;


public class Predicate extends GPNode{
	/*
	  Predicate class
	  represent a predicate of the form board[index] == (player_piece)
	 */
	private int index;
	public Predicate(boolean isRoot, Board board){
		super(isRoot, board);
		setRandomPredicate();
		if(this.isRoot){
			// if true is passed to the constructor, this node will initialize two children as new predicates
			//System.out.println("Added Root Predicate " + getPredicate());
			//leftChild = new Predicate(false, board);
			//rightChild = new Predicate(false, board);
		}
	}

	public Predicate(Predicate predicate){
		// copy constructor
		super(predicate.isRoot, predicate.board);
		this.index = predicate.index;
	}

	
	
	public void setPredicate(int index){
		this.index = index;
	}

	public int getPredicate(){
		return index;
	}

	public void setRandomPredicate(){
		// set a random value to index ranging (0....[board size-1])
		Random rand = new Random();
		index = rand.nextInt(board.getBoardSize());
	}

	public boolean checkPredicate(Player player){
		// returns true if the player piece is located at the board location
		return board.getIndexValue(index) == player.getValue();
	}
}
