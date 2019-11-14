package com.mingj.team.commands;

import java.util.UUID;

import com.mingj.team.TeamMod;
import com.mingj.team.api.command.SubCommand;
import com.mingj.team.team.SaveData;
import com.mingj.team.team.TeamList;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class CreateCommand extends SubCommand{
	public static final String NAME = "create";
	public CreateCommand() {
		super(CreateCommand.NAME);
	}
	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if( args.length < 2 ){
            return false;
		}
		StringBuilder builder = new StringBuilder();
		for( int i = 1; i < args.length; i ++){
			builder.append( args[i] );
			if( i+1 != args.length ){
				builder.append(" ");
			}
		}
		String name = builder.toString();
		if( name.length() > 15 ){
			sender.sendMessage( new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.create.error.toolong" )) );
            return true;
		}
        TeamList teams = TeamMod.getTeams();
        if( teams.getTeamByName(name) != null) {
            sender.sendMessage( new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.create.error.alreadyexists" )) );
            return true;
        }
        EntityPlayer player = (EntityPlayer)sender;
        UUID uuid = player.getUniqueID();
        if(teams.getTeamByUUID( uuid ) != null) {
            sender.sendMessage( new TextComponentString(TextFormatting.RED + I18n.translateToLocal( "cmd.create.error.haveteam" )) );
            return true;
        }
        teams.createTeam( name, uuid);
        sender.sendMessage( new TextComponentString(TextFormatting.DARK_AQUA+ I18n.translateToLocal( "cmd.create.success.create" ) +" "+ name + " !") );
        return true;
	}
	@Override
	public String getUsage() {
		return "<team_name>";
	}


}
