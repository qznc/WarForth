package game;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Tank extends Bot {

	public Tank(String program, Faction color, Random rnd, int maxX, int maxY, int x, int y) {
		super(program, color, rnd, maxX, maxY, x, y);
	}

	@Override
	public void paint(Graphics g, Component observer) throws IOException {
		if (sprite == null) loadSprite();
		super.paint(g, observer);
	}

	private void loadSprite() throws IOException {
		URL url = null;
		switch (color) {
		case Red:
			url = this.getClass().getResource("/resources/img/tank_red.png");
			break;
		case Blue:
			url = this.getClass().getResource("/resources/img/tank_blue.png");
			break;
		case Neutral:
		case Nobody:
			assert false : "tank has wrong color";
		}
		sprite = (BufferedImage) new ImageIcon(ImageIO.read(url)).getImage();
	}

	@Override
	protected double getSpeed(Ground ground) {
		switch (ground) {
		case Forest:
			return 0.85;
		case Grass:
			return 0.89;
		case Rocks:
			return 0.85;
		case Sand:
			return 0.6;
		case Swamp:
			return 0.5;
		case Unknown:
			return 0.0001;
		case Void:
			return 0.0001;
		case Water:
			return 0.5;
		default:
			throw new RuntimeException("Unknown Ground");
		}
	}

	@Override
	protected double getVisualRange(Ground ground) {
		switch (ground) {
		case Forest:
			return 0.3;
		case Grass:
			return 1.0;
		case Rocks:
			return 0.9;
		case Sand:
			return 1.0;
		case Swamp:
			return 0.9;
		case Water:
			return 0.8;
		case Unknown:
			return 0.0001;
		case Void:
			return 0.0001;
		default:
			throw new RuntimeException("Unknown Ground");
		}
	}

	@Override
	protected double getShootingRange(Ground ground) {
		switch (ground) {
		case Forest:
			return 0.4;
		case Grass:
			return 0.5;
		case Rocks:
			return 0.4;
		case Sand:
			return 0.5;
		case Swamp:
			return 0.5;
		case Water:
			return 0.5;
		case Unknown:
			return 0.0001;
		case Void:
			return 0.0001;
		default:
			throw new RuntimeException("Unknown Ground");
		}
	}

	@Override
	protected int getEnergyRefill() {
		return 20;
	}

	@Override
	protected int getDamage() {
		return 50;
	}

	@Override
	protected float getArmorModificator() {
		return 0.7f;
	}

	@Override
	public String toString() {
		return "<tank "+color+"-"+hashCode()+">";
	}

}
