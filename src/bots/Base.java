package bots;

import java.awt.Component;
import java.awt.Graphics;

public class Base extends ColoredActor {
	private static final int BUILD_COMPLETE = 1000;
	private int built = 0;

	public Base(Faction color) {
		super(ActorType.Base, color);
	}

	@Override
	public void paint(Graphics g, Component observer) {
//		TODO
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
