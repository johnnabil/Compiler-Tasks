package csen1002.main.task6;

import java.util.*;

/**
 * Write your info here
 *
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

public class CfgFirstFollow {

    /**
     * Constructs a Context Free Grammar
     *
     * @param cfg A formatted string representation of the CFG. The string
     * representation follows the one in the task description
     */
    ArrayList<String> variables;
    ArrayList<String> terminals;
    ArrayList<CFGRule> CFGRules;
    HashMap<Character, ArrayList<String>> firstSet = new HashMap<>();
    HashMap<Character, ArrayList<String>> followSet = new HashMap<>();

    public CfgFirstFollow(String cfg) {
        // TODO Auto-generated constructor stub
        String[] splitCfg = cfg.split("#");
        String[] splitVariables = splitCfg[0].split(";");
        String[] splitTerminals = splitCfg[1].split(";");
        String[] rules = splitCfg[2].split(";");
        variables = new ArrayList<>();
        variables.addAll(Arrays.asList(splitVariables));
        terminals = new ArrayList<>();
        terminals.addAll(Arrays.asList(splitTerminals));
        CFGRules = CFGRuleConv(rules);
    }

    /**
     * Calculates the First Set of each variable in the CFG.
     *
     * @return A string representation of the First of each variable in the CFG,
     * formatted as specified in the task description.
     */
    public String first() {
        // TODO Auto-generated method stub
        for (String terminal : terminals) {
            ArrayList<String> ter = new ArrayList<>();
            ter.add(terminal);
            firstSet.put(terminal.charAt(0), ter);
        }
        for (String variable : variables) {
            firstSet.put(variable.charAt(0), new ArrayList<>());
        }

        ArrayList<Character> epsilonProducingVariables = new ArrayList<>();
        boolean change = true;
        while (change) {
            change = false;
            for (CFGRule rule : CFGRules) {
                ArrayList<String> firstLHS = firstSet.get(rule.LHS.charAt(0));
                for (String rhs : rule.RHS) {
                    if (rhs.equals("e")) {
                        if (!firstLHS.contains("e")) {
                            firstLHS.add("e");
                            epsilonProducingVariables.add(rule.LHS.charAt(0));
                            change = true;
                        }
                    } else {
                        boolean epsFlag = true;
                        for (char symbol : rhs.toCharArray()) {
                            if (variables.contains(symbol + "")) {
                                ArrayList<String> firstRHS = firstSet.get(symbol);
                                for (String symbolFirst : firstRHS) {
                                    if (!symbolFirst.equals("e") && !firstLHS.contains(symbolFirst)) {
                                        firstLHS.add(symbolFirst);
                                        change = true;
                                    }
                                }
                                if (!epsilonProducingVariables.contains(symbol)) {
                                    epsFlag = false;
                                    break;
                                }
                            } else {
                                if (!firstLHS.contains(symbol + "")) {
                                    firstLHS.add(symbol + "");
                                    change = true;
                                }
                                epsFlag = false;
                                break;
                            }
                        }
                        if (epsFlag && !firstLHS.contains("e")) {
                            firstLHS.add("e");
                            epsilonProducingVariables.add(rule.LHS.charAt(0));
                            change = true;
                        }
                    }
                }
            }
        }
        return FToString(firstSet);
    }

    /**
     * Calculates the Follow Set of each variable in the CFG.
     *
     * @return A string representation of the Follow of each variable in the CFG,
     * formatted as specified in the task description.
     */
    public String follow() {
        // TODO Auto-generated method stub
        first();
        for (String variable : variables) {
            followSet.put(variable.charAt(0), new ArrayList<>());
        }

        followSet.get(variables.get(0).charAt(0)).add("$");
        boolean change = true;
        while (change) {
            change = false;
            for (CFGRule rule : CFGRules) {
                for (String rhs : rule.RHS) {
                    for (int j = 0; j < rhs.length(); j++) {
                        char symbol = rhs.charAt(j);
                        if (variables.contains(symbol + "")) {
                            ArrayList<String> followSymbol = followSet.get(symbol);
                            if (j == rhs.length() - 1) {
                                ArrayList<String> followLHS = followSet.get(rule.LHS.charAt(0));
                                for (String follow : followLHS) {
                                    if (!followSymbol.contains(follow)) {
                                        followSymbol.add(follow);
                                        change = true;
                                    }
                                }
                            } else {
                                boolean epsFlag = true;
                                for (int k = j + 1; k < rhs.length(); k++) {
                                    char nextSymbol = rhs.charAt(k);
                                    ArrayList<String> firstNextSymbol = firstSet.get(nextSymbol);
                                    for (String first : firstNextSymbol) {
                                        if (!first.equals("e") && !followSymbol.contains(first)) {
                                            followSymbol.add(first);
                                            change = true;
                                        }
                                    }
                                    if (!firstNextSymbol.contains("e")) {
                                        epsFlag = false;
                                        break;
                                    }
                                }
                                if (epsFlag) {
                                    ArrayList<String> followLHS = followSet.get(rule.LHS.charAt(0));
                                    for (String follow : followLHS) {
                                        if (!followSymbol.contains(follow)) {
                                            followSymbol.add(follow);
                                            change = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return FToString(followSet);
    }

    public String FToString(HashMap<Character, ArrayList<String>> set) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<variables.size(); i++) {
            result.append(variables.get(i)).append("/");
            ArrayList<String> firstOrFollow = set.get(variables.get(i).charAt(0));
            Collections.sort(firstOrFollow);
            for (String s : firstOrFollow) {
                result.append(s);
            }
            if(i<variables.size()-1) {
                result.append(";");
            }
        }
        return result.toString();
    }

    public ArrayList<CFGRule> CFGRuleConv(String[] rules) {
        ArrayList<CFGRule> cfgRules = new ArrayList<>();
        for (String rule : rules) {
            String[] splitRule = rule.split("/");
            CFGRule cfgRule = new CFGRule(splitRule);
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
}
