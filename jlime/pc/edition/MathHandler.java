package jlime.pc.edition;

import java.util.ArrayList;

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
	/**
	 * The input statement. It will evaluate the expression and output whatever outputs to the
	 * consoleOutput ArrayList
	 * 
	 * @param input The math expression being inputed, without the []
	 * @param consoleOutput The console output array that will put outputed too.
	 * @return See the return values of {@link CommandParser#doCommand(String, String[], int, boolean)}
	 */
	public static int inputMath(String input, ArrayList<String> consoleOutput){
		try{
			if(input.equals("")){
				throw new NullPointerException();
			}
		} catch(NullPointerException e){
			consoleOutput.add("Error: There was nothing to input");
			return -2;
		}
		return -1;
	}
	private static String parser(char[] chars, int offset){
		StringBuilder temp = new StringBuilder();
		for(int x = 0; x < chars.length; x++){
			char y = chars[x];
			switch(x){
				case '(':{
					String[] in = parser(chars, x).split("\u0000");
					x = Integer.parseInt(in[0]);
					temp.append(in[1]);
					break;
				}
				case ')':
					return x+"\u0000"+temp.toString();
				default:
					temp.append(y);
			}
		}
		return null;
	}
}
