package com.mingj.team.commands;

import com.mingj.team.api.command.SubCommand;
import com.mingj.team.api.command.TeamCommand;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class HelpCommand extends SubCommand{
	private static final String NAME= "help";
	public HelpCommand() {
        super( HelpCommand.NAME );
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (TeamCommand.command != null) {
        	
            sender.sendMessage(new TextComponentString( TextFormatting.GOLD +"Team Commands: "));
            for (SubCommand cmd : TeamCommand.command.getSubCommands()) {
            	
            	if( cmd.getCommandName().equals( HelpCommand.NAME ) )continue;
            	
            	sender.sendMessage(new TextComponentString("/team " + cmd.getCommandName() + ' ' + cmd.getUsage() + 
                		TextFormatting.GRAY + " - " + TextFormatting.AQUA + cmd.getInfo() ));
                
                
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInfo() {
        return "";
    }
}
