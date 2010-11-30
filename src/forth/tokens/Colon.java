package forth.tokens;

import java.util.Stack;

import forth.InterpreterState;
import forth.Token;

/* Starts a procedure definition */
public class Colon extends Token {
	public Colon() {
		/* nothing to do */
	}

	@Override
	public void process(InterpreterState state) {
		/* no nothing, interpreters job */
		throw new RuntimeException("Not to be called!");
	}

	@Override
	public String toString() {
		return "<:>";
	}
}
