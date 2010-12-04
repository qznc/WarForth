package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Map {
	private final int width;
	private final int height;
	private final List<List<Ground>> tiles;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new ArrayList<List<Ground>>();
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

	@SuppressWarnings("unused")
	public void paint(Graphics g, Component observer) {
		// nothing
	}
}
