package game;

public interface Search<State, Action> {

	Action chooseMove(State s);
    //Metrics getMetrics();

}
