package forth;

import forth.tokens.Colon;
import forth.tokens.SemiColon;
import forth.tokens.Symbol;

/**
 * This Forth interpreter MUST BE re-entrant, which means only process
 * one instruction/token per call to tick()!
 * 
 * @author beza1e1
 *
 */
public class Interpreter {
	public enum Modes {
		PROCESSING,
		PROCEDURE_DECLARATION,
		PROCEDURE_READING,
	}
	private InterpreterState state;
	public Modes mode = Modes.PROCESSING;

	public Interpreter(String program) {
		state = new InterpreterState(new Tokens(program));
	}
	
	public void tick() {
		if (state.token_position >= state.tokens.size()) {
			/* program finished already */
			return;
		}
		
		Token token = state.tokens.get(state.token_position);
		
		switch (mode) {
		case PROCESSING:
			if (token instanceof Colon) {
				mode = Modes.PROCEDURE_DECLARATION;
				state.token_position += 1;
			} else {
				token.process(state);
			}
			break;
		case PROCEDURE_DECLARATION:
			state.token_position += 1;
			((Symbol)token).setJumpTarget(state);
			mode = Modes.PROCEDURE_READING;
			break;
		case PROCEDURE_READING:
			assert !(token instanceof Colon) : "No declarations inside declarations for now";
			if (token instanceof SemiColon) {
				mode = Modes.PROCESSING;
			} /* else: do nothing */
			state.token_position += 1;
			break;
		}
		
		//System.out.println(state.stack);
		//System.out.println(state.token_position);
	}
	
	public void tick(int steps) {
		for (int i=0; i < steps; i++) tick();
	}
}

