package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	private static Node order(Node poly) {
		Node ptr=poly;
		Node ans=null;
		Node head=null;
		int max=0;
		int min=0;
		while (ptr!=null) {
			if (ptr.term.degree>max) {
				max=ptr.term.degree;
			}
			if (ptr.term.degree<min) {
				min=ptr.term.degree;
			}
			ptr=ptr.next;
		}
		ptr=poly;
		while (max>=min) {
			while (ptr!=null) {
				if (ptr.term.degree==max) {
					ans=new Node(ptr.term.coeff,ptr.term.degree,null);
					ans.next=head;
					head=ans;
					break;
				}
				ptr=ptr.next;
			}
			max--;
			ptr=poly;
		}
		return ans;
	}
	
	
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly1==null) {
			return poly2;
		}
		if (poly2==null) {
			return poly1;
		}
		Node head=null;
		Node ptr1=poly1;
		Node ptr2=poly2;
		Node sum=null;
		float coeff=0;
		int deg=0;
		boolean match=false;
		while (ptr1!=null) {
			while (ptr2!=null) {
				if (ptr1.term.degree==ptr2.term.degree) {
					coeff=ptr1.term.coeff+ptr2.term.coeff;
					deg=ptr1.term.degree;
					if (coeff!=0) {
						sum=new Node(coeff,deg,null);
						sum.next=head;
						head=sum;
					}
					match=true;
				}
				ptr2=ptr2.next;
			}
			if (match==false) {
				sum=new Node(ptr1.term.coeff,ptr1.term.degree,null);
				sum.next=head;
				head=sum;
			}
			
			match=false;
			ptr1=ptr1.next;
			ptr2=poly2;
		}
		ptr1=poly1;
		while (ptr2!=null) {
			while (ptr1!=null) {
				if (ptr2.term.degree==ptr1.term.degree) {
					match=true;
					break;
				}
				ptr1=ptr1.next;
			} 
			if (match==false) {
				sum=new Node(ptr2.term.coeff,ptr2.term.degree,null);
				sum.next=head;
				head=sum;
			}
			ptr2=ptr2.next;
			match=false;
			ptr1=poly1;
		}
		return order(sum);
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly1==null||poly2==null) {
			return new Node(0,0,null);
		}
		Node product=null;
		Node ptr1=poly1;
		Node ptr2=poly2;
		Node head=null;
		float coeff=0;
		int deg=0;
		while (ptr1!=null) {
			while (ptr2!=null) {
				coeff=ptr1.term.coeff*ptr2.term.coeff;
				deg=ptr1.term.degree+ptr2.term.degree;
				product=new Node(coeff,deg,null);
				product.next=head;
				head=product;
				ptr2=ptr2.next;
			}
			ptr1=ptr1.next;
			ptr2=poly2;
		}
		product=getSum(product);
		return order(product);
	}
	
	private static Node getSum(Node poly) {
		Node ans=null;
		Node ptr1=poly;
		Node ptr2=poly.next;
		Node ptr3=poly;
		Node head=null;
		float coeff=0;
		boolean match=false;
		while (ptr1!=null) {
			coeff=ptr1.term.coeff;
			while(ptr2!=null) {
				if(ptr1.term.degree==ptr2.term.degree) {
					coeff+=ptr2.term.coeff;
					while (ptr3.next!=ptr2) {
						ptr3=ptr3.next;
					}
					match=true;
					ptr3.next=ptr3.next.next;
				}
				ptr2=ptr2.next;
			}
			ptr3=ptr1;
			if (match==true) {
				ans=new Node(coeff,ptr1.term.degree,null);
				ans.next=head;
				head=ans;
			}
			if (match==false) {
				ans=new Node(ptr1.term.coeff,ptr1.term.degree,null);
				ans.next=head;
				head=ans;
			}
			
			match=false;
			ptr1=ptr1.next;
			if (ptr1!=null) {
				ptr2=ptr1.next;
				
			}
		}
		return ans;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly==null) {
			return 0;
		}
		float ans=0;
		double ans1=0;
		Node ptr=poly;
		while (ptr!=null) {
			ans1+= ptr.term.coeff*Math.pow(x, ptr.term.degree);
			ans=(float)ans1;
			ptr=ptr.next;
		}
		return ans;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}