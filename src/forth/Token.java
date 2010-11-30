package forth;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import forth.tokens.Colon;
import forth.tokens.Div;
import forth.tokens.Minus;
import forth.tokens.Mod;
import forth.tokens.Mul;
import forth.tokens.Plus;
import forth.tokens.SemiColon;
import forth.tokens.Symbol;

abstract public class Token {	
	static private Map<String,Token> builtins = initBuiltins();
	
	abstract public void process(InterpreterState state);

	private static Map<String, Token> initBuiltins() {
		Map<String,Token> tokens = new HashMap<String, Token>();
		
		tokens.put("+", new Plus());
		tokens.put("-", new Minus());
		tokens.put("*", new Mul());
		tokens.put("/", new Div());
		tokens.put("%", new Mod());
		tokens.put(":", new Colon());
		tokens.put(";", new SemiColon());
		
		return tokens;
	}

	public static Token create(String name) {
		try {
			int num = Integer.parseInt(name);
			return new forth.tokens.Number(num);
		} catch (NumberFormatException e) {
			// that's ok, just use another token type
		}
		
		Token builtin = builtins.get(name);
		if (builtin != null) return builtin;
		
		/* then it is a symbol */
		return new Symbol(name);
	}
}
