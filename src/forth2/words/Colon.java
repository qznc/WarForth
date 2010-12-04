package forth2.words;

import forth2.Frame;
import forth2.InterpreterState;

/**
 * We need two different words for normal and immediate compilation,
 * therefore, this is only an abstract class.
 */
abstract public class Colon extends Word {
	public Colon(String name) {
		super(name);
	}

	@Override
	abstract public void interpret(InterpreterState state);

	protected String getNextToken(InterpreterState state) {
		/* read next token as procedure name */
		Frame fr = state.call_stack.peek();
		TopLevel tl = (TopLevel) fr.word;
		String n = tl.getToken(fr.position);

		state.next();
		return n;
	}

	@Override
	public void compile(InterpreterState state) {
		throw new RuntimeException("Cannot compile <:>!");
	}
}

