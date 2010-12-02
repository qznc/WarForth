package forth2.words;

import java.util.List;

import forth2.Frame;
import forth2.InterpreterState;

public class UserDefinedWord extends Word {
	public List<Word> content;
	
	@SuppressWarnings("unused") /* state is used in TopLevel subclass */
	public Word getWord(InterpreterState state, int position) {
		return content.get(position);
	}

	@Override
	public void interpret(InterpreterState state) {
		Frame frame = new Frame(this,0);
		state.call_stack.add(frame);
	}

	@Override
	public void compile(InterpreterState state) {
		// TODO Auto-generated method stub

	}

}
