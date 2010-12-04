package forth2.words;

import java.util.ArrayList;
import java.util.List;

import forth2.Frame;
import forth2.InterpreterState;

public class UserDefinedWord extends Word {
	public List<Word> content;

	public UserDefinedWord(String token) {
		super(token);
		content = new ArrayList<Word>();
	}

	public void add(Word word) {
		content.add(word);
	}

	public void set(int index, Word w) {
		//System.out.println("Set "+w+" at pos "+index+" in "+content);
		content.set(index, w);
	}

	public Word get(int position) {
		return content.get(position);
	}

	@Override
	public void interpret(InterpreterState state) {
		Frame frame = new Frame(this,0);
		state.call_stack.add(frame);
	}
}
