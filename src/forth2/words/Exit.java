package forth2.words;

import forth2.InterpreterState;

public class Exit extends Word {
	public Exit() {
		super("exit");
	}

	@Override
	public void interpret(InterpreterState state) {
		state.running = false;
	}
}