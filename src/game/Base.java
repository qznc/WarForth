package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;

public class Base extends ColoredActor {
	private static final int BUILD_COMPLETE = 1000;
	private int built = 0;

	public Base(Faction color) {
		super(ActorType.Base, color);
	}

	@Override
	public void paint(Graphics g, Component observer) throws IOException {
		//super.paint(g, observer); sprite necessary!
		switch (color) {
		case Blue:
			g.setColor(Color.BLUE);
			break;
		case Red:
			g.setColor(Color.RED);
			break;
		default:
			break;
		}
		if (hp <= 0) g.setColor(Color.GRAY);
		final int radius = 8;
		g.fillOval(x/POSITION_SCALE - radius, y/POSITION_SCALE - radius, radius*2, radius*2);
	}

	public void turn(GameBoard board) {
		if (built < BUILD_COMPLETE) {
			built += 5; // TODO unit dependent?
			return;
		}

		board.createBot(color, x/POSITION_SCALE, y/POSITION_SCALE);
		built = 0;

		if (hp > 0) hp = Math.min(100, hp + 3);
	}

	@Override
	protected float getArmorModificator() {
		return 0.2f; /* heavily armored */
	}
}
