package forth.words;

import forth.InterpreterState;

public class IntegerWord extends Word {
	public int value;

	public IntegerWord(String number) {
		value = Integer.parseInt(number);
	}

	public IntegerWord(int val) {
		value = val;
	}

	public IntegerWord(boolean b) {
		if (b) {
			value = 1;
		} else {
			value = 0;
		}
	}

	@Override
	public void interpret(InterpreterState state) {
		state.stack.push(this);
	}

	@Override
	public String toString() {
		return "<"+value+">";
	}

	@Override
	public boolean equals(Object o) {
		try {
			IntegerWord other = (IntegerWord) o;
			return other.value == value;
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return value;
	}
}
