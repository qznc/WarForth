package forth2.words;

import forth2.InterpreterState;

public class SemiColon extends Word {
	public SemiColon() {
		super(";");
	}

	@Override
	public void interpret(InterpreterState state) {
		state.call_stack.pop();
	}

	@Override
	public void compile(InterpreterState state) {
		/* add <;> to procedure as "return" */
		super.compile(state);

		/* add word to dictionary */
		state.dictionary.add(0,state.toCompile);

		/* stop compiling */
		state.toCompile = null;
	}
}