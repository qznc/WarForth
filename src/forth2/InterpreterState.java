package forth2;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import forth2.words.Builtins;
import forth2.words.UserDefinedWord;
import forth2.words.Word;

public class InterpreterState {
	public boolean compiling = false;
	public boolean running = true;
	public boolean trace_debugging = false;
	public List<Word> dictionary = new LinkedList<Word>();
	public Stack<Word> stack = new Stack<Word>();
	public Stack<Frame> call_stack = new Stack<Frame>();
	public UserDefinedWord toCompile = null;

	public InterpreterState() {
		Builtins.fill(dictionary);
	}

	public Word getCurrent() {
		Frame frame = call_stack.peek();
		UserDefinedWord procedure = (UserDefinedWord) frame.word;
		return procedure.get(frame.position);
	}

	public void next() {
		call_stack.peek().position += 1;
	}

	public void tick() {
		if (!running) return;

		Word word = getCurrent();
		if (trace_debugging) {
			String mode = compiling ? "compiling" : "interpreting";
			System.out.println(mode+" "+call_stack.peek()+" is "+word+" with stack: "+stack);
		}
		next();

		if (compiling) {
			word.compile(this);
		} else {
			word.interpret(this);
		}

	}
}
