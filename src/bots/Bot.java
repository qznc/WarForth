package bots;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import forth2.Interpreter;
import forth2.InterpreterState;
import forth2.words.IntegerWord;
import forth2.words.Word;

public abstract class Bot {
	private static final int POSITION_SCALE = 10;
	private static final int TICKCOUNT = 100;
	private static final double VRANGE_FACTOR = 80.0 * POSITION_SCALE;
	private static final double SRANGE_FACTOR = 120.0 * POSITION_SCALE;
	private static final double SPEED_FACTOR = 10.0;

	private final Interpreter interpreter;

	protected int x;
	protected int y;
	protected int direction; /* 0--360 degree */
	protected boolean moving;
	protected final Faction color;
	protected final Random rnd;
	protected BufferedImage sprite;
	private final int maxX;
	private final int maxY;
	private List<Bot> sightings;
	private Bot target;
	private int energy = 100;
	private int healthpoints = 100;
	private Bot display_shot;

	public Bot(String program, Faction color, Random rnd, int maxX, int maxY) {
		this.color = color;
		this.rnd = rnd;
		this.maxX = maxX * POSITION_SCALE;
		this.maxY = maxY * POSITION_SCALE;

		interpreter = new Interpreter(program);
		injectWords();
	}

	public void setPosition(int x, int y) {
		this.x = (x * POSITION_SCALE) % maxX;
		this.y = (y * POSITION_SCALE) % maxY;
	}

	public int getHP() {
		return healthpoints;
	}

	public void turn(Map map, List<Bot> bots) {
		interpreter.turn(TICKCOUNT);

		final Ground ground = map.get(x / POSITION_SCALE, y / POSITION_SCALE);

		updateSightings(bots, ground);

		if (target != null && energy >= 100) {
			shoot();
		}

		if (moving) {
			double speed = SPEED_FACTOR * getSpeed(ground);
			final int dx = (int) (Math.round(speed * Math.cos(Math.toRadians(direction))));
			final int dy = (int) (Math.round(speed * Math.sin(Math.toRadians(direction))));
			x += dx;
			y -= dy; /* minus, because (0,0) is top left */

			if (x < 0) x = 0;
			if (y < 0) y = 0;
			if (x >= maxX) x = maxX-1;
			if (y >= maxY) y = maxY-1;
		}

		healthpoints = Math.min(100, healthpoints + 5);
		energy = Math.min(100, energy + getEnergyRefill());
	}

	private void shoot() {
		energy = 0;
		target.damage(getDamage());
		display_shot = target;
	}

	private void damage(int damage) {
		healthpoints -= damage * getArmorModificator();
	}

	private void updateSightings(List<Bot> bots, final Ground ground) {
		sightings = new LinkedList<Bot>();
		target = null;
		double vrange = (VRANGE_FACTOR * getVisualRange(ground));
		double srange = (SRANGE_FACTOR * getShootingRange(ground));
		double min_range = srange;
		for (Bot b : bots) {
			double distance = distance(b, this);
			if (distance < vrange) {
				sightings.add(b);
			}
			if (distance < min_range && !b.color.equals(color)) {
				target = b;
				min_range = distance;
			}
		}
	}

	private static double distance(Bot a, Bot b) {
		int dx = Math.abs(a.x - b.x);
		int dy = Math.abs(a.y - b.y);
		return Math.sqrt(dx*dx + dy*dy);
	}

	protected abstract double getSpeed(Ground ground);
	protected abstract double getVisualRange(Ground ground);
	protected abstract double getShootingRange(Ground ground);
	protected abstract int getEnergyRefill();
	protected abstract int getDamage();
	protected abstract float getArmorModificator();

	private void injectWords() {
		interpreter.injectWord(new Word("move!") {
			@Override
			public void interpret(InterpreterState state) {
				moving = true;
			}
		});
		interpreter.injectWord(new Word("stop!") {
			@Override
			public void interpret(InterpreterState state) {
				moving = false;
			}
		});
		interpreter.injectWord(new Word("turn!") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				direction = a.value;
			}
		});
		interpreter.injectWord(new Word("direction") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(new IntegerWord(direction));
			}
		});
		interpreter.injectWord(new Word("color") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(new IntegerWord(color.ordinal()));
			}
		});
		interpreter.injectWord(new Word("rand") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(new IntegerWord(rnd.nextInt()));
			}
		});
		interpreter.injectWord(new Word("randBounded") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(rnd.nextInt(a.value)));
			}
		});
	}

	public void paint(Graphics g, Component observer) {
		BufferedImage rotated_img = rotate(sprite, direction);
		final int offsetX = (int) (rotated_img.getWidth() / 2.0f);
		final int offsetY = (int) (rotated_img.getHeight() / 2.0f);
		g.drawImage(rotated_img, (x/POSITION_SCALE)-offsetX, (y/POSITION_SCALE)-offsetY, observer);

		if (display_shot != null) {
			g.setColor(Color.WHITE);
			g.drawLine(x/POSITION_SCALE, y/POSITION_SCALE, display_shot.x/POSITION_SCALE, display_shot.y/POSITION_SCALE);
			display_shot = null;
		}
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
