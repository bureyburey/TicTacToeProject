

public class GPTree {
	/* 
	 root is the starting node of the tree (root node)
	 board is the board instance of the class
	 terminalValue is increased each time a terminal node is added and it's value is sent to the Terminal
	 to be set, it is a circular value going from 0,1,2.....,[boardSize-1],0,1,2.....
	 */
	private GPNode root;
	private Board board;
	private Individual individual;

	public GPTree(Board board, Individual individual){
		this.board = board;
		root = null;
		this.individual = individual;
	}

	public GPTree(GPTree tree){
		this.board = tree.board;
		this.root = tree.getRoot().copy(tree.getRoot());
		this.root.setIsRoot(true);
		this.individual = tree.individual;
	}
	
	public boolean growRandomTree(int maxDepth){
		/*
		 * generate a random tree
		 */
		if(root != null)
			return false;
		root = GPNode.generateFullTree(maxDepth, board, individual);
//		root = GPNode.generateTree(maxDepth, board, individual);
		root.setIsRoot(true);
//		root.setIndividual(individual);
		return true;
	}

	public void generateFranky(){
		root = GPNode.generateFranky(board, individual);
		root.assignNodeHeights(root, root.getMaxDepth(root));
	}
	
	public void generateFranky2(){
		root = GPNode.generateFranky2(board, individual);
		root.assignNodeHeights(root, root.getMaxDepth(root));
	}
	
	public void createRoot(){
		/*
		 *  initialize the root node as a new Function
		 *  UNUSED AT THE MOMENT
		 */
		if(root == null)
			root = new Function(true, board, individual.getRandomFunction(), individual);
	}
	
	public Individual getIndividual(){
		return individual;
	}

	public GPNode getRoot(){
		/*
		 * returns the tree root
		 */
		return root;
	}

	public void setRoot(GPNode root){
		this.root = root;
	}

	public void setTreeBoard(Board board){
		this.root.setTreeBoard(board);
	}
	
	public GPTree copyTree(GPNode root){
		/*
		 return a copy of a GPTree
		 must provide initial root node
		 */
		// initialize a new tree object
		GPTree copy = new GPTree(board, individual);
		// copy the root node
		copy.root = root.copy(root);
		// set the root of the tree
		copy.setRoot(copy.getRoot());
		return copy;
	}

	public void print(){
		/*
		 *  call print method of the GPNode root object
		 */
		root.print();
	}

	public String TreeStrPseudo() {
		return root.TreeStrPseudo();
	}

}
