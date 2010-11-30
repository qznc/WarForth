package forth.tokens;

import forth.InterpreterState;
import forth.Token;

public class Symbol extends Token {
	private String symbol;
	
	public Symbol(String name) {
		this.symbol = name;
	}

	@Override
	public void process(InterpreterState state) {
		@SuppressWarnings("boxing")
		int target = state.jump_target.get(symbol);
		
		if (target == 0) { /* just a symbol */
			state.stack.push(this);
			state.token_position += 1;
		} else { /* actually a call */
			@SuppressWarnings("boxing")
			Integer i = state.token_position+1;
			
			state.call_stack.push(i);
			state.token_position = target;
		}
	}

	@Override
	public String toString() {
		return "<"+symbol+">";
	}

	public void setJumpTarget(InterpreterState state) {
		@SuppressWarnings("boxing")
		final Integer pos = state.token_position;
		
		state.jump_target.put(symbol, pos);		
	}
}
