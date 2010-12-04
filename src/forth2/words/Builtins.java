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
		dictionary.add(0, new Colon(":") {
			@Override
			public void interpret(InterpreterState state) {
				state.toCompile = new UserDefinedWord(getNextToken(state));
				state.compiling = true;
			}
		});

		dictionary.add(0, new Colon(":imm") {
			@Override
			public void interpret(InterpreterState state) {
				state.toCompile = new UserDefinedImmediateWord(getNextToken(state));
				state.compiling = true;
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

				if (state.trace_debugging) {
					System.out.println("compiled "+state.toCompile);
				}

				/* stop compiling */
				state.compiling = false;
				state.toCompile = null; /* fail early! */
			}
		});

		dictionary.add(0, new Word("'") { /* TICK */
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(state.getCurrent());
				state.next();
			}
		});

		dictionary.add(0, new Word(",") { /* COMMA */
			@Override
			public void interpret(InterpreterState state) {
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
				state.compiling = false; /* switch to interpretation */
			}
		});

		dictionary.add(0, new Word("]") {
			@Override
			public void interpret(InterpreterState state) {
				state.compiling = true;
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

				Frame fr = state.call_stack.peek();
				fr.position += offset.value;
			}
		});

		dictionary.add(0, new Word("0branch") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord offset = (IntegerWord) state.getCurrent();

				IntegerWord a = (IntegerWord) state.stack.pop();

				Frame fr = state.call_stack.peek();
				if (a.value == 0) {
					fr.position += offset.value; /* jump */
				} else {
					state.next(); /* skip offset */
				}
			}
		});

		dictionary.add(0, new Word("FRAME-POSITION") {
			@Override
			public void interpret(InterpreterState state) {
				Frame fr = state.call_stack.peek();
				state.stack.push(new IntegerWord(fr.position));
			}
		});

		dictionary.add(0, new Word("COMPILE-POSITION") {
			@Override
			public void interpret(InterpreterState state) {
				int pos = state.toCompile.content.size();
				state.stack.push(new IntegerWord(pos));
			}
		});

		dictionary.add(0, new Word("!") { /* store inside the currently compiling word */
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord pos = (IntegerWord) state.stack.pop();
				Word w = state.stack.pop();

				UserDefinedWord function = state.toCompile;
				function.set(pos.value, w);
			}
		});

		dictionary.add(0, new Word("@") { /* load from the current word */
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord pos = (IntegerWord) state.stack.pop();

				Frame fr = state.call_stack.peek();
				UserDefinedWord function = (UserDefinedWord) fr.word;
				state.stack.push(function.get(pos.value));
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
		dictionary.add(0, new Word(".trace") {
			@Override
			public void interpret(InterpreterState state) {
				state.trace_debugging = true;
			}
		});
		dictionary.add(0, new Word(".notrace") {
			@Override
			public void interpret(InterpreterState state) {
				state.trace_debugging = false;
			}
		});

		dictionary.add(0, new Word(".code") {
			@Override
			public void interpret(InterpreterState state) {
				UserDefinedWord w = (UserDefinedWord) state.getCurrent();
				state.next();

				System.out.println("code of "+w+": "+w.content);
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
