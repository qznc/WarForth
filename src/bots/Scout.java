package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Random;


public class Scout extends Bot {
	private static final int POSITION_SCALE = 10;
	private final Image img;

	public Scout(String program, Faction color, Random rnd) {
		super(program, color, rnd);

		URL url = null;
		switch (color) {
		case Red:
			url = this.getClass().getResource("/resources/img/scout_red.png");
			break;
		case Blue:
			url = this.getClass().getResource("/resources/img/scout_blue.png");
			break;
		case Neutral:
			assert false : "no neutral scouts";
		}
		assert (url != null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.createImage(url);
	}

	@Override
	public void paint(Graphics g, Component observer) {
		g.drawImage(img, x/POSITION_SCALE, y/POSITION_SCALE, observer);
	}

}
