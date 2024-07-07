package csen1002.main.task1;
import java.util.*;

/**
 * Write your info here
 *
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

public class RegExToNfa {
    /**
     * Constructs an NFA corresponding to a regular expression based on Thompson's
     * construction
     *
     * @param input The alphabet and the regular expression in postfix notation for
     * which the NFA is to be constructed
     */
    private final Set<Integer> states;
    private final String alphabet;
    private final List<String> transitions;
    private final NodesNFA finalNfa;

    public RegExToNfa(String input) {
        // TODO Auto-generated constructor stub
        String[] split = input.split("#");
        alphabet = split[0];
        String regex = split[1];

        states = new HashSet<Integer>();
        transitions = new ArrayList<String>();
        Stack<NodesNFA> stack = new Stack<NodesNFA>();

        NodesNFA top;
        NodesNFA bottom;
        int start;
        int end;
        int stateIncrement = 0;

        for (char c : regex.toCharArray()) {
            switch (c) {
                case '*':
                    top = stack.pop();
                    start = stateIncrement++;
                    end = stateIncrement++;
                    states.add(start);
                    states.add(end);
                    transitions.add(start + ",e," + top.startState);
                    transitions.add(start + ",e," + end);
                    transitions.add(top.endState + ",e," + top.startState);
                    transitions.add(top.endState + ",e," + end);
                    stack.push(new NodesNFA(start, end));
                    break;

                case '|':
                    top = stack.pop();
                    bottom = stack.pop();
                    start = stateIncrement++;
                    end = stateIncrement++;
                    states.add(start);
                    states.add(end);
                    transitions.add(start + ",e," + bottom.startState);
                    transitions.add(start + ",e," + top.startState);
                    transitions.add(bottom.endState + ",e," + end);
                    transitions.add(top.endState + ",e," + end);
                    stack.push(new NodesNFA(start, end));
                    break;

                case '.':
                    top = stack.pop();
                    bottom = stack.pop();
                    states.remove(top.startState);
                    List<String> transitionsToRemove = new ArrayList<>();
                    List<String> transitionsToAdd = new ArrayList<>();
                    for (String transition : transitions) {
                        String[] trans = transition.split(",");
                        if (Integer.parseInt(trans[0]) == top.startState) {
                            String newTransition = bottom.endState + "," + trans[1] + "," + trans[2];
                            transitionsToAdd.add(newTransition);
                            transitionsToRemove.add(transition);
                        }
                    }
                    transitions.removeAll(transitionsToRemove);
                    transitions.addAll(transitionsToAdd);
                    stack.push(new NodesNFA(bottom.startState, top.endState));
                    break;

                default:
                    start = stateIncrement++;
                    end = stateIncrement++;
                    states.add(start);
                    states.add(end);
                    transitions.add(start + "," + c + "," + end);
                    stack.push(new NodesNFA(start, end));
                    break;
            }
        }
        finalNfa = stack.pop();
    }

	Comparator<String> transitionComparator = new Comparator<String>() {
		public int compare(String transition1, String transition2) {
			String[] trans1 = transition1.split(",");
			String[] trans2 = transition2.split(",");
			int startState1 = Integer.parseInt(trans1[0]);
			int startState2 = Integer.parseInt(trans2[0]);
			int compareStartStates = Integer.compare(startState1, startState2);
			if (compareStartStates != 0) {
				return compareStartStates;
			}
			int endState1 = Integer.parseInt(trans1[2]);
			int endState2 = Integer.parseInt(trans2[2]);
			return Integer.compare(endState1, endState2);
		}
	};

    /**
     * @return Returns a formatted string representation of the NFA. The string
     * representation follows the one in the task description
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String stateStr="";
        for (Integer state : states) {
            if (stateStr.length() > 0) {
                stateStr+=";";
            }
            stateStr+=state+"";
        }
        transitions.sort(transitionComparator);
        String transitionsStr = String.join(";", transitions);
        String initialStateStr = finalNfa.startState + "";
        String AcceptedStateStr = finalNfa.endState + "";
        return stateStr + "#" + alphabet + "#" + transitionsStr + "#" + initialStateStr + "#" + AcceptedStateStr;
    }

    private static class NodesNFA {
        int startState;
        int endState;

        NodesNFA(int startState, int endState) {
            this.startState = startState;
            this.endState = endState;
        }
    }
}
