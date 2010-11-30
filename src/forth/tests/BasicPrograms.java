package forth.tests;

import org.junit.Test;

import forth.Interpreter;


public class BasicPrograms {

	@Test
	public void basicProgram() {
		String program = "10 2 +";
		Interpreter ip = new Interpreter(program);
		ip.tick(3);
	}


	@Test
	public void basicControlFlow() {
		String program = ": f + ; 1 2 3 f f";
		Interpreter ip = new Interpreter(program);
		ip.tick(13);
	}
}
