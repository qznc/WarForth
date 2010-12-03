package forth2;

import java.util.List;

import forth2.words.Colon;
import forth2.words.Exit;
import forth2.words.IntegerWord;
import forth2.words.SemiColon;
import forth2.words.Word;

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

		dictionary.add(0, new Exit());
		dictionary.add(0, new Colon());
		dictionary.add(0, new SemiColon());
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
				state.stack.push(state.stack.remove(1));
			}
		});
		dictionary.add(0, new Word("over") {
			@Override
			public void interpret(InterpreterState state) {
				state.stack.push(state.stack.get(1));
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
				IntegerWord b = (IntegerWord) state.stack.pop();
				IntegerWord a = (IntegerWord) state.stack.pop();
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
				state.stack.push(state.stack.remove(3));
				state.stack.push(state.stack.remove(3));
			}
		});
		dictionary.add(0, new Word("?dup") {
			@Override
			public void interpret(InterpreterState state) {
				IntegerWord a = (IntegerWord) state.stack.get(0);
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
