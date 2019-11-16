package com.mingj.team.api.command;



import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

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
	public String getInfo(){
		return I18n.translateToLocal( "cmd." + this.commandName + ".info" );
	};
}
