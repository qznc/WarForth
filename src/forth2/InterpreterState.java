package forth2;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import forth2.words.UserDefinedWord;
import forth2.words.Word;

public class InterpreterState {
	public boolean compiling = false;
	public boolean running = true;
	public List<Word> dictionary = new LinkedList<Word>();
	public Stack<Word> stack = new Stack<Word>();
	public Stack<Frame> call_stack = new Stack<Frame>();
	public UserDefinedWord toCompile;

	public InterpreterState() {
		Builtins.fill(dictionary);
	}

	public Word getCurrent() {
		Frame frame = call_stack.peek();
		UserDefinedWord procedure = (UserDefinedWord) frame.word;
		return procedure.getWord(frame.position);
	}

	public void next() {
		call_stack.peek().position += 1;
	}

	public void tick() {
		if (!running) return;

		Word word = getCurrent();
		//System.out.println("current at "+call_stack.peek()+" is "+word);
		next();

		if (toCompile == null) {
			word.interpret(this);
		} else {
			word.compile(this);
		}

		//System.out.println(stack);
	}
}
