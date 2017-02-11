package simacogo.framework;

import java.util.LinkedList;
import java.util.List;

/*
 * Represents a potential action for the Simacogo game. Records the coordinates
 * of the newest move and a boolean of which player makes that move. 
 */
public class Action {

	public boolean playerIsX;
	private int xCoord;
	private int yCoord;
	
	//Main constructor, takes coordinates and next player char.
	public Action(int xCoord, int yCoord, char player){
		this.setxCoord(xCoord);
		this.setyCoord(yCoord);
		if(player == 'X')
			this.playerIsX = true;
		else this.playerIsX = false;	
	}
	
	public Action(char player){
		if(player == 'X')
			this.playerIsX = true;
		else this.playerIsX = false;
			
	}
	
	//BEGIN getters and setters
	public int getxCoord() {
		return xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}
	//END getters and setters
	
	/*
	 * custom toString method, mainly for error checking.
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		char player = playerIsX? 'X' : 'O';
		StringBuilder sb = new StringBuilder();
		sb.append("x: " + xCoord + ", ");
		sb.append("y: " + yCoord + ", ");
		sb.append("player: " + player);
		return sb.toString();
	}

	/*
	 * Strictly for unit testing.
	 */
	public static void main(String[] args) {
		Action action = new Action(0,0,'O');
		System.out.println(action.toString());
		
		action = new Action(1, 5, 'X');
		System.out.println(action.toString());

	}

}
