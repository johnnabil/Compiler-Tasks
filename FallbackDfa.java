package csen1002.main.task3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

public class FallbackDfa {
	HashSet<Transitions> transitionsSet;
	HashSet<Integer> accStatesSet;
	int initState;
	ArrayList<String> output = new ArrayList<String>();


	/**
	 * Constructs a Fallback DFA
	 * 
	 * @param fdfa A formatted string representation of the Fallback DFA. The string
	 *             representation follows the one in the task description
	 */

	public FallbackDfa(String fdfa) {
		// TODO Auto-generated constructor stub
		String[] splitFdfa = fdfa.split("#");
		String[] transitions = splitFdfa[2].split(";");
		initState = Integer.parseInt(splitFdfa[3]);
		String[] accStates = splitFdfa[4].split(";");

		transitionsSet = new HashSet<Transitions>();
		for(String transition: transitions){
			transitionsSet.add(new Transitions(transition.split(",")));
		}

		accStatesSet = new HashSet<Integer>();
		for(String accState: accStates){
			accStatesSet.add(Integer.parseInt(accState));
		}
	}

	/**
	 * @param input The string to simulate by the FDFA.
	 * 
	 * @return Returns a formatted string representation of the list of tokens. The
	 *         string representation follows the one in the task description
	 */
	public String run(String input) {
		// TODO Auto-generated method stub
		int lPointer = 0;
		int rPointer = 0;
		boolean accepted = true;
		Stack<Integer> stack = new Stack<Integer>();
		char[] inputArr = new char[input.length()];
		for(int i = 0; i<input.length(); i++){
			inputArr[i] = input.charAt(i);
		}
		stack.add(initState);
		while(rPointer<inputArr.length && accepted) {
			while (lPointer < inputArr.length) {
				int currentState = stack.peek();
				int nextState = findNextState(currentState, inputArr[lPointer]);
				stack.add(nextState);
				lPointer++;
			}
			accepted = false;
			int startPop = stack.peek();
			while (lPointer >= rPointer) {
				lPointer--;
				int currentState = stack.pop();
				if (accStatesSet.contains(currentState)) {
					output.add(input.substring(rPointer, lPointer+1) + "," + currentState);
					lPointer++;
					rPointer = lPointer;
					accepted = true;
					stack.removeAllElements();
					stack.add(initState);
					break;
				}
			}
			if(!accepted) {
				output.add(input.substring(rPointer) + "," + startPop);
			}
		}
		return String.join(";", output);
	}

	public int findNextState(int startState, char transition){
		for (Transitions t : transitionsSet) {
			if (t.startState == startState && t.transitionLetter == transition) {
				return t.endState;
			}
		}
		return -1;
	}

	public static class Transitions{
		int startState;
		char transitionLetter;
		int endState;
		Transitions(String[] transArray) {
			for (int i = 0; i < transArray.length; i++) {
				startState = Integer.parseInt(transArray[0]);
				transitionLetter = transArray[1].charAt(0);
				endState = Integer.parseInt(transArray[2]);
			}
		}
	}
}
