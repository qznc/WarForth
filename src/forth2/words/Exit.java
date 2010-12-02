package forth2.words;

import forth2.InterpreterState;

public class Exit extends Word {
	public Exit() {
		super("exit", false);
	}

	@Override
	public void interpret(InterpreterState state) {
		state.running = false;
	}

	@Override
	public void compile(InterpreterState state) {
		// TODO Auto-generated method stub
		
	}
}