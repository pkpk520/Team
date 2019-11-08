package com.mingj.team.api.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class SubCommand {
	private final String commandName;
	public SubCommand( String commandName ){
		this.commandName = commandName;
	}
	public abstract boolean execute( MinecraftServer server, ICommandSender sender, String[] args );
	
	public abstract String getUsage();
	
	public String getCommandName(){
		return this.commandName;
	}
	public abstract String getInfo();
}
