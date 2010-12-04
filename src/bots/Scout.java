package bots;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Scout extends Bot {

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
		try {
			sprite = (BufferedImage) new ImageIcon(ImageIO.read(url)).getImage();
		} catch (IOException e) {
			/* fail */
		}
	}



}
