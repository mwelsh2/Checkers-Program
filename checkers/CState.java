package checkers;

import java.util.HashSet;
import java.util.Set;

import board.Board;
import board.Tile;
import checkers.CPlayer.Color;

public class CState {
	public Set<Tile> Lb; // Location of black pieces
	public Set<Tile> Lw; // Location of white pieces
	public Set<Tile> K; // Location of kings
	public CPlayer turn; // Player whose turn it is
	
	// counts number of moves taken at that state
	// relevant when checking if moves limit has been reached
	public int moves; 

	// Constructors
	CState() {
		this.Lb = new HashSet<Tile>();
		this.Lw = new HashSet<Tile>();
		this.K = new HashSet<Tile>();
		this.turn = null;
	}

	CState(CState s) {
		this.Lb = s.Lb;
		this.Lw = s.Lw;
		this.K = s.K;
		this.turn = s.turn;
		this.moves = s.moves;
	}

	// Creates the initial state given the player's desired row size and color
	public static CState createInitial(int row_size, CPlayer[] players, Color player) {
		CState s = new CState();
		s.turn = CPlayer.getBlack(players);
		s.moves = 0;
		
		// Set Location of pieces if player chose black
		if (player.equals(Color.BLACK)) {
			for (int i = 0; i < (row_size - 2) / 2; i++) {
				for (int j = 0; j < row_size / 2; j++) {
					char w_letter = (char) (65 + i);
					char b_letter = (char) (65 + (row_size - 1) - i);
					int b_number, w_number;

					// if black is in an even row
					if (b_letter % 2 == 1) {
						b_number = 2 * j + 2;
					}
					// if black is in an odd row
					else {
						b_number = 2 * j + 1;
					}

					// if white is in an even row
					if (w_letter % 2 == 1) {
						w_number = 2 * j + 2;
					}
					// if white is in an odd row
					else {
						w_number = 2 * j + 1;
					}

					s.Lb.add(Board.getTile(String.valueOf(b_letter) + b_number));
					s.Lw.add(Board.getTile(String.valueOf(w_letter) + w_number));
				}
			}
			
		// set location of pieces if player chose white
		} else {
			for (int i = 0; i < (row_size - 2) / 2; i++) {
				for (int j = 0; j < row_size / 2; j++) {
					char b_letter = (char) (65 + i);
					char w_letter = (char) (65 + (row_size - 1) - i);
					int b_number, w_number;

					// if black is in an even row
					if (b_letter % 2 == 1) {
						b_number = 2 * j + 2;
					}
					// if black is in an odd row
					else {
						b_number = 2 * j + 1;
					}

					// if white is in an even row
					if (w_letter % 2 == 1) {
						w_number = 2 * j + 2;
					}
					// if white is in an odd row
					else {
						w_number = 2 * j + 1;
					}

					s.Lb.add(Board.getTile(String.valueOf(b_letter) + b_number));
					s.Lw.add(Board.getTile(String.valueOf(w_letter) + w_number));
				}
			}
		}
		return s;
	}

	// Returns the set of pieces of the player whose turn it is
	public Set<Tile> getTurnPieces() {
		if (turn.color.equals(Color.BLACK)) {
			return Lb;
		} else {
			return Lw;
		}
	}

	// Returns the set of pieces of the player whose turn it is not
	public Set<Tile> getOppPieces() {
		if (turn.color.equals(Color.BLACK)) {
			return Lw;
		} else {
			return Lb;
		}
	}

	// Returns true if Tile is a King in this state
	public boolean hasKing(Tile i) {
		return (K.contains(i));
	}

	// Returns true if the given tile is empty
	public boolean tileIsEmpty(Tile i) {
		if (!Lb.contains(i) && !Lw.contains(i)) {
			return true;
		} else {
			return false;
		}
	}

	// Returns all Tiles diagonal to i, in the appropriate direction(s)
	Set<Tile> getDiagonal(Board b, Tile i) {
		if (hasKing(i)) {
			return b.getKdiagonal(i);
		} else {
			if (turn == Checkers.min) { // if player's turn
				return b.getFdiagonal(i);
			} else { // if computer's turn
				return b.getBdiagonal(i);
			}
		}
	}

	// Checks if capture is possible
	Tile checkCapture(Tile i, Tile j) {

		// Tile k is the tile that is two diagonal from tile i
		// where Tile j is in between Tile i and Tile k
		Tile k = Board.findTwoAhead(i, j);

		// if there exists an empty tile k
		if (k != null && tileIsEmpty(k) && getOppPieces().contains(j)) {
			return k;
		}
		return null;
	}

//	// Checks if multicapture is possible
//	Set<LinkedList<Tile>> checkMulticapture(Board b, Tile i, Tile j, LinkedList<Tile> captures,
//			Set<LinkedList<Tile>> result, Set<Tile> visited) {
//
//		System.out.println("VISITED TILES: " + visited);
//		visited.add(i);
//		System.out.println("CHECKING CAPTIURE FOR: " + i + ", " + j);
//		Tile k = checkCapture(i, j);
//
//		if (k == null) {
//			if (!captures.isEmpty()) {
//				result.add(captures);
//			}
//			return result;
//		} else if (visited.contains(k)) {
//			result.add(captures);
//			return result;
//		} else {
//			captures.add(k);
//
//			Set<Tile> D = getDiagonal(b, k);
//			if (D.isEmpty()) {
//				result.add(captures);
//				return result;
//			} else {
//				for (Tile t : D) {
//					checkMulticapture(b, k, t, captures, result, visited);
//				}
//				return result;
//			}
//		}
//	}

//	void checkCaptures(Tile i, Board b, Set<CAction> applicable) {
//		LinkedList<Tile> captures = new LinkedList<Tile>();
//	
//		Set<Tile> D = getDiagonal(b, i);
//		if (!D.isEmpty()) {
//				for (Tile j : D) {
//
//					// Inherit captures from previous tile
//					LinkedList<Tile> new_captures = new LinkedList<Tile>();
//					new_captures.addAll(captures);
//
//					// k is the tile two ahead from Tile i through Tile j
//					Tile k = Board.findTwoAhead(i, j);
//
//					// if there exists an empty tile k
//					if (k != null && tileIsEmpty(k) && getOppPieces().contains(j)) {
//						new_captures.add(k); // add k to the captures
//						captures = new_captures;
//						checkCaptures(k, b, applicable);
//					} else {
//						// if capture list is empty
//						if (new_captures.isEmpty()) {
//							captures.clear();
//							continue;
//						}
//						// if captures list has one size
//						if (new_captures.size() == 1) {
//							CAction a = new CAction(Type.capture, i, new_captures.getFirst(), new_captures);
//							applicable.add(a);
//						} else {
//							CAction a = new CAction(Type.multicapture, i, new_captures.getLast(), new_captures);
//							applicable.add(a);
//						}
//					}
//				}
//			} else {
//				// if capture list is empty
//				if (captures.isEmpty()) {
//					captures.clear();
//					return;
//				}
//				// if captures list has one size
//				if (captures.size() == 1) {
//					CAction a = new CAction(Type.capture, i, captures.getFirst(), captures);
//					applicable.add(a);
//					captures.clear();
//					return;
//				} else {
//					CAction a = new CAction(Type.multicapture, i, captures.getLast(), captures);
//					applicable.add(a);
//					captures.clear();					
//					return;
//				}
//			}
//		}
//	

	// Returns the player whose turn it is not
	CPlayer getOppPlayer() {
		if (turn == Checkers.min) {
			return Checkers.max;
		} else {
			return Checkers.min;
		}
	}

	public void printLb() {
		Object[] t = Lb.toArray();
		System.out.print("Lb: ");
		for (int i = 0; i < Lb.size(); i++) {
			System.out.print(t[i] + ", ");
		}
		System.out.println();
	}

	public void printLw() {
		Object[] t = Lw.toArray();
		System.out.print("Lw: ");
		for (int i = 0; i < Lw.size(); i++) {
			System.out.print(t[i] + ", ");
		}
		System.out.println();
	}

	// Returns true if the States are equal
	public boolean stateIsEqual(CState s) {
		if (this.Lb.equals(s.Lb) && this.Lw.equals(s.Lw) && this.K.equals(s.K) && this.turn == s.turn) {
			return true;
		}
		return false;
	}

	// Returns a copy of the state
	CState copyState() {
		CState s = new CState();
		s.Lb.addAll(Lb);
		s.Lw.addAll(Lw);
		s.K.addAll(K);
		s.turn = turn;
		s.moves = moves;

		return s;
	}

	public String toString() {
		return "Lb: " + Lb.toString() + "\nLw: " + Lw.toString() + "\nK: " 
				+ K.toString() + "\nTurn: " + turn.color;
	}
	
//	public static void main(String[] args) {
//		Checkers c = new Checkers(8, Color.BLACK, 50, 3);
//		Tile[] white = {Board.getTile("A2"), Board.getTile("A4"), Board.getTile("A6"),
//			Board.getTile("A8"), Board.getTile("B1"), Board.getTile("B3"), Board.getTile("C4"), Board.getTile("B7"), 
//			Board.getTile("C2"), Board.getTile("E4"), Board.getTile("C6"), Board.getTile("C8")};
//		c.s0.Lw.clear();
//		c.s0.Lw.addAll(Arrays.asList(white));
//		
//		Tile[] black = {Board.getTile("F1"), Board.getTile("F3"), Board.getTile("F5"),
//				Board.getTile("F7"), Board.getTile("G2"), Board.getTile("G4"), Board.getTile("G6"), Board.getTile("G8"), 
//				Board.getTile("H1"), Board.getTile("H3"), Board.getTile("H5"), Board.getTile("H7")};
//			c.s0.Lb.clear();
//			c.s0.Lb.addAll(Arrays.asList(black));
//			
//		c.b.printBoard(c.s0);
//		
//		Run.playGame(c, c.s0);
//	}
}
