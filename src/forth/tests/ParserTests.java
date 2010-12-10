package forth.tests;

import org.junit.Test;

import forth.Interpreter;


public class ParserTests {
	@Test
	public void addA() {
		Interpreter ip = new Interpreter("1 2 +");
		ip.turn(10);
	}
	
	@Test
	public void addB() {
		Interpreter ip = new Interpreter("1 2 + ");
		ip.turn(10);
	}

	@Test
	public void addC() {
		Interpreter ip = new Interpreter(" 1 2 +");
		ip.turn(10);
	}

	@Test
	public void addD() {
		Interpreter ip = new Interpreter("  1   2    +   ");
		ip.turn(10);
	}
}
