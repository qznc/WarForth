package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameBoard {
	private final List<Bot> bots = new ArrayList<Bot>();
	private final Map map;
	//private final Random rnd;
	private static final String default_prog = "" +
			"move! " +
			"360 randBounded turn! ";

	public GameBoard(Random rnd) {
		//this.rnd = rnd;

		Bot a = new Scout(default_prog, Faction.Red, rnd);
		a.setPosition(1500,1500);
		bots.add(a);

		Bot b = new Scout(default_prog, Faction.Blue, rnd);
		b.setPosition(1000,1000);
		bots.add(b);

		map = new Map(1000,1000);
	}

	public void paint(Graphics g, Component observer) {
		map.paint(g,observer);

		for (Bot bot : bots) {
			bot.paint(g, observer);
		}
	}

	public void turn() {
		for (Bot bot : bots) {
			bot.turn();
		}
	}
}
