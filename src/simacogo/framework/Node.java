package simacogo.framework;

import java.util.LinkedList;
import java.util.List;

/*
 * Represents a move in the Simacogo game. Consists of a 2D 9x9 board, an action,
 * and a point cost for Min and Max. Keeps links to parent so that a tree can be 
 * returned with the best possible score.
 */
public class Node {
	
	//could place this int in an enum
	private static final int BOARD_SIZE = 9;
	private Node parent;
	private char[][] state;
	private Action action;
	private int	xScore;
	private int oScore;
	private int minMaxVal;
	
	/*
	 * Class constructor which is generated from a parent node.
	 */
	public Node(Node parent, int xScore, int oScore, Action action, 
			char[][]state, int pointsToAdd) {
		this.parent = parent;
		this.action = action;
		this.xScore = xScore;
		this.oScore = oScore;
		this.state = state;
		if(this.action.playerIsX)
			xScore += pointsToAdd;
		else
			oScore += pointsToAdd; 
	}
	
	/*
	 * Constructor for initial game node. All scores are set to 0.
	 * 
	 */
	public Node(){
		this.state = new char[BOARD_SIZE][BOARD_SIZE];
		int x;
		int y;
		
		//create the board
		for(x = 0; x < BOARD_SIZE; x++){
			for(y = 0; y < BOARD_SIZE; y++){
				state[x][y] = '\u00B7';
			}
		}
		this.xScore = 0;
		this.oScore = 0;
		this.parent = null;
		this.action = new Action(0,0,'X');
	}
	
	/*
	 * Constructor for in-play game node. Takes in current scores and
	 * updates according to move.
	 */
	public Node(int xScore, int oScore, char[][] state, Action action){
		this.xScore = xScore;
		this.oScore = oScore;
		this.state = state;
		this.action = action;
	}
	

	/*
	 * Print the column numbers, then the board configuration.
	 * The "\u00B7" marker is an interpunct that represents unmarked board slots.
	 */
	public void printBoard(){
		int x, y;
		for(x = 0; x < BOARD_SIZE; x++)
			System.out.print( (x + 1) + " ");

		for(x = 0; x < BOARD_SIZE; x++){
				System.out.println();
			for(y = 0; y < BOARD_SIZE; y++){
				System.out.print(state[x][y] + " ");
			}
		}
		System.out.println('\n');
	}
	
	//Getters and setters for state of board.
	public char[][] getState(){
		return state;
	}
	
	public void setState(char[][] board) throws Exception{
		if(board[0].length != BOARD_SIZE)
			throw new Exception();
		else 
			this.state = board;
	}
	
	/*
	 * Getters and setters for scores.
	 */
	public int getXScore() {	
		return this.xScore;
	}

	public int getOScore() {
		return this.oScore;
	}
	
	/*
	 * Getters and setters for Action
	 */
	public Action getAction(){
		return this.action;
	}
	
	public void setAction(Action action){
		this.action = action;
	}
	
	/*
	 * Figure out which successor moves are possible given the configuration. 
	 * Create actions and return a list to be added to the stack for Minimax.
	 */
	public List<Node> getChildren() {
		char next;
		
		//Find the 9(or less) possible board moves.

List<Node> children = new LinkedList<Node>();
		int x, y;
		outerloop:
		for (y = 0; y < BOARD_SIZE; y++){
			x = 0;
			while (x < BOARD_SIZE ) {
				//there is no room in the column
				if (x == 0 && state[x][y] != '\u00B7'){
					continue outerloop;
				}
				//the current tile is unoccupied
				if (( state[x][y] == '\u00B7') && (x < BOARD_SIZE - 1))
					x++;
				else {
					if(this.action.playerIsX) 
						 next = 'O';
					else next = 'X';
					int pointsToAdd = movePoints(x, y, next);
					Action nextMove = new Action(x, y, next);
					char[][] newState = copyStateAndAddMove(nextMove);
					children.add(new Node(this, this.xScore, this.oScore, nextMove, newState, pointsToAdd));
					break;
				}	
			}
		}
		return children;
	}
	
	//TODO: make public?
	//Copies the state of the tiles for new Node creation
	public char[][] copyStateAndAddMove(Action move){
		int x = move.getxCoord();
		int y = move.getyCoord();
		char [][] newState = new char[state.length][];
		for(int i = 0; i < state.length; i++)
		    newState[i] = state[i].clone();	
		newState[x][y] = move.playerIsX? 'X' : 'O';
		return newState;
	}
	
	/*
	 * Calculates the score of a new node's added position using the proposed
	 * X and Y coordinates. Examines the tiles surrounding the coordinates.
	 * TODO: Move this outside?
	 */
	public int movePoints(int x, int y, char marker){
		int score = 0;
		
		//2 pts calculation, looks Up, Down, Left, and Right
		if (x > 0) 
			if (state[x - 1][y] == marker) score += 2;
		if (x < BOARD_SIZE - 1)
			if (state[x + 1][y] == marker) score += 2;
		if (y > 0)
			if (state[x][y - 1] == marker) score += 2;
		if (y < BOARD_SIZE - 1)
			if (state[x][y + 1] == marker) score += 2;
		
		//1 pts calculation, looks NW, NE, SE, SW
		if (x > 0 && y > 0) 
			if (state[x - 1][y - 1] == marker) score += 1;
		if (x > 0 && y < BOARD_SIZE - 1)
			if (state[x - 1][y + 1] == marker) score += 1;
		if (x < BOARD_SIZE - 1 && y < BOARD_SIZE - 1)
			if (state[x + 1][y + 1] == marker) score += 1;
		if (x > BOARD_SIZE - 1 && y > 0)
			if (state[x + 1][y - 1] == marker) score += 1;
		
		return score;
	}
	
	public void raiseXScore(int movePoints) {
		this.xScore += movePoints;	
	}
	
	public void raiseOScore(int movePoints) {
		this.oScore += movePoints;		
	}

	
	/*
	 * Strictly for unit testing project.
	 */
	public static void main(String[] args) {
		Node node = new Node();
		node.printBoard();
		List<Node> children = node.getChildren();
		for(Node child : children)
			child.printBoard();
		
		Node firstChild = children.get(0);
		
		children = firstChild.getChildren();
		for(Node child : children)
			child.printBoard();
	}

	public int getMinMaxVal() {
		return minMaxVal;
	}

	public void setMinMaxVal(int minMaxVal) {
		this.minMaxVal = minMaxVal;
	}


	



}
