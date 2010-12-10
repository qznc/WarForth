package forth;

import forth.words.TopLevel;
import forth.words.Word;

public class Interpreter {
	private final InterpreterState state;

	public Interpreter(String program) {
		state = new InterpreterState();
		Frame frame = new Frame(new TopLevel(program, state), 0);
		state.call_stack.add(frame);
	}

	public void tick() {
		state.tick();
	}

	public void turn(int ticks) {
		for (int i=0; i < ticks; i++) {
			tick();
		}
	}

	public void injectWord(Word w) {
		state.insert(w);
	}
}
