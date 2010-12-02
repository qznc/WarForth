package forth2.words;

import forth2.InterpreterState;

abstract public class Word {
	public String name;
	public boolean immediate;
	
	public Word() {
		name = " "; /* uncallable */
		immediate = false;
	}
	
	protected Word(String n, boolean imm) {
		name = n;
		immediate = imm;
	}
	
	abstract public void interpret(InterpreterState state);
	abstract public void compile(InterpreterState state);
	
	@Override
	public String toString() {
		return "<"+name+">";
	}
}
