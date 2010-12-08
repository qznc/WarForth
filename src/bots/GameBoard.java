package bots;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class GameBoard {
	private final List<ColoredActor> things = new ArrayList<ColoredActor>();
	private final Base red_base;
	private final Base blue_base;
	private final Map map;
	private final Random rnd;
	private Faction winner = Faction.Neutral;
	private final String red_prog;
	private final String blue_prog;

	public GameBoard(Random rnd, String red_prog, String blue_prog) throws IOException {
		this.red_prog = red_prog;
		this.blue_prog = blue_prog;
		this.rnd = rnd;

		red_base = new Base(Faction.Red);
		things.add(red_base);
		blue_base = new Base(Faction.Blue);
		things.add(blue_base);

		map = new Map("first.bmp");
		map.init(red_base, blue_base);
	}

	public void paint(Graphics g, Component observer) throws IOException {
		/* draw offscreen */
		Image img = map.cloneImage();
		Graphics ig = img.getGraphics();

		/* paint bases first, so units are on top */
		red_base.paint(ig, observer);
		blue_base.paint(ig, observer);

		for (ColoredActor a : things) {
			if (a.type == ActorType.Base) continue;
			a.paint(ig, observer);
		}

		drawResult(observer, img, ig);

		/* then paste everything at once */
		g.drawImage(img, 0, 0, observer);
	}

	private void drawResult(Component observer, Image img, Graphics ig) {
		String msg = "---";
		Color text_color = Color.WHITE;
		switch(winner) {
		case Blue:
			msg = "Blue Wins!";
			text_color = Color.BLUE;
			break;
		case Neutral:
			msg = "Nobody has won yet.";
			break;
		case Nobody:
			msg = "Tie!";
			break;
		case Red:
			msg = "Red Wins!";
			text_color = Color.RED;
			break;
		}
		if (winner != Faction.Neutral) {
			ig.setColor(text_color);
			ig.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
			ig.drawString(msg, img.getWidth(observer)/2 - 100, img.getHeight(observer)/2);
		}
	}

	/**
	 * @return	whether the game is finished
	 */
	public boolean turn() {
		checkWin();
		if (winner != Faction.Neutral) {
			return true;
		}

		/* shuffle order for fairness */
		Collections.shuffle(things, rnd);

		for (ColoredActor a : things) {
			if (a.type != ActorType.Bot) continue;
			final Bot bot = (Bot) a;
			bot.turn(map, things);
		}

		red_base.turn(this);
		blue_base.turn(this);

		removeDead();
		return false;
	}

	private void checkWin() {
		if (red_base.getHP() <= 0) {
			if (blue_base.getHP() <= 0) {
				winner = Faction.Nobody;
			} else {
				winner = Faction.Blue;
			}
		} else if (blue_base.getHP() <= 0) {
			winner = Faction.Red;
		}
	}

	private void removeDead() {
		List<ColoredActor> dead = new LinkedList<ColoredActor>();
		for (ColoredActor a : things) {
			if (a.getHP() <= 0) {
				dead.add(a);
			}
		}
		for (ColoredActor d : dead) {
			things.remove(d);
		}
	}

	public void createBot(Faction color, int x, int y) {
		String prog = null;
		if (color == Faction.Red) prog = red_prog;
		if (color == Faction.Blue) prog = blue_prog;
		ColoredActor b = new Scout(prog, color, rnd, map.getWidth(), map.getHeight());
		b.setPosition(x,y);
		things.add(b);
	}
}
