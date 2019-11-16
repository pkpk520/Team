package com.mingj.team.commands;

import java.util.List;
import java.util.UUID;

import com.mingj.team.TeamMod;
import com.mingj.team.api.command.SubCommand;
import com.mingj.team.team.Team;
import com.mingj.team.team.TeamList;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import scala.actors.threadpool.Arrays;

public class KickCommand extends SubCommand{
	public static final String NAME = "kick";
	public KickCommand() {
		super( NAME );
	}
	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if( args.length != 2){
			return false;
		}
	
		String[] names = server.getOnlinePlayerNames();
	
		List<String> namelist = Arrays.asList( names );
		if( !namelist.contains( args[1]) ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +args[1]+" "+I18n.translateToLocal( "cmd.kick.error.offline" )));
			return true;
		}
		UUID kickuuid;
		EntityPlayerMP kick;
		try {
			kick = CommandBase.getPlayer(server, sender, args[1]);
			kickuuid = kick.getUniqueID();
		} catch (CommandException e) {
			e.printStackTrace();
			return true;
		}
		
		EntityPlayer player = (EntityPlayer)sender;
		UUID senduuid = player.getUniqueID();
		if( senduuid == kickuuid ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.kick.error.sameplayer" )));
			return true;
		}
		TeamList teams = TeamMod.getTeams();
		Team team = teams.getTeamByUUID( senduuid );
		if( team == null ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.kick.error.noteam" )));
			return true;
		}
		if( !team.isOwner( senduuid ) ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.kick.error.notowner" )));
			return true;
		}
		if( !team.isMember( kickuuid) ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.kick.error.notmember" )));
			return true;
		}
		team.remove( kickuuid );
		teams.leave( kickuuid );
		team.announce(TextFormatting.YELLOW+I18n.translateToLocal("cmd.kick.success.forteam")+" "+kick.getName() );
		kick.sendMessage( new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.kick.success.forplayer" )));
		return true;
	}
	@Override
	public String getUsage() {
		return "<player_name>";
	}

	

}
