package csen1002.main.task4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Write your info here
 *
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

public class CfgEpsUnitElim {
    /**
     * Constructs a Context Free Grammar
     *
     * @param cfg A formatted string representation of the CFG. The string
     * representation follows the one in the task description
     */

    ArrayList<String> variables;
    String terminals;
    ArrayList<CFGRule> CFGRules;

    public CfgEpsUnitElim(String cfg) {
        // TODO Auto-generated constructor stub
        String[] splitCfg = cfg.split("#");
        String[] splitVariables = splitCfg[0].split(";");
        variables = new ArrayList<>();
        variables.addAll(Arrays.asList(splitVariables));
        terminals = splitCfg[1];
        String[] rules = splitCfg[2].split(";");
        CFGRules = CFGRuleConv(rules);
    }

    /**
     * @return Returns a formatted string representation of the CFG. The string
     * representation follows the one in the task description
     */

    @Override
    public String toString() {
        String cfgString;
        String variablesString = String.join(";", variables);
        StringBuilder cfg = new StringBuilder();
        CFGArraySorter(CFGRules);
        for (int i = 0; i < CFGRules.size(); i++) {
            CFGRule rule = CFGRules.get(i);
            cfg.append(rule.LHS.charAt(0)).append("/").append(String.join(",", rule.RHS));
            if (i != CFGRules.size() - 1) {
                cfg.append(";");
            }
        }
        cfgString = variablesString + "#" + terminals + "#" + cfg;
        return cfgString;
    }

    public void CFGArraySorter(ArrayList<CFGRule> rules) {
        for (CFGRule rule : rules) {
            Collections.sort(rule.RHS);
        }
    }

    /**
     * Eliminates Epsilon Rules from the grammar
     */

    public void eliminateEpsilonRules() {
        // TODO Auto-generated method stub
        ArrayList<Character> epsVars = new ArrayList<>();
        boolean epsilonExists = true;
        while (epsilonExists) {
            epsilonExists = false;
            for (int i = 0; i < CFGRules.size(); i++) {
                CFGRule rule = CFGRules.get(i);
                if (rule.RHS.contains("e")) {
                    rule.RHS.remove("e");
                    epsVars.add(rule.LHS.charAt(0));
                    for (CFGRule rule2 : CFGRules) {
                        for (int k = 0; k < rule2.RHS.size(); k++) {
                            String rhs = rule2.RHS.get(k);
                            if (rhs.contains(rule.LHS)) {
                                ArrayList<String> newRHSs = new ArrayList<>();
                                for (int l = 0; l < rhs.length(); l++) {
                                    if (rhs.charAt(l) == rule.LHS.charAt(0)) {
                                        String newRHS = rhs.substring(0, l) + rhs.substring(l + 1);
                                        newRHSs.add(newRHS);
                                    }
                                }
                                for (String newRHS : newRHSs) {
                                    if (!newRHS.isEmpty() && !rule2.RHS.contains(newRHS)) {
                                        rule2.RHS.add(newRHS);
                                    } else if (newRHS.isEmpty() && !epsVars.contains(rule2.LHS.charAt(0)) && !rule2.RHS.contains("e")) {
                                        rule2.RHS.add("e");
                                        epsilonExists = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Eliminates Unit Rules from the grammar
     */

    public void eliminateUnitRules() {
        // TODO Auto-generated method stub
        boolean updated;
        do {
            updated = false;
            ArrayList<CFGRule> rulesToProcess = CFGRules;
            for (CFGRule rule : rulesToProcess) {
                for (int j = rule.RHS.size() - 1; j >= 0; j--) {
                    String subLetter = rule.RHS.get(j);
                    if (variables.contains(subLetter)) {
                        if (rule.LHS.equals(subLetter)) {
                            rule.RHS.remove(j);
                            updated = true;
                        } else {
                            updated = replaceUnitSymbol(rule, subLetter, j) || updated;
                        }
                    }
                }
            }
        } while (updated);
    }

    private boolean replaceUnitSymbol(CFGRule rule, String symbol, int index) {
        boolean hasUnit = false;
        for (CFGRule replacementRule : CFGRules) {
            if (replacementRule.LHS.equals(symbol)) {
                rule.RHS.remove(index);
                for (String rhsSymbol : replacementRule.RHS) {
                    if (!rule.RHS.contains(rhsSymbol)) {
                        rule.RHS.add(rhsSymbol);
                        if (variables.contains(rhsSymbol) && !rule.LHS.equals(rhsSymbol)) {
                            hasUnit = true;
                        }
                    }
                }
                break;
            }
        }
        return hasUnit;
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
