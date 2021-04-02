package checkers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import board.Board;
import board.Tile;
import checkers.CAction.Type;
import checkers.CPlayer.Color;
import game.AlphaBeta;
import game.Game;
import game.Minimax;
import game.Random;
import game.Search;
import game.hMinimax;

public class Checkers implements Game<CState, CAction, CPlayer> {

	public final static CPlayer max = new CPlayer(); // computer
	public final static CPlayer min = new CPlayer(); // player
	public final static CPlayer[] players = { max, min }; // two players
	public CState s0 = new CState(); // initial state
	public Board b; // board
	public int moves_limit; // number of moves when a tie is declared
	public Search<CState, CAction> search; // search method for the computer

	// Constructor
 	public Checkers(int size, Color player, int moves_limit, int search_method, int depth) {
		// Sets color the player chose to play
 		min.color = player;
		
 		// Sets color of the computer
		if (player.equals(Color.BLACK)) {
			max.color = Color.WHITE;
		} else {
			max.color = Color.BLACK;
		}
		
		// Initialize board given player's chosen size
		this.b = new Board(size);
		this.moves_limit = moves_limit;
		
		// Initialize search method of computer given player's choice
		if (search_method == 1) {
			this.search = new Random<CState, CAction, CPlayer>(this);
		}
		if (search_method == 2) {
			this.search = new Minimax<CState, CAction, CPlayer>(this);
		}
		if (search_method == 3) {
			this.search = new AlphaBeta<CState, CAction, CPlayer>(this, depth);
		} 
		if (search_method == 4) {
			this.search = new hMinimax<CState, CAction, CPlayer>(this, depth);
		}
		
		// create the initial state
		s0 = CState.createInitial(size, players, player);
	}

 	// Returns initial state
	@Override
	public CState getInitial() {
		return s0;
	}

	// Returns set of players
	@Override
	public CPlayer[] getPlayers() {
		return players;
	}

	// Returns Player whose turn it is at State s
	@Override
	public CPlayer getPlayerTurn(CState s) {
		return s.turn;
	}

	// Returns set of Actions in State s
	@Override
	public Set<CAction> ACTIONS(CState s) {
		Set<CAction> applicable = new HashSet<CAction>();

		// Location of all tiles whose player's turn it is
		Set<Tile> Lt = s.getTurnPieces();
		// Location of all tiles whose player's turn it is not
		Set<Tile> Lo = s.getOppPieces();

		for (Tile i : Lt) { // for each Tile of the player

			for (Tile j : s.getDiagonal(b, i)) { // for each diagonal of the Tile

				// if the tile is empty, add 'move' to the set of actions
				if (s.tileIsEmpty(j)) {
					CAction a = new CAction(Type.move, i, j);
					applicable.add(a);
					
				} else if (Lo.contains(j)) {
					Tile k = s.checkCapture(i, j);
					if (k != null) {
						LinkedList<Tile> captures = new LinkedList<Tile>();
						captures.add(k);
						CAction a = new CAction(Type.capture, i, captures.get(0), captures);
						applicable.add(a);
					}
				}
				

//				// if the tile is occupied by the opponent
//				} else if (Lo.contains(j)) {
//
//					LinkedList<Tile> captures = new LinkedList<Tile>();
//					Set<LinkedList<Tile>> list = new HashSet<LinkedList<Tile>>();
//					Set<Tile> visited = new HashSet<Tile>();
//					
//					System.out.println("CHECKING MULTICAPTURE FOR: " + i + ", " + j);
//
//					
//					list.addAll(s.checkMulticapture(b, i, j, captures, list, visited));
//
//					if (list.isEmpty()) {
//						break;
//					} else {
//						for (LinkedList<Tile> l : list) {
//							if (l.isEmpty()) {
//								break;
//							} else {
//								for (LinkedList<Tile> c : list) {
//									if (c.size() == 1) {
//										CAction a = new CAction(Type.capture, i, c.getFirst(), c);
//										applicable.add(a);
//									} else {
//										CAction a = new CAction(Type.multicapture, i, c.getLast(), c);
//										applicable.add(a);
//									}
//								}
//							}
//						}
//					}
//				}
			}
		}
		applicable = CAction.filterActions(applicable);
//		System.out.println("Applicable actions: " + applicable.toString());
		return applicable;
	}

	// Returns resulting state of performing Action a on State s
	@Override
	public CState RESULT(CState s, CAction a) {
		CState result = s.copyState();
		result.moves = s.moves + 1;
		
		// Switch turn of player/computer
		result.turn = s.getOppPlayer();
		
		if (s.turn.color == Color.BLACK) { // if black just played
			result.Lb.remove(a.i); // remove start tile
			result.Lb.add(a.j); // add destination tile
			
			// if action is a capture
			if (a.type == Type.capture) {
				Tile k = b.getTileBetween(a.i, a.j);
				result.Lw.remove(k);
//			} else if (a.type == Type.multicapture) {
//				result.Lw.remove(b.getTileBetween(a.i, a.capture_list.get(0)));
//				for (int i = 0; i + 1 < a.capture_list.size(); i++) {
//					Tile j = a.capture_list.get(i);
//					result.Lw.remove(b.getTileBetween(j, a.capture_list.get(i+1)));
//				}
			}
		} else { // if white just played
			result.Lw.remove(a.i); // remove start tile
			result.Lw.add(a.j); // add destination tile
			
			// if action is a capture
			if (a.type == Type.capture) {
				Tile k = b.getTileBetween(a.i, a.j);
				result.Lb.remove(k);
//			} else if (a.type == Type.multicapture) {
//				result.Lb.remove(b.getTileBetween(a.i, a.capture_list.get(0)));
//				for (int i = 0; i + 1 < a.capture_list.size(); i++) {
//					Tile j = a.capture_list.get(i);
//					result.Lb.remove(b.getTileBetween(j, a.capture_list.get(i+1)));
//				}
			}
		}
		
 		// if tile is a king
		if (s.hasKing(a.i)) {
			result.K.remove(a.i); // remove start tile from set of kings
			result.K.add(a.j); // add destination tile to set of kings
		} else { // if start tile isn't a king
			if (s.turn == min) { // if player just played
				// make destination tile a king if it is in row A
				if (a.j.value.contains("A")) {result.K.add(a.j);}
			} else { // if computer just played
				// make destination tile a king if it is in the last row
				if (a.j.value.contains(String.valueOf((char) (65 + (b.r-1))))) {
					result.K.add(a.j);
				}
			}
		}
		return result;
	}

	// Returns true if State s is terminal
	@Override
	public boolean TERMINAL(CState s) {
		
		// if either white or black has no pieces
		if (s.Lb.isEmpty() || s.Lw.isEmpty()) { return true; }
				
		// if there are no applicable action's left
		Set<CAction> applicable = ACTIONS(s);
		if (applicable.isEmpty()) { return true; }
				
		// if you have surpassed the moves limit
		if (s.moves >= moves_limit) { return true; }
				
		return false;
	}

	// Returns utility value of State s for Player p
	@Override
	public int UTILITY(CState s, CPlayer p) {
		
		// If game ended because someone won
		if (s.Lb.isEmpty() || s.Lw.isEmpty() || ACTIONS(s).isEmpty()) {
			if (getPlayerTurn(s) == p) { // if it's the given player's turn
				return -1;
			} else { // if it's not the given player's turn
				return 1;
			}
		} else { // Tie because the moves limit has been reached
			return 0;
		}
	}

	// Returns heuristic estimate of State s
	@Override
	public int heuristic(CState s) {
		Set<Tile> Lt = s.getTurnPieces();
		Set<Tile> Lo = s.getOppPieces();
		int diff = Lt.size() - Lo.size();
		
//		int kings = 0;
//		for (Tile t: Lt) {
//			if (s.hasKing(t)) { kings++; }
//		}
		
		return diff;
	}
	
}
