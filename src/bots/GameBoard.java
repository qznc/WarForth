package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameBoard {
	private final List<Bot> bots = new ArrayList<Bot>();
	private final Map map;
	private final Random rnd;

	public GameBoard(Random rnd) throws IOException {
		this.rnd = rnd;

		map = new Map("first.bmp");
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

	public void createBot(Faction color, String program, int x, int y) throws IOException {
		Bot b = new Scout(program, color, rnd, map.getWidth(), map.getHeight());
		b.setPosition(x,y);
		bots.add(b);
	}
}
