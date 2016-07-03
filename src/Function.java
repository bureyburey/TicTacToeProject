
public class Function extends GPNode{

	/*
	 * Function class
	 * represent a function
	 */
	
	private Individual individual;
	private String functionlIdentity = "UNINITIALIZED";
	//private static String[] functionList;
	// list of the allowed functions
	private static final String[] allowedFunctionList = {
		"If <=",
		"If >=",
		"Minus",
		"Plus",
	"Multi"};

	public Function(boolean isRoot, Board board, String functionIdentity, Individual individual) {
		super(isRoot, board);
		this.individual = individual;
		this.setFunction(functionIdentity);
		this.numChildren = getFunctionChildrenAmount(functionlIdentity);
	}

//	public Function(boolean isRoot, Board board) {
//		super(isRoot, board);
//		this.setRandFunction();
//		this.numChildren = getFunctionChildrenAmount(functionlIdentity);
//	}

	public Function(Function function){
		super(function.isRoot, function.board);		
		this.individual = function.individual;
		this.functionlIdentity = function.functionlIdentity;
		this.numChildren = getFunctionChildrenAmount(functionlIdentity);
		this.height = function.height;
	}

	public void setIdentity(String functionIdentity){
		boolean exists = false;
		for(String id: allowedFunctionList){
			if(id.equalsIgnoreCase(functionIdentity))
				exists = true;
		}
		if(!exists)
			return;
		this.functionlIdentity = functionIdentity;
		this.numChildren = getFunctionChildrenAmount(functionIdentity);
	}

	public String getIdentity(){
		return functionlIdentity;
	}

	public Function copy(){
		return new Function(this);
	}

	public void setRandFunction(){
		//if(functionlIdentity.equalsIgnoreCase("UNINITIALIZED"))
		//functionlIdentity = functionList[(int) (Math.random()*functionList.length)];
		functionlIdentity = individual.getRandomFunction();
		if(functionlIdentity == null){
			functionlIdentity = allowedFunctionList[(int) (Math.random()*allowedFunctionList.length)];
		}
	}

	public void setFunction(String functionIdentity){
		if(functionlIdentity.equalsIgnoreCase("UNINITIALIZED"))
			this.functionlIdentity = functionIdentity;
	}

//	public static String getRandomFunction(){
//		return functionList[(int) (Math.random()*functionList.length)];
//	}

	public int getFunctionChildrenAmount(String functionIdentity){
		if(functionIdentity.equals("If <="))
			return 4;
		if(functionIdentity.equals("If >="))
			return 4;
		if(functionIdentity.equals("Minus"))
			return 2;
		if(functionIdentity.equals("Plus"))
			return 2;
		if(functionIdentity.equals("Multi"))
			return 2;
		return 0;
	}

//	public static void setFunctionList(String[] functionList){
//		Function.functionList = functionList;
//	}

//	public static String[] getFunctionList(){
//		return functionList;
//	}

	@Override
	public String toString(){
		return functionlIdentity;
	}

}
