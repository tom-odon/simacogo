package simacogo.framework;
/*
 * Represents a move in the Simacogo game. Consists of a 2D 9x9 board, an action,
 * and a point cost for Min and Max.
 */
public class Node {
	private static int BOARD_SIZE = 9;
	private char[] board;
	private Action action;
	
	public Node(){
		
	}
	
	
	/*
	 * Strictly for unit testing project.
	 */
	public static void main(String[] args) {
		Node node = new Node();
		

	}

}
