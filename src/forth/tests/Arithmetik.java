package forth.tests;

import org.junit.Test;

import forth.Interpreter;


public class Arithmetik {

	@Test
	public void basicArithmetik() {
		String program = "1 2 + 3 - 4 * 5 / 6 %";
		Interpreter ip = new Interpreter(program);
		ip.tick(3);
	}

}
