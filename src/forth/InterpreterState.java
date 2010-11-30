package forth;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class InterpreterState {
	public Tokens tokens;
	public int token_position = 0;
	public Stack<Token> stack = new Stack<Token>();
	public Map<String,Integer> jump_target = new HashMap<String, Integer>();
	public Stack<Integer> call_stack = new Stack<Integer>();
	
	public InterpreterState(Tokens toks) {
		tokens = toks;
	}
}
