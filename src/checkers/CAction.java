package checkers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import board.Board;
import board.Tile;

public class CAction {

	// An action is either a move, a capture, or multicapture
	public enum Type {
		move, capture, multicapture;
	}

	public Type type; // type of action
	public Tile i; // start tile of action
	public Tile j; // destination tile of action
	public LinkedList<Tile> capture_list; // list of all captured tiles (if applicable)

	// Constructors
	public CAction() {
		this.type = null;
		this.i = null;
		this.j = null;
		this.capture_list = new LinkedList<Tile>();
	}

	public CAction(Type t, Tile i, Tile j) {
		this.type = t;
		this.i = i;
		this.j = j;
		this.capture_list = new LinkedList<Tile>();
	}

	public CAction(Type t, Tile i, Tile j, LinkedList<Tile> capture_list) {
		this.type = t;
		this.i = i;
		this.j = j;
		this.capture_list = new LinkedList<Tile>();
		this.capture_list.addAll(capture_list);
	}

	// returns an action given a String s, used for when user inputs an action
	public static CAction createAction(String s) {
		CAction a = new CAction();
		if (s.contains("-")) {
			a.type = Type.move;
			String[] input = s.split("-");
			if (input.length != 2) {
				return null;
			} else {
				a.i = Board.getTile(input[0]);
				a.j = Board.getTile(input[1]);
				if (a.i == null || a.j == null) { return null; }
				return a;
			}

		} else if (s.contains("x")) {
			String[] input = s.split("x");
			if (input.length == 2) {
				a.i = Board.getTile(input[0]);
				a.j = Board.getTile(input[1]);
				if (a.i == null || a.j == null) { return null; }
				a = new CAction(Type.capture, a.i, a.j);
				a.capture_list.add(a.j);
				return a;
//			} else if (input.length > 2) {
//				a = new CAction(Type.multicapture, Board.getTile(input[0]), Board.getTile(input[input.length - 1]));
//				for (int i = 1; i < input.length; i++) {
//					a.capture_list.add(Board.getTile(input[i]));
//				}
//				return a;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	// returns true if Action is in the set A
	public boolean isInActionSet(Set<CAction> A) {
		for (CAction a: A) {
			if (type.equals(a.type) && i.equals(a.i) 
					&& j.equals(a.j) && capturesAreEqual(a)) {
				return true;
			}
		}
		return false;
	}
	
	// returns true if the captures list of each Action are equal
	boolean capturesAreEqual(CAction a) {
		if (capture_list.size() == a.capture_list.size()) {
			for (int i = 0; i < capture_list.size(); i++) {
				if (!capture_list.get(i).value.equals(a.capture_list.get(i).value)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	// Prints a set of Actions
	public static void printActionSet(Set<CAction> A) {
		System.out.println("Available Actions: ");
		int i = 1;
		for (CAction a : A) {
			System.out.println("  " + i + ". " + a);
			i++;
		}
	}
	
	// Returns a set of actions with the maximum amount of captures
	public static Set<CAction> filterActions(Set<CAction> A) {
		int max = Integer.MIN_VALUE;
		for (CAction a: A) {
			if (a.capture_list.size() > max) {
				max = a.capture_list.size();
			}
		}
		
		if (max < 0) {
			return A;
		}
		
		Set<CAction> result = new HashSet<CAction>();
		for (CAction a: A) {
			if (a.capture_list.size() == max) {
				result.add(a);
			}
		}
		return result;
	}

	public String toString() {
		if (type.equals(Type.move)) {
			return i + "-" + j;
		} else if (type.equals(Type.capture)) {
			return i + "x" + j;
		} else if (type.equals(Type.multicapture)) {
			String s = "";
			for (Tile t : this.capture_list) {
				s = s + "x" + t;
			}
			return i + s;
		}
		return null;
	}
}
