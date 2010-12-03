package bots;

import forth2.Interpreter;
import forth2.InterpreterState;
import forth2.words.IntegerWord;
import forth2.words.Word;

public class Bot {
	private static final int TICKCOUNT = 100;
	private static final float SPEED = 10.0f;

	private final Interpreter interpreter;
	int x;
	int y;
	int direction; /* 0--360 degree */
	boolean moving;

	public Bot(String program) {
		interpreter = new Interpreter(program);

		injectWords();
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void turn() {
		interpreter.turn(TICKCOUNT);

		x = (int) (SPEED * Math.round(Math.cos(x / 360.0 * 2 * Math.PI)));
		y = (int) (SPEED * Math.round(Math.sin(y / 360.0 * 2 * Math.PI)));
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
	}
}
