package forth2.words;

import java.util.ArrayList;
import java.util.List;

import forth2.InterpreterState;

public class TopLevel extends UserDefinedWord {
	private String program;
	private int program_counter = 0;
	
	private List<String> word_strings = new ArrayList<String>();
	
	public TopLevel(String prog) {
		program = prog;
		while (program_counter < program.length()) {
			word_strings.add(readToken());
		}
		program = null; /* not needed anymore */
	}

	private String readToken() {
		int start = program_counter;
		while (program_counter < program.length()) {
			final char c = program.charAt(program_counter);
			switch (c) {
			case ' ' :
			case '\n':
			case '\r':
			case '\t':
				if (start != program_counter) {
					return program.substring(start, program_counter).trim();
				}
				start += 1;
				//$FALL-THROUGH$
			default:
				program_counter += 1;
			}
		}
		
		/* do not forget the last word */
		if (start < program_counter) {
			return program.substring(start, program_counter).trim();
		}
		
		assert false : "Never reached" ;
		return null;
	}
	
	@Override
	public Word getWord(InterpreterState state, int position) {
		if (position >= word_strings.size()) {
			return new Exit();
		}
		
		String tok = word_strings.get(position);
		for (Word w : state.dictionary) {
			if (w.name.equals(tok)) {
				return w;
			}
		}
		
		/* not in dictionary, assume integer */
		return new IntegerWord(tok);
	}
	
	@Override
	public void interpret(InterpreterState state) {
		throw new RuntimeException("Cannot interpret <TopLevel>!");
	}

	@Override
	public void compile(InterpreterState state) {
		throw new RuntimeException("Cannot compile <TopLevel>!");
	}

}
