import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Game game;
	private JPanel buttonsPanel;
	private JButton[] buttons;
	private final int WIDTH = 600;
	private final int HEIGHT = 600;
	private JMenuBar menuBar;
	private JMenu JMenuGame;
	private JMenuItem JMenuItemNewGame, JMenuItemExit;
	private Individual playerOne;
	private Individual playerTwo;
	private final int CONTINUE = 0;
	private final int ANOTHER_GAME = 1;
	private final int QUIT = 2;
	private final int DRAW = -1;
	private int playerTurn = 1;
	//private TreeDraw treeDraw;

	public GameGUI(Game game, Individual opponent){
		super("GUI Game");
		this.game = game;
		this.playerTwo = opponent;
		playerOne = new Individual(game.getBoard(), "Player One", false, null, null);
		playerOne.setValue(1);
		playerOne.setIsHumanPlayer(true);
		//this.treeDraw = treeDraw;
		this.playerTwo.setBoard(game.getBoard());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenuGame = new JMenu("Game");

		JMenuItemNewGame = new JMenuItem("New Game");
		JMenuItemExit = new JMenuItem("Exit");
		JMenuGame.add(JMenuItemNewGame);
		JMenuGame.add(JMenuItemExit);

		menuBar.add(JMenuGame);

		this.setBounds(500, 100, WIDTH, HEIGHT);

		initializeButtons();
		this.getContentPane().add(buttonsPanel, BorderLayout.CENTER);
		setListeners();
	}

	public void showGUI(){
		this.setVisible(true);
	}

	public void Demo(Individual ind1, Individual ind2){
		resetGame();
		JOptionPane.showMessageDialog(this,"Match Between:\n" + 
				ind1.getPlayerName() + " ('X') Vs. " + ind2.getPlayerName() + " ('O')" + 
				"\nPress 'OK' to begin");
		// disable all buttons
		for(int i=0;i<game.getBoard().getBoardSize();i++)
			buttons[i].setEnabled(false);
		int action = CONTINUE; // 0 - continue, 1 - another game, 2 - quit
		// set individuals sides
		ind1.setValue(1);
		ind2.setValue(2);
		this.setTitle(ind1.getPlayerName() + " ('X') Vs. " + ind2.getPlayerName() + " ('O')");
		while(true){
			// make each player move until there is a winner
			ind1.makeStrategyMove();
			markOpponentMove();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			action = checkWinDemo(ind1);
			if(action == ANOTHER_GAME){
				// recursively call another game while switching player's sides
				Demo(ind2,ind1);
				break;
			}
			else if(action == QUIT){
				break;
			}
			ind2.makeStrategyMove();
			markOpponentMove();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			action = checkWinDemo(ind2);
			if(action == ANOTHER_GAME){
				// recursively call another game while switching player's sides
				Demo(ind2,ind1);
				break;
			}
			else if(action == QUIT){
				break;
			}
		}
		this.dispose();
	}

	public int checkWinDemo(Player player){
		int result = 0;
		result = game.getBoard().checkWin(player);

		if(result == player.getValue()){
			//			UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
			int confirm = JOptionPane.showConfirmDialog(this, player.getPlayerName() + " Wins!\nPlay Another Game?","Game Over", JOptionPane.YES_NO_OPTION);

			while(confirm != JOptionPane.YES_OPTION && confirm != JOptionPane.NO_OPTION)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			if(confirm == JOptionPane.YES_OPTION)
				return ANOTHER_GAME;
			else if(confirm == JOptionPane.NO_OPTION)
				return QUIT;
			return QUIT;
		}
		else if(result == DRAW){
			int confirm = JOptionPane.showConfirmDialog(this, "Draw!\nPlay Another Game?","Game Over", JOptionPane.YES_NO_OPTION);

			while(confirm != JOptionPane.YES_OPTION && confirm != JOptionPane.NO_OPTION)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			if(confirm == JOptionPane.YES_OPTION)
				return ANOTHER_GAME;
			else if(confirm == JOptionPane.NO_OPTION)
				return QUIT;
			return QUIT;
		}
		return CONTINUE;
	}


	public void initializeButtons(){
		int buttonsAmountRowCol = (int) Math.sqrt(game.getBoard().getBoardSize());
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(buttonsAmountRowCol,buttonsAmountRowCol,0,0));
		buttons = new JButton[buttonsAmountRowCol*buttonsAmountRowCol];
		for(int i=0;i<buttonsAmountRowCol*buttonsAmountRowCol;i++){
			buttons[i] = new JButton("");
			buttons[i].setName(String.valueOf(i));
			buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
			buttons[i].addActionListener(this);
			buttons[i].setToolTipText("<html>" + "<font size='3'>" + "Coordinate = " + "[" + i / game.getBoard().getBoardSizeRow() + "]" + "[" + i % game.getBoard().getBoardSizeRow() + "]" + "<br>" + "Index = " + Integer.toString(i) + "</font>" + "<html>");
			buttonsPanel.add(buttons[i]);
		}
		buttonsPanel.setVisible(true);
	}

	public void setListeners(){
		JMenuItemNewGame.addActionListener(this);
		JMenuItemExit.addActionListener(this);
	}

	public void resetGame(){
		int buttonsAmountRowCol = (int) Math.sqrt(game.getBoard().getBoardSize());
		for(int i=0;i<buttonsAmountRowCol*buttonsAmountRowCol;i++){
			buttons[i].setText("");
			buttons[i].setBackground(null);
		}
		game.resetBoard();
	}

	public boolean setIndex(int index, int value){
		String side = "";
		if(buttons[index].getText() != "" || game.getBoard().getIndexValue(index) != 0)
			return false;
		if(value == 1){
			side = "X";
		}
		else{
			side = "O";
		}
		buttons[index].setText(side);
		game.getBoard().setIndex(index, value);
		return true;		
	}

	public void markOpponentMove(){
		int buttonsAmountRowCol = (int) Math.sqrt(game.getBoard().getBoardSize());
		for(int i=0;i<buttonsAmountRowCol*buttonsAmountRowCol;i++){
			if(game.getBoard().getIndexValue(i) == 1){
				buttons[i].setText("X");
				buttons[i].setBackground(Color.cyan);
			}
			else if(game.getBoard().getIndexValue(i) == 2){
				buttons[i].setText("O");
				buttons[i].setBackground(Color.red);
			}
		}
	}

	public void setPlayer(Individual Player, int playerNum){
		if(playerNum == 1)
			this.playerOne = Player;
		else
			this.playerTwo = Player;
	}

	public boolean checkWin(Player player){
		int result = 0;
		result = game.getBoard().checkWin(player);

		if(result == player.getValue()){
			//			UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
			int confirm = JOptionPane.showConfirmDialog(this, player.getPlayerName() + " Wins!\nPlay Another Game?","Game Over", JOptionPane.YES_NO_OPTION);

			if(confirm == JOptionPane.YES_OPTION)
				return true;
			else if(confirm == JOptionPane.NO_OPTION)
				this.dispose();
			return true;
		}
		else if(result == DRAW){
			int confirm = JOptionPane.showConfirmDialog(this, "Draw!\nPlay Another Game?","Game Over", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.YES_OPTION)
				return true;
			else
				this.dispose();
			return true;
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(playerOne.isHumanPlayer && playerTwo.isHumanPlayer && (e.getSource() instanceof JButton)){
			if((e.getSource() instanceof JButton)){
				int index = Integer.parseInt(((JButton)e.getSource()).getName());
				if(playerTurn == 1){
					if(setIndex(index, 1)){
						if(checkWin(playerOne))
							resetGame();
						playerTurn = 2;
					}
				}
				else{
					if(setIndex(index, 2)){
						if(checkWin(playerTwo))
							resetGame();
						playerTurn = 1;
					}
				}
				markOpponentMove();
			}
			return;
		}
		if((e.getSource() instanceof JButton)){
			if(playerTwo.getIsHumanPlayer()){
				Individual temp = playerOne;
				playerOne = playerTwo;
				playerTwo = temp;
				playerOne.setValue(1);
				playerTwo.setValue(2);
			}
			int index = Integer.parseInt(((JButton)e.getSource()).getName());
			if(setIndex(index, 1)){
				if(checkWin(playerOne))
					resetGame();
				if(!playerTwo.makeStrategyMove()){
					System.out.println("Failed To Make Move!!!");
					this.dispose();
				}
				else{
					markOpponentMove();
					if(checkWin(playerTwo)){
						resetGame();
						markOpponentMove();
					}
				}
			}
			//			System.out.println("Will Win: " + game.getBoard().willWinBlock(index, 1));
			//			System.out.println("Will Block: " + game.getBoard().willWinBlock(index, 2));
			//			opponent.getStrategyRoot().countNodes();
			//			game.printBoard();
			//			System.out.println("Neighbors:" + game.getBoard().countNeighbors(index, 1));
			//			System.out.println("in Row:" + game.getBoard().countRow(index, 1));
			//			System.out.println("in Row Streak:" + game.getBoard().countRowStreak(index, 1));
			//			System.out.println("in Column:" + game.getBoard().countColumn(index, 1));
			//			System.out.println("in Column Streak:" + game.getBoard().countColumnStreak(index, 1));
			//			System.out.println("in main Diag:" + game.getBoard().countDiagMain(index, 1));
			//			System.out.println("in main Diag Streak:" + game.getBoard().countDiagMainStreak(index, 1));
			//			System.out.println("in sec Diag:" + game.getBoard().countDiagSec(index, 1));
			//			System.out.println("in sec Diag Streak:" + game.getBoard().countDiagSecStreak(index, 1));
			return;
		}
		//System.out.println(e.getSource());
		//System.out.println(((JButton)e.getSource()).getName());
		//((JButton)e.getSource()).setText("X");
		switch(e.getActionCommand())
		{
		case "New Game":
			resetGame();
			break;
		case "Exit":
			System.exit(0);
		default:
			break;
		}
	}
}

