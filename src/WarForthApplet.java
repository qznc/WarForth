

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import bots.GameMain;

public class WarForthApplet extends Applet {
	private static final long serialVersionUID = -4662920398000709556L;
	private GameMain game;
	private static final String default_prog = "" +
	":imm begin   COMPILE-POSITION ; " +
	":imm again   ' branch , COMPILE-POSITION - , ; " +
	":imm until   ' 0branch , COMPILE-POSITION - , ; " +
	": sleep 35 begin 1- dup 0= until ; " +
	": rotating begin sleep direction 1+ turn! again ; " +
	"move! " +
	"360 randBounded turn! " +
	"rotating ";

	@Override
	public void init() { /* applet loading */
		try {
			game = new GameMain(1337, default_prog, default_prog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
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
		game.stop();
	}

	@Override
	public void destroy() { /* applet is unloaded */
		game.destroy();
	}

	@Override
	public void update(Graphics g) {
		/* overriding update, prevents deletion of screen */
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			game.paint(g, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
