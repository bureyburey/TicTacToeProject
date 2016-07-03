import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import java.awt.Cursor;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.GridLayout;

import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

public class MainGUI {
	/*
	 * Recommendation:
	 * go to
	 * https://eclipse.org/windowbuilder/download.php
	 * and download the Window Builder plug-in for eclipse easy GUI creation
	 */
	// default initial values
	private int initialDepth = 4;
	private int maxDepth = 6;
	private boolean selectRandomMaxIndex = true; // choose whether to select first maximal value index or a random (if there are multiples)
	private int popSize = 100;
	private int maxGenerations = 50;
	private int keepBestIndividualsInGeneration = 0; // amount of best individuals to keep from the old generation (pop[0],pop[1],...,pop[keepBestIndividualsInGeneration-1])
	private int playEveryNGame = 100; // determines how often there will be a game against human player (-1 for None)
	private boolean playTournament = false;
	private boolean playWithFranky = true;
	private double mutationProb   = 0.2;
	private double crossoverProb  = 0.8;

	private Individual bestIndividual = null;

	private String[] functionList = new String[]{};
	private String[] terminalList = new String[]{};

	private boolean validInput = true;

	private final String HTML_OPEN = "<html>";
	private final String HTML_CLOSE = "</html>";
	private final String HTML_NEWLINE = "<br>";

	private JFrame frame;
	private JLabel lblSelectRandomMaxIndex;
	private JComboBox<String> inputSelectRandomMaxIndex;
	private JLabel lblKeepBestIndividualsInGeneration;
	private JSlider inputKeepBestIndividualsInGeneration;
	private JSlider inputInitialDepth;
	private JSlider inputMaxDepth;
	private JSlider inputPopSize;
	private JSlider inputMaxGenerations;
	private JLabel lblPlayEveryNGame;
	private JSlider inputPlayEveryNGame;
	private JPanel panelTournamentMode;
	private JCheckBox inputFrankyTournament;
	private JCheckBox inputPlayEachOther;
	private JLabel lblCrossoverProb;
	private JSlider inputCrossoverProb;
	private JLabel lblMutationProbability;
	private JSlider inputMutationProb;
	private JButton btnRunEvolution;
	private JLabel lblFunctionSet;
	private JLabel lblTerminalSet;
	private JPanel panelFunctionSet;
	private JCheckBox chckbxIfGE;
	private JCheckBox chckbxIfLE;
	private JCheckBox chckbxPlus;
	private JCheckBox chckbxMinus;
	private JCheckBox chckbxMulti;
	private JPanel panelTerminalSet;
	private JCheckBox chckbxCountneightbors;
	private JCheckBox chckbxCornercount;
	private JCheckBox chckbxCountrow;
	private JCheckBox chckbxCountcolumn;
	private JCheckBox chckbxCountdiagmain;
	private JCheckBox chckbxCountdiagsec;
	private JCheckBox chckbxRowstreak;
	private JCheckBox chckbxColumnstreak;
	private JCheckBox chckbxDiagmainstreak;
	private JCheckBox chckbxDiagsecstreak;
	private JCheckBox chckbxRandval;
	private JCheckBox chckbxIsrandindex;
	private JCheckBox chckbxWinorblock;
	private JLabel lblTournamentMode;
	private JMenuBar menuBar;
	private JMenu mnMainMenu;
	private JMenuItem mntmHelp;
	private JMenuItem mntmAbout;
	private JMenuItem mntmExit;
	private JPanel panelProgressBar;
	private ProgressImage progressEvolution;
	private JButton btnPlayAgainst;
	private JPanel panelPlayAgainst;
	private JComboBox<String> comboBoxPlayerOne;
	private JLabel lblVersus;
	private JComboBox<String> comboBoxPlayerTwo;
	//	private JProgressBar progressEvolution;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Genetic Programming Tic Tac Toe");
		frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		frame.setBounds(10, 10, 770, 706);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 64};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0};
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblInitialDepth = new JLabel("Initial Individual Tree Depth");
		lblInitialDepth.setToolTipText("Initial individual tree depth");
		GridBagConstraints gbc_lblInitialDepth = new GridBagConstraints();
		gbc_lblInitialDepth.insets = new Insets(0, 0, 5, 5);
		gbc_lblInitialDepth.gridx = 0;
		gbc_lblInitialDepth.gridy = 0;
		frame.getContentPane().add(lblInitialDepth, gbc_lblInitialDepth);

		inputInitialDepth = new JSlider();
		inputInitialDepth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				lblInitialDepth.setText("Initial Individual Tree Depth (" + inputInitialDepth.getValue() + ")");
			}
		});
		inputInitialDepth.setMajorTickSpacing(1);
		inputInitialDepth.setPaintLabels(true);
		inputInitialDepth.setValue(3);
		inputInitialDepth.setMinimum(3);
		inputInitialDepth.setMaximum(10);
		GridBagConstraints gbc_inputInitialDepth = new GridBagConstraints();
		gbc_inputInitialDepth.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputInitialDepth.insets = new Insets(0, 0, 5, 0);
		gbc_inputInitialDepth.gridx = 1;
		gbc_inputInitialDepth.gridy = 0;
		frame.getContentPane().add(inputInitialDepth, gbc_inputInitialDepth);

		JLabel lblMaxDepth = new JLabel("Maximal Individual Tree Depth");
		lblMaxDepth.setToolTipText(HTML_OPEN + "Maximum individual tree depth" + 
				HTML_NEWLINE + "(excess depths will be trimmed)" + HTML_CLOSE);
		GridBagConstraints gbc_lblMaxDepth = new GridBagConstraints();
		gbc_lblMaxDepth.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxDepth.gridx = 0;
		gbc_lblMaxDepth.gridy = 1;
		frame.getContentPane().add(lblMaxDepth, gbc_lblMaxDepth);

		inputMaxDepth = new JSlider();
		inputMaxDepth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lblMaxDepth.setText("Maximal Individual Tree Depth (" + inputMaxDepth.getValue() + ")");
			}
		});
		inputMaxDepth.setMajorTickSpacing(1);
		inputMaxDepth.setPaintLabels(true);
		inputMaxDepth.setMinimum(3);
		inputMaxDepth.setValue(6);
		inputMaxDepth.setMaximum(10);
		GridBagConstraints gbc_inputMaxDepth = new GridBagConstraints();
		gbc_inputMaxDepth.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputMaxDepth.insets = new Insets(0, 0, 5, 0);
		gbc_inputMaxDepth.gridx = 1;
		gbc_inputMaxDepth.gridy = 1;
		frame.getContentPane().add(inputMaxDepth, gbc_inputMaxDepth);

		JLabel lblPopSize = new JLabel("Population Size");
		lblPopSize.setToolTipText("The evolution population size");
		GridBagConstraints gbc_lblPopSize = new GridBagConstraints();
		gbc_lblPopSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblPopSize.gridx = 0;
		gbc_lblPopSize.gridy = 2;
		frame.getContentPane().add(lblPopSize, gbc_lblPopSize);

		inputPopSize = new JSlider();
		inputPopSize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lblPopSize.setText("Population Size (" + inputPopSize.getValue() + ")");
			}
		});
		inputPopSize.setValue(100);
		inputPopSize.setPaintLabels(true);
		inputPopSize.setMajorTickSpacing(50);
		inputPopSize.setMaximum(500);
		GridBagConstraints gbc_inputPopSize = new GridBagConstraints();
		gbc_inputPopSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputPopSize.insets = new Insets(0, 0, 5, 0);
		gbc_inputPopSize.gridx = 1;
		gbc_inputPopSize.gridy = 2;
		frame.getContentPane().add(inputPopSize, gbc_inputPopSize);

		JLabel lblMaxGenerations = new JLabel("Maximum Generations");
		lblMaxGenerations.setToolTipText(HTML_OPEN + "Maximum amount of generations (tournaments to play)" +
				HTML_NEWLINE + "(evolution can stop prematurely if an ideal individual is found)" + HTML_CLOSE);
		GridBagConstraints gbc_lblMaxGenerations = new GridBagConstraints();
		gbc_lblMaxGenerations.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxGenerations.gridx = 0;
		gbc_lblMaxGenerations.gridy = 3;
		frame.getContentPane().add(lblMaxGenerations, gbc_lblMaxGenerations);

		inputMaxGenerations = new JSlider();
		inputMaxGenerations.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lblMaxGenerations.setText("Maximum Generations (" + inputMaxGenerations.getValue() + ")");
			}
		});
		inputMaxGenerations.setPaintLabels(true);
		inputMaxGenerations.setMajorTickSpacing(5);
		inputMaxGenerations.setMaximum(50);
		GridBagConstraints gbc_inputMaxGenerations = new GridBagConstraints();
		gbc_inputMaxGenerations.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputMaxGenerations.insets = new Insets(0, 0, 5, 0);
		gbc_inputMaxGenerations.gridx = 1;
		gbc_inputMaxGenerations.gridy = 3;
		frame.getContentPane().add(inputMaxGenerations, gbc_inputMaxGenerations);

		lblSelectRandomMaxIndex = new JLabel("Best Evaluated Index Selection Method");
		lblSelectRandomMaxIndex.setToolTipText(HTML_OPEN + "Random Maximum:" +
				HTML_NEWLINE + "Selects a random index which have the maximum value" +
				HTML_NEWLINE + "First Maximum:"+
				HTML_NEWLINE +"Selects the first appearence of a maximum value (0 to BoardSize-1)" + HTML_CLOSE);
		GridBagConstraints gbc_lblSelectRandomMaxIndex = new GridBagConstraints();
		gbc_lblSelectRandomMaxIndex.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectRandomMaxIndex.gridx = 0;
		gbc_lblSelectRandomMaxIndex.gridy = 4;
		frame.getContentPane().add(lblSelectRandomMaxIndex, gbc_lblSelectRandomMaxIndex);

		inputSelectRandomMaxIndex = new JComboBox<String>();
		inputSelectRandomMaxIndex.setModel(new DefaultComboBoxModel<String>(new String[] {"Random Maximum", "First Maximum"}));
		GridBagConstraints gbc_inputSelectRandomMaxIndex = new GridBagConstraints();
		gbc_inputSelectRandomMaxIndex.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputSelectRandomMaxIndex.insets = new Insets(0, 0, 5, 0);
		gbc_inputSelectRandomMaxIndex.gridx = 1;
		gbc_inputSelectRandomMaxIndex.gridy = 4;
		frame.getContentPane().add(inputSelectRandomMaxIndex, gbc_inputSelectRandomMaxIndex);

		lblKeepBestIndividualsInGeneration = new JLabel("Amount of Best Individuals to Keep");
		lblKeepBestIndividualsInGeneration.setToolTipText(HTML_OPEN + "Selects amount of top individuals to keep from the previous generation" +
				HTML_NEWLINE + "(will be copied without crossover or mutation)" +
				HTML_NEWLINE +"(selected from index 0 to amount-1)" + HTML_CLOSE);
		GridBagConstraints gbc_lblKeepBestIndividualsInGeneration = new GridBagConstraints();
		gbc_lblKeepBestIndividualsInGeneration.insets = new Insets(0, 0, 5, 5);
		gbc_lblKeepBestIndividualsInGeneration.gridx = 0;
		gbc_lblKeepBestIndividualsInGeneration.gridy = 5;
		frame.getContentPane().add(lblKeepBestIndividualsInGeneration, gbc_lblKeepBestIndividualsInGeneration);

		inputKeepBestIndividualsInGeneration = new JSlider();
		inputKeepBestIndividualsInGeneration.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lblKeepBestIndividualsInGeneration.setText("Amount of Best Individuals to Keep (" + inputKeepBestIndividualsInGeneration.getValue() + ")");
			}
		});
		inputKeepBestIndividualsInGeneration.setMajorTickSpacing(5);
		inputKeepBestIndividualsInGeneration.setPaintLabels(true);
		inputKeepBestIndividualsInGeneration.setMaximum(50);
		inputKeepBestIndividualsInGeneration.setValue(0);
		//inputKeepBestIndividualsInGeneration.setToolTipText(Integer.toString(inputKeepBestIndividualsInGeneration.getValue()));
		GridBagConstraints gbc_inputKeepBestIndividualsInGeneration = new GridBagConstraints();
		gbc_inputKeepBestIndividualsInGeneration.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputKeepBestIndividualsInGeneration.insets = new Insets(0, 0, 5, 0);
		gbc_inputKeepBestIndividualsInGeneration.gridx = 1;
		gbc_inputKeepBestIndividualsInGeneration.gridy = 5;
		frame.getContentPane().add(inputKeepBestIndividualsInGeneration, gbc_inputKeepBestIndividualsInGeneration);

		lblPlayEveryNGame = new JLabel("Play Against Best Individual After # Generations");
		lblPlayEveryNGame.setToolTipText("Allows to play against the best individual of the current generation if the value matches");
		GridBagConstraints gbc_lblPlayEveryNGame = new GridBagConstraints();
		gbc_lblPlayEveryNGame.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlayEveryNGame.gridx = 0;
		gbc_lblPlayEveryNGame.gridy = 6;
		frame.getContentPane().add(lblPlayEveryNGame, gbc_lblPlayEveryNGame);

		inputPlayEveryNGame = new JSlider();
		inputPlayEveryNGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lblPlayEveryNGame.setText("Play Against Best Individual After # Generations (" + inputPlayEveryNGame.getValue() + ")");
			}
		});
		inputPlayEveryNGame.setValue(0);
		inputPlayEveryNGame.setMaximum(inputMaxGenerations.getMaximum());
		inputPlayEveryNGame.setPaintLabels(true);
		inputPlayEveryNGame.setMajorTickSpacing(10);
		GridBagConstraints gbc_inputPlayEveryNGame = new GridBagConstraints();
		gbc_inputPlayEveryNGame.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputPlayEveryNGame.insets = new Insets(0, 0, 5, 0);
		gbc_inputPlayEveryNGame.gridx = 1;
		gbc_inputPlayEveryNGame.gridy = 6;
		frame.getContentPane().add(inputPlayEveryNGame, gbc_inputPlayEveryNGame);

		lblTournamentMode = new JLabel("Tournament Mode");
		lblTournamentMode.setToolTipText(HTML_OPEN + "Play With Franky Mode:" +
				HTML_NEWLINE +"Each individual from the population will play two games of two sets (play first and play second) against a custom built indviduals:"+
				HTML_NEWLINE + "Franky := an individual with offensive tendencies" +
				HTML_NEWLINE + "Each individual from the population will play two games of two sets (play first and play second) against a custom built indviduals:"+
				HTML_NEWLINE + "Original Franky := an individual with deffensive tendencies" +
				HTML_NEWLINE + "Each individual from the population will play two games of two sets (play first and play second) against a custom built indviduals:"+
				HTML_NEWLINE + "Play Against Each Other:" +
				HTML_NEWLINE + "Each individual from the population will play two games of two sets (play first and play second) against a custom built indviduals:"+
				HTML_NEWLINE + "Each individual will play one game of two sets (play first and play second) against all other individuals of the population" + HTML_CLOSE);
		GridBagConstraints gbc_lblTournamentMode = new GridBagConstraints();
		gbc_lblTournamentMode.fill = GridBagConstraints.VERTICAL;
		gbc_lblTournamentMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblTournamentMode.gridx = 0;
		gbc_lblTournamentMode.gridy = 7;
		frame.getContentPane().add(lblTournamentMode, gbc_lblTournamentMode);

		panelTournamentMode = new JPanel();
		GridBagConstraints gbc_panelTournamentMode = new GridBagConstraints();
		gbc_panelTournamentMode.insets = new Insets(0, 0, 5, 0);
		gbc_panelTournamentMode.fill = GridBagConstraints.BOTH;
		gbc_panelTournamentMode.gridx = 1;
		gbc_panelTournamentMode.gridy = 7;
		frame.getContentPane().add(panelTournamentMode, gbc_panelTournamentMode);
		GridBagLayout gbl_panelTournamentMode = new GridBagLayout();
		gbl_panelTournamentMode.columnWidths = new int[]{0, 0, 0};
		gbl_panelTournamentMode.rowHeights = new int[] {0};
		gbl_panelTournamentMode.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelTournamentMode.rowWeights = new double[]{0.0};
		panelTournamentMode.setLayout(gbl_panelTournamentMode);

		inputFrankyTournament = new JCheckBox("Play Against Franky");
		inputFrankyTournament.setSelected(true);
		GridBagConstraints gbc_inputFrankyTournament = new GridBagConstraints();
		gbc_inputFrankyTournament.anchor = GridBagConstraints.WEST;
		gbc_inputFrankyTournament.insets = new Insets(0, 0, 5, 5);
		gbc_inputFrankyTournament.gridx = 0;
		gbc_inputFrankyTournament.gridy = 0;
		panelTournamentMode.add(inputFrankyTournament, gbc_inputFrankyTournament);

		inputPlayEachOther = new JCheckBox("Play Against Each Other");
		GridBagConstraints gbc_inputPlayEachOther = new GridBagConstraints();
		gbc_inputPlayEachOther.insets = new Insets(0, 0, 5, 0);
		gbc_inputPlayEachOther.anchor = GridBagConstraints.WEST;
		gbc_inputPlayEachOther.gridx = 1;
		gbc_inputPlayEachOther.gridy = 0;
		panelTournamentMode.add(inputPlayEachOther, gbc_inputPlayEachOther);

		lblCrossoverProb = new JLabel("Crossover Probability");
		lblCrossoverProb.setToolTipText("Probability for crossover operation on an individual");
		GridBagConstraints gbc_lblCrossoverProb = new GridBagConstraints();
		gbc_lblCrossoverProb.insets = new Insets(0, 0, 5, 5);
		gbc_lblCrossoverProb.gridx = 0;
		gbc_lblCrossoverProb.gridy = 8;
		frame.getContentPane().add(lblCrossoverProb, gbc_lblCrossoverProb);

		Dictionary<Integer, JLabel> probabilityValues = new Hashtable<Integer, JLabel>();
		for(int i=0;i<=10;i++){
			String text = String.format("%1.1f", (float)(i*0.1));
			probabilityValues.put(i, new JLabel(text));
		}

		inputCrossoverProb = new JSlider();
		inputCrossoverProb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lblCrossoverProb.setText("Crossover Probability (" + String.format("%1.1f", inputCrossoverProb.getValue()*0.1) + ")");
			}
		});
		inputCrossoverProb.setValue(8);
		inputCrossoverProb.setLabelTable(probabilityValues);
		inputCrossoverProb.setMajorTickSpacing(1);
		inputCrossoverProb.setPaintLabels(true);
		inputCrossoverProb.setMaximum(10);
		GridBagConstraints gbc_inputCrossoverProb = new GridBagConstraints();
		gbc_inputCrossoverProb.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputCrossoverProb.insets = new Insets(0, 0, 5, 0);
		gbc_inputCrossoverProb.gridx = 1;
		gbc_inputCrossoverProb.gridy = 8;
		frame.getContentPane().add(inputCrossoverProb, gbc_inputCrossoverProb);

		lblMutationProbability = new JLabel("Mutation Probability");
		lblMutationProbability.setToolTipText("Probability for mutation operation on an individual");
		GridBagConstraints gbc_lblMutationProbability = new GridBagConstraints();
		gbc_lblMutationProbability.insets = new Insets(0, 0, 5, 5);
		gbc_lblMutationProbability.gridx = 0;
		gbc_lblMutationProbability.gridy = 9;
		frame.getContentPane().add(lblMutationProbability, gbc_lblMutationProbability);

		inputMutationProb = new JSlider();
		inputMutationProb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lblMutationProbability.setText("Mutation Probability (" + String.format("%1.1f", inputMutationProb.getValue()*0.1) + ")");
			}
		});
		inputMutationProb.setValue(2);
		inputMutationProb.setLabelTable(probabilityValues);
		inputMutationProb.setMajorTickSpacing(1);
		inputMutationProb.setMaximum(10);
		inputMutationProb.setPaintLabels(true);
		GridBagConstraints gbc_inputMutationProb = new GridBagConstraints();
		gbc_inputMutationProb.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputMutationProb.insets = new Insets(0, 0, 5, 0);
		gbc_inputMutationProb.gridx = 1;
		gbc_inputMutationProb.gridy = 9;
		frame.getContentPane().add(inputMutationProb, gbc_inputMutationProb);

		lblFunctionSet = new JLabel("Function Set");
		lblFunctionSet.setToolTipText("The function set that will be used for the evolution process");
		GridBagConstraints gbc_lblFunctionSet = new GridBagConstraints();
		gbc_lblFunctionSet.insets = new Insets(0, 0, 5, 5);
		gbc_lblFunctionSet.gridx = 0;
		gbc_lblFunctionSet.gridy = 10;
		frame.getContentPane().add(lblFunctionSet, gbc_lblFunctionSet);

		panelFunctionSet = new JPanel();
		panelFunctionSet.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panelFunctionSet = new GridBagConstraints();
		gbc_panelFunctionSet.insets = new Insets(0, 0, 5, 0);
		gbc_panelFunctionSet.fill = GridBagConstraints.BOTH;
		gbc_panelFunctionSet.gridx = 1;
		gbc_panelFunctionSet.gridy = 10;
		frame.getContentPane().add(panelFunctionSet, gbc_panelFunctionSet);
		GridBagLayout gbl_panelFunctionSet = new GridBagLayout();
		gbl_panelFunctionSet.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelFunctionSet.rowHeights = new int[]{0, 0};
		gbl_panelFunctionSet.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelFunctionSet.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelFunctionSet.setLayout(gbl_panelFunctionSet);

		chckbxIfGE = new JCheckBox("If>=");
		chckbxIfGE.setSelected(true);
		chckbxIfGE.setToolTipText(HTML_OPEN + "If GreaterEqual(arg0, arg1, arg2, arg3)" +
				HTML_NEWLINE + "if(arg0 Greater or Equal to arg1)" +
				HTML_NEWLINE + "return arg2" +
				HTML_NEWLINE + "else" +
				HTML_NEWLINE + "return arg3");
		GridBagConstraints gbc_chckbxIfGE = new GridBagConstraints();
		gbc_chckbxIfGE.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxIfGE.gridx = 0;
		gbc_chckbxIfGE.gridy = 0;
		panelFunctionSet.add(chckbxIfGE, gbc_chckbxIfGE);

		chckbxIfLE = new JCheckBox("if<=");
		chckbxIfLE.setToolTipText(HTML_OPEN + "If LowerEqual(arg0, arg1, arg2, arg3)" +
				HTML_NEWLINE + "if(arg0 Lower or Equal to arg1)" +
				HTML_NEWLINE + "return arg2" +
				HTML_NEWLINE + "else" +
				HTML_NEWLINE + "return arg3");
		GridBagConstraints gbc_chckbxIfLE = new GridBagConstraints();
		gbc_chckbxIfLE.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxIfLE.gridx = 1;
		gbc_chckbxIfLE.gridy = 0;
		panelFunctionSet.add(chckbxIfLE, gbc_chckbxIfLE);

		chckbxPlus = new JCheckBox("Plus");
		chckbxPlus.setSelected(true);
		chckbxPlus.setToolTipText(HTML_OPEN + "Plus(arg0, arg1)"+
				HTML_NEWLINE + "return arg0 + arg1" + HTML_CLOSE);
		GridBagConstraints gbc_chckbxPlus = new GridBagConstraints();
		gbc_chckbxPlus.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxPlus.gridx = 2;
		gbc_chckbxPlus.gridy = 0;
		panelFunctionSet.add(chckbxPlus, gbc_chckbxPlus);

		chckbxMinus = new JCheckBox("Minus");
		chckbxMinus.setToolTipText(HTML_OPEN + "Minus(arg0, arg1)"+
				HTML_NEWLINE + "return arg0 - arg1" + HTML_CLOSE);
		GridBagConstraints gbc_chckbxMinus = new GridBagConstraints();
		gbc_chckbxMinus.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxMinus.gridx = 3;
		gbc_chckbxMinus.gridy = 0;
		panelFunctionSet.add(chckbxMinus, gbc_chckbxMinus);

		chckbxMulti = new JCheckBox("Multi");
		chckbxMulti.setToolTipText(HTML_OPEN + "Multi(arg0, arg1)"+
				HTML_NEWLINE + "return arg0 * arg1" + HTML_CLOSE);
		GridBagConstraints gbc_chckbxMulti = new GridBagConstraints();
		gbc_chckbxMulti.gridx = 4;
		gbc_chckbxMulti.gridy = 0;
		panelFunctionSet.add(chckbxMulti, gbc_chckbxMulti);

		lblTerminalSet = new JLabel("Terminal Set");
		lblTerminalSet.setToolTipText("The terminal set that will be used for the evolution process");
		GridBagConstraints gbc_lblTerminalSet = new GridBagConstraints();
		gbc_lblTerminalSet.anchor = GridBagConstraints.NORTH;
		gbc_lblTerminalSet.insets = new Insets(0, 0, 5, 5);
		gbc_lblTerminalSet.gridx = 0;
		gbc_lblTerminalSet.gridy = 11;
		frame.getContentPane().add(lblTerminalSet, gbc_lblTerminalSet);

		btnRunEvolution = new JButton("Run Evolution");
		btnRunEvolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initializeEvolutionValuesAndRun();
			}
		});

		panelTerminalSet = new JPanel();
		panelTerminalSet.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panelTerminalSet = new GridBagConstraints();
		gbc_panelTerminalSet.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelTerminalSet.anchor = GridBagConstraints.NORTH;
		gbc_panelTerminalSet.insets = new Insets(0, 0, 5, 0);
		gbc_panelTerminalSet.gridx = 1;
		gbc_panelTerminalSet.gridy = 11;
		frame.getContentPane().add(panelTerminalSet, gbc_panelTerminalSet);
		GridBagLayout gbl_panelTerminalSet = new GridBagLayout();
		gbl_panelTerminalSet.columnWidths = new int[] {0};
		gbl_panelTerminalSet.rowHeights = new int[] {0};
		gbl_panelTerminalSet.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		gbl_panelTerminalSet.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		panelTerminalSet.setLayout(gbl_panelTerminalSet);

		chckbxCountneightbors = new JCheckBox("CountNeightbors");
		chckbxCountneightbors.setToolTipText("counts the amount of neightbor pieces of type friend or enemy (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxCountneightbors = new GridBagConstraints();
		gbc_chckbxCountneightbors.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxCountneightbors.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCountneightbors.gridx = 0;
		gbc_chckbxCountneightbors.gridy = 0;
		panelTerminalSet.add(chckbxCountneightbors, gbc_chckbxCountneightbors);

		chckbxCornercount = new JCheckBox("CornerCount");
		chckbxCornercount.setToolTipText("counts the amount of pieces of type friend or enemy at the board corners (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxCornercount = new GridBagConstraints();
		gbc_chckbxCornercount.anchor = GridBagConstraints.WEST;
		gbc_chckbxCornercount.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCornercount.gridx = 1;
		gbc_chckbxCornercount.gridy = 0;
		panelTerminalSet.add(chckbxCornercount, gbc_chckbxCornercount);

		chckbxWinorblock = new JCheckBox("WinOrBlock");
		chckbxWinorblock.setSelected(true);
		chckbxWinorblock.setToolTipText(HTML_OPEN + "check for winning move if set to friend checking" +
				HTML_NEWLINE + "check for block enemy winning move if set to enemy checking" +
				HTML_NEWLINE + "returns Integer.MAX_VALUE if leads to win/block move" +
				HTML_NEWLINE + "else, return 0");
		GridBagConstraints gbc_chckbxWinorblock = new GridBagConstraints();
		gbc_chckbxWinorblock.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxWinorblock.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxWinorblock.gridx = 2;
		gbc_chckbxWinorblock.gridy = 0;
		panelTerminalSet.add(chckbxWinorblock, gbc_chckbxWinorblock);

		chckbxCountrow = new JCheckBox("CountRow");
		chckbxCountrow.setToolTipText("counts the amount of pieces of type friend or enemy in the same row of the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxCountrow = new GridBagConstraints();
		gbc_chckbxCountrow.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxCountrow.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCountrow.gridx = 0;
		gbc_chckbxCountrow.gridy = 1;
		panelTerminalSet.add(chckbxCountrow, gbc_chckbxCountrow);

		chckbxCountcolumn = new JCheckBox("CountColumn");
		chckbxCountcolumn.setToolTipText("counts the amount of pieces of type friend or enemy in the same column of the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxCountcolumn = new GridBagConstraints();
		gbc_chckbxCountcolumn.anchor = GridBagConstraints.WEST;
		gbc_chckbxCountcolumn.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCountcolumn.gridx = 1;
		gbc_chckbxCountcolumn.gridy = 1;
		panelTerminalSet.add(chckbxCountcolumn, gbc_chckbxCountcolumn);

		chckbxCountdiagmain = new JCheckBox("CountDiagMain");
		chckbxCountdiagmain.setToolTipText("counts the diagonal (top-leftto right-bottom) pieces amount of type friend or enemy starting from the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxCountdiagmain = new GridBagConstraints();
		gbc_chckbxCountdiagmain.anchor = GridBagConstraints.WEST;
		gbc_chckbxCountdiagmain.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCountdiagmain.gridx = 2;
		gbc_chckbxCountdiagmain.gridy = 1;
		panelTerminalSet.add(chckbxCountdiagmain, gbc_chckbxCountdiagmain);

		chckbxCountdiagsec = new JCheckBox("CountDiagSec");
		chckbxCountdiagsec.setToolTipText("counts the diagonal (top-right to left-bottom) pieces amount of type friend or enemy starting from the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxCountdiagsec = new GridBagConstraints();
		gbc_chckbxCountdiagsec.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxCountdiagsec.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxCountdiagsec.gridx = 3;
		gbc_chckbxCountdiagsec.gridy = 1;
		panelTerminalSet.add(chckbxCountdiagsec, gbc_chckbxCountdiagsec);

		chckbxRowstreak = new JCheckBox("RowStreak");
		chckbxRowstreak.setSelected(true);
		chckbxRowstreak.setToolTipText("counts the row streak length of pieces of type friend or enemy starting from the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxRowstreak = new GridBagConstraints();
		gbc_chckbxRowstreak.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxRowstreak.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRowstreak.gridx = 0;
		gbc_chckbxRowstreak.gridy = 2;
		panelTerminalSet.add(chckbxRowstreak, gbc_chckbxRowstreak);

		chckbxColumnstreak = new JCheckBox("ColumnStreak");
		chckbxColumnstreak.setSelected(true);
		chckbxColumnstreak.setToolTipText("counts the column streak length of pieces of type friend or enemy starting from the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxColumnstreak = new GridBagConstraints();
		gbc_chckbxColumnstreak.anchor = GridBagConstraints.WEST;
		gbc_chckbxColumnstreak.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxColumnstreak.gridx = 1;
		gbc_chckbxColumnstreak.gridy = 2;
		panelTerminalSet.add(chckbxColumnstreak, gbc_chckbxColumnstreak);

		chckbxDiagmainstreak = new JCheckBox("DiagMainStreak");
		chckbxDiagmainstreak.setSelected(true);
		chckbxDiagmainstreak.setToolTipText("counts the diagonal (top-left to right-bottom) streak length of pieces of type friend or enemy starting from the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxDiagmainstreak = new GridBagConstraints();
		gbc_chckbxDiagmainstreak.anchor = GridBagConstraints.WEST;
		gbc_chckbxDiagmainstreak.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDiagmainstreak.gridx = 2;
		gbc_chckbxDiagmainstreak.gridy = 2;
		panelTerminalSet.add(chckbxDiagmainstreak, gbc_chckbxDiagmainstreak);

		chckbxDiagsecstreak = new JCheckBox("DiagSecStreak");
		chckbxDiagsecstreak.setSelected(true);
		chckbxDiagsecstreak.setToolTipText("counts the diagonal (top-right to left-bottom) streak length of pieces of type friend or enemy starting from the current index (friend of enemy determined at terminal creation)");
		GridBagConstraints gbc_chckbxDiagsecstreak = new GridBagConstraints();
		gbc_chckbxDiagsecstreak.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxDiagsecstreak.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDiagsecstreak.gridx = 3;
		gbc_chckbxDiagsecstreak.gridy = 2;
		panelTerminalSet.add(chckbxDiagsecstreak, gbc_chckbxDiagsecstreak);

		chckbxRandval = new JCheckBox("RandVal");
		chckbxRandval.setToolTipText("returns a random value (0 to 10, generated once at terminal creation)");
		GridBagConstraints gbc_chckbxRandval = new GridBagConstraints();
		gbc_chckbxRandval.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxRandval.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRandval.gridx = 0;
		gbc_chckbxRandval.gridy = 3;
		panelTerminalSet.add(chckbxRandval, gbc_chckbxRandval);

		chckbxIsrandindex = new JCheckBox("IsRandIndex");
		chckbxIsrandindex.setToolTipText(HTML_OPEN + "returns 1 if the inspected index is the same as this value (generated at terminal creation)" +
				HTML_NEWLINE + "else, returns 0" + HTML_CLOSE);
		GridBagConstraints gbc_chckbxIsrandindex = new GridBagConstraints();
		gbc_chckbxIsrandindex.anchor = GridBagConstraints.WEST;
		gbc_chckbxIsrandindex.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxIsrandindex.gridx = 1;
		gbc_chckbxIsrandindex.gridy = 3;
		panelTerminalSet.add(chckbxIsrandindex, gbc_chckbxIsrandindex);

		btnPlayAgainst = new JButton("Play Against");
		btnPlayAgainst.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				playAgainst();
			}
		});
		GridBagConstraints gbc_btnPlayAgainst = new GridBagConstraints();
		gbc_btnPlayAgainst.fill = GridBagConstraints.BOTH;
		gbc_btnPlayAgainst.insets = new Insets(0, 0, 5, 5);
		gbc_btnPlayAgainst.gridx = 0;
		gbc_btnPlayAgainst.gridy = 12;
		frame.getContentPane().add(btnPlayAgainst, gbc_btnPlayAgainst);

		panelPlayAgainst = new JPanel();
		panelPlayAgainst.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panelPlayAgainst = new GridBagConstraints();
		gbc_panelPlayAgainst.insets = new Insets(0, 0, 5, 0);
		gbc_panelPlayAgainst.fill = GridBagConstraints.BOTH;
		gbc_panelPlayAgainst.gridx = 1;
		gbc_panelPlayAgainst.gridy = 12;
		frame.getContentPane().add(panelPlayAgainst, gbc_panelPlayAgainst);
		GridBagLayout gbl_panelPlayAgainst = new GridBagLayout();
		gbl_panelPlayAgainst.columnWidths = new int[]{171, 171, 171, 0};
		gbl_panelPlayAgainst.rowHeights = new int[]{33, 0};
		gbl_panelPlayAgainst.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelPlayAgainst.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelPlayAgainst.setLayout(gbl_panelPlayAgainst);

		comboBoxPlayerOne = new JComboBox<String>();
		comboBoxPlayerOne.setModel(new DefaultComboBoxModel<String>(new String[] {"Human Player", "Original Franky", "Franky", "Random Generated"}));
		GridBagConstraints gbc_comboBoxPlayerOne = new GridBagConstraints();
		gbc_comboBoxPlayerOne.fill = GridBagConstraints.BOTH;
		gbc_comboBoxPlayerOne.insets = new Insets(0, 0, 0, 5);
		gbc_comboBoxPlayerOne.gridx = 0;
		gbc_comboBoxPlayerOne.gridy = 0;
		panelPlayAgainst.add(comboBoxPlayerOne, gbc_comboBoxPlayerOne);

		lblVersus = new JLabel("Vs.");
		GridBagConstraints gbc_lblVersus = new GridBagConstraints();
		gbc_lblVersus.fill = GridBagConstraints.VERTICAL;
		gbc_lblVersus.insets = new Insets(0, 0, 0, 5);
		gbc_lblVersus.gridx = 1;
		gbc_lblVersus.gridy = 0;
		panelPlayAgainst.add(lblVersus, gbc_lblVersus);

		comboBoxPlayerTwo = new JComboBox<String>();
		comboBoxPlayerTwo.setModel(new DefaultComboBoxModel<String>(new String[] {"Human Player", "Original Franky", "Franky", "Random Generated"}));
		GridBagConstraints gbc_comboBoxPlayerTwo = new GridBagConstraints();
		gbc_comboBoxPlayerTwo.fill = GridBagConstraints.BOTH;
		gbc_comboBoxPlayerTwo.gridx = 2;
		gbc_comboBoxPlayerTwo.gridy = 0;
		panelPlayAgainst.add(comboBoxPlayerTwo, gbc_comboBoxPlayerTwo);
		GridBagConstraints gbc_btnRunEvolution = new GridBagConstraints();
		gbc_btnRunEvolution.fill = GridBagConstraints.BOTH;
		gbc_btnRunEvolution.insets = new Insets(0, 0, 0, 5);
		gbc_btnRunEvolution.gridx = 0;
		gbc_btnRunEvolution.gridy = 13;
		frame.getContentPane().add(btnRunEvolution, gbc_btnRunEvolution);

		panelProgressBar = new JPanel();
		GridBagConstraints gbc_panelProgressBar = new GridBagConstraints();
		gbc_panelProgressBar.fill = GridBagConstraints.BOTH;
		gbc_panelProgressBar.gridx = 1;
		gbc_panelProgressBar.gridy = 13;
		frame.getContentPane().add(panelProgressBar, gbc_panelProgressBar);
		panelProgressBar.setLayout(new GridLayout(0, 1, 0, 0));

		progressEvolution = new ProgressImage(panelProgressBar);
		progressEvolution.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelProgressBar.add(progressEvolution);

		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnMainMenu = new JMenu("Main Menu");
		menuBar.add(mnMainMenu);

		mntmHelp = new JMenuItem("Help");
		mntmHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(frame, "Help Window\nHover on labels for description");
			}
		});
		mnMainMenu.add(mntmHelp);

		mntmAbout = new JMenuItem("About");
		mntmAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(frame, "Genetic Tic Tac Toe v1.0\nWritten By:\nBar Galili\nEli Miles\nDani Shapira\nDaniel Guyshuber\nIlliya Yurkevich");
			}
		});
		mnMainMenu.add(mntmAbout);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		mnMainMenu.add(mntmExit);
	}

	protected void playAgainst() {

		initializeFunctionTerminalSets();
		if(!validInput){
			JOptionPane.showMessageDialog(frame, "Missing Input!\nEither Tournament Mode is Unselected Or Missing Function/Terminal");
			validInput = true;
			return;
		}

		Board board = new Board();
		Game game = new Game(board);

		selectRandomMaxIndex = (inputSelectRandomMaxIndex.getSelectedIndex() == 0)? true:false;
		Individual PlayerOne = new Individual(board, "Player One", selectRandomMaxIndex, functionList, terminalList);
		Individual PlayerTwo = new Individual(board, "Player Two", selectRandomMaxIndex, functionList, terminalList);
		PlayerOne.setValue(1);
		PlayerTwo.setValue(2);
		final GameGUI gameGUI = new GameGUI(game, PlayerTwo);
		// play human vs. human
		if(comboBoxPlayerOne.getSelectedIndex() == 0 && comboBoxPlayerTwo.getSelectedIndex() == 0){
			PlayerOne.setIsHumanPlayer(true);
			PlayerTwo.setIsHumanPlayer(true);
			gameGUI.setPlayer(PlayerOne, 1);
			gameGUI.setPlayer(PlayerTwo, 2);
			gameGUI.showGUI();
			return;
		}

		//		Function.setFunctionList(functionList);
		//		Terminal.setTerminalList(terminalList);

		if(comboBoxPlayerOne.getSelectedIndex() == 0){
			PlayerOne.setIsHumanPlayer(true);
		}
		if(comboBoxPlayerOne.getSelectedIndex() == 1){
			PlayerOne.frankenstein();
			PlayerOne.setIsHumanPlayer(false);
		}
		if(comboBoxPlayerOne.getSelectedIndex() == 2){
			PlayerOne.frankenstein2();
			PlayerOne.setIsHumanPlayer(false);
		}
		if(comboBoxPlayerOne.getSelectedIndex() == 3){
			PlayerOne.generateRandomStrategy(inputInitialDepth.getValue());
			PlayerOne.setIsHumanPlayer(false);
		}

		if(comboBoxPlayerTwo.getSelectedIndex() == 0){
			PlayerTwo.setIsHumanPlayer(true);
		}
		if(comboBoxPlayerTwo.getSelectedIndex() == 1){
			PlayerTwo.frankenstein();
			PlayerTwo.setIsHumanPlayer(false);
		}
		if(comboBoxPlayerTwo.getSelectedIndex() == 2){
			PlayerTwo.frankenstein2();
			PlayerTwo.setIsHumanPlayer(false);
		}
		if(comboBoxPlayerTwo.getSelectedIndex() == 3){
			PlayerTwo.generateRandomStrategy(inputInitialDepth.getValue());
			PlayerTwo.setIsHumanPlayer(false);
		}
		if(comboBoxPlayerTwo.getSelectedIndex() == 4){
			PlayerTwo.setStrategy(bestIndividual.getStrategy());
			PlayerTwo.setPlayerName(bestIndividual.getPlayerName());
			PlayerTwo.setIsHumanPlayer(false);
		}

		PlayerOne.setBoard(board);
		PlayerTwo.setBoard(board);
		gameGUI.setPlayer(PlayerOne, 1);
		gameGUI.setPlayer(PlayerTwo, 2);
		gameGUI.showGUI();
		if(comboBoxPlayerOne.getSelectedIndex() != 0 && comboBoxPlayerTwo.getSelectedIndex() != 0){
			Thread demoThread = new Thread(){
				public void run() {
					gameGUI.Demo(PlayerOne, PlayerTwo);
				}
			};
			demoThread.start();
		}
	}

	public void initializeEvolutionValuesAndRun(){
		initialDepth = inputInitialDepth.getValue();
		maxDepth = inputMaxDepth.getValue();
		// choose whether to select first maximal value index or a random (if there are multiples)
		selectRandomMaxIndex = (inputSelectRandomMaxIndex.getSelectedIndex() == 0)? true:false;
		popSize = inputPopSize.getValue();
		if(popSize == 0)
			popSize = 1;
		maxGenerations = inputMaxGenerations.getValue();
		// amount of best individuals to keep from the old generation (pop[0],pop[1],...,pop[keepBestIndividualsInGeneration-1])
		keepBestIndividualsInGeneration = inputKeepBestIndividualsInGeneration.getValue();
		// determines how often there will be a game against human player (0 for None)
		playEveryNGame = inputPlayEveryNGame.getValue();
		playTournament = inputPlayEachOther.isSelected();
		playWithFranky = inputFrankyTournament.isSelected();
		if(!playTournament && !playWithFranky)
			validInput = false;
		crossoverProb  = inputCrossoverProb.getValue()*0.1;
		mutationProb   = inputMutationProb.getValue()*0.1;
		progressEvolution.setPercentage(0);
		progressEvolution.setImageSize();

		initializeFunctionTerminalSets();

		if(!validInput){
			JOptionPane.showMessageDialog(frame, "Missing Input!\nEither Tournament Mode is Unselected Or Missing Function/Terminal");
			validInput = true;
			return;
		}

		// initialize the selection method and the new population
		Selection selection = new TournamentSelection(mutationProb, crossoverProb);
		Population population = new Population(popSize, selection, initialDepth ,maxDepth, selectRandomMaxIndex, keepBestIndividualsInGeneration, functionList, terminalList);
		// initialize the evolution and start the evolution engine
		Evolution evolution = new Evolution(population, maxGenerations, playEveryNGame, playTournament, playWithFranky, selectRandomMaxIndex, progressEvolution);

		Thread runEvolutionThread = new Thread(){
			public void run(){
				btnRunEvolution.setEnabled(false);
				bestIndividual = evolution.evolve();
				if(comboBoxPlayerTwo.getItemCount() == 4)
					comboBoxPlayerTwo.addItem("Best Individual From Last Evolution");
				btnRunEvolution.setEnabled(true);
			}
		};
		runEvolutionThread.start();
	}

	public void initializeFunctionTerminalSets(){
		List<String> functionList = new ArrayList<String>();
		List<String> terminalList = new ArrayList<String>();

		if(chckbxIfGE.isSelected())
			functionList.add("If >=");
		if(chckbxIfLE.isSelected())
			functionList.add("If <=");
		if(chckbxPlus.isSelected())
			functionList.add("Plus");
		if(chckbxMinus.isSelected())
			functionList.add("Minus");
		if(chckbxMulti.isSelected())
			functionList.add("Multi");

		if(chckbxCountneightbors.isSelected())
			terminalList.add("CountNeightbors");
		if(chckbxCornercount.isSelected())
			terminalList.add("CornerCount");
		if(chckbxCountrow.isSelected())
			terminalList.add("CountRow");
		if(chckbxCountcolumn.isSelected())
			terminalList.add("CountColumn");
		if(chckbxCountdiagmain.isSelected())
			terminalList.add("DiagMainStreak");
		if(chckbxCountdiagsec.isSelected())
			terminalList.add("CountDiagSec");
		if(chckbxRowstreak.isSelected())
			terminalList.add("RowStreak");
		if(chckbxColumnstreak.isSelected())
			terminalList.add("ColumnStreak");
		if(chckbxDiagmainstreak.isSelected())
			terminalList.add("DiagMainStreak");
		if(chckbxDiagsecstreak.isSelected())
			terminalList.add("DiagSecStreak");
		if(chckbxRandval.isSelected())
			terminalList.add("RandVal");
		if(chckbxIsrandindex.isSelected())
			terminalList.add("IsRandIndex");
		if(chckbxWinorblock.isSelected())
			terminalList.add("WinOrBlock");

		this.functionList = functionList.toArray(new String[0]);
		this.terminalList = terminalList.toArray(new String[0]);
		if(functionList.size() == 0 || terminalList.size() == 0){
			System.out.println("Function Set And Terminal Set Must Contain At Least One Item!");
			validInput = false;
		}
	}
}
