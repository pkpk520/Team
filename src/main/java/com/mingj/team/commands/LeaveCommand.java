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
        Team team = teams.getTeamByUUID( uuid );
        
        if( team == null){
        	sender.sendMessage( new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.leave.error.noteam" )) );
        	return false;
        }
        
		if( team.isOwner(uuid) ){
			String msg = TextFormatting.RED + I18n.translateToLocal( "cmd.leave.success.ownerleave" ) ;
			team.announce( msg);
			team.getMembers().forEach( uid -> {
				teams.leave( uid );
			});
			teams.disband( team.getName() );
			team = null;
		
		}else{
			team.remove( uuid );
			teams.leave( uuid );
			String msg = sender.getName() + " " + I18n.translateToLocal( "cmd.leave.success.alreadyleave" );
			team.announce( msg);
			sender.sendMessage( new TextComponentString(TextFormatting.DARK_AQUA + I18n.translateToLocal( "cmd.leave.success.leave" )) );
		}
        
        
		return true;
	}
	@Override
	public String getUsage() {
		return "";
	}

	

}
