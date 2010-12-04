package forth2.words;

import forth2.InterpreterState;

abstract public class Word {
	public String name;
	public boolean immediate;

	public Word() {
		name = " "; /* uncallable */
		immediate = false;
	}

	protected Word(String n) {
		name = n;
	}

	abstract public void interpret(InterpreterState state);

	public void compile(InterpreterState state) {
		assert (state.compiling);
		state.toCompile.add(this);
	}

	@Override
	public String toString() {
		return "<"+name+">";
	}
}
