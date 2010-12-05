package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class Actor {
	/** internally an actor is more acurate about its position */
	protected static final int POSITION_SCALE = 10;

	protected ActorType type;
	protected int x;
	protected int y;
	protected final Faction color;
	protected BufferedImage sprite;
	protected int direction; /* 0--360 degree */

	public Actor(ActorType kind, Faction color) {
		this.type = kind;
		this.color = color;
	}

	public abstract void setPosition(int x, int y);

	public void paint(Graphics g, Component observer) {
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
