import javax.swing.JPanel;

public class Driver {

	public static void runEvolution(){
		/*
		 * For different board size or different win streak, change values in Board.java
		 */
		int initialDepth = 4;
		int maxDepth = 6;
		boolean selectRandomMaxIndex = true; // choose whether to select first maximal value index or a random (if there are multiples)
		int popSize = 100;
		int maxGenerations = 50;
		int keepBestIndividualsInGeneration = 0; // amount of best individuals to keep from the old generation (pop[0],pop[1],...,pop[keepBestIndividualsInGeneration-1])
		int playEveryNGame = 100; // determines how often there will be a game against human player (-1 for None)
		boolean playTournament = false;
		boolean playWithFranky = true;
		double mutationProb   = 0.2;
		double crossoverProb  = 0.8;


		final String[] functionList = {
				"If <=",
				//"If >=",
				//"Minus",
				"Plus",
				//"Multi"
		};
		final String[] terminalList = {
				//"CountNeightbors",
				//"CountRow",
				"RowStreak",
				//"CountColumn",
				"ColumnStreak",
				//"CountDiagMain",
				"DiagMainStreak",
				//"CountDiagSec",
				"DiagSecStreak",
				//"CornerCount",
				//"RandVal",
				//"IsRandIndex",
				"WinOrBlock"
		};

		//		Function.setFunctionList(functionList);
		//		Terminal.setTerminalList(terminalList);



		// initialize the selection method and the new population
		Selection selection = new TournamentSelection(mutationProb, crossoverProb);
		Population population = new Population(popSize, selection, initialDepth ,maxDepth, selectRandomMaxIndex, keepBestIndividualsInGeneration, functionList, terminalList);
		// initialize the evolution and start the evolution engine
		Evolution evolution = new Evolution(population, maxGenerations, playEveryNGame, playTournament, playWithFranky, selectRandomMaxIndex, new ProgressImage(new JPanel()));
		evolution.evolve();
	}

	public static void runFranky(){
		//		 Play against a generated individual example
		NameGenerator randName = new NameGenerator();
		Board board = new Board();
		Game game = new Game(board);

		final String[] functionList = {
				"If <=",
				//"If >=",
				//"Minus",
				"Plus",
				//"Multi"
		};
		final String[] terminalList = {
				//"CountNeightbors",
				//"CountRow",
				"RowStreak",
				//"CountColumn",
				"ColumnStreak",
				//"CountDiagMain",
				"DiagMainStreak",
				//"CountDiagSec",
				"DiagSecStreak",
				//"CornerCount",
				//"RandVal",
				//"IsRandIndex",
				"WinOrBlock"
		};
		//		Function.setFunctionList(functionList);
		//		Terminal.setTerminalList(terminalList);


		Individual ind = new Individual(board, randName.generateName(), true, functionList, terminalList);
		ind.setValue(2);

		// comment either generateRandomStrategy or frankenstein
		//		ind.generateRandomStrategy(5); // generate random strategy
		ind.frankenstein2(); // generate custom strategy (pre-defined)
		//		System.out.println(ind.getStrategyRoot().TreeStrPseudo());


		Individual ind2 = new Individual(board, randName.generateName(), true, functionList, terminalList);
		ind2.setValue(2);
		ind2.frankenstein2();
		//				ind2.generateRandomStrategy(5);
		TreeDraw td = new TreeDraw(ind2);
		GameGUI playGUI = new GameGUI(game, ind2);
		playGUI.showGUI();
		//		playGUI.Demo(ind2, ind);
		td.repaint();
	}

	public static void main(String[] args) {

		//		runEvolution();
		//		runFranky();

		final String[] functionList = {
				//				"If <=",
				"If >=",
				//				"Minus",
				"Plus",
				"Multi"
		};
		final String[] terminalList = {
				"CountNeightbors",
				//				"CountRow",
				"RowStreak",
				//				"CountColumn",
				"ColumnStreak",
				//				"CountDiagMain",
				"DiagMainStreak",
				//				"CountDiagSec",
				"DiagSecStreak",
				//				"CornerCount",
				//				"RandVal",
				//				"IsRandIndex",
		"WinOrBlock"};

		//		Individual ind = new Individual(new Board(), "individual", false,functionList, terminalList);
		//		ind.generateRandomStrategy(3);
		//		TreeDraw td = new TreeDraw(ind);
		//		td.repaint();

		//		 Trimming example
		//		Individual ind = new Individual(new Board(), "individual",true,functionList,terminalList);
		//		ind.generateRandomStrategy(5);
		//		Individual copy = new Individual(ind);
		//		TreeDraw td1 = new TreeDraw(ind);
		//		TreeDraw td2 = new TreeDraw(copy);
		//		copy.trim(4);

		//				Crossover example
		//				Individual ind1 = new Individual(new Board(), "individual1", false, functionList, terminalList);
		//				Individual ind2 = new Individual(new Board(), "individual2", false, functionList,terminalList);
		//				ind1.generateRandomStrategy(3);
		//				ind2.generateRandomStrategy(3);
		//				Individual[] children = ind1.crossover(ind2);
		//		
		//				TreeDraw td1 = new TreeDraw(ind1);
		//				TreeDraw td2 = new TreeDraw(ind2);
		//				TreeDraw td3 = new TreeDraw(children[0]);
		//				TreeDraw td4 = new TreeDraw(children[1]);

	}

}
