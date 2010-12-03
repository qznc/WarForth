package forth2.words;

import forth2.InterpreterState;

public class IntegerWord extends Word {
	public int value;

	public IntegerWord(String number) {
		value = Integer.parseInt(number);
	}

	public IntegerWord(int val) {
		value = val;
	}

	@Override
	public void interpret(InterpreterState state) {
		state.stack.push(this);
	}

	@Override
	public String toString() {
		return "<"+value+">";
	}
}
