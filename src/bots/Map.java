package bots;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Map {
	private static final int TILE_SIZE = 10;
	private final int width;
	private final int height;
	private final List<List<Ground>> tiles = new ArrayList<List<Ground>>();
	private BufferedImage offscreen;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;

		initImage();
	}

	public Map(String name) throws IOException {
		URL url = this.getClass().getResource("/resources/maps/"+name);
		assert (url != null);
		final BufferedImage map_img = (BufferedImage) new ImageIcon(ImageIO.read(url)).getImage();

		width = map_img.getWidth() * TILE_SIZE;
		height = map_img.getHeight() * TILE_SIZE;
		offscreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// TODO paint a nicer map!
		for (int x = 0; x < map_img.getWidth(); x++) {
			for (int y = 0; y < map_img.getWidth(); y++) {
				int pixel = map_img.getRGB(x, y);
				for (int i = 0; i < TILE_SIZE; i++) {
					for (int j = 0; j < TILE_SIZE; j++) {
						offscreen.setRGB(x*TILE_SIZE+i, y*TILE_SIZE+j, pixel);
					}
				}
			}
		}
	}

	private void initImage() {
		offscreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = offscreen.getGraphics();
        g.setColor(Color.getHSBColor(0.25f, 0.7f, 0.3f));
        g.fillRect(0,0,width,height);
	}

	public Ground get(final int x, final int y) {
		if (x < 0 || y < 0 || x > width || y > height) {
			return Ground.Void;
		}

		final int tileX = x / 10;
		final int tileY = y / 10;

		try {
			return tiles.get(tileX).get(tileY);
		} catch (IndexOutOfBoundsException e) {
			return Ground.Grass; /* default */
		}
	}

	public void paint(Graphics g, Component observer) {
		g.drawImage(offscreen, 0, 0, observer);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
