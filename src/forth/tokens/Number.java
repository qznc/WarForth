package forth.tokens;

import java.util.Stack;

import forth.InterpreterState;
import forth.Token;

public class Number extends Token {
	int number;
	
	public Number(int number) {
		this.number = number;
	}

	@Override
	public void process(InterpreterState state) {
		state.stack.push(this);
		state.token_position += 1;
	}

	@Override
	public String toString() {
		return "<"+Integer.toString(number)+">";
	}
}
