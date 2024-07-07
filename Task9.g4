/**
 * Write your info here
 *
 * @name John Nabil
 * @id 49-0694
 * @labNumber 18
 */

grammar Task9;

@members {
	/**
	 * Compares two integer numbers
	 *
	 * @param x the first number to compare
	 * @param y the second number to compare
	 * @return 1 if x is equal to y, and 0 otherwise
	 */
	public static int equals(int x, int y) {
	    return x == y ? 1 : 0;
	}
}

s returns [int check]: f {$check = $f.check*$f.m;};
 // Write the definition of parser rule "s" here

 // Write additional lexer and parser rules here
f returns [int check, int m]: d[1,1] t[2,$d.l]
{$check=$d.check*$t.check; $m=$t.m;};

t[int r, int l] returns[int check, int m] : '#' n[$r, 1, $l]
{$check=$n.check; $m=$n.m;}
 | {$check=1; $m=1;};

n[int r, int c, int l] returns [int check, int m] : d[$r, 1] t[$r+1, $l]
{$check=$d.check*$t.check; $m=equals($d.l,$l)*$t.m; $c=1;};

d[int r, int c] returns [int l, int check] : '0' d1=d[$r, $c+1]
{$l =$d1.l; $check= (1-equals($c, $r))*$d1.check;}
 | '1' d1=d[$r, $c+1]
 {$l=$d1.l; $check=equals($c, $r)*$d1.check;}
 | '0'
 {$l=$c; $check= 1- equals($c, $r);}
 | '1' {$l=$c; $check=equals($c, $r);};
