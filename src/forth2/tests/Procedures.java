package forth2.tests;

import junit.framework.Assert;

import org.junit.Test;

import forth2.Interpreter;
import forth2.InterpreterState;
import forth2.words.Word;


public class Procedures {
	@Test
	public void minimalProcedure() {
		Interpreter ip = new Interpreter(": two 2 ; two 2 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic1() {
		Interpreter ip = new Interpreter("2 3 + 1- dup * 4 / 4 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic2() {
		Interpreter ip = new Interpreter("0 2 3 2dup - * + -1 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic3() {
		Interpreter ip = new Interpreter("1 3 over + * 4 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic4() {
		Interpreter ip = new Interpreter("2 3 swap - 1 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic5() {
		Interpreter ip = new Interpreter("2 3 dup - * 0 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic6() {
		Interpreter ip = new Interpreter("1 2 drop 1 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic7() {
		Interpreter ip = new Interpreter("2 1 ?dup 2 2 - ?dup + 1 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void arithmetic8() {
		Interpreter ip = new Interpreter("1 2 3 4 2swap - -1 assertEqual - -1 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}


	@Test
	public void meta1() {
		Interpreter ip = new Interpreter(" ' dup ' dup assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	private void insertTestInstructions(Interpreter ip) {
		ip.injectWord(new Word("assertEqual") {
			@Override
			public void interpret(InterpreterState state) {
				Word b = state.stack.pop();
				Word a = state.stack.pop();
				Assert.assertEquals(b, a);
			}
		});
	}
}
