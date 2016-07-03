import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@SuppressWarnings("rawtypes")
public class Individual extends Player implements Comparable{

	private double fitness;
	private GPTree strategy;
	private Board board;
	private boolean selectRandomMaxIndex; // decides whether to choose first encountered max index, or a random in case of multiple maximums
	// the arrays of game results (draws, losses, wins) represent a game result
	// index 0 represent the result when the player started the game
	// index 1 represent the result when the player has not started the game
	private String[] functionList = null;
	private String[] terminalList = null;

	private int[] wins;
	private int[] losses;
	private int[] draws;
	// index values for wins, losses, draws arrays
	private final int STARTED_GAME = 0;
	private final int NOT_STARTED_GAME = 1;
	// constants for fitness evaluation
	private final double WIN_START_FACTOR = 1;
	private final double WIN_NOT_START_FACTOR = 2;
	private final double LOSE_START_FACTOR = 0;
	private final double LOSE_NOT_START_FACTOR = 0;
	private final double DRAW_START_FACTOR = 0.3;
	private final double DRAW_NOT_START_FACTOR = 0.7;
	//		private final double DRAW_START_FACTOR = 3;
	//		private final double DRAW_NOT_START_FACTOR = 4;


	public Individual(Board board, String playerName, boolean selectRandomMaxIndex, String[] functionList, String[] terminalList) {
		super(playerName);
		fitness = 0;
		strategy = null;
		this.board = board;
		this.selectRandomMaxIndex = selectRandomMaxIndex;
		setFunctionList(functionList);
		setTerminalList(terminalList);
		wins = new int[2];
		losses = new int[2];
		draws = new int[2];
	}

	public Individual(Individual individual){
		super(individual.playerName);
		fitness = individual.fitness;
		strategy = new GPTree(individual.getStrategy());
		this.setBoard(individual.board);
		setFunctionList(individual.getFunctionList());
		setTerminalList(individual.getTerminalList());
		wins = new int[2];
		losses = new int[2];
		draws = new int[2];
	}

	public void setFunctionList(String[] functionList){
		if(functionList != null)
			this.functionList = functionList.clone();
	}

	public void setTerminalList(String[] terminalList){
		if(terminalList != null)
			this.terminalList = terminalList.clone();
	}

	public String[] getFunctionList(){
		return this.functionList;
	}

	public String[] getTerminalList(){
		return this.terminalList;
	}

	public String getRandomFunction(){
		String function = functionList[(int) (Math.random()*functionList.length)];
		return function;
	}

	public String getRandomTerminal(){
		String terminal = terminalList[(int) (Math.random()*terminalList.length)];
		return terminal;
	}

	public void addDraw(boolean started){
		if(started)
			draws[STARTED_GAME]++;
		else
			draws[NOT_STARTED_GAME]++;
	}

	public void addLoss(boolean started){
		if(started)
			losses[STARTED_GAME]++;
		else
			losses[NOT_STARTED_GAME]++;
	}

	public void addWin(boolean started){
		if(started)
			wins[STARTED_GAME]++;
		else
			wins[NOT_STARTED_GAME]++;
	}

	public void resetGameStats(){
		wins[STARTED_GAME] =
				wins[NOT_STARTED_GAME] =
				losses[STARTED_GAME] =
				losses[NOT_STARTED_GAME]=
				draws[STARTED_GAME] =
				draws[NOT_STARTED_GAME] = 0;
	}

	public int getGamesPlayed(){
		return (wins[STARTED_GAME] + wins[NOT_STARTED_GAME] + losses[STARTED_GAME] + losses[NOT_STARTED_GAME] + draws[STARTED_GAME] + draws[NOT_STARTED_GAME]);
	}

	public void printStats(){
		System.out.println(getPlayerName() + " Stats");
		System.out.println("Fitness: " + fitness);
		System.out.println("Game Played: " + (wins[STARTED_GAME] + wins[NOT_STARTED_GAME] + losses[STARTED_GAME] + losses[NOT_STARTED_GAME] + draws[STARTED_GAME] + draws[NOT_STARTED_GAME]));
		System.out.println("      Started     Not Started");
		System.out.println("Wins    " + wins[STARTED_GAME] + "             " + wins[NOT_STARTED_GAME]);
		System.out.println("Losses  " + losses[STARTED_GAME] + "             " + losses[NOT_STARTED_GAME]);
		System.out.println("Draws   " + draws[STARTED_GAME] + "             " + draws[NOT_STARTED_GAME]);
	}

	public void setStrategy(GPTree strategy){
		this.strategy = strategy;
	}

	public GPTree getStrategy(){
		/*
		 * returns the strategy tree (GPTree object)
		 */
		return strategy;
	}

	public GPNode getStrategyRoot(){
		/*
		 * returns the strategy tree root node (GPNode object)
		 */
		return strategy.getRoot();
	}

	public void setBoard(Board board){
		/*
		 * sets the individual playing board
		 */
		this.board = board;
		if(strategy != null)
			this.getStrategyRoot().setTreeBoard(board);
	}

	public Board getBoard(){
		/*
		 * returns the individual playing board
		 */
		return board;
	}

	public void evalFitness(){
		/*
		 * fitness calculation
		 * the fitness is determined by the tournament results of an individual
		 * each result type (win/loss/draw) have a value
		 * to evaluate, multiply each result amount by type score factor and sum it all
		 */
		fitness = wins[STARTED_GAME]*WIN_START_FACTOR +
				wins[NOT_STARTED_GAME]*WIN_NOT_START_FACTOR +
				losses[STARTED_GAME]*LOSE_START_FACTOR +
				losses[NOT_STARTED_GAME]*LOSE_NOT_START_FACTOR +
				draws[STARTED_GAME]*DRAW_START_FACTOR +
				draws[NOT_STARTED_GAME]*DRAW_NOT_START_FACTOR;
	}

	public double getFitness(){
		/*
		 * returns the individual fitness
		 */
		return fitness;
	}


	public Individual[] crossover(Individual otherIndividual){
		/*
		 * crossover function
		 * clone both parent (the clones will be the new children after the crossover operation)
		 * randomly choose a node from each cloned parent
		 * swap the nodes
		 */
		Random rand = new Random();
		int randNum;
		final int MIN = 2;
		Individual[] children = new Individual[2];
		children[0] = new Individual(this);
		children[1] = new Individual(otherIndividual);
		// GPNode objects to temporary hold the returned references
		GPNode swap1 = null;
		GPNode swap2 = null;

		// randomize a number ranging from 2....nodesAmount of first parent
		randNum = rand.nextInt(children[0].getStrategyRoot().countNodes() - MIN + 1) + MIN;
		// paint the selected nodes at the original first parent
		this.getStrategyRoot().getNode(this.getStrategyRoot(), randNum).paintNode(Color.red);
		// get the first random node reference
		swap1 = children[0].getStrategyRoot().getNode(children[0].getStrategyRoot(), randNum);
		// randomize a number ranging from 2....nodesAmount of second parent
		randNum = rand.nextInt(children[1].getStrategyRoot().countNodes() - MIN + 1) + MIN;
		// paint the selected nodes at the original second parent
		otherIndividual.getStrategyRoot().getNode(otherIndividual.getStrategyRoot(), randNum).paintNode(Color.red);
		// get the second random node reference
		swap2 = children[1].getStrategyRoot().getNode(children[1].getStrategyRoot(),randNum);

		// paint the swapped nodes
		swap1.paintNode(Color.green);
		swap2.paintNode(Color.green);

		// make the reference swaps
		//		System.out.println("Swapping " + swap1.TreeStr() + " with " + swap2.TreeStr() + " node #" + temp);
		children[0].getStrategyRoot().swapNodes(swap1, swap2);
		//		System.out.println("Swapping " + swap2.TreeStr() + " with " + swap1.TreeStr() + " node #" + randNum);
		children[1].getStrategyRoot().swapNodes(swap2, swap1);
		return children;
	}

	public Individual crossover2(Individual otherIndividual){
		/*
		 * crossover function
		 * clone both parent (the clones will be the new children after the crossover operation)
		 * randomly choose a node from each cloned parent
		 * swap the nodes
		 */
		Random rand = new Random();
		int randNum;
		final int MIN = 2;
		// GPNode objects to temporary hold the returned references
		GPNode swap1 = null;
		GPNode swap2 = null;

		// randomize a number ranging from 2....nodesAmount of first parent
		randNum = rand.nextInt(this.getStrategyRoot().countNodes() - MIN + 1) + MIN;
		// paint the selected nodes at the original first parent
		this.getStrategyRoot().getNode(this.getStrategyRoot(), randNum).paintNode(Color.red);
		// get the first random node reference
		swap1 = this.getStrategyRoot().getNode(this.getStrategyRoot(), randNum);
		// randomize a number ranging from 2....nodesAmount of second parent
		randNum = rand.nextInt(otherIndividual.getStrategyRoot().countNodes() - MIN + 1) + MIN;
		// paint the selected nodes at the original second parent
		otherIndividual.getStrategyRoot().getNode(otherIndividual.getStrategyRoot(), randNum).paintNode(Color.red);
		// get the second random node reference
		swap2 = otherIndividual.getStrategyRoot().getNode(otherIndividual.getStrategyRoot(),randNum);

		// paint the swapped nodes
		swap1.paintNode(Color.green);
		swap2.paintNode(Color.green);

		// make the reference swaps
		//		System.out.println("Swapping " + swap1.TreeStr() + " with " + swap2.TreeStr() + " node #" + temp);
		this.getStrategyRoot().swapNodes(swap1, swap2.copy(swap2));

		// combine the parents name to create a new name
		this.setPlayerName(this.getPlayerName().substring(0, this.getPlayerName().length()/2) + 
				otherIndividual.getPlayerName().substring(otherIndividual.getPlayerName().length()/2-1, otherIndividual.getPlayerName().length() - 1));
		return this;
	}

	public Individual mutate(){
		/*
		 * randomly choose a node and swap it with randomly generated tree with max depth of 3
		 * (not a full tree)
		 */
		Random rand = new Random();
		int randNum;
		Individual mutatedIndividual = new Individual(this);
		final int MIN = 2;
		GPNode mutateNode = null;
		GPNode mutateNodeSwap = null;
		randNum = rand.nextInt(this.getStrategyRoot().countNodes() - MIN + 1) + MIN;
		mutateNode = mutatedIndividual.getStrategyRoot().getNode(mutatedIndividual.getStrategyRoot(), randNum-1);

		if(mutateNode instanceof Terminal){
			// if the selected node is a terminal,
			// set it to another random terminal
			((Terminal)mutateNode).setRandTerminal();
		}
		else{
			// if the selected node is a function,
			// generate a random tree (not full) of max depth of 3 and swap the nodes

			// need to research other sizes of random generated mutation tree
			mutateNodeSwap = GPNode.generateFullTree(3, board, this);
			mutatedIndividual.getStrategyRoot().swapNodes(mutateNode, mutateNodeSwap);
		}
		return mutatedIndividual;
	}

	public void trim(int maxDepth){
		/*
		 * trim existing tree to have maximal depth of maxDepth
		 */
		this.getStrategyRoot().trim(maxDepth-1, this);
	}
	public boolean generateRandomStrategy(int maxDepth){
		/*
		 * generate a random strategy tree by initializing
		 * the tree and adding functions and terminals
		 */
		if(strategy != null)
			return false;
		strategy = new GPTree(board, this);
		return strategy.growRandomTree(maxDepth);
	}

	public void frankenstein(){
		strategy = new GPTree(board, this);
		strategy.generateFranky();
		setPlayerName("Original Franky");
	}

	public void frankenstein2(){
		strategy = new GPTree(board, this);
		strategy.generateFranky2();
		setPlayerName("Franky");
	}

	public boolean makeStrategyMove(){
		/*
		 *  evaluate the board by running the strategy tree on each of the board's indexes
		 *  the index with max value is the chosen one
		 */
		long[] gradesBoard = new long[strategy.getRoot().getBoard().getBoardSize()];
		long max = Integer.MIN_VALUE;
		for(int i=0;i<gradesBoard.length;i++){
			if(strategy.getRoot().getBoard().getIndexValue(i) != 0){
				// mark occupied spaces (the location is occupied by '1' or '2')
				gradesBoard[i] = -1;
			}
			else{
				// grade free spaces by running the evaluation tree on the location
				gradesBoard[i] = Math.abs(strategy.getRoot().evalIndexGrade(strategy.getRoot(), i));
				//				gradesBoard[i] = strategy.getRoot().evalIndexGrade(strategy.getRoot(),i);
				if(gradesBoard[i] >= max)
					max = gradesBoard[i];
			}
		}

		// find the max index in the board
		int index = getMaxIndex(gradesBoard, max);

		//		printEvaluatedBoard(gradesBoard);
		//		System.out.println("Setting at " + index);

		// double check to attempt move
		return board.attemptMove(index, this);
	}

	public int getMaxIndex(long[] gradesBoard, long max){
		/*
		 * return the index of the location with best grade
		 * if multiple bests, returns the first which was found or a random max index
		 * (depends on selectRandomMaxIndex boolean variable)
		 */

		// create an array of indexes
		List<Integer> maxIndexesArray = new ArrayList<Integer>();
		// iterate all indexes and add the indexes of the max ones
		for(int i=0;i<gradesBoard.length;i++){
			// skip occupied spaces
			if(strategy.getRoot().getBoard().getIndexValue(i) != 0);
			else if(gradesBoard[i] == max) // found max, add to the array
				maxIndexesArray.add(i);
		}

		// depending of the individual settings, select first max or a random one
		try{
			if(selectRandomMaxIndex)
				return maxIndexesArray.get((int) (Math.random()*maxIndexesArray.size()));
			return maxIndexesArray.get(0);
		}catch(IndexOutOfBoundsException e){
			System.out.println(e.getMessage());
			return getRandomFreeIndex();
		}
	}


	public void printEvaluatedBoard(long[] board){
		/*
		 *  prints the evaluation grades board
		 */
		for(int i=0;i<board.length;i++){
			if(i%getBoard().getBoardSizeRow() == 0){
				System.out.println();
				System.out.print(i + " | ");
			}
			System.out.print(" " + board[i] + " ");
		}
		System.out.println();
	}
	public int getRandomFreeIndex(){
		/*
		 * generate random index and attempt to make a move to that index
		 * keep on generating and attempting until success to place at the generated index
		 */
		int index;
		boolean flag = false;
		Random rand = new Random();
		do{
			index = rand.nextInt(board.getBoardSize());
			if(board.indexEmpty(index)){
				//board.setIndex(index, this);
				flag = true;
			}
		}while(!flag);
		return index;
	}

	public void setTreeBoard(Board board){
		this.strategy.setTreeBoard(board);
	}

	@Override
	public int compareTo(Object obj) {
		Individual other = (Individual) obj;
		// sort from lower to higher
		//		return new Double(getFitness()).compareTo(new Double(other.getFitness()));
		// sort from higher to lower
		return new Double(other.getFitness()).compareTo(new Double(getFitness()));
	}

	public boolean isIdeal(int popSize) {
		return losses[STARTED_GAME] +
				losses[NOT_STARTED_GAME] +
				draws[STARTED_GAME] +
				draws[NOT_STARTED_GAME] == 0;
	}

}
