package game;

import java.util.Set;

public interface Game<State, Action, Player> {
	State getInitial(); // get initial state
	Player[] getPlayers(); // Return players
	Player getPlayerTurn(State s); // Returns whose turn it is
	Set<Action> ACTIONS(State s); // Applicable actions method
	State RESULT(State s, Action a); // Result method
	boolean TERMINAL(State s); // Terminal test
	int UTILITY(State s, Player p); // Utility test
	int heuristic(State s);


}
