package csen1002.main.task5;

import java.util.*;

/**
 * Write your info here
 *
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

public class CfgLeftRecElim {

    /**
     * Constructs a Context Free Grammar
     *
     * @param cfg A formatted string representation of the CFG. The string
     * representation follows the one in the task description
     */
    ArrayList<String> variables;
    String terminals;
    ArrayList<CFGRule> CFGRules;
    int varSize;

    public CfgLeftRecElim(String cfg) {
        // TODO Auto-generated constructor stub
        String[] splitCfg = cfg.split("#");
        String[] splitVariables = splitCfg[0].split(";");
        variables = new ArrayList<>();
        variables.addAll(Arrays.asList(splitVariables));
        varSize = variables.size();
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
        // TODO Auto-generated method stub
        String cfgString;
        String variablesString = String.join(";", variables);
        StringBuilder cfg = new StringBuilder();
        for (int i = 0; i < CFGRules.size(); i++) {
            CFGRule rule = CFGRules.get(i);
            cfg.append(rule.LHS).append("/").append(String.join(",", rule.RHS));
            if (i != CFGRules.size() - 1) {
                cfg.append(";");
            }
        }
        cfgString = variablesString + "#" + terminals + "#" + cfg;
        return cfgString;
    }

    /**
     * Eliminates Left Recursion from the grammar
     */
    public void eliminateLeftRecursion() {
        // TODO Auto-generated method stub
        boolean lRec;

        for (int i = 0; i < varSize; i++) {
            String variable = variables.get(i);
            CFGRule rule = CFGRules.get(i);
            lRec = false;
            for (int j = 0; j < i; j++) {
                ArrayList<String> newProductions = getNewProd(j, rule);
                rule.RHS = new ArrayList<>(newProductions);
            }

            ArrayList<String> updatedProd = new ArrayList<>(CFGRules.get(i).RHS);
            ArrayList<String> newProd = new ArrayList<>();
            ArrayList<String> oldProd = new ArrayList<>();
            for (String prod : updatedProd) {
                if (prod.startsWith(variable)) {
                    lRec = true;
                    newProd.add(prod.substring(1) + variable + "'");
                } else {
                    oldProd.add(prod + variable + "'");
                }
            }

            if (lRec) {
                rule.RHS = oldProd;
                newProd.add("e"); // Adding epsilon production
                CFGRules.add(new CFGRule(new String[]{variable + "'", String.join(",", newProd)}));
                variables.add(variable + "'");
            }
        }
    }

    public ArrayList<String> getNewProd(int j, CFGRule rule) {
        String prevVariable = variables.get(j);
        ArrayList<String> newProductions = new ArrayList<>();
        for (String production : rule.RHS) {
            if (production.startsWith(prevVariable)) {
                CFGRule prevRule = CFGRules.get(j);
                for (String prevProduction : prevRule.RHS) {
                    newProductions.add(prevProduction + production.substring(1));
                }
            } else {
                newProductions.add(production);
            }
        }
        return newProductions;
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
