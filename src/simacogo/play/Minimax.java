package simacogo.play;

import simacogo.framework.TerminalTest;
import simacogo.framework.Node;
import simacogo.framework.Action;

import java.util.Collections;
import java.util.List;


/*
 * AI decision logic for game.
 * 
 * POSSIBLE IMPROVEMENT: Change utility function to look for highest scoring
 * nodes first.
 * 
 * ANOTHER IMPROVEMENT: Utilize a priority queue to go further into the tree.
 */
public class Minimax {

	/*
	 * Main computer decision logic.
	 */
	public Minimax(){		
	}
	
	/*
	 * Calls the minimax working function and returns the column of the best 
	 * move for the computer to make. 
	 */
	public int decide(Node node, 
					  int depth, 
					  boolean ABPrune, 
					  boolean opponentWantsToLose){
		
		//Alpha-beta pruning driver, calls first ply
		if(ABPrune) {
			List<Node> children = node.getChildren();
			for(Node child : children){
				child.setMinMaxVal(abPrune(child, depth - 1, Integer.MIN_VALUE, 
										   Integer.MAX_VALUE, false));		
			}
			Collections.sort(children, new MinMaxComparator());
			
			if(opponentWantsToLose){
				int playerWillLose = guaranteeLoss(node, children);
				if (playerWillLose > -1)
					return playerWillLose;
			}			
			return children.get(0).getAction().getyCoord();
			
		//Non A-B minimax driver, calls first ply
		} else {
			List<Node> children = node.getChildren();
			for(Node child : children){
				child.setMinMaxVal(minimax(child, depth - 1, false));
			}
			Collections.sort(children, new MinMaxComparator());
			
			if(opponentWantsToLose){
				int playerWillLose = guaranteeLoss(node, children);
				if (playerWillLose > -1)
					return playerWillLose;
			}
			
			return children.get(0).getAction().getyCoord();
		}		
	}
	
	
	/*
	 * Minimax decision logic. Taken from book & wikipedia pseudocode.
	 */
	public int minimax(Node node, int depth, boolean maxPlayer){
		if(depth == 0 || TerminalTest.isTerminalState(node)){
			node.setMinMaxVal(node.getOScore() - node.getXScore());
			//System.out.println("O: " + node.getOScore() + " X: " + node.getXScore() + " minmax: " + node.getMinMaxVal());
			return node.getMinMaxVal();
		}
		
		if(maxPlayer){
			int bestMove = Integer.MIN_VALUE;
			List<Node> children = node.getChildren();
			for(Node child : children){
				bestMove = Math.max(bestMove, minimax(child, depth - 1, false));
 			}
			return bestMove;
		}
		
		else {
			int bestMove = Integer.MAX_VALUE;
			List<Node> children = node.getChildren();
			for(Node child : children){
				bestMove = Math.min(bestMove, minimax(child, depth - 1, true));
 			}
			return bestMove;
		}
	}
	
	/*
	 * A_B Pruning decision logic. Used Pseudocode from Wikipedia & textbook.
	 */
	public int abPrune(Node node, int depth, int alpha, int beta, boolean maxPlayer){
		if(depth == 0 || TerminalTest.isTerminalState(node)){
			node.setMinMaxVal(node.getOScore() - node.getXScore());
			return node.getMinMaxVal();
		}
		

		if(maxPlayer){
			int bestValue = Integer.MIN_VALUE;
			List<Node> children = node.getChildren();
			for(Node child : children){
				bestValue = Math.max(bestValue, abPrune(child, depth - 1, alpha, beta, false));
				
				if(bestValue >= beta){
					return bestValue;		
				}			
				alpha = Math.max(alpha, bestValue);
			}
			return bestValue;
			
		} else {
			int bestValue = Integer.MAX_VALUE;
			List<Node> children = node.getChildren();
			for(Node child : children){
				bestValue = Math.max(bestValue, abPrune(child, depth - 1, alpha, beta, true));
				beta = Math.min(beta, bestValue);
				
				if(bestValue <= alpha){
					return bestValue;		
				}
				beta = Math.min(beta, bestValue);
			}
			return bestValue;		
		}
	}
	
	
	
	/*
	 * Method that will guarantee either a tie or a loss if human player has first
	 * move. If the value of the minimax is the same for all children nodes, then 
	 * the computer moves by placing a tile on top of the human's last move, if 
	 * it's available. Otherwise, returns -1 to defer to miniax or ABPrune result.
	 */
	public int guaranteeLoss(Node node, List<Node> children){
		int bestVal = children.get(0).getMinMaxVal(); 
		int allMatch = 0;
		for(Node child : children){
			if(child.getMinMaxVal() == bestVal)
				allMatch ++;
		}
		char[][] state = node.getState();
		if(allMatch == children.size() && state[0][node.getAction().getyCoord()] != 'X'){
			for(Node child : children) System.out.print(child.getMinMaxVal() + " ");
			return node.getAction().getyCoord();
		}
		return -1;
	}
}
	

	
	
