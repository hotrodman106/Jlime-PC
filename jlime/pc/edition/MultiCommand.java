package jlime.pc.edition;

import java.util.ArrayList;

/**
 * Created by Coolway99 on 2015-01-07.
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 * @author hotrodman106 (Java Docs)
 */
public class MultiCommand{
	private final ArrayList<Command> commands = new ArrayList<>();
	private final boolean debug;
	
	/**
	 * Sets debug variable to users preference.
	 *
	 * @param debug Does user want debug commands during normal operation?
	 */
	public MultiCommand(boolean debug){
		this.debug = debug;
	}
	
	/**
	 * Stores executed commands in memory until complete run or users calls
	 * '/flush:true'.
	 *
	 * @param cmd Main part of command. ( EX: /flush: )
	 * @param args All arguments following a command ( EX: true )
	 */
	public void put(String cmd, String[] args){
		this.put(new Command(cmd, args));
	}
	
	/**
	 * Stores executed commands in memory until complete run or users calls
	 * '/flush:true'.
	 *
	 * @param c Created command objected
	 */
	public void put(Command c){
		this.commands.add(c);
	}
	
	/**
	 * Gets a command in memory.
	 *
	 * @param index Desired command's index.
	 * @return Returns command's index.
	 */
	public Command get(int index){
		return this.commands.get(index);
	}
	
	/**
	 * Gets size of command array.
	 *
	 * @return Returns an integer that corresponds to command array's length.
	 */
	public int size(){
		return this.commands.size();
	}
	
	/**
	 * Runs all commands currently in memory.
	 *
	 * @param level Offset in the array. (startdepth)
	 * @return Returns '-1' if the command is consumed, '-2' if there is an
	 * error, '-3' if there is no command by name, and a value 0 or greater if
	 * it is goto.
	 */
	public int run(int level){
		for(Command c : this.commands){
			System.out.println(c);
			int ret = CommandParser.doCommand(c, level, this.debug);
			if(ret != -1){ return ret; }
		}
		return -1;
	}
}