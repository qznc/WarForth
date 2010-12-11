package forth.tests;

import junit.framework.Assert;

import org.junit.Test;

import forth.Interpreter;
import forth.InterpreterState;
import forth.words.Word;


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
	public void arithmetic9() {
		Interpreter ip = new Interpreter("10 10 = 1 assertEqual   10 20 = 0 assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void meta1() {
		Interpreter ip = new Interpreter(" ' dup ' dup assertEqual");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void branches() {
		Interpreter ip = new Interpreter(" " +
				" 3    branch 2 4   3 assertEqual " +
				" 3 0 0branch 2 4   3 assertEqual " +
				" 3 1 0branch 2 4   4 assertEqual ");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void ifthen() {
		Interpreter ip = new Interpreter(" " +
				":imm if    ' 0branch , COMPILE-POSITION 0 , ; " +
				":imm then  dup COMPILE-POSITION swap - swap ! ; " +
				": iftest   1 0 if 2 then ; " +
				"iftest 1 assertEqual ");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void ifthenelse() {
		Interpreter ip = new Interpreter(" " +
				":imm if    ' 0branch , COMPILE-POSITION 0 , ; " +
				":imm else  ' branch , COMPILE-POSITION 0 , " +
				"           swap dup COMPILE-POSITION swap - swap ! ; " +
				":imm then  dup COMPILE-POSITION swap - swap ! ; " +
				": iftestA   1 if 2 else 3 then ; " +
				"iftestA 2 assertEqual " +
				": iftestB   0 if 2 else 3 then ; " +
				"iftestB 3 assertEqual ");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void begin_until() {
		Interpreter ip = new Interpreter(" " +
				":imm begin   COMPILE-POSITION ; " +
				":imm until   ' 0branch , COMPILE-POSITION - , ; " +
				": looping 10 begin 1- dup 0= until ; " +
				"looping 0 assertEqual ");
		insertTestInstructions(ip);
		ip.turn(100);
	}

	@Test
	public void begin_again() { /* aka infinite loop */
		Interpreter ip = new Interpreter(" " +
				":imm begin   COMPILE-POSITION ; " +
				":imm again   ' branch , COMPILE-POSITION - , ; " +
				": looping 0 begin 1+ dup again ; " +
				"looping " +
				"0 1 assertEqual "); /* assert is never reached */
		insertTestInstructions(ip);
		ip.turn(200);
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
