import java.io.IOException;

import bots.Faction;
import bots.GameMain;


public class Headless {
	private static final String default_prog = "" +
	":imm begin   COMPILE-POSITION ; " +
	":imm again   ' branch , COMPILE-POSITION - , ; " +
	":imm until   ' 0branch , COMPILE-POSITION - , ; " +
	": sleep 35 begin 1- dup 0= until ; " +
	": rotating begin sleep direction 1+ turn! again ; " +
	"move! " +
	"360 randBounded turn! " +
	"rotating ";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameMain game;
		try {
			game = new GameMain(1337, default_prog, default_prog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		Faction winner = game.runNonthreaded();
		System.out.println("Winner: "+winner);
	}

}
