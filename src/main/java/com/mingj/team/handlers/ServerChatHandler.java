package com.mingj.team.handlers;

import java.util.UUID;

import com.mingj.team.TeamMod;
import com.mingj.team.team.SaveData;
import com.mingj.team.team.Team;
import com.mojang.realmsclient.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerChatHandler {
	@SubscribeEvent
	public void onServerChatEvent( ServerChatEvent evt ){
		
		
		
	
    	EntityPlayer ply = evt.getPlayer() ;
    	if( ply == null ){
    		return;
    	}
       
        if(!ConfigHandler.client.disablePrefix) {
        	
            UUID uuid = ply.getUniqueID();
            Team team = TeamMod.getTeams().getTeamByUUID(uuid);
       
            if( team != null) {
                String message = "[" + team.getName() + "]" + " <" + ply.getName() + "> "  + evt.getMessage();
                evt.setComponent(new TextComponentString(message));
            }
        }
           
	}
}
