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

public class AcceptCommand extends SubCommand{
	public static final String NAME = "accept";
	public AcceptCommand() {
		super(NAME);
	}
	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if( args.length != 2){
			return false;
		}
	
		String[] names = server.getOnlinePlayerNames();
		List<String> namelist = Arrays.asList( names );
		if( !namelist.contains( args[1]) ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +args[1]+" "+I18n.translateToLocal( "cmd.accept.error.offline" )));
			return true;
		}
		UUID recvuuid;
		EntityPlayerMP reciver;
		try {
			reciver = CommandBase.getPlayer(server, sender, args[1]);
			recvuuid = reciver.getUniqueID();
		} catch (CommandException e) {
			e.printStackTrace();
			return true;
		}
		EntityPlayer player = (EntityPlayer)sender;
		UUID senduuid = player.getUniqueID();
		
		if( senduuid == recvuuid ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.accept.error.sameplayer" )));
			return true;
		}
		TeamList teams = TeamMod.getTeams();
		if( teams.getTeamByUUID( senduuid ) != null){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.accept.error.haveteam" )));
			return true;
		}
		Team team = teams.getTeamByUUID( recvuuid );
		if( team == null){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.accept.error.noteam" )));
			return true;
		}
		if( !team.hasInvite( recvuuid, senduuid) ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.accept.error.noinvite" )));
			return true;
		}
		teams.join(senduuid, team);
		String msg =  TextFormatting.DARK_AQUA + player.getName()+" "+I18n.translateToLocal( "cmd.accept.success.accept" );
		team.announceExcept( msg,senduuid);
		sender.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA +I18n.translateToLocal( "cmd.accept.success.intoteam" )+" "+team.getName()));
		return true;
	}
	@Override
	public String getUsage() {
		return "<player_name>";
	}
	
	
}
