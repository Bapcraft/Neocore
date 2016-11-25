package io.neocore.api.cmd;

import java.util.List;

public class AbstractTreeCommand extends AbstractCommand {
	
	private String rootPerm;
	private List<AbstractCommand> subcommands;
	
	public AbstractTreeCommand(String name, String perm, List<AbstractCommand> subs) {
		
		super(name);
		
		this.rootPerm = perm;
		this.subcommands = subs;
		
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission(this.rootPerm)) this.noPerms();
		
		// We need to have at least one base argument.
		if (args.length >= 1) this.badUsage();
		
		// Now we determine which subcommand to use.
		for (AbstractCommand ac : this.subcommands) {
			
			if (ac.getName().equalsIgnoreCase(args[0])) {
				
				// Figure out the remainings arguments.
				String[] subArgs = new String[args.length - 1];
				for (int i = 1; i < args.length; i++) {
					subArgs[i - 1] = args[i];
				}
				
				ac.onExecute(sender, subArgs);
				return;
				
			}
			
		}
		
		// At this point we can assume there were no children, so assume bad syntax.
		this.badUsage();
		
	}

}
