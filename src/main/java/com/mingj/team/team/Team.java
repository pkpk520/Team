package com.mingj.team.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.mingj.team.TeamMod;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;


public class Team {
	private ArrayList<UUID> members;
	private String name;
	private UUID owner;
	private boolean pvp;
	public int limit;
	private HashMap<UUID,ArrayList<UUID>> invite = new HashMap<>();
	
	public Team(String name, UUID owner){
		this.name = name;
		this.owner = owner;
		this.pvp = false;
		this.limit = 5;
		this.members = new ArrayList<>();
		this.members.add( owner );
	}
	
	
	public int size(){
		return this.members.size();
	}
	public boolean pvp(){
		return this.pvp;
	}
	
	public boolean isOwner( UUID uuid ){
		return this.owner == uuid;
	}
	
	public String getName(){
		return this.name;
	}
	public boolean add( UUID uuid ){
		if( this.members.size() < limit ){
			this.members.add( uuid );
			return true;
		}
		return false;
	}
	
	public void remove( UUID uuid ){
		this.members.remove( uuid );
	}
	
	public void delete(){
		this.members.clear();
		this.invite.clear();
		this.invite = null;
		this.members = null;
		this.name = null;
		this.owner = null;

		
	}
	
	public boolean isMember( UUID uuid ){
		return this.members.contains( uuid );
	}
	
	
	public void createInvite( UUID sender, UUID reciver){
		if( !this.invite.containsKey(sender) ){
			this.invite.put(sender,new ArrayList<UUID>());
		}
		this.invite.get( sender ).add( reciver );
	}
	
	public boolean hasInvite( UUID sender, UUID reciver ){
		return this.invite.containsKey( sender ) && this.invite.get(sender).contains( reciver );
	}
	
	public void removeInvite( UUID reciver ){
		this.invite.forEach((sender, invitelist)->{

			if( invitelist.contains( reciver ) ){
				invitelist.remove( reciver );
			}
			
		});
		
	}
	
	public void announce( String msg ){
	
	
		this.members.forEach((uuid) -> {
			EntityPlayerMP player = TeamMod.server.getPlayerList().getPlayerByUUID(uuid);
			player.sendMessage( new TextComponentString(msg) );
		});
	}
	
	public void announceExcept( String msg, UUID exceptuuid ){
	
		this.members.forEach((uuid) -> {
			if( exceptuuid != uuid ){
				EntityPlayerMP player = TeamMod.server.getPlayerList().getPlayerByUUID(uuid);
				player.sendMessage( new TextComponentString(msg) );
			}
		});
	}
	
	public ArrayList<UUID> getMembers(){
		return this.members;
	}
	
	public String getMembersString(){
		StringBuilder builder = new StringBuilder();
		for( UUID uuid : this.members ){
			builder.append( TeamMod.server.getPlayerList().getPlayerByUUID(uuid).getName() );
			if( this.members.indexOf( uuid )+1 != this.members.size() ){
				builder.append(" , ");
			}
		}
		return builder.toString();
	}
	
	public String getInfo(){
		StringBuilder builder = new StringBuilder();
		builder.append( I18n.translateToLocal("cmd.msg.teamname") );
		builder.append(" : ");
		builder.append( this.name );
		builder.append("\n");
		builder.append( I18n.translateToLocal("cmd.msg.captain") );
		builder.append(" : ");
		builder.append( TeamMod.server.getPlayerList().getPlayerByUUID(this.owner).getName() );
		builder.append("\n");
		builder.append( I18n.translateToLocal("cmd.msg.membercount") );
		builder.append(" : ");
		builder.append( this.members.size());
		builder.append("/");
		builder.append( this.limit );
		builder.append("\n");
		builder.append( I18n.translateToLocal("cmd.msg.member"));
		builder.append(" : ");
		builder.append( this.getMembersString() );
		return builder.toString();
	}
}
