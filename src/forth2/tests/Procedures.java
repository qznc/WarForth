package forth2.tests;

import org.junit.Test;

import forth2.Interpreter;


public class Procedures {
	@Test
	public void minimal() {
		Interpreter ip = new Interpreter(": two 2 ; two");
		ip.turn(10);
	}
}
