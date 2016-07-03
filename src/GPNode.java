import java.awt.Color;
import java.util.Random;

public class GPNode {

	protected GPNode[] children;
	protected int numChildren;

	protected Board board;
	protected int height;
	protected boolean isRoot;

	protected Color color;
	private static int counter = 0;
	private final Color defaultColor = new Color(100,100,255);

	public GPNode(boolean isRoot, Board board){
		children = null;
		numChildren = 0;
		this.board = board;
		this.isRoot = isRoot;
		color = defaultColor;
	}

	public GPNode copy(GPNode rootSource) {
		/*
		 * create a copy of node starting at rootSource
		 * works similarly to generate tree methods
		 * instead of initializing new nodes, call copy constructors of Terminal and Function
		 */
		GPNode root;  
		if(rootSource instanceof Terminal)
			root = new Terminal((Terminal)rootSource);
		else{ //if(rootSource instanceof Function)
			root = new Function((Function) rootSource);
			root.children = new GPNode[root.getNumChildren()];
		}
		for(int i = 0; i < root.getNumChildren(); i++)  {
			root.children[i] = rootSource.copy(rootSource.children[i]);  
		}
		return root;
	}  

	public static GPNode generateFullTree(int maxDepth, Board board, Individual individual) {
		/*
		 * generate a full grown tree
		 */
		GPNode root;
		//If we are not at the max depth, choose a function  
		if(maxDepth > 1)  {
			// create new function
			root = new Function(false, board, individual.getRandomFunction(), individual);
			// create the proper amount of children (according to the function that was just created)
			root.children = new GPNode[root.getNumChildren()];
		}
		//Otherwise, choose a terminal  
		else  {
			// create new terminal
			root = new Terminal(board, individual.getRandomTerminal(), individual);
		}
		root.height = maxDepth;
		//Recursively assign child nodes  
		for(int i = 0; i < root.getNumChildren(); i++)  {
			root.children[i] = GPNode.generateFullTree(maxDepth - 1, board, individual);  
		}
		// return the created root
		return root;  
	}

	public static GPNode generateTree(int maxDepth, Board board, Individual individual) {
		/*
		 * works very similarly to generateFullTree
		 * instead of ALWAYS creating a function if the maximum depth hasn't reached
		 * randomly select either terminal or a function, causing some branches to stop developing
		 */
		GPNode root;  
		//If we are not at the max depth, choose a function  
		if(maxDepth > 1)  {
			Random rand = new Random();
			if(rand.nextBoolean() == true){
				root = new Function(false, board, individual.getRandomFunction(), individual);
				root.children = new GPNode[root.getNumChildren()];
			}
			else
				root = new Terminal(board, individual.getRandomTerminal(), individual);
		}
		//Otherwise, choose a terminal  
		else  
			root = new Terminal(board, individual.getRandomTerminal(), individual);
		root.height = maxDepth;
		//Recursively assign child nodes  
		for(int i = 0; i < root.getNumChildren(); i++)  {
			root.children[i] = GPNode.generateTree(maxDepth - 1, board, individual);  
		}
		return root;  
	}  

	public void trim(int maxDepth, Individual individual) {
		/*
		 * traverse all nodes and check depths
		 * if reached maximum depth-1, check if the children of the current node (if there are any)
		 * are functions, and if so, replace them with random terminals, otherwise, do nothing
		 */
		for(int i = 0; i < this.getNumChildren(); i++) {
			if(maxDepth == 1){
				// one level below is the maximal depth,
				// if the child is a function,
				// swap with a terminal, else, do nothing
				if(children[i] instanceof Function)
					this.children[i] = new Terminal(board, individual.getRandomTerminal(), individual);
			}
			else if(this.children[i] != null){
				// if haven't reached the maximal depth, and the child is a function,
				// recursively call each child with the trim method
				this.children[i].trim(maxDepth - 1, individual);  
			}
		}
	}

	public void setTreeBoard(Board board){
		/*
		 * traverse the entire tree and set each node board
		 */
		// set the board of the current node
		this.setBoard(board);
		// if no children to the current node (it's a terminal) return
		if (numChildren == 0)
			return;
		// visit each node recursively
		for (int i=0;i<numChildren;i++){
			if(children[i] != null)
				children[i].setTreeBoard(board);
		}
		return;
	}
	
	public int countNodes(){
		/*
		 * count the tree nodes
		 * can also be used to reset each node's color
		 */
		// recursively count the nodes of the tree	
		this.color = defaultColor;
		// if no children to the current node (it's a terminal) return 1 as the count of the node itself
		if (numChildren == 0)
			return 1;
		// 1 is the node itself
		int size = 1;

		// visit each node and count the terminals + functions
		for (int i=0;i<numChildren;i++){
			if(children[i] != null)
				size += children[i].countNodes();
		}
		return size;
	}

	public long evalIndexGrade(GPNode root, int index) {
		/*
		 * evaluation function
		 * traverse the entire tree and returns the result of the evaluation
		 */
		long grade = 0;
		//		root.color = Color.red;
		// if the current root is a terminal, return it's evaluation
		if(root instanceof Terminal){
			grade = ((Terminal) root).Eval(index);
			return grade;
		}

		// if the root is 'If <=' function, check if arg0 is <= than arg1, if so, return arg2, else, return arg3
		if(((Function) root).getIdentity().equalsIgnoreCase("If <=")){
			if(evalIndexGrade(root.getChildAtIndex(0), index) <= evalIndexGrade(root.getChildAtIndex(1), index))
				grade = evalIndexGrade(root.getChildAtIndex(2), index);
			else
				grade = evalIndexGrade(root.getChildAtIndex(3), index);
		}
		if(((Function) root).getIdentity().equalsIgnoreCase("If >=")){
			if(evalIndexGrade(root.getChildAtIndex(0), index) >= evalIndexGrade(root.getChildAtIndex(1), index))
				grade = evalIndexGrade(root.getChildAtIndex(2), index);
			else
				grade = evalIndexGrade(root.getChildAtIndex(3), index);
		}
		if(((Function) root).getIdentity().equalsIgnoreCase("Minus")){
			grade = evalIndexGrade(root.getChildAtIndex(0), index) -
					evalIndexGrade(root.getChildAtIndex(1), index);
		}
		if(((Function) root).getIdentity().equalsIgnoreCase("Plus")){
			grade = evalIndexGrade(root.getChildAtIndex(0), index) +
					evalIndexGrade(root.getChildAtIndex(1), index);
		}
		if(((Function) root).getIdentity().equalsIgnoreCase("Multi")){
			grade = evalIndexGrade(root.getChildAtIndex(0), index) *
					evalIndexGrade(root.getChildAtIndex(1), index);
		}

		return grade;
	}

	//	public Individual getIndividual(){
	//		return individual;
	//	}
	//
	//	public void setIndividual(Individual individual){
	//		this.individual = individual;
	//	}

	public Board getBoard(){
		return board;
	}

	public void setBoard(Board board){
		this.board = board;
	}

	public int getNumChildren(){
		return this.numChildren;
	}

	public String toString(){
		if(this instanceof Function)
			return ((Function)this).toString();
		else if(this instanceof Terminal)
			return ((Terminal)this).toString();
		return null;
	}

	public boolean getIsRoot(){
		return isRoot;
	}

	public void swapNodes(GPNode original, GPNode swap){
		/*
		 * swap between arguments: original node and swap node
		 * traverse the tree until reaching the original node, then make the swap
		 */
		for(int i=0;i<this.numChildren;i++){
			if(children[i] == null)
				return;
			if(children[i] == original){
				children[i] = swap;
				//				System.out.println("Swapped " + original + " With " + swap);
				return;
			}
			if(children[i] != null)
				children[i].swapNodes(original, swap);
		}
		return;
	}

	public void setIsRoot(boolean isRoot){
		this.isRoot = isRoot;
	}

	public GPNode getChildAtIndex(int index){
		/*
		 * returns child of 
		 */
		if(index >= 0 && index < numChildren)
			return children[index];
		return null;
	}

	//	public int countNodes(){
	//		// works with two nodes for each node
	//		// recursively count the nodes of the tree
	//		this.color = Color.blue;
	//		return 1 + 
	//				((rightChild == null)? 0 : rightChild.countNodes()) + 
	//				((leftChild == null)? 0 : leftChild.countNodes());
	//	}

	public void setNodeColor(Color color){
		this.color = color;
	}

	public void paintNode(Color color){
		/*
		 * paints the tree with the color that was sent
		 */
		this.color = color;
		for(int i=0;i<numChildren;i++){
			if(children[i] != null)
				children[i].paintNode(color);
		}
	}

	public void resetCounter(){
		/*
		 * resets the static variable counter which is used to count traversed nodes
		 * when using the method getNodeN
		 */
		counter = 0;
	}

	public int getMaxDepth(GPNode root){
		/*
		 * return the max depth of the tree
		 * (the node or nodes with maximal depth)
		 */
		if(root instanceof Terminal){
			// return one as an extra depth of the node
			return 1;
		}
		// create heights array for the children nodes
		int height[] = new int[root.numChildren];
		for(int i=0;i<root.numChildren;i++){
			// recursively find each child depth
			height[i] = getMaxDepth(root.children[i]);
		}
		return maxDepth(height) + 1;
	}

	public int maxDepth(int[] heights){
		/*
		 * returns the maximum value in the array of heights
		 */
		int max=0;
		for(int i=0;i<heights.length;i++){
			if(heights[i] > max)
				max = heights[i];
		}
		return max;
	}

	public int getHeight(){
		return height;
	}
	
	public void assignNodeHeights(GPNode root, int currentHeight){
		if(root == null)
			return;
		root.height = currentHeight;
		for(int i=0;i<root.getNumChildren();i++){
			root.assignNodeHeights(root.children[i], currentHeight - 1);
		}
	}

	public GPNode getNode(GPNode root, int n) {
		// reset the counter and call getNodeN which returns the requested node (pre-order traversal)
		// root, left, right
		counter = 0;
		return getNodeN(root, n);
	}

	public GPNode getNodeN(GPNode root, int n){
		/* 
			 returns the Nth node of the tree
			 received parameters are the root node of the tree and a number n
			 the method works by counting a static variable and when the counter reaches the number n,
			 the method will return the current node
			 n value is ranged from 1....nodeCount
			 the traversal method is pre-order traversal
		 */
		counter++;
		GPNode returnRoot = null;
		// if the counter reached n, return the current root
		if(counter == n)
			return root;

		for(int i=0;i<root.getNumChildren();i++){
			if(root.getChildAtIndex(i) != null){
				returnRoot = root.getChildAtIndex(i).getNodeN(root.getChildAtIndex(i), n);
				if(returnRoot != null)
					return returnRoot;
			}
		}
		return null;
	}

	public static GPNode generateFranky(Board board, Individual individual){
		GPNode root = new Function(true, board, "If >=", individual);
		((Function)root).setIdentity("If >=");
		root.children = new GPNode[root.getNumChildren()];

		// left child, check if winning move
		root.children[0] = new Terminal(board, "WinOrBlock", individual);
		//((Terminal)root.children[0]).setIdentity("WinOrBlock");
		((Terminal)root.children[0]).setFriendOrEnemy(1);

		// if winning move, return this value
		root.children[2] = new Terminal(board, "WinOrBlock", individual);
		((Terminal)root.children[2]).setIdentity("WinOrBlock");
		((Terminal)root.children[2]).setFriendOrEnemy(1);

		GPNode ifBlockOrBestStreak = new Function(true, board, "If >=", individual);
		((Function)ifBlockOrBestStreak).setIdentity("If >=");
		ifBlockOrBestStreak.children = new GPNode[root.getNumChildren()];

		////////////////////////////////////////////////
		ifBlockOrBestStreak.children[2] = new Terminal(board, "WinOrBlock", individual);
		((Terminal)ifBlockOrBestStreak.children[2]).setIdentity("WinOrBlock");
		((Terminal)ifBlockOrBestStreak.children[2]).setFriendOrEnemy(2);

		ifBlockOrBestStreak.children[0] = new Function(false, board, "Plus", individual);
		((Function)ifBlockOrBestStreak.children[0]).setIdentity("Plus");

		ifBlockOrBestStreak.children[0].children = new GPNode[ifBlockOrBestStreak.children[0].getNumChildren()];


		ifBlockOrBestStreak.children[0].children[0] = new Terminal(board, "WinOrBlock", individual);
		((Terminal)ifBlockOrBestStreak.children[0].children[0]).setIdentity("WinOrBlock");
		((Terminal)ifBlockOrBestStreak.children[0].children[0]).setFriendOrEnemy(2);


		GPNode ifRowStreakOrElse = new Function(false, board, "If >=", individual);
		((Function)ifRowStreakOrElse).setIdentity("If >=");
		ifRowStreakOrElse.children = new GPNode[root.getNumChildren()];

		ifRowStreakOrElse.children[0] = new Terminal(board, "RowStreak", individual);
		((Terminal)ifRowStreakOrElse.children[0]).setIdentity("RowStreak");
		((Terminal)ifRowStreakOrElse.children[0]).setFriendOrEnemy(2);

		ifRowStreakOrElse.children[2] = new Terminal(board, "RowStreak", individual);
		((Terminal)ifRowStreakOrElse.children[2]).setIdentity("RowStreak");
		((Terminal)ifRowStreakOrElse.children[2]).setFriendOrEnemy(2);


		GPNode ifColStreakOrElse = new Function(false, board, "If >=", individual);
		((Function)ifColStreakOrElse).setIdentity("If >=");
		ifColStreakOrElse.children = new GPNode[root.getNumChildren()];

		ifColStreakOrElse.children[0] = new Terminal(board, "ColumnStreak", individual);
		((Terminal)ifColStreakOrElse.children[0]).setIdentity("ColumnStreak");
		((Terminal)ifColStreakOrElse.children[0]).setFriendOrEnemy(2);

		ifColStreakOrElse.children[2] = new Terminal(board, "ColumnStreak", individual);
		((Terminal)ifColStreakOrElse.children[2]).setIdentity("ColumnStreak");
		((Terminal)ifColStreakOrElse.children[2]).setFriendOrEnemy(2);


		GPNode ifMainDiagStreakOrElse = new Function(false, board, "If >=", individual);
		((Function)ifMainDiagStreakOrElse).setIdentity("If >=");
		ifMainDiagStreakOrElse.children = new GPNode[root.getNumChildren()];

		ifMainDiagStreakOrElse.children[0] = new Terminal(board, "DiagMainStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[0]).setIdentity("DiagMainStreak");
		((Terminal)ifMainDiagStreakOrElse.children[0]).setFriendOrEnemy(2);

		ifMainDiagStreakOrElse.children[2] = new Terminal(board, "DiagMainStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[2]).setIdentity("DiagMainStreak");
		((Terminal)ifMainDiagStreakOrElse.children[2]).setFriendOrEnemy(2);


		ifMainDiagStreakOrElse.children[1] = new Terminal(board, "DiagSecStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[1]).setIdentity("DiagSecStreak");
		((Terminal)ifMainDiagStreakOrElse.children[1]).setFriendOrEnemy(2);

		ifMainDiagStreakOrElse.children[3] = new Terminal(board, "DiagSecStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[3]).setIdentity("DiagSecStreak");
		((Terminal)ifMainDiagStreakOrElse.children[3]).setFriendOrEnemy(2);


		ifColStreakOrElse.children[1] = ifMainDiagStreakOrElse;
		ifColStreakOrElse.children[3] = ifMainDiagStreakOrElse;

		ifRowStreakOrElse.children[1] = ifColStreakOrElse;
		ifRowStreakOrElse.children[3] = ifColStreakOrElse;

		ifBlockOrBestStreak.children[1] = ifRowStreakOrElse;
		ifBlockOrBestStreak.children[3] = ifRowStreakOrElse;

		ifBlockOrBestStreak.children[0].children[1] = ifRowStreakOrElse;
		ifBlockOrBestStreak.children[2] = ifBlockOrBestStreak.children[0];

		root.children[1] = ifBlockOrBestStreak;
		root.children[3] = ifBlockOrBestStreak;

		return root;
	}

	public static GPNode generateFranky2(Board board, Individual individual){
		GPNode root = new Function(true, board, "If >=", individual);
		((Function)root).setIdentity("If >=");
		root.children = new GPNode[root.getNumChildren()];

		// left child, check if winning move
		root.children[0] = generateFranky(board, individual);
		//root.children[0] = new Terminal(board, individual);
		//((Terminal)root.children[0]).setIdentity("WinOrBlock");
		//((Terminal)root.children[0]).setFriendOrEnemy(2);

		// if winning move, return this value
		root.children[2] = generateFranky(board, individual);
		//root.children[2] = new Terminal(board, individual);
		//((Terminal)root.children[2]).setIdentity("WinOrBlock");
		//((Terminal)root.children[2]).setFriendOrEnemy(2);

		GPNode ifBlockOrBestStreak = new Function(true, board, "If >=", individual);
		((Function)ifBlockOrBestStreak).setIdentity("If >=");
		ifBlockOrBestStreak.children = new GPNode[root.getNumChildren()];

		////////////////////////////////////////////////
		ifBlockOrBestStreak.children[2] = new Terminal(board, "WinOrBlock", individual);
		((Terminal)ifBlockOrBestStreak.children[2]).setIdentity("WinOrBlock");
		((Terminal)ifBlockOrBestStreak.children[2]).setFriendOrEnemy(2);

		ifBlockOrBestStreak.children[0] = new Function(false, board, "Plus", individual);
		((Function)ifBlockOrBestStreak.children[0]).setIdentity("Plus");

		ifBlockOrBestStreak.children[0].children = new GPNode[ifBlockOrBestStreak.children[0].getNumChildren()];


		ifBlockOrBestStreak.children[0].children[0] = new Terminal(board, "WinOrBlock", individual);
		((Terminal)ifBlockOrBestStreak.children[0].children[0]).setIdentity("WinOrBlock");
		((Terminal)ifBlockOrBestStreak.children[0].children[0]).setFriendOrEnemy(2);


		GPNode ifRowStreakOrElse = new Function(false, board, "If >=", individual);
		((Function)ifRowStreakOrElse).setIdentity("If >=");
		ifRowStreakOrElse.children = new GPNode[root.getNumChildren()];

		ifRowStreakOrElse.children[0] = new Terminal(board, "RowStreak", individual);
		((Terminal)ifRowStreakOrElse.children[0]).setIdentity("RowStreak");
		((Terminal)ifRowStreakOrElse.children[0]).setFriendOrEnemy(1);

		ifRowStreakOrElse.children[2] = new Terminal(board, "RowStreak", individual);
		((Terminal)ifRowStreakOrElse.children[2]).setIdentity("RowStreak");
		((Terminal)ifRowStreakOrElse.children[2]).setFriendOrEnemy(1);


		GPNode ifColStreakOrElse = new Function(false, board, "If >=", individual);
		((Function)ifColStreakOrElse).setIdentity("If >=");
		ifColStreakOrElse.children = new GPNode[root.getNumChildren()];

		ifColStreakOrElse.children[0] = new Terminal(board, "ColumnStreak", individual);
		((Terminal)ifColStreakOrElse.children[0]).setIdentity("ColumnStreak");
		((Terminal)ifColStreakOrElse.children[0]).setFriendOrEnemy(1);

		ifColStreakOrElse.children[2] = new Terminal(board, "ColumnStreak", individual);
		((Terminal)ifColStreakOrElse.children[2]).setIdentity("ColumnStreak");
		((Terminal)ifColStreakOrElse.children[2]).setFriendOrEnemy(1);


		GPNode ifMainDiagStreakOrElse = new Function(false, board, "If >=", individual);
		((Function)ifMainDiagStreakOrElse).setIdentity("If >=");
		ifMainDiagStreakOrElse.children = new GPNode[root.getNumChildren()];

		ifMainDiagStreakOrElse.children[0] = new Terminal(board, "DiagMainStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[0]).setIdentity("DiagMainStreak");
		((Terminal)ifMainDiagStreakOrElse.children[0]).setFriendOrEnemy(1);

		ifMainDiagStreakOrElse.children[2] = new Terminal(board, "DiagMainStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[2]).setIdentity("DiagMainStreak");
		((Terminal)ifMainDiagStreakOrElse.children[2]).setFriendOrEnemy(1);


		ifMainDiagStreakOrElse.children[1] = new Terminal(board, "DiagSecStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[1]).setIdentity("DiagSecStreak");
		((Terminal)ifMainDiagStreakOrElse.children[1]).setFriendOrEnemy(1);

		ifMainDiagStreakOrElse.children[3] = new Terminal(board, "DiagSecStreak", individual);
		((Terminal)ifMainDiagStreakOrElse.children[3]).setIdentity("DiagSecStreak");
		((Terminal)ifMainDiagStreakOrElse.children[3]).setFriendOrEnemy(1);


		ifColStreakOrElse.children[1] = ifMainDiagStreakOrElse;
		ifColStreakOrElse.children[3] = ifMainDiagStreakOrElse;

		ifRowStreakOrElse.children[1] = ifColStreakOrElse;
		ifRowStreakOrElse.children[3] = ifColStreakOrElse;

		ifBlockOrBestStreak.children[1] = ifRowStreakOrElse;
		ifBlockOrBestStreak.children[3] = ifRowStreakOrElse;

		ifBlockOrBestStreak.children[0].children[1] = ifRowStreakOrElse;
		ifBlockOrBestStreak.children[2] = ifBlockOrBestStreak.children[0];

		root.children[1] = ifBlockOrBestStreak;
		root.children[3] = ifBlockOrBestStreak;

		return root;
	}

	public void print(){
		if(this  instanceof Function){
			System.out.println("Function: " + ((Function)this).toString());
		}
		else if(this instanceof Terminal){
			System.out.println("Terminal: " + ((Terminal)this).toString());
		}
		for(int i=0;i<numChildren;i++){
			if(children[i] != null)
				children[i].print();
		}
	}

	public String TreeStrPseudo(){
		/*
		 * returns a pseudo-code like representation of the tree
		 * reminder: the program which evolves is an evaluator for a location on the game board
		 */
		String line = "(";
		if(this instanceof Function){
			if(this.numChildren == 4){
				if(this.getIsRoot())
					line+="[";
				line += "if:";
				if(this.getIsRoot())
					line+="]\n";
				line += children[0].TreeStrPseudo();
				line += ((((Function)this).getIdentity() == "If >=")? ">=":"<=");
				line += children[1].TreeStrPseudo();
				line += "\nthen:\n" + children[2].TreeStrPseudo();
				line += "\nelse:\n" + children[3].TreeStrPseudo();
			}
			else{
				line += children[0].TreeStrPseudo();
				if(this.getIsRoot())
					line+="\n[";
				line += ((Function)this);
				if(this.getIsRoot())
					line+="]\n";
				line += children[1].TreeStrPseudo();
			}
		}
		else if(this instanceof Terminal){
			line += "T: " + ((Terminal)this);
		}
		return line + ")";
	}

	public String TreeStrFlat(){
		String line = "(";
		if(this.getIsRoot())
			line+="[";
		if(this instanceof Function)
			line += "F: " + ((Function)this);
		else if(this instanceof Terminal)
			line += "T: " + ((Terminal)this);
		if(this.getIsRoot())
			line+="]";
		
		for(int i=0;i<numChildren;i++){
			if(children[i] != null)
				line += children[i].TreeStrFlat();
		}

		return line + ")";
	}
}
