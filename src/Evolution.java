import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Evolution {
	private final Population population;
	private final int maxGenerations;
	private Board board;
	private Game game;
	private int playEveryNGame;
	private boolean selectRandomMaxIndex;

	private Individual frankyOriginal;
	private Individual franky;

	private boolean playTournament;
	private boolean playWithFranky;

	private CSV_Writer reportGenerator;

	//	private JProgressBar progressBar;
	private ProgressImage progressBar;


	public Evolution(Population population, int maxGenerations, int playEveryNGame, boolean playTournament, boolean playWithFranky, boolean selectRandomMaxIndex, ProgressImage progressBar) {
		this.population     = population;
		this.maxGenerations = maxGenerations;
		this.board = population.getBoard();
		this.game = new Game(board);
		this.selectRandomMaxIndex = selectRandomMaxIndex;
		this.playEveryNGame = playEveryNGame;
		this.playTournament = playTournament;
		this.playWithFranky = playWithFranky;
		this.progressBar = progressBar;
		// initialize franky
		frankyOriginal = new Individual(game.getBoard(), "Original Franky", selectRandomMaxIndex, null, null);
		frankyOriginal.frankenstein();
		frankyOriginal.setPlayerName("Original Franky");
		franky = new Individual(population.getBoard(), "Franky", selectRandomMaxIndex, null, null);
		franky.frankenstein2();
	}

	public Individual getBest() {
		return population.getBest();
	}

	public Individual getWorst(){
		return population.getWorst();
	}

	public void setTournaments(boolean playTournament, boolean playWithFranky){
		this.playTournament = playTournament;
		this.playWithFranky = playWithFranky;
	}

	public Individual evolve() {
		/*
		 * evolution engine
		 * each generation is a tournament where each individual play a two set game against all other individuals
		 */
		int gen;
		if(!playTournament && !playWithFranky){
			System.out.println("Must play at least one tournament!!!");
			System.exit(1);
		}

		// variables for estimating time left for evaluation
		double start = System.currentTimeMillis();
		double lastRoundStart = 0;
		double lastRoundTime = 0;
		double end;

		// create the report file
		createReportFile();
		// scores arrays for graph plotting
		List<Double> avgFitness = new ArrayList<Double>();
		List<Double> bestFitness = new ArrayList<Double>();
		List<Double> frankyOriginalFitness = new ArrayList<Double>();
		List<Double> frankyFitness = new ArrayList<Double>();

		System.out.println("Starting Evolution.....");
		for (gen = 1;  gen <= maxGenerations;  ++gen) {
			// get time before starting the next generation
			lastRoundStart = System.currentTimeMillis();
			// reset all individuals game results
			population.resetGameStats();
			if(playTournament){
				// play the tournament
				playTournament();
			}
			if(playWithFranky){
				// reset franky's game results
				frankyOriginal.resetGameStats();
				franky.resetGameStats();
				// play a tournament against franky
				playTournamentAgainstFranky(frankyOriginal);
				playTournamentAgainstFranky(franky);
				// evaluate franky's fitness
				frankyOriginal.evalFitness();
				frankyOriginal.printStats();
				franky.evalFitness();
				franky.printStats();
				// add franky's fitness to array (for graph)
				frankyOriginalFitness.add(frankyOriginal.getFitness());
				frankyFitness.add(franky.getFitness());
			}
			// evaluate population fitness
			population.evaluatePopulationFitness();
			// sort the individuals by their fitness (higher fitness on lower index)
			population.sort();
			// record the current generation data to .csv file
			writeGenerationData(gen);
			System.out.println("Generation " + gen + " of " + maxGenerations + ": " + getBest().getPlayerName() + " Fitness: " + getBest().getFitness());

			// get the time it took to play the tournament (divide by 1000 since the time is in milliseconds)
			lastRoundTime = (System.currentTimeMillis() - lastRoundStart)/1000;
			System.out.println("Last Round Took " + lastRoundTime + " Seconds");
			// multiply the last round time by the amount of rounds remaining (all rounds - current round)
			//System.out.println("Estimated Time to finish: " + ((lastRoundTime)*(maxGenerations - gen)) + " Seconds");
			System.out.println("Estimated Time to finish:");
			System.out.println("In Minutes: " + (((lastRoundTime)*(maxGenerations - gen))/60));
			System.out.println("In Seconds: " + (((lastRoundTime)*(maxGenerations - gen))));


			// add average and best fitness results to an array for graph
			avgFitness.add(population.getAvgPopulationFitness());
			bestFitness.add(getBest().getFitness());

			if(playEveryNGame != 0){
				if((gen % playEveryNGame == 0 || gen == maxGenerations)){
					// play a game against the top individual
					getBest().printStats();	
					playHumanVsBest(new Individual(getBest()));
				}
			}

			if (getBest().isIdeal(population.getPopSize())){
				System.out.println("Found Best Individual!!!");
				break;
			}

			// produce the next generation (crossover, mutation....)
			if(gen < maxGenerations) // do crossover, mutation, etc... if at last generation
				population.nextGeneration();
			progressBar.setPercentage(gen*100/maxGenerations);
			//			progressBar.setValue(gen*100/maxGenerations);
		}

		end = System.currentTimeMillis();
		System.out.println("Evolution Process Took " + (end-start)/1000 + " Seconds");


		if (gen-1 == maxGenerations)
			System.out.println("Best attempt: " + getBest());
		else
			System.out.println("Solution: " + getBest());

		// plot result graphs
		population.showGraph(avgFitness, "Average Fitness Graph");
		population.showGraph(bestFitness, "Best Fitness Graph");
		if(playWithFranky){
			population.showGraph(frankyOriginalFitness, "Original Franky Fitness Graph");
			population.showGraph(frankyFitness, "Franky Fitness Graph");
		}

		//runDemo(getBest(),franky);

		// prints the entire population stats
		//		population.printPopulation();
		return getBest();
	}

	private void runDemo(Individual ind1, Individual ind2) {
		/*
		 * run a demonstration game
		 */
		// show a demonstration game between two individuals
		GameGUI playGUI = new GameGUI(game, ind1);
		playGUI.showGUI();
		playGUI.Demo(ind1, ind2);
	}

	public void playHumanVsBest(Individual best){
		/*
		 * play a game against the current top individual of the population
		 */
		Board board = new Board();
		Game game = new Game(board);
		best.setValue(2);
		TreeDraw td = new TreeDraw(best);
		GameGUI playGUI = new GameGUI(game, best);
		playGUI.showGUI();
		td.repaint();
	}

	public void playTournament(){
		/*
		 * play a tournament between the population individuals
		 */
		System.out.println("Tournament Progress:");
		int popSize = population.getPopSize();
		for(int i=0;i<popSize;i++){
			// if the population size is divisible by 10, there will be a progress indication every 10% (0% - 90%)
			if(((float)(i*100)/popSize)%10 == 0){
				// print percentage of population which done competing
				System.out.print(" " + (i*100/popSize) + "%");
			}
			for(int j=i+1;j<popSize;j++){
				// play a match with individual at index [i] against individual at index [j]
				game.playTwoSetMatch(population.getIndividualAtIndex(i), population.getIndividualAtIndex(j));
			}
		}
		System.out.println();
	}

	private void playTournamentAgainstFranky(Individual franky) {
		/*
		 * play a tournament between the population individuals and a custom pre-made individual named Franky
		 */
		System.out.println("Tournament With '" + franky.getPlayerName() + "' Progress:");
		int popSize = population.getPopSize();
		for(int i=0;i<popSize;i++){
			// if the population size is divisible by 10, there will be a progress indication every 10% (0% - 90%)
			if(((float)(i*100)/popSize)%10 == 0){
				System.out.print(" " + (i*100/popSize) + "%");	
			}
			// play a match with individual at index [i] against franky
			game.playTwoSetMatch(population.getIndividualAtIndex(i), franky);
		}
		System.out.println();
		// evaluate the population fitness by calculating each individual wins, losses, draws
	}

	/*
	public void dump() {
		System.out.println("--------------------");

		for (Object o: population) {
			Individual ind = (Individual) o;
			System.out.println(ind);
		}

		System.out.println("--------------------");
	}
	 */

	public void createReportFile(){
		/*
		 * create the .csv file with basic information about the experiment parameters
		 */
		reportGenerator = new CSV_Writer(new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()) + ".csv");
		reportGenerator.createCsvFile();
		reportGenerator.appendCsvFile(new String[]{"Experiment Parameters:"});
		reportGenerator.appendCsvFile(new String[]{"Board Size:", Integer.toString(game.getBoard().getBoardSizeRow())});
		reportGenerator.appendCsvFile(new String[]{"Required Streak:", Integer.toString(game.getBoard().getStreak())});
		reportGenerator.appendCsvFile(new String[]{"Population Size:" , Integer.toString(population.getPopSize())});
		reportGenerator.appendCsvFile(new String[]{"Max Generations:" , Integer.toString(maxGenerations)});
		reportGenerator.appendCsvFile(new String[]{"Select Maximal Index Method:" , (selectRandomMaxIndex == true)? "Random":"First"});
		reportGenerator.appendCsvFile(new String[]{"Initial Individual Tree Depth:", Integer.toString(population.getInitialDepth())});
		reportGenerator.appendCsvFile(new String[]{"Maximum Individual Tree Depth:", Integer.toString(population.getMaxDepth())});
		reportGenerator.appendCsvFile(new String[]{"Crossover Probability:", Double.toString(population.getSelection().getCrossoverProb())});
		reportGenerator.appendCsvFile(new String[]{"Mutation Probability:",  Double.toString(population.getSelection().getMutationProb())});
		reportGenerator.appendCsvFile(new String[]{""}); // just add a line separator

		// write the function and terminal sets which were used in the experiment
		reportGenerator.appendCsvFile(new String[]{"Function Set:"});
		reportGenerator.appendCsvFile(population.getFunctionList());
		reportGenerator.appendCsvFile(new String[]{"Terminal Set:"});	
		reportGenerator.appendCsvFile(population.getTerminalList());

		reportGenerator.appendCsvFile(new String[]{""}); // just add a line separator
		reportGenerator.appendCsvFile(new String[]{"Generation","Worst Individual Name","Worst Individual Fitness","Best Individual Name","Best Individual Fitness","Average Fitness", "Best Individual Tree"});
	}

	public void writeGenerationData(int gen){
		/*
		 * write the current generation data
		 * generation number, worst individual name and fitness, best individual and fitness, average fitness, best individual tree representation as a flat string
		 */
		reportGenerator.appendCsvFile(new String[]{Integer.toString(gen),
				getWorst().getPlayerName(), Double.toString(getWorst().getFitness()),
				getBest().getPlayerName(), Double.toString(getBest().getFitness()),
				Double.toString(population.getAvgPopulationFitness()),
				getBest().getStrategyRoot().TreeStrFlat()});
	}
}
