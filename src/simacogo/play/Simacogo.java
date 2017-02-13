package simacogo.play;

import simacogo.framework.*;

import java.util.Scanner;

/*
 * Main game play class. Takes an int from the user indicating the ply desired, 
 * and then employs Minimax from the computer to calculate the most optimal move.
 * Stops when the game comes to a terminal end.
 */
public class Simacogo {
	
	private int ply;	
	private final int BOARD_SIZE = 9;

	public Simacogo(){}
		
	/*
	 * Primary working method.
	 */
	public void play() {
			
		//Initialize a node, a Minimax algo, and input scanner.
		Node initialNode = new Node();
		Minimax minimax = new Minimax();
		Scanner scanner = new Scanner(System.in);
		boolean humanMove = true;
		boolean ABPrune = false;
		boolean playerWillLose = false;
		
		//Grab some basic input from user.
		System.out.println("Let's play Simacogo!");
		System.out.println("Choose a ply: (1 - 10)");
		String plyInput = scanner.next();
		ply = tryParse(plyInput);
		
		System.out.println("Use alpha beta pruning? y / n");
		String answer = scanner.next();
		if(answer.equals("y") || answer.equals("Y")){
			ABPrune = true;
		}
		
		System.out.println("Do you want to lose? y / n");
		answer = scanner.next();
		if(answer.equals("y") || answer.equals("Y")){
			playerWillLose = true;
		}
		
		
		Node current = initialNode;
		
		//if the game hasn't reached terminal state...
		while(!TerminalTest.isTerminalState(current)) {  
			
			Node nextMove;
			printBoard(current);
			printScore(current);
			
			/*
			 * For both human and computer, output the board, the score;
			 * and then get the next move either by human input or the 
			 * CPU's Minimax algorithm.
			 */
			if(humanMove) {
				
				//Prompt human for move
				System.out.println("It's your move! Choose a slot number (1 - 9)"
									+ " to drop your X marker in.");
				String humanMoveChoice = scanner.next();
				int rawMove = tryParse(humanMoveChoice);
				
				//Check for valid move
				while(validateMove(rawMove) == -1){
					System.out.println("Hmm, that wasn't a good input. Try "
							+ "choosing another number...");
					humanMoveChoice = scanner.next();
					rawMove = tryParse(humanMoveChoice);
				}
				
				//Convert move into Action and Node, update game state.
				Action action = tryAction(validateMove(rawMove), current, true);
				nextMove = new Node(current.getXScore(), current.getOScore(), 
									copyState(current.getState()), action);
				
				try {
					nextMove.setState(nextMove.copyStateAndAddMove(action));
					nextMove.raiseXScore(nextMove.movePoints
							(action.getxCoord(), action.getyCoord(), 'X'));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				humanMove = false;
			
				
			} else {
				//Prompt computer for move
				System.out.println("It's the computer's move! It's thinking...\n");
				long startTime = System.currentTimeMillis();
				int x = minimax.decide(current, ply, ABPrune, playerWillLose);
				System.out.println("The computer chose slot " + (x + 1));
				long endTime = System.currentTimeMillis();
				System.out.println("Decision took " + (endTime - startTime) + " ms");
				
				//Convert move into Action and Node, update game state.
				Action action = tryAction(x, current, false);
				nextMove = new Node(current.getXScore(), current.getOScore(), 
									copyState(current.getState()), action);
				
				try {
					nextMove.setState(nextMove.copyStateAndAddMove(action));
					nextMove.raiseOScore(nextMove.movePoints
							(action.getxCoord(), action.getyCoord(), 'O'));
				} catch (Exception e) {
					e.printStackTrace();
				}			
				humanMove = true;
			}
			
			//make the move, set node to the current Node
			current = nextMove;
		}
		
		//end the game when it's terminated
		printBoard(current);
		printScore(current);
		System.out.println("GAME OVER!");
		if (current.getXScore() > current.getOScore())
			System.out.println("X wins!");
		else if(current.getXScore() > current.getOScore())
			System.out.println("O wins!");
		else
			System.out.println("It's a tie!");
		scanner.close();
	}
	
	/*
	 * Copy the state of the board for creating a new node.
	 */
	private char[][] copyState(char[][] state) {
		char [][] newState = new char[BOARD_SIZE][];
		for(int i = 0; i < state.length; i++)
		    newState[i] = state[i].clone();
		return newState;
	}
	

	/*
	 * Makes sure an action taken by the player will fit on the board, throws
	 * an exception if it isn't.
	 */
	private Action tryAction(int rawMove, Node nextMove, boolean humanPlayer) {
		//TODO: check that move is valid
			//if not, press for another move
		Action action;
		char[][] board = nextMove.getState();
		int x = 0;
		while(x < BOARD_SIZE){
			if (board[x][rawMove] == 'X' || board[x][rawMove] == 'O'){
				x--;
				break;
			}
			else if (x == BOARD_SIZE - 1 && board[x][rawMove] == '\u00B7')
				break;
			else
				x++;
		}
		if (x < 0){ 
			System.out.println("that's not a valid move!");
			return null;
		}
		if(humanPlayer)
			action = new Action(x, rawMove, 'X');
		else
			action = new Action(x, rawMove, 'O');
		return action;
	}
	
	/*
	 * Checks that human input is between the column size of 1 and 9.
	 */
	private int validateMove(int rawMove) {
		//TODO: also check for closed-off columns
		if(rawMove > 0 && rawMove < 10)
			return rawMove - 1;
		else return - 1;
	}

	//Print the board
	public void printBoard(Node node){
		System.out.println("\nThe current board looks like this: \n");
		node.printBoard();
	}
	
	//Print the score
	public void printScore(Node node){
		System.out.println("\nThe current score is: ");
		System.out.println("X : " + node.getXScore());  
		System.out.println("O : " + node.getOScore() + "\n");
	}
	
	//Try parsing the human input for a number to make a move off of.
	public Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
		    System.out.println("Oops! That's not a number. Try again!");
		    return -1;
		  }
	}
	
	public static void main(String[] args){
		Simacogo simacogo = new Simacogo();
		simacogo.play();
	}

}
