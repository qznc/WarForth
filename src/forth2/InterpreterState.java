package forth2;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import forth2.words.Exit;
import forth2.words.IntegerWord;
import forth2.words.UserDefinedWord;
import forth2.words.Word;

public class InterpreterState {
	public boolean compiling = false;
	public boolean running = true;
	public List<Word> dictionary = new LinkedList<Word>();
	public Stack<Word> stack = new Stack<Word>();
	public Stack<Frame> call_stack = new Stack<Frame>();
	
	public InterpreterState() {
		createBuiltins();
	}

	private void createBuiltins() {
		dictionary.add(0, new Word("+", false) {
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
	}

	public void tick() {
		if (!running) return;
		
		Frame frame = call_stack.peek();
		UserDefinedWord procedure = (UserDefinedWord) frame.word;
		Word word = procedure.getWord(this, frame.position);
		frame.position += 1;
		
		if (compiling) {
			word.compile(this);
		} else {
			word.interpret(this);
		}
		
		System.out.println(stack);
	}
}
