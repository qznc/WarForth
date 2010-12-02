package forth2.words;

import forth2.InterpreterState;

public class IntegerWord extends Word {
	public int value;
	
	public IntegerWord(String number) {
		try {
			value = Integer.parseInt(number);
		} catch (NumberFormatException e) {
			value = 0;
		}
	}

	public IntegerWord(int val) {
		value = val;
	}

	@Override
	public void interpret(InterpreterState state) {
		state.stack.push(this);
	}

	@Override
	public void compile(InterpreterState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "<"+value+">";
	}
}
