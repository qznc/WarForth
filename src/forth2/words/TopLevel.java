package forth2.words;

import java.util.ArrayList;
import java.util.List;

import forth2.InterpreterState;

public class TopLevel extends UserDefinedWord {
	private final List<String> word_strings = new ArrayList<String>();
	private final InterpreterState state;

	public TopLevel(final String program, final InterpreterState st) {
		super("top level"); /* uncallable */
		state = st;
		splitIntoTokens(program);
		//System.out.println("tokens: "+word_strings);
	}

	private void splitIntoTokens(final String prog) {
		int position = 0;
		next_token:
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
						continue next_token;
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
	public Word get(int position) {
		if (position >= word_strings.size()) {
			return new Exit();
		}

		String tok = word_strings.get(position);
		try {
			return state.find(tok);
		} catch (RuntimeException e) {
			/* not in dictionary, assume integer */
			return new IntegerWord(tok);
		}
	}

	public String getToken(int position) {
		if (position >= word_strings.size()) {
			return "exit";
		}
		return word_strings.get(position);
	}

	@SuppressWarnings("hiding")
	@Override
	public void interpret(InterpreterState state) {
		throw new RuntimeException("Cannot interpret <TopLevel>!");
	}

	@SuppressWarnings("hiding")
	@Override
	public void compile(InterpreterState state) {
		throw new RuntimeException("Cannot compile <TopLevel>!");
	}

}
