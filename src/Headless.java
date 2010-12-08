import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import bots.Faction;
import bots.GameMain;


public class Headless {
	private static final String nl = System.getProperty("line.separator");
	private static final String USAGE = "" +
			"usage: <exec> <random seed> <red program path> <blue program path>";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
				return;
			}
		} else {
			System.out.println(USAGE);
			return;
		}

		GameMain game;
		try {
			game = new GameMain(random_seed, red_prog, blue_prog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		Faction winner = game.runNonthreaded();
		System.out.println("Winner: "+winner);
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
