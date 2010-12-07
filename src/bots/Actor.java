package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Actor {
	/** internally an actor is more acurate about its position */
	protected static final int POSITION_SCALE = 10;

	protected BufferedImage sprite;
	protected int direction; /* 0--360 degree */
	protected int x, y;

	public void setPosition(int x, int y) {
		this.x = x * POSITION_SCALE;
		this.y = y * POSITION_SCALE;
	}

	/**
	 * @throws IOException	in subclasses, if graphic data cannot be loaded
	 */
	public void paint(Graphics g, Component observer) throws IOException {
		BufferedImage rotated_img = rotate(sprite, direction);
		final int offsetX = (int) (rotated_img.getWidth() / 2.0f);
		final int offsetY = (int) (rotated_img.getHeight() / 2.0f);
		g.drawImage(rotated_img, (x/POSITION_SCALE)-offsetX, (y/POSITION_SCALE)-offsetY, observer);
	}

	public static BufferedImage rotate(BufferedImage img, int direction) {
		int dir = 360 - direction; /* transformation is backwards */
		AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(dir),
				img.getWidth() / 2.0,
				img.getHeight() / 2.0);
		BufferedImage rotatedImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
		g.setTransform(affineTransform);
		g.drawImage(img, 0, 0, null);
		return rotatedImage;
	}
}
