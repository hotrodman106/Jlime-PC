package jlime.pc.edition;

import java.awt.Color;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * The main class used for parsing the commands.
 * Version:7
 * @author Coolway99
 */
public class CommandParser{
	private static final String r = "\n";
	private static HashMap<String, String> varList = new HashMap<>();
	private static ArrayList<String> consoleOutput = new ArrayList<>();
	/*
	 * \u0005<index>\u0006 will reference the multiCommand with the specified
	 * index
	 */
	private static final ArrayList<MultiCommand> commandList = new ArrayList<>();
	private static ArrayList<String> moduleList = new ArrayList<>();

	/**
	 * Outputs whatever is currently in the consoleOutput Array list to the
	 * console then clears it.
	 *
	 * @param clear Clear the pending output afterwards?
	 */
	public static void flush(boolean clear){
		for(String x : consoleOutput){
			if(x.startsWith("\u0001")){
				ConsoleProxy.setText("");
				try{
					String s = x.substring(1);
					if(s.equals("")){
						continue;
					}
					ConsoleProxy.append(x.substring(1) + r);
				} catch(Exception e){
					// Hi
				}
			} else {
				ConsoleProxy.append(x + r);
			}
		}
		if(clear){
			consoleOutput.clear();
		}
	}

	/**
	 * Gets the value of a variable.
	 *
	 * @param key Name of variable ( EX: the x in %x% ).
	 * @return Returns the value of the variable.
	 */
	public static String getVar(String key){
		return varList.get(key);
	}

	/**
	 * The start of the command parser, raw user-input should through here,
	 * though anything calling nested commands should instead do
	 * {@link #doCommand(String, int)}
	 *
	 * @param input String to be parsed.
	 * @param debug Whether the command parser goes through the debug parser or
	 * not.
	 */
	public static void inputCommand(String[] input, boolean debug){
		String error = "";
		for(int x = 0; x < input.length; x++){
			String s = input[x];
			if(s.startsWith("::")){
				continue;
			}
			commandList.add(new MultiCommand(debug));
			ReturnInfo ret = parser(s.toCharArray(), 0, 0);
			if(ret != null){
				for(String string : ret.additionalData){
					error += string+r;
				}
				JOptionPane.showMessageDialog(GUI.jTextArea2, "OI! Error while parsing!" + r
						+ "There is an error on line " + (x + 1) + "!"+r
						+ "The information about the error is as follows:"+r
						+ error, "ERROR!", JOptionPane.ERROR_MESSAGE);
				break;
			}
			ret = commandList.get(0).run(0);
			commandList.clear();
			if(ret.getRetCode() != -1){
				if(ret.getRetCode() == -2){
					for(String string : ret.additionalData){
						error += string+r;
					}
					JOptionPane.showMessageDialog(GUI.jTextArea2, "OI! that's not right!" + r
							+ "There is an error on line " + (x + 1) + "!"+r
							+ "The information about the error is as follows:"+r
							+ error, "ERROR!", JOptionPane.ERROR_MESSAGE);
					break;
				}
				x = ret.getRetCode() - 1;
			}
		}
		flush(true);
	}

	/**
	 * Takes a single line of raw user input 
	 * @param input
	 * @param debug
	 */
	public static void inputCommand(String input, boolean debug){
		commandList.add(new MultiCommand(debug));
		ReturnInfo ret = parser(input.toCharArray(), 0, 0);
		for(String s : ret.getAdditionalData()){
			consoleOutput.add(s);
		}
		flush(true);
		commandList.clear();
	}

	private static ReturnInfo parser(char[] chars, int offset, int key){
		StringBuilder ret = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		boolean escaped = false;
		boolean inVar = false;
		String command = null;
		int commandIndex = 0;
		for(int x = offset; x < chars.length; x++){
			char y = chars[x];
			if(escaped){
				temp.append(y);
				continue;
			}
			switch(y){
				case '%':
					if(inVar){
						inVar = false;
						String var = getVar(temp.toString());
						if(var != null){
							ret.append(var);
							temp.setLength(0);
						} else {
							return new ReturnInfo(-2, "Oi! That was not a valid variable!", "Name: " + temp, "At position "+(x+1));
						}
					} else {
						inVar = true;
						ret.append(temp);
						temp.setLength(0);
					}
					break;
				case '\\':
					escaped = true;
					break;
				case '[':{
					StringBuilder mathTemp = new StringBuilder();
					while(chars[++x] != ']'){
						mathTemp.append(chars[x]);
					}
					temp.append(MathHandler.inputMath(mathTemp.toString()).getAdditionalData().get(0));
					break;
				}
				case '(':{
					commandList.add(new MultiCommand(false));
					ReturnInfo returned = parser(chars, ++x, commandList.size()-1);
					if(returned.getRetCode() < 0){
						return returned;
					}
					x = returned.getRetCode();
					ret.append("\u0005"+returned.getAdditionalData().get(0)+"\u0006");
					break;
				}
				case ')':{
					if(command == null){
						commandList.get(key).put(new Command(ret.append(temp).toString()));
					} else {
						commandList.get(key).get(commandIndex).addArg(ret.append(temp).toString());
					}
					return new ReturnInfo(x, ""+key);
				}
				case '&':
					if(command == null){
						commandList.get(key).put(new Command(ret.append(temp).toString()));
					} else {
						commandList.get(key).get(commandIndex).addArg(ret.append(temp).toString());
						command = null;
					}
					temp.setLength(0);
					ret.setLength(0);
					commandIndex++;
					break;
				case ',':
					if(command == null){
						return new ReturnInfo(-2, "Error: command was not defined before , at"
								+ "position "+(x+1));
					}
					commandList.get(key).get(commandIndex).addArg(ret.append(temp).toString());
					temp.setLength(0);
					ret.setLength(0);
					break;
				case ':':
					if(command != null){
						return new ReturnInfo(-2, "OI! You have : where you already have declared a command!",
								"At position "+(x+1));
					}
					command = ret.append(temp).toString();
					commandList.get(key).put(new Command(command));
					temp.setLength(0);
					ret.setLength(0);
					break;
				default:
					temp.append(y);
					break;
			}
		}
		if(command == null){
			commandList.get(key).put(new Command(ret.append(temp).toString()));
		} else {
			commandList.get(key).get(commandIndex).addArg(ret.append(temp).toString());
		}
		if(key != 0){
			return new ReturnInfo(-2, "Error, unclosed parenthesies");
		}
		return null;
	}

	/**
	 * Expands the command given to it and runs
	 *
	 * @param c The command
	 * @param startDepth The starting depth
	 * @param debug Should use debug or not?
	 * @return See the return value of
	 * {@link #doCommand(String, String[], int, boolean)}
	 * @see #doCommand(String, int)
	 * @see #doCommand(String, String[], int, boolean)
	 */
	public static ReturnInfo doCommand(Command c, int startDepth, boolean debug){
		return doCommand(c.getCmd(), c.getArgs(), startDepth, debug);
	}

	/**
	 * Decides where to send the command
	 *
	 * @param cmd The command
	 * @param args Arguments for the command, can be null
	 * @param startDepth The starting depth for the command
	 * @param debug Should go through debug?
	 * @return Returns '-1' if the command is consumed, '-2' if there is an
	 * error, '-3' if there is no command by name, and a value 0 or greater if
	 * it is goto.
	 * @see #doCommand(String, int)
	 * @see #doCommand(Command, int, boolean)
	 */
	public static ReturnInfo doCommand(String cmd, String[] args, int startDepth, boolean debug){
		if(cmd.startsWith("!")){ return header(cmd, args); }
		if(debug){ return debug(cmd, args, startDepth); }
		return parseInput(cmd, args, startDepth);
	}

	/**
	 * This is what's usually called for nested arguments. It expects to have
	 * either a command in parentheses or a simple command without any
	 * arguments.
	 * 
	 * @param cmd The command
	 * @param startDepth The starting depth
	 * @return See the return value of
	 * {@link #doCommand(String, String[], int, boolean)}
	 * @see #doCommand(Command, int, boolean)
	 * @see #doCommand(String, String[], int, boolean)
	 */
	public static ReturnInfo doCommand(String cmd, int startDepth){
		if(cmd.contains("\u0005")){
				int code = Integer.parseInt(cmd.substring(cmd.indexOf('\u0005')+1, cmd.indexOf('\u0006')));
				return commandList.get(code).run(startDepth);
		}
		return parseInput(cmd, null, startDepth);
	}

	/**
	 * Handles command's arguments
	 *
	 * @param args All arguments given to a command. ( EX: true,false,0 )
	 * @param offset Offset of array.
	 * @param length Amount of arguments to parse.
	 * @param startDepth Offset for 'commands' array.
	 * @param consoleOutput The array that stores all output to be printed to
	 * console.
	 * @return Returns a list of arguments, now expanded by one level
	 */
	public static String[] parseArgs(String[] args, int offset, int length, int startDepth,
			ArrayList<String> consoleOutput){
		for(int x = offset; x < length; x++){
			if(args[x].contains("\u0005")){
				CommandParser.doCommand(args[x], startDepth);
				args[x] = consoleOutput.remove(consoleOutput.size() - 1);
			}
			args[x] = args[x].trim();
		}
		return args;
	}

	/**
	 * Parses all header commands.
	 *
	 * @param cmd Main part of command ( EX: !import: )
	 * @param args All arguments following command ( EX: true )
	 * @return The same thing {@link #doCommand(String, String[], int, boolean)}
	 * returns
	 */
	private static ReturnInfo header(String cmd, String[] args){
		try{
			switch(cmd){
				case "!import":
					for(String x : args){
						moduleList.add(x);
					}
					break;
				case "!foreground":
					ConsoleProxy.setForeground(new Color(parseInt(args[0]), parseInt(args[1]), parseInt(args[2])));
					break;
				case "!background":
					ConsoleProxy.setBackground(new Color(parseInt(args[0]), parseInt(args[1]), parseInt(args[2])));
					break;
				case "!resetColors":
					ConsoleProxy.resetColors();
					break;
				default:
					JOptionPane.showMessageDialog(GUI.jTextArea2, "OI! that's not right!" + r
							+ "There is an error with a header statement!", "ERROR!",
							JOptionPane.ERROR_MESSAGE);
					// consoleOutput.add("OI! invalid ! command!"+r);
			}
		} catch(Exception e){
			return new ReturnInfo(-2, "ERROR WITH HEADER COMMAND");
		}
		return new ReturnInfo(-1);
	}

	/**
	 * Parses commands with the 'debug.' prefix
	 *
	 * @param cmd Main part of command ( EX: /debug.help: )
	 * @param args All arguments following command ( EX: true )
	 * @param startDepth Offset for Multicommand's 'commands' array.
	 * @return See the return value of
	 * {@link #doCommand(String, String[], int, boolean)}
	 */

	private static ReturnInfo debug(String cmd, String[] args, int startDepth){
		switch(cmd){
			case "/debug.help":
				consoleOutput.clear();
				consoleOutput
				.add("\u0001COMMAND LIST:"
						+ r
						+ "/ping     PONG!"
						+ r
						+ "/pong    PING!"
						+ r
						+ "/clear     Clears the screen"
						+ r
						+ "/linebreak     Adds a carriage return"
						+ r
						+ "/echo:[String]     Writes a string to the console"
						+ r
						+ "/gettime:[date String]    Outputs the date/time"
						+ r
						+ "/random:[Integer]     Outputs a random number up to the value specified"
						+ r
						+ "/loop:[Integer],([Command])     Loops a command a set number of times"
						+ r
						+ "/if:[Integer][<,>,=,<=,>=][Integer],([True Command]),([False Command])      Checks if a statement is true and, if so, runs a command"
						+ r
						+ "/for:[Integer],[Integer],[Integer],([Command])    Loops a command for a set number of times in certain increments"
						+ r);
				break;
			case "/debug.headerHelp":
				consoleOutput.add("Available commands:\n" + "!import:[key1],[key2],[key3]...\n"
						+ "\tImports the module, only useful in lime edit\n"
						+ "!foreground:[int R],[int G],[int B]\n"
						+ "\tChanges the foreground color (not the setting)"
						+ "!background:[int R],[int G],[int B]\n"
						+ "\tChanges the background color (not the setting)\n" + "!resetColors\n"
						+ "\tResets the colors to the user setting\n"
						+ "NOTE: Affects main window!" + r);
				break;
			case "/debug.close":
				try{
					args = parseArgs(args, 0, args.length, startDepth, consoleOutput);
					System.exit(parseInt(args[0]));
				} catch(Exception q){
					consoleOutput.add("OI! There is an error with your get close command!");
				}
				break;
			case "/debug.clearVar":
				try{
					String name = args[0];
					if(varList.remove(name) == null){
						consoleOutput.add("There is no variable in memory by that name!");
					} else {
						consoleOutput.add("Variable " + name + " removed from memory!");
					}
				} catch(Exception p){
					consoleOutput.add("OI! There is an error with your clear variable command!");
				}
				break;
			case "/debug.listModules":
				try{
					String[] name = ModuleManager.getList();
					File[] file = ModuleManager.getFiles();
					for(int x = 0; x < name.length; x++){
						consoleOutput.add(name[x] + ", true name is " + file[x].getName());
					}
				} catch(Exception e){
					consoleOutput.add("OI! I dun broke it!");
				}
				break;
			default:
				return parseInput(cmd, args, startDepth);
		}
		return new ReturnInfo(-1);
	}

	/**
	 * Parses all commands without arguments.
	 *
	 * @param cmd Main part of command ( EX: /debug.help: )
	 * @param args All arguments following command ( EX: true )
	 * @param startDepth Offset for Multicommand's 'commands' array.
	 * @return See the return value of
	 * {@link #doCommand(String, String[], int, boolean)}
	 */
	private static ReturnInfo parseInput(String cmd, String[] args, int startDepth){
		switch(cmd){
			case "/ping":
				consoleOutput.add("PONG!");
				break;
			case "/pong":
				consoleOutput.add("PING!");
				break;
			case "/clear":
				consoleOutput.clear();
				consoleOutput.add("\u0001");
				break;
			case "/linebreak":
				consoleOutput.add(r);
				break;
			default:
				return parseAdvanceCommand(cmd, args, startDepth);
		}
		return new ReturnInfo(-1);
	}

	/**
	 * Parses commands with one or more arguments.
	 *
	 * @param cmd Main part of command ( EX: /debug.help: )
	 * @param args All arguments following command ( EX: true )
	 * @param startDepth Offset for Multicommand's 'commands' array.
	 * @return Returns '-1' if the command is consumed, '-2' if there is an
	 * error, '-3' if there is no command by name, and a value 0 or greater if
	 * it is goto.
	 */
	private static ReturnInfo parseAdvanceCommand(String cmd, String[] args, int startDepth){
		try{
			switch(cmd){
				case "/echo":
					args = parseArgs(args, 0, args.length, startDepth, consoleOutput);
					String out = args[0];
					for(int x = 1; x < args.length; x++){
						out += ", " + args[x];
					}
					consoleOutput.add(out);
					break;
				case "/random":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						int var = parseInt(args[0]);
						Random random = new Random();
						consoleOutput.add(random.nextInt(var) + "");
					} catch(Exception p){
						return new ReturnInfo(-2, "OI! That's not a integer! Try inputting a integer!");
					}
					break;
				case "/loop":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						int var1 = parseInt(args[0]);
						String command = args[1];
						while(var1 != 0){
							var1--;
							ReturnInfo y = doCommand(command, startDepth);
							if(y.getRetCode() != -1){ return y; }
						}
					} catch(Exception p){
						return new ReturnInfo(-2, "OI! There is an error with your loop statement!");
					}
					break;
				case "/for":
					try{
						args = parseArgs(args, 0, 3, startDepth, consoleOutput);
						int var2 = parseInt(args[1]);
						int var3 = parseInt(args[2]);
						String command = args[3];
						for(int var1 = parseInt(args[0]); var1 < var2; var1 += var3){
							ReturnInfo y = doCommand(command, startDepth);
							if(y.getRetCode() != -1){ return y; }
						}
					} catch(Exception p){
						return new ReturnInfo(-2, "OI! There is an error with your for statement!");
					}
					break;
				case "/var":
					try{
						args = parseArgs(args, 0, 2, startDepth, consoleOutput);
						String name = args[0];
						String string = args[1];
						Boolean verbose = false;
						if(args.length > 2){
							verbose = Boolean.parseBoolean(args[2]);
						}

						varList.put(name, string);
						if(verbose){
							consoleOutput.add("Variable " + name + " set to " + string);
						}
					} catch(Exception p){
						return new ReturnInfo(-2, "OI! There is an error with your variable declaration statement!");
					}
					break;
				case "/getTime":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						DateFormat df = new SimpleDateFormat(args[0]);
						Date dateObj = new Date();
						consoleOutput.add(df.format(dateObj));
					} catch(Exception p){
						return new ReturnInfo(-2, "OI! That's not a proper date String! Try inputting a date String!");
					}
					break;
				case "/if":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						try{
							if(Boolean.parseBoolean(args[0])){ return doCommand(args[1], startDepth); }
							if(!args[0].toUpperCase().equals("FALSE")){ throw new Exception(); }
							if(args.length > 2){ return doCommand(args[2], startDepth); }
							return doCommand("\u0002", startDepth);
						} catch(Exception e){}

						String[] num = args[0].split("[<=>]+");
						String operator = args[0].split("[\\d\\.]+")[1];
						int var1 = parseInt(num[0]);
						int var2 = parseInt(num[1]);
						String trueCommand = args[1];
						String falseCommand;
						try{
							falseCommand = args[2];
						} catch(ArrayIndexOutOfBoundsException e){
							falseCommand = "\u0002";
						}
						switch(operator){
							case "==":
							case "=":
								if(var1 == var2){ return doCommand(trueCommand, startDepth); }
								return doCommand(falseCommand, startDepth);

							case "<":
								if(var1 < var2){ return doCommand(trueCommand, startDepth); }
								return doCommand(falseCommand, startDepth);

							case ">":
								if(var1 > var2){ return doCommand(trueCommand, startDepth); }
								return doCommand(falseCommand, startDepth);

							case "<=":
								if(var1 <= var2){ return doCommand(trueCommand, startDepth); }
								return doCommand(falseCommand, startDepth);

							case ">=":
								if(var1 >= var2){ return doCommand(trueCommand, startDepth); }
								return doCommand(falseCommand, startDepth);
							default:
								return new ReturnInfo(-2, "OI! Invalid Operator for your if statement!");
						}
					} catch(Exception p){
						return new ReturnInfo(-2, "OI! There is an error with your if statement!");
					}
				case "\u0002":
					break;
				case "/goto":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						int x = parseInt(args[0]);
						if(x < 0){
							return new ReturnInfo(-2, "OI! There is no such thing as a negative line!");
						}
						return new ReturnInfo(x - 1);
					} catch(Exception e){
						return new ReturnInfo(-2, "OI! Not a valid integer");
					}
				case "/last":
					try{
						if(args[0].contains("\u0005")){
							doCommand(args[0], startDepth);
							args[0] = consoleOutput.remove(consoleOutput.size() - 1);
						}
						int x = parseInt(args[0]) - 1;
						while(x > 0){
							consoleOutput.remove(consoleOutput.size() - 1);
							x--;
						}
					} catch(Exception e){
						return new ReturnInfo(-2, "OI! Not a valid integer");
					}
					break;
				case "/flush":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						boolean b = Boolean.parseBoolean(args[0]);
						flush(b);
					} catch(Exception e){
						return new ReturnInfo(-2, "OI! Not a valid boolean");
					}
					break;
				case "/sleep":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						Thread.sleep(parseInt(args[0]));
					} catch(InterruptedException e){
						return new ReturnInfo(-2, "OI! I just don't know went wrong");
					} catch(NumberFormatException e){
						return new ReturnInfo(-2, "OI! Not a valid Integer");
					}
					break;
				default:
					return ModuleManager.run(moduleList, cmd, args, startDepth, consoleOutput);

			}
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			return new ReturnInfo(-2, "OI! A command didn't have the right number of arguments!");
		}
		return new ReturnInfo(-1);
	}

	/**
	 * A class that stores the info for returning stuff 
	 * @author Coolway99
	 */
	public static class ReturnInfo{
		private final int retCode;
		private final ArrayList<String> additionalData;

		/**
		 * The most basic constructor for the class
		 * @param retCode The code to return, see {@link CommandParser#doCommand(String, String[], int, boolean)}
		 * @see ReturnInfo#ReturnInfo(int, ArrayList)
		 * @see ReturnInfo#ReturnInfo(int, String...)
		 */
		public ReturnInfo(int retCode){
			this.retCode = retCode;
			this.additionalData = new ArrayList<>();
		}

		/**
		 * Sets the additional data to what is specified
		 * @param retCode See {@link ReturnInfo#ReturnInfo(int)}
		 * @param additionalData The additional data to attach to the return code
		 * @see ReturnInfo#ReturnInfo(int)
		 * @see ReturnInfo#ReturnInfo(int, String...)
		 */
		public ReturnInfo(int retCode, ArrayList<String> additionalData){
			this.retCode = retCode;
			this.additionalData = additionalData;
		}

		/**
		 * Same as the other two, except a variable number of strings
		 * @param retCode The return code
		 * @param additionalData The additional data
		 * @see ReturnInfo#ReturnInfo(int)
		 * @see ReturnInfo#ReturnInfo(int, ArrayList)
		 */
		public ReturnInfo(int retCode, String... additionalData){
			this.retCode = retCode;
			ArrayList<String> addData = new ArrayList<>();
			for(String x : additionalData){
				addData.add(x);
			}
			this.additionalData = addData;
		}

		/**
		 * Adds data to the end of the array for additionalData
		 * @param data The data to be added
		 */
		public void addAdditionalData(String... data){
			for(String x : data){
				this.additionalData.add(x);
			}
		}

		/**
		 * Removes data at the specified index
		 * @param index The data to be removed
		 */
		public void removeAdditionalData(int index){
			this.additionalData.remove(index);
		}

		/**
		 * Gets the additional data
		 * @return The ArrayList of additional data
		 */
		public ArrayList<String> getAdditionalData(){
			return this.additionalData;
		}

		/**
		 * Gets the return code: The code returned, usually the standard but parser uses it differently;
		 * for the offset in the parsing array.
		 * @return The return code
		 */
		public int getRetCode(){
			return this.retCode;
		}
	}
	
	/**
	 * Method used to convert from string to int. In here to provide change-independent way
	 * for covering to int. The method first converts it to double then rounds it. Afterwards it
	 * casts it to int.
	 * 
	 * @param string The string representing an integer (ex. "2")
	 * @return The integer represented by the string (ex. 2)
	 * @see CommandParser#parseDouble(String)
	 */
	public static int parseInt(String string){
		return (int) Math.round(Double.parseDouble(string));
	}
	
	/**
	 * Method used to convert from string to double. Used for so that if I break compatibility
	 * with that all modules don't have to re-update
	 * 
	 * @param string The string to turn into a double
	 * @return The double represented by the string
	 */
	public static double parseDouble(String string){
		return Double.parseDouble(string);
	}
}