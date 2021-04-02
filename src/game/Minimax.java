package game;

public class Minimax<State, Action, Player> implements Search<State, Action> {

	Game<State, Action, Player> g;
	int states_visited;
	
	public Minimax(Game<State, Action, Player> g) {
		this.g = g;
	}
	
	@Override
	public Action chooseMove(State s) {
		states_visited = 0;
		long start = System.currentTimeMillis();
		int max = Integer.MIN_VALUE;
		Action action = null;
		Player p = g.getPlayerTurn(s);
		for (Action a : g.ACTIONS(s)) {
			int value = MinValue(g.RESULT(s, a), p);
			if (value > max) {
				max = value;
				action = a;
			}
		}
		long finish = System.currentTimeMillis();
		float time = (finish - start) / 1000F;
		System.out.println("  Visited: " + states_visited + " states");
		System.out.println("  Best move: " + action + " Value: " + max);
		System.out.println("Elapsed time: " + time + " secs.");
		return action;
	}

	public int MaxValue(State s, Player p) {
		states_visited++;
		if (g.TERMINAL(s)) {
			return g.UTILITY(s, p);
		} 
		
		int max = Integer.MIN_VALUE;
		for (Action a : g.ACTIONS(s)) {
			max = Math.max(max, MinValue(g.RESULT(s, a), p));
		}
		return max;
	
	}

	int MinValue(State s, Player p) {
		states_visited++;
		if (g.TERMINAL(s)) {
			return g.UTILITY(s, p);
		} 
		
		int min = Integer.MAX_VALUE;
		for (Action a : g.ACTIONS(s)) {
			min = Math.min(min, MaxValue(g.RESULT(s, a), p));
		}
		return min;
	}
}
