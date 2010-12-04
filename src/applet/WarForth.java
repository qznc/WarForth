package applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

import bots.GameMain;

public class WarForth extends Applet {
	private static final long serialVersionUID = -4662920398000709556L;
	private GameMain game;

	@Override
	public void init() { /* applet loading */
		game = new GameMain(1337);
		game.setObserver(this);

		setBackground( Color.BLACK );
		setBounds(0, 0, 500, 500);
		setEnabled(true);
		setName("WarForth");
	}

	@Override
	public void start() { /* after applet is loaded */
		game.start(); /* new thread for game progress */

	}

	@Override
	public void stop() { /* when unloading the applet */

	}

	@Override
	public void destroy() { /* applet is unloaded */

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		game.paint(g, this);
	}
}
