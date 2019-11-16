package com.mingj.team.commands;

import java.util.UUID;

import com.mingj.team.TeamMod;
import com.mingj.team.api.command.SubCommand;
import com.mingj.team.team.Team;
import com.mingj.team.team.TeamList;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class ListCommand extends SubCommand{
	public static final String NAME = "list";
	public ListCommand() {
		super( NAME );
	
	}
	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer)sender;
		UUID uuid = player.getUniqueID();
		TeamList teams = TeamMod.getTeams();
		Team team = teams.getTeamByUUID( uuid );
		if( team == null ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.list.error.noteam" )));
			return true;
		}
		player.sendMessage( new TextComponentString( team.getMembersString()) );
		return true;
	}
	@Override
	public String getUsage() {
		return "";
	}


}
