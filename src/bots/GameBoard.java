package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameBoard {
	private final List<Bot> bots = new ArrayList<Bot>();
	private final Map map;
	private final Random rnd;

	public GameBoard(Random rnd) {
		this.rnd = rnd;
		final int width = 1000;
		final int height = 1000;

		map = new Map(width, height);
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

	public void createBot(Faction color, String program, int x, int y) {
		Bot b = new Scout(program, color, rnd, map.getWidth(), map.getHeight());
		b.setPosition(x,y);
		bots.add(b);
	}
}
