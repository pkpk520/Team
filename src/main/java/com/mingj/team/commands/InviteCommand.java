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
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import scala.actors.threadpool.Arrays;

public class InviteCommand extends SubCommand{
	private static final String NAME = "invite";
	public InviteCommand() {
		super( InviteCommand.NAME );
	
	}

	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if( args.length != 2){
			return false;
		}
	
		String[] names = server.getOnlinePlayerNames();
		List<String> namelist = Arrays.asList( names );
		if( !namelist.contains( args[1]) ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +args[1]+" "+I18n.translateToLocal( "cmd.invite.error.offline" )));
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
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.invite.error.sameplayer" )));
			return true;
		}
		TeamList teams = TeamMod.getTeams();
		if( teams.getTeamByUUID( reciver.getUniqueID()) != null ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.invite.error.haveteam" )));
			return true;
		}
		Team team = TeamMod.getTeams().getTeamByUUID( senduuid );
		if( team == null){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.invite.error.noteam" )));
			return true;
		}
		if( team.hasInvite( senduuid, recvuuid) ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.invite.error.alreadyinvite" )+" "+args[1]));
			return true;
		}
		if( team.limit == team.size() ){
			sender.sendMessage(new TextComponentString(TextFormatting.RED +I18n.translateToLocal( "cmd.invite.error.limit" )));
			return true;
		}
		team.createInvite(senduuid, recvuuid);
		TextComponentString accept = new TextComponentString( 
				TextFormatting.WHITE +"["+
				TextFormatting.GREEN+I18n.translateToLocal( "cmd.msg.accept" )+
				TextFormatting.WHITE +"]"
			);
		accept.getStyle().setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team accept "+sender.getName()) );
		TextComponentString reject = new TextComponentString( 
				TextFormatting.WHITE +"["+
				TextFormatting.DARK_RED+I18n.translateToLocal( "cmd.msg.reject" )+
				TextFormatting.WHITE +"]"
			);
		reject.getStyle().setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team reject "+sender.getName()) );
		sender.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+ I18n.translateToLocal( "cmd.invite.success.invite" ) + " " + args[1]));
		reciver.sendMessage(new TextComponentString(TextFormatting.YELLOW+ I18n.translateToLocal( "cmd.invite.receive" ) + " : " +sender.getName()+" , ").
				appendSibling(accept).appendText(" ").appendSibling(reject));
		
		return true;
	}

	@Override
	public String getUsage() {
		return "<player_name>";
	}



}
