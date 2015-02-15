package jlime.pc.edition;

import java.util.ArrayList;
import java.util.Stack;

import jlime.pc.edition.CommandParser.ReturnInfo;

/**
 * The class that handles math statements and commands. Statements are expressed
 * within []. Future plans are to add API support for modules to add their own
 * operators. Will return the type given (if it has decimals, then will return a
 * double, else return an integer), but can be forced into either type by
 * prefixing it with &#64;d or &#64;i for decimal or integer respectively. The
 * current supported operators are as follows:
<table border="1" style="width:100%">
	<tr>
		<th>Operators</th>
		<th>Function</th>
	</tr>
	<tr>
		<td>+</td>
		<td>Adds two numbers together.</td>
	</tr>
	<tr>
		<td>-</td>
		<td>Subtracts two numbers together</td>
	</tr>
	<tr>
		<td>/</td>
		<td>divides the first number from the second number</td>
	</tr>
	<tr>
		<td>*</td>
		<td>Multiplies two numbers together</td>
	</tr>
	<tr>
		<td>%</td>
		<td>Takes the remainder of the division</td>
	</tr>
</table>
 *
 *Additionally, anything starting with # is considered a command. Like before can be prefixed
 *with &#64;d or &#64;i to to force it into decimal or integer, else it will return what it was given.
 *For example, you could do &#64;d#
 * @author Coolway99
 */
public class MathHandler{

	private static final ArrayList<ArrayList<Character>> priorityList = new ArrayList<>();

	/**
	 * The initialization statement, must be called upon program boot up
	 */
	public static void init(){
		//Exponential
		priorityList.add(new ArrayList<Character>());
		priorityList.get(0).add('^');
		//Multiplicative
		priorityList.add(new ArrayList<Character>());
		priorityList.get(1).add('*');
		priorityList.get(1).add('/');
		//Additive
		priorityList.add(new ArrayList<Character>());
		priorityList.get(2).add('+');
		priorityList.get(2).add('-');
		//Bitwise
		priorityList.add(new ArrayList<Character>());
		priorityList.get(3).add('&');
		priorityList.get(3).add('|');
		priorityList.get(3).add('~');
	}

	/**
	 * The input statement. It will evaluate the expression and output whatever outputs to the
	 * consoleOutput ArrayList
	 * 
	 * @param input The math expression being inputed, without the []
	 * @return See the return values of {@link CommandParser#doCommand(String, String[], int, boolean)}
	 */
	public static ReturnInfo inputMath(String input){
		if(input.equals("")){
			return new ReturnInfo(-2, "Error: There was nothing to input for your math!");
		}
		return new ReturnInfo(-1, parser(input));
	}

	private static boolean equalsOperator(String line){
		return line.matches("\\*|\\+|\\-|\\/|\\(|\\)");
	}

	private static String parser(String in){
		Stack<Character> opStack = new Stack<>();
		Stack<String> stack = new Stack<>();
		char[] chars = in.toCharArray();
		for(int x = 0; x < chars.length; x++){
			char y = chars[x];
			if(equalsOperator(y+"")){
				if(opStack.isEmpty()){
					opStack.push(y);
					continue;
				}
				if(opStack.peek() == '('){
					opStack.push(y);
					continue;
				}
				char z = opStack.peek();
				switch(y){
					case '(':
						opStack.push(y);
						break;
					case ')':
						opStack.pop();
						while(z != '('){
							stack.push(z+"");
							z = opStack.pop();
							if(opStack.isEmpty()){
								break;
							}
						}
						break;
					case '-':
					case '+':
						stack.push(opStack.pop()+"");
						opStack.push(y);
						break;
					case '*':
					case '/':
						if(z == '*' || z == '/'){
							stack.push(opStack.pop()+"");
						}
						opStack.push(y);
						break;
				}
			} else {
				StringBuilder temp = new StringBuilder();
				do{
					temp.append(y);
					try{
						y = chars[++x];
					} catch(ArrayIndexOutOfBoundsException e){
						break;
					}
				} while(!equalsOperator(y+""));
				x--;
				stack.push(temp.toString());
			}
		}
		if(!opStack.isEmpty()){
			while(!opStack.isEmpty()){
				stack.push(opStack.pop()+"");
			}
		}
		System.out.println(stack.toString());
		simplify(stack);
		return stack.pop();
	}

	private static void simplify(Stack<String> stack){
		String opperand = stack.pop();
		String var1 = stack.pop();
		String var2 = stack.pop();
		if(equalsOperator(var1)){
			stack.push(var2);
			stack.push(var1);
			simplify(stack);
			var1 = stack.pop();
			var2 = stack.pop();
		}
		if(equalsOperator(var2)){
			stack.push(var2);
			simplify(stack);
			var2 = stack.pop();
		}
		double num1 = Double.parseDouble(var2), num2 = Double.parseDouble(var1);
		switch(opperand){
			case "+":
				stack.push(""+(num1 + num2));
				break;
			case "-":
				stack.push(""+(num1 - num2));
				break;
			case "*":
				stack.push(""+(num1 * num2));
				break;
			case "/":
				stack.push(""+(num1 / num2));
				break;
		}
	}
}
