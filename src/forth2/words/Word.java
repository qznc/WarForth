package forth2.words;

import forth2.InterpreterState;

abstract public class Word {
	protected final String name;

	public Word() {
		name = " uncallable ";
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

	public boolean hasName(String n) {
		return n.equals(name);
	}
}
