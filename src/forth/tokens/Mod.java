package forth.tokens;

import java.util.Stack;

import forth.InterpreterState;
import forth.Token;

public class Mod extends Token {	
	public Mod() {
		/* nothing to do */
	}

	@Override
	public void process(InterpreterState state) {
		Number b = (Number) state.stack.pop();
		Number a = (Number) state.stack.pop();
		state.stack.push( new Number(a.number % b.number) );
	}

	@Override
	public String toString() {
		return "<%>";
	}
}
