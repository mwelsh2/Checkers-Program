import java.util.Scanner;

import java.util.Set;

import checkers.CAction;
import checkers.CPlayer.Color;
import checkers.CState;
import checkers.Checkers;

public class Run {
	static Scanner scanner = new Scanner(System.in);
	static String scan;

	// Checks to see if user wants to quit or restart the game
	public static void getInput() {
		scan = scanner.next();
		if (scan.equals("q")) {
			quitGame();
		} else if (scan.equals("r")) {
			restart();
		}
	}

	// If user enters an 'r'
	static void restart() {
		System.out.println("\n||||||||||||||||||\n|| Restarting...||" + "\n||||||||||||||||||\n");
		Checkers c = gameInit();
		playGame(c, c.s0);

	}

	// If user enters a 'q'
	static void quitGame() {
		System.out.println(
				"\n|||||||||||||||||||||\n|| Quitting Game..." + "||\n|||||||||||||||||||||\nThanks for playing!");
		System.exit(1);
	}

	// Initialize the game
	public static Checkers gameInit() {

		System.out.print("Choose your game:\n1. Small 4x4 Checkers\n" + "2. Standard 8x8 Checkers\nYour choice? ");

		getInput();
		while (!(scan.equals("1") || scan.equals("2"))) {
			System.out.print("Invalid choice. Try again. ");
			getInput();
		}

		int r_size = 0;
		int moves_limit = 0;
		if (scan.equals("1")) {
			r_size = 4;
			moves_limit = 10;
		} else if (scan.equals("2")) {
			r_size = 8;
			moves_limit = 50;
		}

		System.out.println("Choose your opponent:");
		System.out.print("1. An agent that plays randomly\n2. An agent that uses MINIMAX\n"
				+ "3. An agent that uses MINIMAX with alpha-beta pruning\n4. An agent that uses "
				+ "H-MINIMAX with a fixed depth cutoff\nYour choice? ");

		getInput();
		while (!(scan.equals("1") || scan.equals("2") || scan.equals("3") || scan.equals("4"))) {
			System.out.print("Invalid choice. Try again. ");
			getInput();
		}

		int search_method = Integer.parseInt(scan);
		int depth = 0;
		if (search_method == 3) {
			if (r_size > 4) {
				System.out.print("Depth Limit? ");
				getInput();
				while (depth == 0) {
					try {
						depth = Integer.parseInt(scan);
					} catch (Exception e) {
						System.out.println("Invalid input try again.");
						getInput();
					}
				}
			} else {
				depth = Integer.MAX_VALUE;
			}
		}

		if (search_method == 4) {
			System.out.print("Depth Limit? ");
			getInput();
			while (depth == 0) {
				try {
					depth = Integer.parseInt(scan);
				} catch (Exception e) {
					System.out.println("Invalid input try again.");
					getInput();
				}
			}
		}

		System.out.print("Do you want to play BLACK (b) or WHITE (w)? ");

		getInput();
		while (!(scan.equals("w") || scan.equals("b"))) {
			System.out.print("Invalid choice. Try again. ");
			getInput();
		}

		String str = scan;
		Color color = Color.WHITE;
		if (str.equals("w")) {
			System.out.println("You chose white");
		} else if (str.equals("b")) {
			System.out.println("You chose black");
			color = Color.BLACK;
		}

		Checkers c = new Checkers(r_size, color, moves_limit, search_method, depth);

		return c;
	}

	static CAction getPlayerMove(Checkers c, CState s) {

		getInput();
		while (scan.equals("?")) {
			CAction.printActionSet(c.ACTIONS(s));
			getInput();
		}

		CAction input_action = CAction.createAction(scan);
		if (input_action == null) {
			System.out.print("Invalid syntax. Try again (? for help): ");
			return getPlayerMove(c, s);
		}

		Set<CAction> applicable = c.ACTIONS(s);

		// if the input action is in the set of applicable actions
		if (input_action.isInActionSet(applicable)) {
			return input_action;
		} else {
			System.out.println("Invalid move. Try again (? for help): ");
			return getPlayerMove(c, s);
		}
	}

	static void winGame() {
		System.out.print("Congrats you won the game! Would you like to play again? " + "\nYes (y) or No (n) ? ");
		getInput();

		while (!(scan.equals("y") || scan.equals("n"))) {
			System.out.print("Invalid choice. Try again. ");
			getInput();
		}

		if (scan.equals("y")) {
			restart();
		} else {
			quitGame();
		}

	}

	static void loseGame() {
		System.out.print("Sorry, you lost the game! Would you like to play again? " + "\nYes (y) or No (n) ? ");
		getInput();

		while (!(scan.equals("y") || scan.equals("n"))) {
			System.out.print("Invalid choice. Try again. ");
			getInput();
		}

		if (scan.equals("y")) {
			restart();
		} else {
			quitGame();
		}

	}

	public static void tieGame() {
		System.out.print("Tie! Would you like to play again? " + "\nYes (y) or No (n) ? ");
		getInput();

		while (!(scan.equals("y") || scan.equals("n"))) {
			System.out.print("Invalid choice. Try again. ");
			getInput();
		}

		if (scan.equals("y")) {
			restart();
		} else {
			quitGame();
		}
	}

	public static void playGame(Checkers c, CState s) {

		while (!c.TERMINAL(s)) {
			System.out.println("\n======================");
			System.out.println("MOVES: " + s.moves);
			c.b.printBoard(s);
			System.out.println("Next to play: " + c.getPlayerTurn(s).color);

			if (c.getPlayerTurn(s) == Checkers.max) {
				System.out.println("I'm thinking...");
			} else {
				System.out.print("Your move (? for help): ");
			}

			CAction a = null;
			if (c.getPlayerTurn(s) == Checkers.min) {
				// if it's the player's turn
				a = getPlayerMove(c, s);
			} else {
				// if it's the computer's turn
				a = c.search.chooseMove(s);
			}

			System.out.println("ACTION: " + a);
			s = c.RESULT(s, a);

		}

		System.out.println("\n======================");
		System.out.println("MOVES: " + s.moves);
		c.b.printBoard(s);
		System.out.println("\n|||||||||||||||\n|| GAME OVER ||\n|||||||||||||||\n");

		switch (c.UTILITY(s, Checkers.max)) {
		case 1: {
			loseGame();
		}
		case -1: {
			winGame();
		}
		case 0: {
			tieGame();
		}
		}
	}

	public static void main(String args[]) {

		System.out
				.println("Checkers by Melissa Welsh.\nAt any point enter 'q'" + "to quit the game and 'r' to restart");

		Checkers c = gameInit();

		playGame(c, c.s0);

	}

}
