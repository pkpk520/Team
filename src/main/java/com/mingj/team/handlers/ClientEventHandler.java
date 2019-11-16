package com.mingj.team.handlers;

import com.mingj.team.TeamMod;
import com.mingj.team.client.Keybind;
import com.mingj.team.team.SaveData;
import com.mingj.team.team.Team;
import com.mojang.realmsclient.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientEventHandler {

    public static Map<String, Pair<String,Long>> chatMap = new HashMap<>();
    public static boolean displayHud = true;
    public static long ticks = 0;
    public static Map<String,ResourceLocation> skinMap = new HashMap<>();
    public static Map<UUID,String> idtoNameMap = new HashMap<>();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        ticks+=1;
    }

    public boolean isPlayer( String text ){
    	
    	return true;
    }
    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
    	System.out.print( "\n trigger client chat");
        String text = event.getMessage().getUnformattedText();
        if( !text.contains(">")){
        	return;
        }
    	int slice = text.indexOf(">") + 1;
        
        if(slice>0) {
            
        	String playerName = text.substring(1,slice-1);
        	EntityPlayer ply = FMLClientHandler.instance().getClient().world.getPlayerEntityByName(playerName);
        	if( ply == null ){
        		return;
        	}
            if(!ConfigHandler.client.disableChatBubble) {
                Pair<String,Long> temp = Pair.of(text.substring(slice+1),ticks);
                chatMap.put(playerName,temp);
            }
            if(!ConfigHandler.client.disablePrefix) {
                UUID uuid = ply.getUniqueID();
                Team team = TeamMod.getTeams().getTeamByUUID(uuid);
                if( team != null) {
                    String message = "[" + team.getName() + "]" + " <" + playerName + "> "  + text.substring(slice+1);
                    event.setMessage(new TextComponentString(message));
                }
            }
            if(!ConfigHandler.client.disablePing) {
                EntityPlayerSP p = Minecraft.getMinecraft().player;
                String team = SaveData.teamMap.get(p.getUniqueID());
                if(text.substring(slice).contains(p.getDisplayNameString()) || 
                		(team!=null && ((text.substring(slice).contains(' ' + team + ' ')) || 
                				text.substring(slice).equals(' ' + team) || 
                				text.indexOf(team)==text.length()-team.length()))) {
                    Style newStyle = new Style();
                    newStyle.setBold(true);
                    event.setMessage(event.getMessage().setStyle(newStyle));
                    p.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,1.0F,5.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(Keybind.display.isPressed()) {
            displayHud=!displayHud;
        }
    }
}
