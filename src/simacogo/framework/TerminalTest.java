package simacogo.framework;

/*
 * Static check to see if a given node is a terminal state of the game, where 
 * there are no further moves left to make. Checks for an interpunct at
 * each position, and if one exists, returns false. Full board should
 * have no interpuncts. 
 */
public class TerminalTest {

	//cast into enum?
	private static final int BOARD_SIZE = 9;
	
	//check of whole board
	public static boolean isTerminalState(Node node){

		char[][] tiles = node.getState();
		
		for(int x = 0; x < BOARD_SIZE; x++){
			for(int y = 0; y < BOARD_SIZE; y++){
				if(tiles[x][y] == '\u00B7')
					return false;
			}
		}
		return true;
	}
	
	//check of top row in board
	public static boolean isTerminalStateFast(Node node){
		char[][] tiles = node.getState();
		
		for(int y = 0; y < BOARD_SIZE; y++){
			if(tiles[0][y] == '\u00B7')
				return false;
			}
		return true;
	}
	
	/*
	 * Main for unit testing. Checks empty board & full board.
	 */
	public static void main(String[] args) throws Exception {
		Node test = new Node();
		test.printBoard();
		System.out.println("Empty board is terminal: " + 
				TerminalTest.isTerminalState(test));

		int z = 0;
		char[][] fullBoard = new char[9][9];
		for(int x = 0; x< BOARD_SIZE; x++){
			for(int y = 0; y< BOARD_SIZE; y++){
				fullBoard[x][y] = (z % 2 == 0) ? 'X' : 'O'; 
				z++;
			}
		}
		test.setState(fullBoard);
		test.printBoard();
		System.out.println("Full board is terminal: " + 
				TerminalTest.isTerminalState(test));		
	}

}
