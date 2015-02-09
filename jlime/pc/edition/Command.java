package jlime.pc.edition;

import java.util.ArrayList;

/**
 * Created by Coolway99 on 2015-01-24
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 * @author hotrodman106 (Java Docs)
 */
public class Command{
	private final String cmd;
	private final ArrayList<String> args;
	/**
	 * Creates a basic command object.
	 * @param cmd Main command
	 */
	public Command(String cmd){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = new ArrayList<>();
	}
	/**
	 * Creates an advanced command object.
	 * @param cmd Main command
	 * @param args All arguments
	 */
	public Command(String cmd, String[] args){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = new ArrayList<>();
		for(String s : args){
			this.args.add(s);
		}
	}
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
	 * Add arguments to command object.
	 * @param arg Argument to add
	 */
	public void addArg(String arg){
		this.args.add(arg);
	}
	public void addArgs(String[] args){
		for(String s : args){
			this.args.add(s);
		}
	}
	public void addArgs(ArrayList<String> args){
		this.args.addAll(args);
	}

	@Override
	/**
	 * Converts command object to string.
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
