package com.mingj.team.commands;

import java.util.UUID;

import com.mingj.team.TeamMod;
import com.mingj.team.api.command.SubCommand;
import com.mingj.team.team.TeamList;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class LeaveCommand extends SubCommand{
	public static final String NAME = "leave";
	public LeaveCommand() {
		super(NAME);
		
	}
	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if( args.length != 1){
			sender.sendMessage( new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.leave.error.noargs" )) );
			return false;		
		}
		TeamList teams = TeamMod.getTeams();
		EntityPlayer player = (EntityPlayer)sender;
        UUID uuid = player.getUniqueID();
        if( teams.getTeamByUUID(uuid) == null){
        	sender.sendMessage( new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.leave.error.noteam" )) );
        	return false;
        }
        teams.leave( uuid );
        sender.sendMessage( new TextComponentString(TextFormatting.DARK_AQUA + I18n.translateToLocal( "cmd.leave.success.leave" )) );
		return true;
	}
	@Override
	public String getUsage() {
		return "";
	}

	

}
