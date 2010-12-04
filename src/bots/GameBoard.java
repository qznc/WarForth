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
			":imm begin   COMPILE-POSITION ; " +
			":imm again   ' branch , COMPILE-POSITION - , ; " +
			":imm until   ' 0branch , COMPILE-POSITION - , ; " +
			": sleep 10 begin 1- dup 0= until ; " +
			": rotating begin sleep direction 1+ turn! again ; " +
			"move! " +
			"360 randBounded turn! " +
			"rotating ";

	public GameBoard(Random rnd) {
		//this.rnd = rnd;
		final int width = 1000;
		final int height = 1000;

		map = new Map(width, height);

		Bot a = new Scout(default_prog, Faction.Red, rnd, 1000, 1000);
		a.setPosition(100,1000);
		bots.add(a);

		Bot b = new Scout(default_prog, Faction.Blue, rnd, 1000, 1000);
		b.setPosition(1000,800);
		bots.add(b);

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
