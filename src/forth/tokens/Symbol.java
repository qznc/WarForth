package forth.tokens;

import forth.InterpreterState;
import forth.Token;

public class Symbol extends Token {
	private String symbol;
	private int jump_target = 0;
	
	public Symbol(String name) {
		this.symbol = name;
	}

	@Override
	public void process(InterpreterState state) {
		if (jump_target == 0) { /* just a symbol */
			state.stack.push(this);
		} else { /* actually a call */
			state.token_position = jump_target;
		}
	}

	@Override
	public String toString() {
		return "<"+symbol+">";
	}
	
	public void setJumpTarget(int position) {
		assert position > 0;
		jump_target = position;
	}
}
