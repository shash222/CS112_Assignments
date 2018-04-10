package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	int length=expr.length();
    	for (int i=0;i<length;i++) {//removes spaces from string
    		if (expr.charAt(i)==' ') {
    			expr=expr.substring(0, i)+expr.substring(i+1);
    			length--;
    		}
    	}
    	boolean repeat=false;
    	String name="";
    	for (int i=0;i<length;i++) {
    		name="";
    		if (Character.toUpperCase(expr.charAt(i))>=65&&Character.toUpperCase(expr.charAt(i))<=90) {
    			while (Character.toUpperCase(expr.charAt(i))>=65&&Character.toUpperCase(expr.charAt(i))<=90){
    				name+=expr.charAt(i);
    				if (i>=length-1) {
    					break;
    				}
    				i++;
    			}
        		if (expr.charAt(i)=='[') {
        			for (int j=0;j<arrays.size();j++) {
        				if (arrays.get(j).name.equals(name)) {
        					repeat=true;
        					break;
        				}
        			}
        			if (repeat==false) {
        				arrays.add(new Array(name));
        			}
        		}
        		else {
        			for (int j=0;j<vars.size();j++) {
        				if (vars.get(j).name.equals(name)) {
        					repeat=true;
        					break;
        				}
        			}
        			if (repeat==false) {
            			vars.add(new Variable(name));
        			}
        		}
    		}
    		repeat=false;
    	}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    
    private static String divide(String expr){
    	String sum="";
    	int j=1;
    	int k=1;
    	int length=expr.length();
    	for (int i=0;i<length;i++) {
    		j=1;
    		k=1;
    		if (i<expr.length()-1&&expr.charAt(i)=='/') {
        		while ((expr.charAt(i-j)>=48&&expr.charAt(i-j)<=57)||expr.charAt(i-j)==46) {
        			j++;
        			if (i<j) {
        				break;
        			}
        		}
        		while (expr.charAt(i+k)>=48&&expr.charAt(i+k)<=57||expr.charAt(i+k)==46) {
        			if (i+k>expr.length()-2) {
        				break;
        			}
        			k++;
        		}
        		j--;
        		if (expr.substring(i+k).length()==1) {
        			sum=Float.toString((Float.parseFloat(expr.substring(i-j,i)))/(Float.parseFloat(expr.substring(i+1,i+k+1))));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k+1);
        		}
        		else {
            		sum=Float.toString(Float.parseFloat(expr.substring(i-j,i))/Float.parseFloat(expr.substring(i+1,i+k)));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k);
        		}
        		length-=sum.length();
    			i=0;
    		}
    	}
    	return expr;
    }
    
    private static String multiply(String expr){
    	String sum="";
    	int j=1;
    	int k=1;
    	int length=expr.length();
    	for (int i=0;i<length;i++) {
    		j=1;
    		k=1;
    		if (i<expr.length()-1&&expr.charAt(i)=='*') {
        		while ((expr.charAt(i-j)>=48&&expr.charAt(i-j)<=57)||expr.charAt(i-j)==46) {
        			j++;
        			if (i<j) {
        				break;
        			}
        		}
        		while (expr.charAt(i+k)>=46&&expr.charAt(i+k)<=57||expr.charAt(i+k)==46) {
        			if (i+k>expr.length()-2) {
        				break;
        			}
        			k++;
        		}
        		j--;
        		if (expr.substring(i+k).length()==1) {
        			sum=Float.toString(Float.parseFloat(expr.substring(i-j,i))*Float.parseFloat(expr.substring(i+1,i+k+1)));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k+1);
        		}
        		else {
            		sum=Float.toString(Float.parseFloat(expr.substring(i-j,i))*Float.parseFloat(expr.substring(i+1,i+k)));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k);
        		}
        		length-=sum.length();
    			i=0;
    		}
    	}
    	return expr;
    }
    
    private static String subtract(String expr){
    	String sum="";
    	int j=1;
    	int k=1;
    	int length=expr.length();
    	for (int i=0;i<length;i++) {
    		j=1;
    		k=1;
    		if (i<expr.length()-1&&expr.charAt(i)=='-'&&i!=0) {
        		while ((expr.charAt(i-j)>=48&&expr.charAt(i-j)<=57)||expr.charAt(i-j)==46) {
        			j++;
        			if (i<j) {
        				break;
        			}
        		}
        		while (expr.charAt(i+k)>=46&&expr.charAt(i+k)<=57||expr.charAt(i+k)==46) {
        			if (i+k>expr.length()-2) {
        				break;
        			}
        			k++;
        		}
        		j--;
        		if (expr.substring(i+k).length()==1) {
        			sum=Float.toString(Float.parseFloat(expr.substring(i-j,i))-Float.parseFloat(expr.substring(i+1,i+k+1)));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k+1);
        		}
        		else {
            		sum=Float.toString(Float.parseFloat(expr.substring(i-j,i))-Float.parseFloat(expr.substring(i+1,i+k)));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k);
        		}
        		length-=sum.length();
        		if (expr.charAt(0)!='-') {
        			i=0;
        		}
        		else {
        			i=1;
        		}
    		}
    	}
    	return expr;
    }
    
    private static String add(String expr){
    	String sum="";
    	int j=1;
    	int k=1;
    	int length=expr.length();
    	for (int i=0;i<length;i++) {
    		j=1;
    		k=1;
    		if (i<expr.length()-1&&expr.charAt(i)=='+') {
        		while ((expr.charAt(i-j)>=48&&expr.charAt(i-j)<=57)||expr.charAt(i-j)==46) {
        			j++;
        			if (i<j) {
        				break;
        			}
        		}
        		while (expr.charAt(i+k)>=46&&expr.charAt(i+k)<=57||expr.charAt(i+k)==46) {
        			if (i+k>expr.length()-2) {
        				break;
        			}
        			k++;
        		}
        		j--;
        		if (expr.substring(i+k).length()==1) {
        			sum=Float.toString(Float.parseFloat(expr.substring(i-j,i))+Float.parseFloat(expr.substring(i+1,i+k+1)));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k+1);
        		}
        		else {
            		sum=Float.toString(Float.parseFloat(expr.substring(i-j,i))+Float.parseFloat(expr.substring(i+1,i+k)));
              		expr=expr.substring(0,i-j)+sum+expr.substring(i+k);
        		}
        		length-=sum.length();
    			i=0;
    		}
    	}
    	return expr;
    }
    
    private static String arr(String expr,ArrayList<Variable> vars,ArrayList<Array> arrays) {
    	int j=0;//counter for number of (
    	int k=expr.indexOf('[');//stores index of first mention of (
    	int x=0;//stores index of last )
    	String sub="";
    	String ansStr=expr;
    	for (int i=0;i<expr.length();i++) {
    		if (expr.charAt(i)=='[') {
    			j++;
    		}
    		if (expr.charAt(i)==']') {
    			j--;
    			if (j==0) {
    				x=i; 
    				break;
    			}
    		}
    	}
    	if (x!=0) {
    		ansStr=ansStr.substring(k+1, x);
        	sub+=parenthesis(ansStr,vars,arrays);
    	}
    	ansStr=expr.substring(0, expr.indexOf(ansStr))+ansStr;
   		sub=getNum(ansStr,vars,arrays);
    	return sub;
    }
    
    private static String solve(String sub,ArrayList<Variable> vars,ArrayList<Array> arrays) {
    	String ans=sub;
    	for (int i=0;i<sub.length();i++) {
    		if (sub.charAt(i)=='*') {
    			ans=multiply(ans);
    		}
    	}
    	for (int i=0;i<sub.length();i++) {
    		if (sub.charAt(i)=='/') {
    			ans=divide(ans);
    		}
    	}
    	for (int i=0;i<sub.length();i++) {
    		if (sub.charAt(i)=='+') {
    			ans=add(ans);
    		}
    	}
    	for (int i=0;i<sub.length();i++) {
    		if (sub.charAt(i)=='-') {
    			ans=subtract(ans);
    		}
    	}
    	return ans;
    }
    
    private static String parenthesis(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays){
    	int j=0;//counter for number of (
    	int k=expr.indexOf('(');//stores index of first mention of (
    	int x=0;//stores index of last )
    	String sub="";
    	String ansStr=expr;
    	for (int i=0;i<expr.length();i++) {
    		if (expr.charAt(i)=='(') {
    			j++;
    		}
    		else if (expr.charAt(i)==')') {
    			j--;
    			if (j==0) {
    				x=i; 
    				break;
    			}
    		}
    	}
    	if (x!=0) {
    		ansStr=ansStr.substring(k+1, x);
        	sub+=parenthesis(ansStr,vars,arrays);
    	}
    	ansStr=expr.substring(0, expr.indexOf(ansStr))+ansStr;
   		sub=getNum(ansStr,vars,arrays);
    	return sub;
    }
    
    private static String getNum(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	String name;
    	String sub="";
    	String num;
    	int length=expr.length();
    	int start=0;
    	boolean var=false;
    	for (int i=0;i<length;i++) {
    		name="";
    		var=false;
    		if (Character.toUpperCase(expr.charAt(i))>=65&&Character.toUpperCase(expr.charAt(i))<=90){
    			start=i;
				var=true;
    			while (Character.toUpperCase(expr.charAt(i))>=65&&Character.toUpperCase(expr.charAt(i))<=90){
    				name+=expr.charAt(i);
    				if (i>=length-1) {
    					break;
    				}
    				i++;
    			}
    		}
    		else if (expr.charAt(i)>=48&&expr.charAt(i)<=57){
    			start=i;
    			while ((expr.charAt(i)>=48&&expr.charAt(i)<=57)||expr.charAt(i)==46){
    				name+=expr.charAt(i);
    				if (i>=length-1) {
    					break;
    				}
    				i++;
    			}
    		}
     		if (i>=length-1) {
    			i++;
    		}
    		if (sub.equals("")) {
    			sub=expr.substring(0, start);
    		}
    		if (var) {
	    		for (int j=0;j<vars.size();j++) {
	    			if (vars.get(j).name.equals(name)) {
	    				sub+=Float.toString(vars.get(j).value);
	    				if (i<length-1) {
	    					sub+=expr.charAt(i);
	    				}
	    			}
	    		}
    		}
    		else {
    			if (i<length-1) {
    				sub+=expr.substring(i-name.length(),i+1);
    			}
    			else {
        			sub+=expr.substring(i-name.length(),i);
    			}
    		}
    	}
    	sub=solve(sub,vars,arrays);
    	return sub;
    }
    
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	int length=expr.length();
    	for (int i=0;i<length;i++) {//removes spaces from string
    		if (expr.charAt(i)==' ') {
    			expr=expr.substring(0, i)+expr.substring(i+1);
    			length--;
    			i=0;
    		}
    	}
    	String ans=parenthesis(expr,vars,arrays);
    	ans=solve(ans,vars,arrays);
    	System.out.println(ans);
    	return Float.parseFloat(ans);
    }
}