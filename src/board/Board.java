package board;

import java.util.HashSet;
import java.util.Set;

import checkers.CState;

public class Board {
	public static int D; // number of dark squares
	public static Tile[] Tiles; // list of dark tiles
	public int r; // number of rows/columns
	public int npr; // number of dark squares per row

	// Constructor
	public Board(int r) {

		this.r = r;
		Board.D = (r * r) / 2;
		Board.Tiles = new Tile[D];
		this.npr = r / 2;

		setValues(r, npr); // set values (A2, A4, etc.)

		// For every dark square tile...
		for (int i = 0; i < D; i++) {

			/* Setting Diagonals */
			// if tile is not in first or last row and not in left or right column
			if (i > npr && i < D - npr && i % r != npr && i % r != npr - 1) {
				if (i % r < npr) { // if tile is in an even row (A, C, E, etc.)
					Tiles[i].fnodes.left = Tiles[i - npr];
					Tiles[i].fnodes.right = Tiles[i - (npr - 1)];
					Tiles[i].bnodes.left = Tiles[i + npr];
					Tiles[i].bnodes.right = Tiles[i + npr + 1];
				} else { // if tile is in an odd row (B, D, F, etc.)
					Tiles[i].fnodes.left = Tiles[i - (npr + 1)];
					Tiles[i].fnodes.right = Tiles[i - npr];
					Tiles[i].bnodes.left = Tiles[i + (npr - 1)];
					Tiles[i].bnodes.right = Tiles[i + npr];
				}
			} else {
				// if tile is in the first row
				if (i < npr) {
					Tiles[i].fnodes.left = null; // set forward left empty
					Tiles[i].fnodes.right = null; // set forward right empty
					Tiles[i].bnodes.left = Tiles[i + npr]; // set backward left node
					if (i != npr - 1) { // if it is not right most tile
						Tiles[i].bnodes.right = Tiles[i + npr + 1]; // set backward right node
					} else {
						Tiles[i].bnodes.right = null; // set backward right node empty
					}
				}
				// if tile is in the last row
				if (i >= (D - npr)) {
					if (i != D - npr) { // if it is not the left most node
						Tiles[i].fnodes.left = Tiles[i - (npr + 1)]; // set forward left node
					} else {
						Tiles[i].fnodes.left = null; // set forward left node empty
					}
					Tiles[i].fnodes.right = Tiles[i - npr]; // set forward right node
					Tiles[i].bnodes.left = null; // set backward left empty
					Tiles[i].bnodes.right = null; // set backward right empty
				}
				// if tile is in the left column
				if (i % r == npr) {
					Tiles[i].fnodes.left = null; // set forward left empty
					Tiles[i].fnodes.right = Tiles[i - npr]; // set forward right
					Tiles[i].bnodes.left = null; // set backward left empty
					if (i != D - npr) { // if it is not the bottom most node
						Tiles[i].bnodes.right = Tiles[i + npr]; // set backward right node
					} else {
						Tiles[i].bnodes.right = null; // set backward right empty
					}
				}
				// if tile is in the right column
				if (i % r == npr - 1) {
					if (i != npr - 1) { // if it is not the top most node
						Tiles[i].fnodes.left = Tiles[i - npr]; // set forward left node
					} else {
						Tiles[i].fnodes.left = null; // set forward left node empty
					}
					Tiles[i].fnodes.right = null; // set forward right empty
					Tiles[i].bnodes.left = Tiles[i + npr]; // set backward left node
					Tiles[i].bnodes.right = null; // set backward right empty
				}
			}
		}
	}

	// Method to set all values of the square (A2, A4, etc.)
	void setValues(int r, int npr) {
		for (int i = 0; i < D; i++) {
			Tiles[i] = new Tile(); // initialize the Tile

			char letter = (char) (65 + (i / npr));
			String number = null;

			if (i % r >= npr) { // if tile is an odd row (B, D, F, etc.)
				number = Integer.toString(1 + 2 * (i % npr));
			} else { // tile is an even row (A, C, E, etc.)
				number = Integer.toString(2 + 2 * (i % npr));
			}

			/* Concatenate values for Tile value */
			Tiles[i].value = String.valueOf(letter) + number;
		}
	}

	// Returns the tile represented by the string s
	public static Tile getTile(String s) {
		for (int i = 0; i < D; i++) {
			if (Tiles[i].value.equals(s)) {
				return Tiles[i];
			}
		}
		return null;
	}

	/* Prints the configuration of the board given a State s */
	public void printBoard(CState s) {
		
		// for each row
		for (int i = 0; i < r; i++) {

			// print numbers if first row
			if (i == 0) {
				System.out.print("  ");
				for (int j = 0; j < r; j++) {
					System.out.print(j + 1 + " ");
				}
				System.out.println();
			}

			// print line of +-+-...
			System.out.print(" ");
			for (int j = 0; j < r; j++) {
				System.out.print("+-");
			}
			System.out.println("+");

			// print content of board
			for (int j = 0; j < r; j++) {
				// print letters if first column
				if (j == 0) {
					char c = (char) (65 + i);
					System.out.print(c + "|");
				}

				String value = String.valueOf((char) (65 + i)) + (j + 1);
				Tile t = getTile(value);

				if (s.Lb.contains(t)) { // if the square contains a black piece
					if (s.hasKing(t)) {
						System.out.print("B|");
					} else {
						System.out.print("b|");
					}
				} else if (s.Lw.contains(t)) {// if the square contains a white piece
					if (s.hasKing(t)) {
						System.out.print("W|");
					} else {
						System.out.print("w|");
					}
				} else { // else print empty tile
					System.out.print(" |");
				}
			}
			System.out.print("\n");
		}

		// print last line of +-+-...
		System.out.print(" ");
		for (int j = 0; j < r; j++) {
			System.out.print("+-");
		}
		System.out.println("+");

	}

	// returns true if Tile j is forwards diagonal (up) from Tile i
	public boolean isFdiagonal(Tile i, Tile j) {
		if (i.fnodes.left == j || i.fnodes.right == j) {
			return true;
		}
		return false;
	}

	// returns true if Tile j is backwards diagonal (down) from Tile i
	public boolean isBdiagonal(Tile i, Tile j) {
		if (i.bnodes.left == j || i.bnodes.right == j) {
			return true;
		}
		return false;
	}

	// returns true if Tile j is forwards or backwards diagonal from Tile i
	public boolean isKdiagonal(Tile i, Tile j) {
		if (isFdiagonal(i, j) || isBdiagonal(i, j)) {
			return true;
		}
		return false;
	}

	// Returns forward diagonal nodes of a tile
	public Set<Tile> getFdiagonal(Tile i) {
		Set<Tile> Fdiag = new HashSet<Tile>();
		if (i.fnodes.left != null) {
			Fdiag.add(i.fnodes.left);
		}
		if (i.fnodes.right != null) {
			Fdiag.add(i.fnodes.right);
		}
		return Fdiag;
	}

	// Returns backward diagonal nodes of a tile
	public Set<Tile> getBdiagonal(Tile i) {
		Set<Tile> Bdiag = new HashSet<Tile>();
		if (i.bnodes.left != null) {
			Bdiag.add(i.bnodes.left);
		}
		if (i.bnodes.right != null) {
			Bdiag.add(i.bnodes.right);
		}
		return Bdiag;
	}

	// Returns all diagonal nodes of a tile
	public Set<Tile> getKdiagonal(Tile i) {
		Set<Tile> Kdiag = getFdiagonal(i);
		Kdiag.addAll(getBdiagonal(i));
		return Kdiag;
	}

	// Finds the tile diagonally between two tiles
	public static Tile findTwoAhead(Tile i, Tile j) {
		if (i.fnodes.left != null && i.fnodes.left.tileEquals(j)) {
			return i.fnodes.left.fnodes.left;
		} else if (i.fnodes.right != null && i.fnodes.right.tileEquals(j)) {
			return i.fnodes.right.fnodes.right;
		} else if (i.bnodes.left != null && i.bnodes.left.tileEquals(j)) {
			return i.bnodes.left.bnodes.left;
		} else if (i.bnodes.right != null && i.bnodes.right.tileEquals(j)) {
			return i.bnodes.right.bnodes.right;
		}
		return null;
	}
	
	// Returns tile between Tile i and Tile j
	public Tile getTileBetween(Tile i, Tile j) {
		if (i.fnodes.left != null 
				&& i.fnodes.left.fnodes.left != null
				&& tileIsEqual(i.fnodes.left.fnodes.left, j)) {
			return i.fnodes.left;
		} else if (i.fnodes.right != null 
				&& i.fnodes.right.fnodes.right != null
				&& tileIsEqual(i.fnodes.right.fnodes.right, j)) {
			return i.fnodes.right;
		} else if (i.bnodes.left != null 
				&& i.bnodes.left.bnodes.left != null
				&& tileIsEqual(i.bnodes.left.bnodes.left, j)) {
			return i.bnodes.left;
		} else if (i.bnodes.right != null 
				&& i.bnodes.right.bnodes.right != null
				&& tileIsEqual(i.bnodes.right.bnodes.right, j)) {
			return i.bnodes.right;
		}
		return null;
	}
	
	// Returns true if two tiles are equal
	public static boolean tileIsEqual(Tile i, Tile j) {
		return (i.value.equals(j.value));
	}
}
