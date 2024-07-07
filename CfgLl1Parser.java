package csen1002.main.task8;

import java.util.*;

/**
 * Write your info here
 *
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

public class CfgLl1Parser {

	/**
	 * Constructs a Context Free Grammar
	 *
	 * @param cfg A formatted string representation of the CFG, the First sets of
	 * each right-hand side, and the Follow sets of each variable. The
	 * string representation follows the one in the task description
	 */
	ArrayList<String> variables;
	ArrayList<String> terminals;
	ArrayList<CFGRule> cfgRules;
	ArrayList<First> firstSets;
	ArrayList<Follow> followSets;

	public CfgLl1Parser(String input) {
		// TODO Auto-generated constructor stub
		String[] splitCfg = input.split("#");
		String[] splitVariables = splitCfg[0].split(";");
		String[] splitTerminals = splitCfg[1].split(";");
		String[] rules = splitCfg[2].split(";");
		String[] first = splitCfg[3].split(";");
		String[] follow = splitCfg[4].split(";");
		variables = new ArrayList<>();
		variables.addAll(Arrays.asList(splitVariables));
		terminals = new ArrayList<>();
		terminals.addAll(Arrays.asList(splitTerminals));
		cfgRules = cfgRuleConv(rules);
		firstSets = firstConv(first);
		followSets = followConv(follow);
	}

	public static void main(String[] args) {
		CfgLl1Parser cfgLl1Parser = new CfgLl1Parser("S;A;B#a;b;c;d#S/AaS,d;A/BbBaSc,e;B/e#S/ab,d;A/b,e;B/e#S/$c;A/a;B/ab");
		System.out.println(cfgLl1Parser.parse("abadcad"));
		System.out.println(cfgLl1Parser.parse("bbbd"));

		CfgLl1Parser cfgLl1Parser1 = new CfgLl1Parser("S;T#a;c;i#S/iST,e;T/cS,a#S/i,e;T/c,a#S/$ac;T/$ac");
		System.out.println(cfgLl1Parser1.parse("iiac"));
		System.out.println(cfgLl1Parser1.parse("iia"));
	}

	/**
	 * @param input The string to be parsed by the LL(1) CFG.
	 * @return A string encoding a left-most derivation.
	 */
	public String parse(String input) {
		// TODO Auto-generated method stub
		HashMap<String, String> table = constructTable(cfgRules, firstSets, followSets);
		String[] splitInput = input.split("");
		splitInput = Arrays.copyOf(splitInput, splitInput.length + 1);
		splitInput[splitInput.length - 1] = "$";
		Stack<Character> stack = new Stack<>();
		stack.add('$');
		stack.add(cfgRules.get(0).LHS.charAt(0));
		ArrayList<String> result = new ArrayList<>();
		result.add(stack.peek() + "");
		String prefix = "";

		for (String s : splitInput) {
			boolean found = false;
			while (!found) {
				char top = stack.peek();
				if (top != s.charAt(0)) {
					String key = stack.pop() + "," + s;
					String newIn = table.get(key);
					if (newIn == null) {
						result.add("ERROR");
						return String.join(";", result);
					}
					String[] splitNewIn = newIn.split("");
					for (int i = splitNewIn.length - 1; i >= 0; i--) {
						if (splitNewIn[i].charAt(0) != 'e') {
							stack.add(splitNewIn[i].charAt(0));
						}
					}
					result.add(prefix + printStack(stack));
				} else {
					char tmp = stack.pop();
					found = true;
					prefix = prefix + tmp;
				}
			}
		}
		return String.join(";", result);
	}

	public String printStack(Stack<Character> stack) {
		String result = "";
		for (int i = stack.size() - 1; i >= 1; i--) {
			result = result + stack.get(i);
		}
		return result;
	}

	public HashMap<String, String> constructTable(ArrayList<CFGRule> rules, ArrayList<First> first, ArrayList<Follow> follow) {
		HashMap<String, String> table = new HashMap<>();
		for (int i = 0; i < rules.size(); i++) {
			for (int j = 0; j < first.get(i).RHS.size(); j++) {
				for (String s : first.get(i).RHS.get(j)) {
					if (!s.equals("e"))
						table.put(rules.get(i).LHS + "," + s, rules.get(i).RHS.get(j));
					else {
						for (String followVar : follow.get(i).RHS) {
							table.put(rules.get(i).LHS + "," + followVar, "e");
						}
					}
				}
			}
		}
		return table;
	}

	public ArrayList<CFGRule> cfgRuleConv(String[] rules) {
		ArrayList<CFGRule> cfgRules = new ArrayList<>();
		for (String rule : rules) {
			String[] splitRule = rule.split("/");
			CFGRule cfgRule = new CFGRule(splitRule);
			cfgRules.add(cfgRule);
		}
		return cfgRules;
	}

	public ArrayList<First> firstConv(String[] rules) {
		ArrayList<First> cfgRules = new ArrayList<>();
		for (String rule : rules) {
			String[] splitRule = rule.split("/");
			First cfgRule = new First(splitRule);
			cfgRules.add(cfgRule);
		}
		return cfgRules;
	}

	public ArrayList<Follow> followConv(String[] rules) {
		ArrayList<Follow> cfgRules = new ArrayList<>();
		for (String rule : rules) {
			String[] splitRule = rule.split("/");
			Follow cfgRule = new Follow(splitRule);
			cfgRules.add(cfgRule);
		}
		return cfgRules;
	}

	public static class CFGRule {
		String LHS;
		ArrayList<String> RHS = new ArrayList<>();

		public CFGRule(String[] rules) {
			LHS = rules[0];
			String[] splitRHS = rules[1].split(",");
			Collections.addAll(RHS, splitRHS);
		}
	}

	public static class First {
		String LHS;
		ArrayList<String[]> RHS = new ArrayList<>();

		public First(String[] rules) {
			LHS = rules[0];
			String[] splitRHS = rules[1].split(",");
			for (String rhs : splitRHS) {
				RHS.add(rhs.split(""));
			}
		}
	}

	public static class Follow {
		String LHS;
		ArrayList<String> RHS = new ArrayList<>();

		public Follow(String[] rules) {
			LHS = rules[0];
			String[] splitRHS = rules[1].split("");
			Collections.addAll(RHS, splitRHS);
		}
	}

}
