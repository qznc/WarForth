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
		@SuppressWarnings("boxing")
		int pop = state.call_stack.pop();
		
		state.token_position = pop;
	}

	@Override
	public String toString() {
		return "<;>";
	}
}
