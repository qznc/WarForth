package forth.words;

import forth.Frame;
import forth.InterpreterState;

public class UserDefinedImmediateWord extends UserDefinedWord {
	private final Word immediate_return = new Word("imm ;") {
		@Override
		public void interpret(InterpreterState state) {
			/* switch back into compilation mode */
			state.compiling = true;

			/* return, just like <;> does */
			state.call_stack.pop();
		}
	};

	public UserDefinedImmediateWord(String token) {
		super(token);
	}

	@Override
	public void add(Word word) {
		if (word.name.equals(";")) {
			content.add(immediate_return);
		} else { /* anything but <;> */
			content.add(word);
		}
	}

	@Override
	public void interpret(InterpreterState state) {
		throw new RuntimeException("Calling immediate word in interpret mode");
	}

	@Override
	public void compile(InterpreterState state) {
		/* switch into interpreting mode */
		state.compiling = false;

		/* push myself onto call stack */
		Frame frame = new Frame(this,0);
		state.call_stack.add(frame);
	}
}
