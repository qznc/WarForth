package forth2.words;

import java.util.List;

import forth2.Frame;
import forth2.InterpreterState;

public final class Builtins {
	private Builtins() {
		/* utility class */
	}

	public static void fill(List<Word> dictionary) {
		integerArithmetic(dictionary);
		basicStackOperations(dictionary);
		debugging(dictionary);
		comparison(dictionary);
		bitwise(dictionary);

		interpreterControl(dictionary);
	}

	private static void interpreterControl(List<Word> dictionary) {
		dictionary.add(0, new Word(":") {
			@Override
			public void interpret(InterpreterState state) {
				/* read next token as procedure name */
				Frame fr = state.call_stack.peek();
				TopLevel tl = (TopLevel) fr.word;
				String n = tl.getToken(fr.position);

				state.next();

				state.toCompile = new UserDefinedWord(n);
			}

			@Override
			public void compile(InterpreterState state) {
				throw new RuntimeException("Cannot compile <:>!");
			}
		});

		dictionary.add(0, new Word(";") {
			@Override
			public void interpret(InterpreterState state) {
				state.call_stack.pop();
			}

			@Override
			public void compile(InterpreterState state) {
				/* add <;> to procedure as "return" */
				super.compile(state);

				/* add word to dictionary */
				state.dictionary.add(0,state.toCompile);

				/* stop compiling */
				state.toCompile = null;
			}
		});

		dictionary.add(0, new Word("'") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(state.getCurrent());
				state.next();
			}

			@Override
			public void compile(InterpreterState state) {
				interpret(state); /* same behavior */
			}
		});

		dictionary.add(0, new Word(",") {
			@Override
			public void interpret(InterpreterState state) {
				throw new RuntimeException("Cannot interpret <,>!");
			}

			@Override
			public void compile(InterpreterState state) {
				state.toCompile.content.add(state.stack.pop());
			}
		});

		dictionary.add(0, new Word("[") {
			@Override
			public void interpret(InterpreterState state) {
				throw new RuntimeException("Cannot interpret <[>!");
			}

			@Override
			public void compile(InterpreterState state) {
				state.stack.push(state.toCompile);
				state.toCompile = null; /* switch to interpretation */
			}
		});

		dictionary.add(0, new Word("]") {
			@Override
			public void interpret(InterpreterState state) {
				state.toCompile = (UserDefinedWord) state.stack.pop();
			}

			@Override
			public void compile(InterpreterState state) {
				throw new RuntimeException("Cannot compile <]>!");
			}
		});

		dictionary.add(0, new Word("branch") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord offset = (IntegerWord) state.getCurrent();
				state.next();

				Frame fr = state.call_stack.peek();
				fr.position += offset.value;
			}
		});

		dictionary.add(0, new Word("0branch") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord offset = (IntegerWord) state.stack.pop();
				state.next();

				IntegerWord a = (IntegerWord) state.stack.pop();

				Frame fr = state.call_stack.peek();
				if (a.value == 0) {
					fr.position += offset.value;
				}
			}
		});

		dictionary.add(0, new Exit());
	}

	private static void bitwise(List<Word> dictionary) {
		dictionary.add(0, new Word("and") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value & b.value));
			}
		});
		dictionary.add(0, new Word("or") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value | b.value));
			}
		});
		dictionary.add(0, new Word("xor") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value ^ b.value));
			}
		});
		dictionary.add(0, new Word("invert") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(~a.value));
			}
		});
	}

	private static void comparison(List<Word> dictionary) {
		dictionary.add(0, new Word("=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value == b.value));
			}
		});
		dictionary.add(0, new Word("<>") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value != b.value));
			}
		});
		dictionary.add(0, new Word(">") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value > b.value));
			}
		});
		dictionary.add(0, new Word("<") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value < b.value));
			}
		});
		dictionary.add(0, new Word(">=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value >= b.value));
			}
		});
		dictionary.add(0, new Word("<=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value <= b.value));
			}
		});dictionary.add(0, new Word("0=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value == 0));
			}
		});
		dictionary.add(0, new Word("0<>") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value != 0));
			}
		});
		dictionary.add(0, new Word("0>") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value > 0));
			}
		});
		dictionary.add(0, new Word("0<") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value < 0));
			}
		});
		dictionary.add(0, new Word("0>=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value >= 0));
			}
		});
		dictionary.add(0, new Word("0<=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value <= 0));
			}
		});
	}

	private static void debugging(List<Word> dictionary) {
		dictionary.add(0, new Word(".stack") {
			@Override
			public void interpret(InterpreterState state) {
				System.out.println(state.stack);
			}
		});
		dictionary.add(0, new Word(".dictionary") {
			@Override
			public void interpret(InterpreterState state) {
				System.out.println(state.dictionary);
			}
		});
		dictionary.add(0, new Word(".callstack") {
			@Override
			public void interpret(InterpreterState state) {
				System.out.println(state.call_stack);
			}
		});
	}

	private static void basicStackOperations(List<Word> dictionary) {
		dictionary.add(0, new Word("dup") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(state.stack.peek());
			}
		});
		dictionary.add(0, new Word("drop") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.pop();
			}
		});
		dictionary.add(0, new Word("swap") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				state.stack.push(state.stack.remove(len-2));
			}
		});
		dictionary.add(0, new Word("over") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				state.stack.push(state.stack.get(len-2));
			}
		});
		dictionary.add(0, new Word("rot") {
			@Override
			public void interpret(InterpreterState state) {
				/* a b c -- b c a */
				IntegerWord c = (IntegerWord) state.stack.pop();
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(b);
				state.stack.push(c);
				state.stack.push(a);
			}
		});
		dictionary.add(0, new Word("-rot") {
			@Override
			public void interpret(InterpreterState state) {
				/* a b c -- c a b */
				IntegerWord c = (IntegerWord) state.stack.pop();
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(c);
				state.stack.push(a);
				state.stack.push(b);
			}
		});
		dictionary.add(0, new Word("2dup") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				IntegerWord b = (IntegerWord) state.stack.get(len-1);
				IntegerWord a = (IntegerWord) state.stack.get(len-2);
				state.stack.push(a);
				state.stack.push(b);
			}
		});
		dictionary.add(0, new Word("2drop") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.pop();
				state.stack.pop();
			}
		});
		dictionary.add(0, new Word("2swap") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				state.stack.push(state.stack.remove(len-4));
				state.stack.push(state.stack.remove(len-4));
			}
		});
		dictionary.add(0, new Word("?dup") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				IntegerWord a = (IntegerWord) state.stack.get(len-1);
				if (a.value != 0) state.stack.push(a);
			}
		});
	}

	private static void integerArithmetic(List<Word> dictionary) {
		dictionary.add(0, new Word("+") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value + b.value));
			}
		});
		dictionary.add(0, new Word("-") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value - b.value));
			}
		});
		dictionary.add(0, new Word("/") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value / b.value));
			}
		});
		dictionary.add(0, new Word("%") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value % b.value));
			}
		});
		dictionary.add(0, new Word("*") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value * b.value));
			}
		});
		dictionary.add(0, new Word("1+") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value + 1));
			}
		});
		dictionary.add(0, new Word("1-") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value - 1));
			}
		});
	}
}
