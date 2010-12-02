package forth2.words;

import java.util.ArrayList;
import java.util.List;

import forth2.InterpreterState;

public class TopLevel extends UserDefinedWord {
	private final List<String> word_strings = new ArrayList<String>();

	public TopLevel(final String program) {
		splitIntoTokens(program);
	}

	private void splitIntoTokens(final String prog) {
		int position = 0;
		while (position < prog.length()) {
			int start = position;
			while (position < prog.length()) {
				final char c = prog.charAt(position);
				switch (c) {
				case ' ' :
				case '\n':
				case '\r':
				case '\t':
					if (start != position) {
						word_strings.add(prog.substring(start, position).trim());
					}
					start += 1;
					//$FALL-THROUGH$
				default:
					position += 1;
				}
			}

			/* do not forget the last word */
			if (start < position) {
				word_strings.add(prog.substring(start, position).trim());
			}
		}
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
