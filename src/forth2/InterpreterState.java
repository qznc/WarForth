package forth2;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import forth2.words.Colon;
import forth2.words.Exit;
import forth2.words.IntegerWord;
import forth2.words.SemiColon;
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
		createBuiltins();
	}

	private void createBuiltins() {
		dictionary.add(0, new Word("+") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value + b.value));
			}

			@Override
			public void compile(InterpreterState state) {
				// TODO Auto-generated method stub

			}
		});

		dictionary.add(0, new Exit());
		dictionary.add(0, new Colon());
		dictionary.add(0, new SemiColon());
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
