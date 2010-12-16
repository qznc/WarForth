

import game.GameMain;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class WarForthApplet extends Applet {
	private static final long serialVersionUID = -4662920398000709556L;
	private GameMain game;

	@Override
	public void init() { /* applet loading */
		String prog;
		try {
			prog = loadDefault();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		try {
			game = new GameMain(1337, prog, prog);
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

	private String loadDefault() throws IOException {
		InputStream stream = this.getClass().getResourceAsStream("/resources/programs/ghengis.wf");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuffer buf = new StringBuffer();

		while (reader.ready()) {
			buf.append(reader.readLine());
			buf.append("\n");
		}
		return buf.toString();
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
