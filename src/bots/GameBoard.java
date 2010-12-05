package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
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
		/* draw offscreen */
		Image img = map.cloneImage();
		Graphics ig = img.getGraphics();
		for (Bot bot : bots) {
			bot.paint(ig, observer);
		}

		/* then paste everything at once */
		g.drawImage(img, 0, 0, observer);
	}

	public void turn() {
		for (Bot bot : bots) {
			bot.turn(map);
		}
	}

	public void createBot(Faction color, String program, int x, int y) throws IOException {
		Bot b = new Scout(program, color, rnd, map.getWidth(), map.getHeight());
		b.setPosition(x,y);
		bots.add(b);
	}
}
