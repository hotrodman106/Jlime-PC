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
 * Created by hotrodman106 and Coolway99 on 1/1/2015.
 */
public class CommandParser{
    private static final String r = "\n";
    private static HashMap<String,String> stringList = new HashMap<>();
    private static HashMap<String,Integer> intList = new HashMap<>();
    private static HashMap<String,Boolean> booleanList = new HashMap<>();
	private static ArrayList<String> consoleOutput = new ArrayList<>();
	/*
	 * \u0005<index>\u0006 will reference the multiCommand with the specified index
	 */
	private static ArrayList<MultiCommand> multiCommandList = new ArrayList<>();
	private static ArrayList<StringBuilder> stringBuilderList = new ArrayList<>();
	private static ArrayList<String> moduleList = new ArrayList<>();
	private static boolean parsed = false;
	
	/**
	 * Outputs whatever is currently in the consoleOutput Array list to the console
	 * then clears it.
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
					ConsoleProxy.append(x.substring(1)+r);
				} catch(Exception e){
					//Hi
				}
			} else {
				ConsoleProxy.append(x+r);
			}
		}
		if(clear){
			consoleOutput.clear();
		}
	}
/**
 * Replaces a variable in the users script with its value.
 * @param key Name of variable ( EX: the x in %x% ).
 * @return Returns the value of the variable.
 */
	private static String getVar(String key){
		if(stringList.get(key) != null){
			return stringList.get(key);
		}
		if(intList.get(key) != null){
			return Integer.toString(intList.get(key));
		}
		if(booleanList.get(key) != null){
			return Boolean.toString(booleanList.get(key));
		}
		return null;
	}
	/**
	 * The root of the command parser. Decides where to send the command based of its contents.
	 * @param input String to be parsed.
	 * @param debug Whether the command parser uses the debug parser or not.
	 */
	public static void inputCommand(String[] input, boolean debug){
		for(int x = 0; x < input.length; x++){
			String s = input[x];
			if(s.startsWith("::")){
				continue;
			}
			multiCommandList.add(new MultiCommand(debug));
			stringBuilderList.add(new StringBuilder());
			String[] in = s.replaceFirst(":", "\u0000").split("\u0000");
			multiCommandList.get(0).put(new Command(in[0]));
			int y = doCommand(in[0], (in.length == 2 ? in[1] : null), 0, debug);
			multiCommandList.clear();
			stringBuilderList.clear();
			parsed = false;
			if(y != -1){
				if(y == -2){
                    JOptionPane.showMessageDialog(GUI.jTextArea2, "OI! that's not right!" + r +
                    		consoleOutput.remove(consoleOutput.size()-1) + r +
                    		"There is an error on line " + (x+1) + "!", "ERROR!", JOptionPane.ERROR_MESSAGE);
					break;
				}
				x = y-1;
			}
		}
		flush(true);
	}
	public static void inputCommand(String input, boolean debug){
		multiCommandList.add(new MultiCommand(debug));
		stringBuilderList.add(new StringBuilder());
		String[] in = input.replaceFirst(":", "\u0000").split("\u0000");
		multiCommandList.get(0).put(new Command(in[0]));
		doCommand(in[0], (in.length == 2 ? in[1] : null), 0, debug);
		flush(true);
		multiCommandList.clear();
		stringBuilderList.clear();
		parsed = false;
	}
	public static int doCommand(Command c, int startDepth, boolean debug){
		return doCommand(c.getCmd(), c.getArgs(), startDepth, debug);
	}
	public static int doCommand(String cmd, String args, int startDepth, boolean debug){
		if(!parsed && args != null){
			char[] in = args.toCharArray();
			boolean inVar = false;
			boolean escaped = false;
			String temp = "";
			String command = null;
			ArrayList<Integer> depth = new ArrayList<>();
			ArrayList<Integer> commandDepth = new ArrayList<>();
			int level = 0;
			depth.add(startDepth);
			commandDepth.add(0);
			for(int x = 0; x < in.length; x++){
				char y = in[x];
				if(escaped){
					temp += y;
					escaped = false;
				} else {
					switch(y){
						case '%':
							if(inVar){
								inVar = false;
								String var = getVar(temp);
								if(var != null){
									stringBuilderList.get(depth.get(level)).append(var);
									temp = "";
								} else {
									consoleOutput.add("Oi! That was not a valid variable!" + r + "Name: " + temp);
									return -2;
								}
							} else {
								inVar = true;
								stringBuilderList.get(depth.get(level)).append(temp);
								temp = "";
							}
							break;
						case '\\':
							escaped = true;
							break;
						case '(':
							multiCommandList.add(new MultiCommand(debug));
							stringBuilderList.add(new StringBuilder());
							depth.add(stringBuilderList.size() - 1);
							stringBuilderList.get(depth.get(level++)).append(temp);
							commandDepth.add(0);
							temp = "";
							command = null;
							break;
						case ')':
							/*if(command != null){
								String s = stringBuilderList.get(depth.get(level)).append(temp).toString();
								multiCommandList.get(depth.get(level)).get(commandDepth.get(level)).addArg(s);
							} else {
								multiCommandList.get(depth.get(level)).put(new Command(temp));
							}
							stringBuilderList.get(depth.get(level-1)).append("\u0005" + depth.remove(level--) + "\u0006");
							command = null;
							temp = "";*/
							if(command != null){
								multiCommandList.get(depth.get(level)).get(commandDepth.get(level)).addArg(stringBuilderList.get(depth.get(level)).append(temp).toString());
							} else {
								multiCommandList.get(depth.get(level)).put(new Command(temp));
							}
							stringBuilderList.get(depth.get(level-1)).append("\u0005" + depth.remove(level--) + "\u0006");
							temp = "";
							command = null;
							break;
						case '&':
							if(command != null){
								multiCommandList.get(depth.get(level)).get(commandDepth.get(level)).addArg(stringBuilderList.get(depth.get(level)).append(temp).toString());
							} else {
								multiCommandList.get(depth.get(level)).put(new Command(temp));
							}
							stringBuilderList.set(depth.get(level), new StringBuilder());
							commandDepth.set(level, commandDepth.get(level)+1);
							command = null;
							temp = "";
							break;
						case ',':
							multiCommandList.get(depth.get(level)).get(commandDepth.get(level)).addArg(stringBuilderList.get(depth.get(level)).append(temp).toString());
							stringBuilderList.set(depth.get(level), new StringBuilder());
							temp = "";
							break;
						case ':':
							if(command != null){
								consoleOutput.add("OI! You have : where you already have declared a command!");
								return -2;
							}
							multiCommandList.get(depth.get(level)).put(new Command(temp));
							command = temp;
							temp = "";
							break;
						default:
							temp += y;
							break;
					}
				}
			}
			multiCommandList.get(startDepth).get(commandDepth.get(level)).addArg(stringBuilderList.get(startDepth).append(temp).toString());
			parsed = true;
		}
		return multiCommandList.get(startDepth).run(startDepth);
	}
	public static int doCommand(String cmd, String[] args, int startDepth, boolean debug){
		if(cmd.startsWith("!")){
			return header(cmd, args);
		}
		if(debug){
			return debug(cmd, args, startDepth);
		}
		return parseInput(cmd, args, startDepth);
	}
	public static int doCommand(String cmd, int startDepth){
		if(cmd.startsWith("\u0005")){
			return getOut(cmd, startDepth);
		}
		return parseInput(cmd, null, startDepth);
	}
/**
 * Handles command's arguments
 * @param args All arguments given to a command. ( EX: true,false,0 )
 * @param offset Offset of array.
 * @param length Amount of arguments to parse.
 * @param startDepth Offset for 'commands' array.
 * @param consoleOutput The array that stores all output to be printed to console.
 * @return Returns a list of arguments
 */
	public static String[] parseArgs(String[] args, int offset, int length, int startDepth,
			ArrayList<String> consoleOutput){
		System.out.println(offset);
		System.out.println(length);
		for(int x = offset; x < length;x++){
			if(args[x].contains("\u0005")){
				CommandParser.doCommand(args[x], startDepth);
				args[x] = consoleOutput.remove(consoleOutput.size()-1);
			}
			args[x] = args[x].trim();
			System.out.println(x);
		}
		return args;
	}

	private static int getOut(String code, int startDepth){
		int intCode = Integer.parseInt(code.substring(code.indexOf('\u0005')+1, code.indexOf('\u0006')));
		return getOut(intCode, startDepth);
	}
	private static int getOut(int code, int startDepth){
		return multiCommandList.get(code).run(startDepth);
	}
	/**
	 * Parses all header commands.
	 * @param cmd Main part of command ( EX: !import: )
     * @param args All arguments following command ( EX: true )
	 * @return Returns '-1' if the command is consumed, '-2' if there is an error, '-3' if there is no command by name, and a value 0 or greater if it is goto. 
	 */
	private static int header(String cmd, String[]args){
		try{
			switch(cmd){
				case "!import":
					for(String x : args){
						moduleList.add(x);
					}
					break;
				case "!foreground":
					ConsoleProxy.setForeground(new Color(Integer.parseInt(args[0]),
							Integer.parseInt(args[1]),
							Integer.parseInt(args[2])));
					break;
				case "!background":
					ConsoleProxy.setBackground(new Color(Integer.parseInt(args[0]),
							Integer.parseInt(args[1]),
							Integer.parseInt(args[2])));
					break;
				case "!resetColors":
					ConsoleProxy.resetColors();
					break;
				default:
                    JOptionPane.showMessageDialog(GUI.jTextArea2, "OI! that's not right!" + r +"There is an error with a header statement!", "ERROR!", JOptionPane.ERROR_MESSAGE);
					//consoleOutput.add("OI! invalid ! command!"+r);
			}
		} catch(Exception e){
			consoleOutput.add("ERROR WITH HEADER COMMAND");
			return -2;
		}
		return -1;
	}
/**
 * Parses commands with the 'debug.' prefix
 * @param cmd Main part of command ( EX: /debug.help: )
 * @param args All arguments following command ( EX: true )
 * @param startDepth Offset for Multicommand's 'commands' array.
 * @return Returns '-1' if the command is consumed, '-2' if there is an error, '-3' if there is no command by name, and a value 0 or greater if it is goto. 
 */

	private static int debug(String cmd, String[] args, int startDepth){
		switch (cmd){
                    case "/debug.help":
                consoleOutput.clear();
                consoleOutput.add("\u0001COMMAND LIST:" + r
                        + "/ping     PONG!" + r
                        + "/pong    PING!" + r
                        + "/clear     Clears the screen" + r
                        + "/linebreak     Adds a carriage return" + r
                        + "/echo:[String]     Writes a string to the console" + r
                        + "/gettime:[date String]    Outputs the date/time" + r
                        + "/random:[Integer]     Outputs a random number up to the value specified" + r
                        + "/loop:[Integer],([Command])     Loops a command a set number of times" + r
                        + "/if:[Integer][<,>,=,<=,>=][Integer],([True Command]),([False Command])      Checks if a statement is true and, if so, runs a command" + r
                        + "/for:[Integer],[Integer],[Integer],([Command])    Loops a command for a set number of times in certain increments" + r);
                break;
                    case "/debug.headerHelp":
					consoleOutput.add("Available commands:\n"+
							"!import:[key1],[key2],[key3]...\n"+
							"\tImports the module, only useful in lime edit\n" +
							"!foreground:[int R],[int G],[int B]\n" +
							"\tChanges the foreground color (not the setting)" +
							"!background:[int R],[int G],[int B]\n" +
							"\tChanges the background color (not the setting)\n" +
							"!resetColors\n" +
							"\tResets the colors to the user setting\n" +
                                                        "NOTE: Affects main window!" + r);
					break;
			case "/debug.close":
				try{
				args = parseArgs(args, 0, args.length, startDepth, consoleOutput);
				System.exit(Integer.parseInt(args[0]));
				}catch(Exception q){
					consoleOutput.add("OI! There is an error with your get close command!");
				}
				break;
			case "/debug.getVar":
				try{
					String name = args[0];
					if(stringList.get(name) == null){
						if(booleanList.get(name) == null){
							if(intList.get(name) == null){
								consoleOutput.add("There is no variable in memory by that name!");
							} else {
								consoleOutput.add(intList.get(name)+"");
							}
						} else {
							consoleOutput.add(booleanList.get(name)+"");
						}
					} else {
						consoleOutput.add(stringList.get(name)+"");
					}
				} catch (Exception p){
					consoleOutput.add("OI! There is an error with your get variable command!");
				}
				break;
			case "/debug.clearVar":
				try{
					String name = args[0];
					if(stringList.remove(name) == null && booleanList.remove(name) == null && intList.remove(name) == null){
						consoleOutput.add("There is no variable in memory by that name!");
					} else{
						consoleOutput.add("Variable " + name + " removed from memory!");
					}
				} catch(Exception p){
					consoleOutput.add("OI! There is an error with your clear variable command!");
				}
				break;
			case "/debug.varType":
				try{
					String name = args[0];
					if(stringList.get(name) == null){
						if(booleanList.get(name) == null){
							if(intList.get(name) == null){
								consoleOutput.add("There is no variable in memory by that name!");
							} else {
								consoleOutput.add("Variable is an Integer");
							}
						} else {
							consoleOutput.add("Variable is a Boolean");
						}
					}else {
						consoleOutput.add("Variable is a String");
					}
				} catch(Exception e){
					consoleOutput.add("OI! There is an error with your variable type command!");
				}
				break;
			case "/debug.listModules":
				try{
					String[] name = ModuleManager.getList();
					File[] file = ModuleManager.getFiles();
					for(int x = 0; x < name.length; x++){
						consoleOutput.add(name[x] + ", true name is "+file[x].getName());
					}
				} catch(Exception e){
					consoleOutput.add("OI! I dun broke it!");
				}
				break;
			default:
				return parseInput(cmd, args, startDepth);
		}
		return -1;
	}
	/**
	 * Parses all commands without arguments.
	 * @param cmd Main part of command ( EX: /debug.help: )
	 * @param args All arguments following command ( EX: true )
	 * @param startDepth Offset for Multicommand's 'commands' array.
	 * @return Returns '-1' if the command is consumed, '-2' if there is an error, '-3' if there is no command by name, and a value 0 or greater if it is goto. 
	 */
    private static int parseInput(String cmd, String[] args, int startDepth) {
        switch (cmd) {
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
	    return -1;
    }
    /**
     * Parses commands with one or more arguments.
     * @param cmd Main part of command ( EX: /debug.help: )
     * @param args All arguments following command ( EX: true )
     * @param startDepth Offset for Multicommand's 'commands' array.
     * @return Returns '-1' if the command is consumed, '-2' if there is an error, '-3' if there is no command by name, and a value 0 or greater if it is goto. 
     */
    private static int parseAdvanceCommand(String cmd, String[] args, int startDepth) {
		try{
			switch(cmd){
    			case "/echo":
    				args = parseArgs(args, 0, args.length, startDepth, consoleOutput);
    				String out = args[0];
    				for(int x = 1; x < args.length; x++){
    					out += ", "+args[x];
    				}
    				consoleOutput.add(out);
    				break;
			    case "/random":
				    try{
					    args = parseArgs(args, 0, 1, startDepth, consoleOutput);
					    int var = Integer.parseInt(args[0]);
					    Random random = new Random();
					    consoleOutput.add(random.nextInt(var)+"");
				    } catch(Exception p){
					    consoleOutput.add("OI! That's not a integer! Try inputting a integer!");
					    return -2;
				    }
				    break;
			    case "/loop":
				    try{
					    args = parseArgs(args, 0, 1, startDepth, consoleOutput);
					    int var1 = Integer.parseInt(args[0]);
					    String command = args[1];
					    while(var1 != 0){
						    var1--;
							int y = doCommand(command, startDepth);
							if(y != -1){
								return y;
							}
					    }
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your loop statement!");
					    return -2;
				    }
				    break;
			    case "/for":
				    try{
						args = parseArgs(args, 0, 3, startDepth, consoleOutput);
					    int var2 = Integer.parseInt(args[1]);
					    int var3 = Integer.parseInt(args[2]);
					    String command = args[3];
					    for(int var1 = Integer.parseInt(args[0]); var1 < var2; var1 += var3){
							int y = doCommand(command, startDepth);
							if(y != -1){
								return y;
							}
					    }
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your for statement!");
					    return -2;
				    }
				    break;
			    case "/String":
				    try{
						args = parseArgs(args, 0, 2, startDepth, consoleOutput);
					    String name = args[0];
					    String string = args[1];
						Boolean verbose = false;
						if(args.length > 2){
							verbose = Boolean.parseBoolean(args[2]);
						}

					    booleanList.remove(name);
					    intList.remove(name);
					    stringList.put(name, string);
						if(verbose){
						    consoleOutput.add("String " + name + " set to " + string);
						}
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your String declaration statement!");
					    return -2;
				    }
				    break;
			    case "/Int":
				    try{
						args = parseArgs(args, 0, 2, startDepth, consoleOutput);
						Boolean verbose = false;
						if(args.length > 2){
							verbose = Boolean.parseBoolean(args[2]);
						}
					    String name = args[0];
					    int integer = Integer.parseInt(args[1]);

					    booleanList.remove(name);
					    stringList.remove(name);
					    intList.put(name, integer);
					    if(verbose){
						    consoleOutput.add("Integer " + name + " set to " + integer);
					    }
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your Integer declaration statement!");
					    return -2;
				    }
				    break;
			    case "/Boolean":
				    try{
						args = parseArgs(args, 0, 2, startDepth, consoleOutput);
						Boolean verbose = false;
						if(args.length > 2){
							verbose = Boolean.parseBoolean(args[2]);
						}
					    String name = args[0];
					    boolean b = Boolean.parseBoolean(args[1]);

					    stringList.remove(name);
					    intList.remove(name);
					    booleanList.put(name, b);
						if(verbose){
							consoleOutput.add("Boolean " + name + " set to " + b);
						}
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your Boolean declaration statement!");
					    return -2;
				    }
				    break;
			    case "/getTime":
				    try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
					    DateFormat df = new SimpleDateFormat(args[0]);
					    Date dateObj = new Date();
					    consoleOutput.add(df.format(dateObj));
				    } catch(Exception p){
					    consoleOutput.add("OI! That's not a proper date String! Try inputting a date String!");
					    return -2;
				    }
				    break;
			    case "/if":
				    try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						try{
							if(Boolean.parseBoolean(args[0])){
								return doCommand(args[1], startDepth);
							} else {
								if(!args[0].toUpperCase().equals("FALSE")){
									throw new Exception();
								}
								if(args.length > 2){
									return doCommand(args[2], startDepth);
								}
								return doCommand("\u0002", startDepth);
							}
						} catch(Exception e){}
						String[] num = args[0].split("[<=>]+");
						String operator = args[0].split("\\d+")[1];
						int var1 = Integer.parseInt(num[0]);
						int var2 = Integer.parseInt(num[1]);
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
							    if(var1 == var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case "<":
							    if(var1 < var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case ">":
							    if(var1 > var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case "<=":
							    if(var1 <= var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case ">=":
							    if(var1 >= var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }
						    default:
							    consoleOutput.add("OI! Invalid Operator for your if statement!");
							    return -2;
					    }
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your if statement!");
					    return -2;
					}
				case "\u0002":
					break;
				case "/goto":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						int x = Integer.parseInt(args[0]);
						if(x < 0){
							consoleOutput.add("OI! There is no such thing as a negative line!");
							return -2;
						}
						return x - 1;
					} catch(Exception e){
						consoleOutput.add("OI! Not a valid integer");
						return -2;
					}
				case "/last":
					try{
						if(args[0].contains("\u0005")){
							doCommand(args[0], startDepth);
							args[0] = consoleOutput.remove(consoleOutput.size()-1);
						}
						int x = Integer.parseInt(args[0]) - 1;
						while(x > 0){
							consoleOutput.remove(consoleOutput.size()-1);
							x--;
						}
					} catch(Exception e){
						consoleOutput.add("OI! Not a valid integer");
						return -2;
					}
					break;
				case "/flush":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						boolean b = Boolean.parseBoolean(args[0]);
						flush(b);
					} catch(Exception e){
						consoleOutput.add("OI! Not a valid boolean");
						return -2;
					}
					break;
				case "/sleep":
					try{
						args = parseArgs(args, 0, 1, startDepth, consoleOutput);
						Thread.sleep(Integer.parseInt(args[0]));
					} catch(InterruptedException e){
						consoleOutput.add("OI! I just don't know went wrong");
						return -2;
					} catch(NumberFormatException e){
						consoleOutput.add("OI! Not a valid Integer");
						return -2;
					}
					break;
			    default:
				    return ModuleManager.run(moduleList, cmd, args, startDepth, consoleOutput);

		    }
	    } catch(ArrayIndexOutOfBoundsException e){
		    consoleOutput.add("OI! A command didn't have the right number of arguments!");
		    e.printStackTrace();
	    return -2;
    }
	    return -1;
    }
}