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

public class InfoCommand extends SubCommand{
	public static final String NAME = "info";
	public InfoCommand() {
		super(NAME);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer)sender;
		UUID uuid = player.getUniqueID();
		TeamList teams = TeamMod.getTeams();
		Team team = teams.getTeamByUUID( uuid );
		if( team == null ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.info.error.noteam" )));
			return true;
		}
		player.sendMessage( new TextComponentString( team.getInfo()) );
		return true;
	}
	@Override
	public String getUsage() {
		return "";
	}

}
