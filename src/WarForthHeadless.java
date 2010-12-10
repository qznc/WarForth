import game.Faction;
import game.GameMain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class WarForthHeadless {
	private static final String nl = System.getProperty("line.separator");
	private static final String USAGE = "" +
			"usage: <exec> <random seed> <red program path> <blue program path>";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameMain game = createGame(args);
		if (game == null) return;

		Faction winner = game.runNonthreaded();
		System.out.println("Winner: "+winner);
	}

	public static GameMain createGame(String[] args) {
		int random_seed = 1337;
		String red_prog = null;
		String blue_prog = null;

		if (args.length == 3) {
			random_seed = Integer.parseInt(args[0]);
			try {
				red_prog = readProg(args[1]);
				blue_prog = readProg(args[2]);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			System.out.println(USAGE);
			return null;
		}

		GameMain game;
		try {
			game = new GameMain(random_seed, red_prog, blue_prog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return game;
	}

	private static String readProg(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String output = "";
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				return output;
			}
			output += line + nl;
		}
	}

}
