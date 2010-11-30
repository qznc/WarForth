package forth.tokens;

import forth.InterpreterState;
import forth.Token;

public class Minus extends Token {	
	public Minus() {
		/* nothing to do */
	}

	@Override
	public void process(InterpreterState state) {
		Number b = (Number) state.stack.pop();
		Number a = (Number) state.stack.pop();
		state.stack.push( new Number(a.number - b.number) );
		state.token_position += 1;
	}

	@Override
	public String toString() {
		return "<->";
	}
}
