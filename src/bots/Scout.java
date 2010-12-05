package bots;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Scout extends Bot {

	public Scout(String program, Faction color, Random rnd, int maxX, int maxY) throws IOException {
		super(program, color, rnd, maxX, maxY);

		URL url = null;
		switch (color) {
		case Red:
			url = this.getClass().getResource("/resources/img/scout_red.png");
			break;
		case Blue:
			url = this.getClass().getResource("/resources/img/scout_blue.png");
			break;
		case Neutral:
		case Nobody:
			assert false : "scout has wrong color";
		}
		sprite = (BufferedImage) new ImageIcon(ImageIO.read(url)).getImage();
	}

	@Override
	protected double getSpeed(Ground ground) {
		switch (ground) {
		case Forest:
			return 0.9;
		case Grass:
			return 1.5;
		case Rocks:
			return 0.9;
		case Sand:
			return 0.9;
		case Swamp:
			return 0.8;
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
			return 0.5;
		case Grass:
			return 2.0;
		case Rocks:
			return 1.5;
		case Sand:
			return 2.0;
		case Swamp:
			return 1.2;
		case Water:
			return 1.5;
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
			return 0.3;
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
		return 50;
	}

	@Override
	protected int getDamage() {
		return 30;
	}

	@Override
	protected float getArmorModificator() {
		return 1.0f;
	}

	@Override
	public String toString() {
		return "<scout "+color+"-"+hashCode()+">";
	}

}
