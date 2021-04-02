package checkers;

public class CPlayer {
	
	// Checkers player has a color
	public Color color;
	public enum Color {
		WHITE, BLACK;
	}
	
	// Player has a Minimax value
	public Minimax m;
	public enum Minimax {
		MAX, MIN;
	}
	
	// Constructors
	public CPlayer() {
		this.m = null;
		this.color = null;
	}
	
	CPlayer(Color color, Minimax m) {
		this.color = color;
		this.m = m;
	}
	
	// Returns the Player whose playing as black
	public static CPlayer getBlack(CPlayer[] players) {
		if (players[0].color.equals(Color.BLACK)) {
			return players[0];
		} else {
			return players[1];
		}
	}
	
	public String toString() {
		if (m.equals(Minimax.MAX)) {
			return "computer";
		} else {
			return "player";
		}
	}
}
