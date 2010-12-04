package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Random;

import forth2.Interpreter;
import forth2.InterpreterState;
import forth2.words.IntegerWord;
import forth2.words.Word;

public abstract class Bot {
	private static final int TICKCOUNT = 100;
	private static final float SPEED = 10.0f;

	private final Interpreter interpreter;
	protected int x;
	protected int y;
	protected int direction; /* 0--360 degree */
	protected boolean moving;
	protected final Faction color;
	protected final Random rnd;

	public Bot(String program, Faction color, Random rnd) {
		this.color = color;
		this.rnd = rnd;

		interpreter = new Interpreter(program);
		injectWords();
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void turn() {
		interpreter.turn(TICKCOUNT);

		if (moving) {
			final int dx = (int) (Math.round(SPEED * Math.cos(direction / 360.0 * 2 * Math.PI)));
			final int dy = (int) (Math.round(SPEED * Math.sin(direction / 360.0 * 2 * Math.PI)));
			x += dx;
			y -= dy; /* minus, because (0,0) is top left */

			if (x < 0) x = 0;
			if (y < 0) y = 0;
		}
	}

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

	abstract public void paint(Graphics g, Component observer);
}
