package simacogo.play;

import simacogo.framework.TerminalTest;
import simacogo.framework.Node;
import simacogo.framework.Action;

import java.util.Collections;
import java.util.List;



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
	public int decide(Node node, int depth){
		MMDecision result = minimax(node, depth, true);
		return result.action.getxCoord();
	}
	
	public MMDecision minimax(Node node, int depth, boolean maxPlayer){
		if(depth == 0 || TerminalTest.isTerminalState(node))
			return new MMDecision(node.getAction(), (node.getOScore() - node.getXScore()));
		
		if(maxPlayer){
			MMDecision bestDecision = new MMDecision(node.getAction(), Integer.MIN_VALUE);
			List<Node> children = node.getChildren();
			for(Node child : children){
				MMDecision childDecision = minimax(child, depth - 1, false);
				if(bestDecision.compareTo(childDecision) < 0)
					bestDecision.heuristic = childDecision.heuristic;
 			}
			return bestDecision;
		}
		
		if(!maxPlayer){
			MMDecision bestDecision = new MMDecision(node.getAction(), Integer.MAX_VALUE);
			List<Node> children = node.getChildren();
			for(Node child : children){
				MMDecision childDecision = minimax(child, depth - 1, false);
				if(bestDecision.compareTo(childDecision) > 0)
					bestDecision.heuristic = childDecision.heuristic;
			}
			return bestDecision;
		}
		return new MMDecision(node.getAction(), (node.getOScore() - node.getXScore()));
	}
	
	public class MMDecision{
		private Action action;
		private int heuristic;
		
		public MMDecision(Action action, int heuristic){
			this.setAction(action);
			this.setHeuristic(heuristic);
		}

		public Action getAction() {
			return action;
		}

		public void setAction(Action action) {
			this.action = action;
		}

		public int getHeuristic() {
			return heuristic;
		}

		public void setHeuristic(int heuristic) {
			this.heuristic = heuristic;
		}
		
		public int compareTo(MMDecision other){
			if(this.heuristic < other.heuristic)
				return -1;
			else if(this.heuristic > other.heuristic)
				return 1;
			else return 0;
		}
	}
	
	

}
