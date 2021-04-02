package game;

import java.util.Set;

public class Random<State, Action, Player> implements Search<State, Action> {

	Game<State, Action, Player> g;
	
	public Random(Game<State, Action, Player> g) {
		this.g = g;
	}
	
	@Override
	public Action chooseMove(State s) {
		long start = System.currentTimeMillis();

		Set<Action> applicable = g.ACTIONS(s);
		if (applicable.isEmpty()) {
			return null;
		}

		System.out.println("  " + applicable.size() + " legal actions.");
		int rand = (int) (Math.random() * applicable.size());
		@SuppressWarnings("unchecked")
		Action a = (Action) applicable.toArray()[rand];

		long finish = System.currentTimeMillis();
		float time = (finish - start) / 1000F;

		System.out.println("  I choose: " + a);
		System.out.println("Elapsed time: " + time + " secs.");

		return a;
	}
}
