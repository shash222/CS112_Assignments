package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		/** COMPLETE THIS METHOD **/
		Stack<TagNode> htmlTags = new Stack<TagNode>();
		String curr = sc.nextLine();
		root = new TagNode(curr.substring(1,curr.length()-1), null, null);
		htmlTags.push(root);
		TagNode lstPtr;
		while (sc.hasNextLine()) {
			curr = sc.nextLine();

			if (curr.charAt(0)=='<'&&curr.charAt(1)=='/') {
				htmlTags.pop();
			} 
			else if (curr.charAt(0)=='<'&&curr.charAt(1)!='/') {
				if (htmlTags.peek().firstChild == null) {
					lstPtr = new TagNode(curr.substring(1,curr.length()-1), null, null);
					htmlTags.peek().firstChild = lstPtr;
					htmlTags.push(lstPtr);
				} 
				else {
					TagNode ptr2 = htmlTags.peek().firstChild;
					while (ptr2.sibling != null) {
						ptr2 = ptr2.sibling;
					}
					lstPtr = new TagNode(curr.substring(1,curr.length()-1), null, null);
					ptr2.sibling = lstPtr;
					htmlTags.push(lstPtr);
				}
			} 
			else {
				if (htmlTags.peek().firstChild == null) {
					htmlTags.peek().firstChild = new TagNode(curr, null, null);
				} 
				else {
					TagNode ptr2 = htmlTags.peek().firstChild;

					while (ptr2.sibling != null) {
						ptr2 = ptr2.sibling;
											}
					ptr2.sibling = new TagNode(curr, null, null);
				}
			}
		}
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		repRecur(oldTag, newTag, root);
	}
	
	private void repRecur(String oldTag, String newTag, TagNode ptr) {
		if (ptr.tag.equals(oldTag)) {
			ptr.tag=newTag;
		}
		if (ptr!=null) {
			if (ptr.firstChild!=null) {
				repRecur(oldTag,newTag,ptr.firstChild);
			}
			if (ptr.sibling!=null) {
				repRecur(oldTag,newTag,ptr.sibling);
			}
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		bolRecur(root,0,row);
	}
	
	private void bolRecur(TagNode ptr, int x, int row) {
		TagNode ptr2;
		TagNode child;
		if (ptr!=null) {
			if (ptr.firstChild!=null) {
				if (ptr.tag.equals("tr")) {
					x++;
					if (x>row) {
						return;
					}
					else if (x==row) {
						ptr2=ptr.firstChild;
						while(ptr2!=null) {
							System.out.println(ptr2.tag);
							if (ptr2.tag.equals("td")) {
								child=ptr2.firstChild;
								ptr2.firstChild=new TagNode("b",child,null);
							}
							ptr2=ptr2.sibling;
						}
					}
				}
				bolRecur(ptr.firstChild,x,row);
			}
			if (ptr.sibling!=null) {
				bolRecur(ptr.sibling,x,row);
			}
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li htmlTags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */

	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		remRecur(tag,root);
		print(root,1);
	}
	
	private void remRecur(String tag, TagNode ptr) {
		TagNode sib;
		TagNode ptr2;
		TagNode lstPtr;
		if (ptr!=null) {
			if (ptr.firstChild!=null) {
				if (ptr.firstChild.tag.equals(tag)) {
					sib=ptr.firstChild.sibling;
					ptr.firstChild=ptr.firstChild.firstChild;
					ptr2=ptr.firstChild;
					while (ptr2.sibling!=null) {
						ptr2=ptr2.sibling;
					}
						ptr2.sibling=sib;
					if ((tag.equals("ol")||tag.equals("ul"))&&ptr.firstChild!=null) {
						ptr=ptr.firstChild;
						while (ptr.sibling!=null) {
							if (ptr.tag.equals("li")){
								ptr.tag="p";
							}
							ptr=ptr.sibling;
						}
					}
					ptr=root;
				}
				remRecur(tag,ptr.firstChild);
			}
			if (ptr.sibling!=null) {
				if (ptr.sibling.tag.equals(tag)) {
					sib=ptr.sibling.sibling;
					//add while loop somewhere here
					ptr.sibling=ptr.sibling.firstChild;
					ptr.sibling.sibling=sib;
					if ((tag.equals("ol")||tag.equals("ul"))) {
						while (ptr!=null) {
							if (ptr.tag.equals("li")){
								ptr.tag="p";
							}
							if (ptr.sibling==null) {
								break;
							}
							ptr=ptr.sibling;
						}
					}
				}
				remRecur(tag,ptr.sibling);
			}
		}
	}

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		addRecur(word, tag, root);
	}
	
	private void addRecur(String word, String tag, TagNode ptr) {
		tagRecur(ptr,word,tag);
		if (ptr!=null) {
			if (ptr.firstChild!=null) {
				addRecur(word,tag,ptr.firstChild);
			}
			if (ptr.sibling!=null) {
				addRecur(word,tag,ptr.sibling);
			}
		}
	}
	
	private void tagRecur(TagNode ptr, String word, String tag) {
		String punc=".;:?!";
		String newWord="";
		String line=ptr.tag;
		boolean exists=line.toUpperCase().contains(word.toUpperCase());
		int init=line.indexOf(word);
		int len=word.length();
		TagNode current;
		TagNode after;
		if (exists) {//if item is in tag
			if (line.charAt(0)==line.charAt(init)||line.charAt(init-1)==' ') {//if first letter of item is first letter of sentence
				if (line.charAt(line.length()-1)==line.charAt(init+len-1)||line.charAt(init+len)==' ') {
					newWord=newWord;
				}
				else if (punc.contains(line.charAt(init+len)+"")){
					newWord=newWord+line.charAt(init+len);
				}
				else {
					return;
				}
				after=new TagNode(line,null,null);
				current=new TagNode(tag,new TagNode(word,null,null),after);
				ptr.tag=line.substring(0,init);
				line=line.substring(init+len);
				ptr.firstChild=null;
				ptr.sibling=current;
				print(root,1);
				tagRecur(ptr,word,tag);
			}
				
			else {
				return;
			}
		}
	}
		
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
