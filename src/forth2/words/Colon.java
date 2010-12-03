package forth2.words;

import forth2.Frame;
import forth2.InterpreterState;

public class Colon extends Word {
	public Colon() {
		super(":");
	}

	@Override
	public void interpret(InterpreterState state) {
		/* read next token as procedure name */
		Frame fr = state.call_stack.peek();
		TopLevel tl = (TopLevel) fr.word;
		String n = tl.getToken(fr.position);

		state.next();

		state.toCompile = new UserDefinedWord(n);
	}

	@Override
	public void compile(InterpreterState state) {
		throw new RuntimeException("Cannot compile <:>!");
	}
}