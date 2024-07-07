/**
 * Write your info here
 *
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

grammar Task7;

/**
 * This rule is to check your grammar using "ANTLR Preview"
 */
test: /* (Rule1 | Rule2 | ... | RuleN)+ */ (ONE | ZERO | ERROR)+ EOF; //Replace the non-fragment lexer rules here

// Write all the necessary lexer rules and fragment lexer rules here
ONE: BIT '1' '1' | '0' '0' '0';
ZERO: BIT BIT '0' | BIT '0' BIT;
ERROR: BIT BIT?;

fragment BIT: '0' | '1';
