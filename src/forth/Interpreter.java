package forth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import forth.InterpreterState.states;
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
	private InterpreterState state;

	public Interpreter(String program) {
		state = new InterpreterState(new Tokens(program));
	}
	
	public void tick() {
		Token token = state.tokens.get(state.token_position);
		
		switch (state.state) {
		case PROCESSING:
			if (token instanceof Colon) {
				state.state = states.PROCEDURE_DECLARATION;
				state.token_position += 1;
			} else {
				token.process(state);
			}
			break;
		case PROCEDURE_DECLARATION:
			((Symbol)token).setJumpTarget(state.token_position+1);
			state.token_position += 1;
			break;
		case PROCEDURE_READING:
			assert !(token instanceof Colon) : "No declarations inside declarations for now";
			if (token instanceof SemiColon) {
				state.state = states.PROCESSING;
			} /* else: do nothing */
			state.token_position += 1;
			break;
		}
		
		System.out.println(state.stack);
	}
	
	public void tick(int steps) {
		for (int i=0; i < steps; i++) tick();
	}
}
