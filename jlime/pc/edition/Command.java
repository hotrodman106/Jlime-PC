package jlime.pc.edition;

import java.util.ArrayList;

/**
 * A class programmably representing a command
 *
 * @author Coolway99
 * @author hotrodman106 (Added Java Docs)
 */
public class Command{
	private final String cmd;
	private final ArrayList<String> args;
	
	/**
	 * Creates a basic command
	 * @param cmd The command
	 */
	public Command(String cmd){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = new ArrayList<>();
	}
	
	/**
	 * Creates an advanced command object.
	 * @param cmd The command
	 * @param args Starting arguments for the command
	 */
	public Command(String cmd, String[] args){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = new ArrayList<>();
		for(String s : args){
			this.args.add(s);
		}
	}
	
	/**
	 * Creates an advanced command
	 * @param cmd The command
	 * @param args Starting arguments for the command
	 */
	public Command(String cmd, ArrayList<String> args){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = args;
	}
	
	/**
	 * Gets command object
	 * @return Command object
	 */
	public String getCmd(){
		return cmd;
	}
	
	/**
	 * Gets all arguments associated with command
	 * @return Array of all arguments
	 */
	public String[] getArgs(){
		return args.toArray(new String[0]);
	}
	
	/**
	 * Adds arguments to the command
	 * @param arg Argument to add
	 */
	public void addArg(String arg){
		this.args.add(arg);
	}
	
	/**
	 * Adds a few arguments to the command
	 * @param args A list of arguments to be added
	 */
	public void addArgs(String[] args){
		for(String s : args){
			this.args.add(s);
		}
	}
	
	/**
	 * Adds a few arguments to the command
	 * @param args A list of arguments to be added
	 */
	public void addArgs(ArrayList<String> args){
		this.args.addAll(args);
	}

	@Override
	/**
	 * Converts the command to a string.
	 * @return A String representing the command
	 */
	public String toString(){
		System.out.println(cmd);
		StringBuilder temp = new StringBuilder(cmd);
		if(args != null){
			temp.append(":");
			for(String s : args){
				temp.append(s);
				temp.append(",");
			}
		}
		return temp.toString();
	}
}
