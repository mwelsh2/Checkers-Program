package board;

public class Tile {

	// represents tiles in same direction
	// ex: forward left tile and forward right tile
	public static class NodeSet {
		public Tile left; // left tile
		public Tile right; // right tile

		NodeSet() {
			this.left = null;
			this.right = null;
		}
	}

	public String value; // Value of tile (A2, A4 etc.)
	public NodeSet fnodes;// tiles in forward diagonal direction
	public NodeSet bnodes; // tiles in backwards diagonal direction

	// Constructor
	Tile() {
		this.value = null;
		this.fnodes = new NodeSet();
		this.bnodes = new NodeSet();
	}

	// Returns true if given tile is equal
	public boolean tileEquals(Tile t) {
		if (this.value.equals(t.value)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return value;
	}

}
