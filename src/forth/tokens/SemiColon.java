package forth.tokens;

import forth.InterpreterState;
import forth.Token;

/* Ends a procedure definition */
public class SemiColon extends Token {
	public SemiColon() {
		/* nothing to do */
	}

	@Override
	public void process(InterpreterState state) {
		/* no nothing, interpreters job */
		throw new RuntimeException("Not to be called!");
	}

	@Override
	public String toString() {
		return "<;>";
	}
}
