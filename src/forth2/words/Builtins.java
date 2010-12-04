package forth2.words;

import forth2.Frame;
import forth2.InterpreterState;

public final class Builtins {
	private Builtins() {
		/* utility class */
	}

	public static void fill(InterpreterState state) {
		integerArithmetic(state);
		basicStackOperations(state);
		debugging(state);
		comparison(state);
		bitwise(state);
		interpreterControl(state);
	}

	private static void interpreterControl(InterpreterState s) {
		s.insert(new Colon(":") {
			@Override
			public void interpret(InterpreterState state) {
				state.toCompile = new UserDefinedWord(getNextToken(state));
				state.compiling = true;
			}
		});

		s.insert(new Colon(":imm") {
			@Override
			public void interpret(InterpreterState state) {
				state.toCompile = new UserDefinedImmediateWord(getNextToken(state));
				state.compiling = true;
			}
		});

		s.insert(new Word(";") {
			@Override
			public void interpret(InterpreterState state) {
				state.call_stack.pop();
			}

			@Override
			public void compile(InterpreterState state) {
				/* add <;> to procedure as "return" */
				super.compile(state);

				/* add word to dictionary */
				state.insert(state.toCompile);

				if (state.trace_debugging) {
					System.out.println("compiled "+state.toCompile);
				}

				/* stop compiling */
				state.compiling = false;
				state.toCompile = null; /* fail early! */
			}
		});

		s.insert(new Word("'") { /* TICK */
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(state.getCurrent());
				state.next();
			}
		});

		s.insert(new Word(",") { /* COMMA */
			@Override
			public void interpret(InterpreterState state) {
				state.toCompile.content.add(state.stack.pop());
			}
		});

		s.insert(new Word("[") {
			@Override
			public void interpret(InterpreterState state) {
				throw new RuntimeException("Cannot interpret <[>!");
			}

			@Override
			public void compile(InterpreterState state) {
				state.compiling = false; /* switch to interpretation */
			}
		});

		s.insert(new Word("]") {
			@Override
			public void interpret(InterpreterState state) {
				state.compiling = true;
			}

			@Override
			public void compile(InterpreterState state) {
				throw new RuntimeException("Cannot compile <]>!");
			}
		});

		s.insert(new Word("branch") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord offset = (IntegerWord) state.getCurrent();

				Frame fr = state.call_stack.peek();
				fr.position += offset.value;
			}
		});

		s.insert(new Word("0branch") {
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

		s.insert(new Word("FRAME-POSITION") {
			@Override
			public void interpret(InterpreterState state) {
				Frame fr = state.call_stack.peek();
				state.stack.push(new IntegerWord(fr.position));
			}
		});

		s.insert(new Word("COMPILE-POSITION") {
			@Override
			public void interpret(InterpreterState state) {
				int pos = state.toCompile.content.size();
				state.stack.push(new IntegerWord(pos));
			}
		});

		s.insert(new Word("!") { /* store inside the currently compiling word */
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord pos = (IntegerWord) state.stack.pop();
				Word w = state.stack.pop();

				UserDefinedWord function = state.toCompile;
				function.set(pos.value, w);
			}
		});

		s.insert(new Word("@") { /* load from the current word */
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord pos = (IntegerWord) state.stack.pop();

				Frame fr = state.call_stack.peek();
				UserDefinedWord function = (UserDefinedWord) fr.word;
				state.stack.push(function.get(pos.value));
			}
		});

		s.insert(new Exit());
	}

	private static void bitwise(InterpreterState s) {
		s.insert(new Word("and") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value & b.value));
			}
		});
		s.insert(new Word("or") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value | b.value));
			}
		});
		s.insert(new Word("xor") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value ^ b.value));
			}
		});
		s.insert(new Word("invert") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(~a.value));
			}
		});
	}

	private static void comparison(InterpreterState s) {
		s.insert(new Word("=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value == b.value));
			}
		});
		s.insert(new Word("<>") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value != b.value));
			}
		});
		s.insert(new Word(">") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value > b.value));
			}
		});
		s.insert(new Word("<") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value < b.value));
			}
		});
		s.insert(new Word(">=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value >= b.value));
			}
		});
		s.insert(new Word("<=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value <= b.value));
			}
		});s.insert(new Word("0=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value == 0));
			}
		});
		s.insert(new Word("0<>") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value != 0));
			}
		});
		s.insert(new Word("0>") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value > 0));
			}
		});
		s.insert(new Word("0<") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value < 0));
			}
		});
		s.insert(new Word("0>=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value >= 0));
			}
		});
		s.insert(new Word("0<=") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value <= 0));
			}
		});
	}

	private static void debugging(InterpreterState s) {
		s.insert(new Word(".stack") {
			@Override
			public void interpret(InterpreterState state) {
				System.out.println(state.stack);
			}
		});
		s.insert(new Word(".callstack") {
			@Override
			public void interpret(InterpreterState state) {
				System.out.println(state.call_stack);
			}
		});
		s.insert(new Word(".dictionary") {
			@Override
			public void interpret(InterpreterState state) {
				System.out.println(state.dictionary);
			}
		});
		s.insert(new Word(".trace") {
			@Override
			public void interpret(InterpreterState state) {
				state.trace_debugging = true;
			}
		});
		s.insert(new Word(".notrace") {
			@Override
			public void interpret(InterpreterState state) {
				state.trace_debugging = false;
			}
		});

		s.insert(new Word(".code") {
			@Override
			public void interpret(InterpreterState state) {
				UserDefinedWord w = (UserDefinedWord) state.getCurrent();
				state.next();

				System.out.println("code of "+w+": "+w.content);
			}
		});
	}

	private static void basicStackOperations(InterpreterState s) {
		s.insert(new Word("dup") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(state.stack.peek());
			}
		});
		s.insert(new Word("drop") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.pop();
			}
		});
		s.insert(new Word("swap") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				state.stack.push(state.stack.remove(len-2));
			}
		});
		s.insert(new Word("over") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				state.stack.push(state.stack.get(len-2));
			}
		});
		s.insert(new Word("rot") {
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
		s.insert(new Word("-rot") {
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
		s.insert(new Word("2dup") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				IntegerWord b = (IntegerWord) state.stack.get(len-1);
				IntegerWord a = (IntegerWord) state.stack.get(len-2);
				state.stack.push(a);
				state.stack.push(b);
			}
		});
		s.insert(new Word("2drop") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.pop();
				state.stack.pop();
			}
		});
		s.insert(new Word("2swap") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				state.stack.push(state.stack.remove(len-4));
				state.stack.push(state.stack.remove(len-4));
			}
		});
		s.insert(new Word("?dup") {
			@Override
			public void interpret(InterpreterState state) {
				int len = state.stack.size();
				IntegerWord a = (IntegerWord) state.stack.get(len-1);
				if (a.value != 0) state.stack.push(a);
			}
		});
	}

	private static void integerArithmetic(InterpreterState s) {
		s.insert(new Word("+") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value + b.value));
			}
		});
		s.insert(new Word("-") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value - b.value));
			}
		});
		s.insert(new Word("/") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value / b.value));
			}
		});
		s.insert(new Word("%") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value % b.value));
			}
		});
		s.insert(new Word("*") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value * b.value));
			}
		});
		s.insert(new Word("1+") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value + 1));
			}
		});
		s.insert(new Word("1-") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.pop();
				state.stack.push(new IntegerWord(a.value - 1));
			}
		});
	}
}
