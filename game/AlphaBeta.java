package game;

public class AlphaBeta<State, Action, Player> implements Search<State, Action>{

	Game<State, Action, Player> g;
	int depth_limit;
	int states_visited;
	
	public AlphaBeta(Game<State, Action, Player> g, int d) {
		this.g = g;
		this.depth_limit = d;
	}

	
	@Override
	public Action chooseMove(State s) {
		states_visited = 0;
		long start = System.currentTimeMillis();
		int max = Integer.MIN_VALUE;
		Action action = null;
		Player p = g.getPlayerTurn(s);
		int depth = 1;
		for (Action a : g.ACTIONS(s)) {
			int value = MinValue(g.RESULT(s, a), p, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
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

	int MaxValue(State s, Player p, int depth, int alpha, int beta) {
		states_visited++;
		if (CUTOFF(depth)) {
			return g.heuristic(s);
		} else if (g.TERMINAL(s)) {
			return g.UTILITY(s, p);
		}
		
		depth++;
		
		int max = Integer.MIN_VALUE;

		for (Action a : g.ACTIONS(s)) {
			max = Math.max(max, MinValue(g.RESULT(s, a), p, depth, alpha, beta));
			if (max >= beta) { return max; }
			alpha = Math.max(alpha, max);
		}
		return max;
	
	}

	int MinValue(State s, Player p, int depth, int alpha, int beta) {
		states_visited++;
		if (CUTOFF(depth)) {
			return g.heuristic(s);
		} else if (g.TERMINAL(s)) {
			return g.UTILITY(s, p);
		}
		
		depth++;
		
		int min = Integer.MAX_VALUE;
		for (Action a : g.ACTIONS(s)) {
			min = Math.min(min, MaxValue(g.RESULT(s, a), p, depth, alpha, beta));
			if (min <= alpha) { return min; }
			beta = Math.min(beta, min);
		}
		return min;
	}
	
	boolean CUTOFF(int depth) {
		return (depth >= depth_limit);
	}
}