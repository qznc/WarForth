package bots;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Map {
	private final int width;
	private final int height;
	private final List<List<Ground>> tiles;
	private BufferedImage offscreen;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new ArrayList<List<Ground>>();

		initImage();
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
