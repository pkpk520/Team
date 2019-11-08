package com.mingj.team.handlers;

import com.mingj.team.TeamMod;
import com.mingj.team.network.*;
import com.mingj.team.team.SaveData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommonEventHandler {

    public static int ticks = 0;

    @SubscribeEvent
    public static void tickEvent(TickEvent.ServerTickEvent event) {
        ticks+=1;
        if (ticks>20) {
            Iterator<EntityPlayerMP> playerMPIterator = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().iterator();
            while(playerMPIterator.hasNext()) {
                EntityPlayerMP playerMP = playerMPIterator.next();
                PacketHandler.INSTANCE.sendToAll(new MessageHunger(playerMP.getUniqueID(),playerMP.getFoodStats().getFoodLevel(),(int)Math.ceil(playerMP.getHealth())));
            }
            ticks=0;
        }
    }

    @SubscribeEvent
    public static void arrowShootPlayer(LivingAttackEvent event) {
        if (!ConfigHandler.server.enableFriendlyFire && event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) event.getSource().getTrueSource();
            EntityPlayer target = (EntityPlayer)event.getEntityLiving();
            String targetTeam = null;
            String attackerTeam = null;
            if(SaveData.teamMap.containsKey(target.getUniqueID())) {
                targetTeam = SaveData.teamMap.get(target.getUniqueID());
            }
            if(SaveData.teamMap.containsKey(attacker.getUniqueID())) {
                attackerTeam = SaveData.teamMap.get(attacker.getUniqueID());
            }
            if(targetTeam!=null && attackerTeam!=null && targetTeam.equals(attackerTeam)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void playerHitPlayer(AttackEntityEvent event) {
        if(!ConfigHandler.server.enableFriendlyFire && (event.getEntityLiving() instanceof EntityPlayer) && event.getTarget() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
            EntityPlayer target = (EntityPlayer)event.getTarget();
            EntityPlayer attacker = event.getEntityPlayer();
            String targetTeam = null;
            String attackerTeam = null;
            if(SaveData.teamMap.containsKey(target.getUniqueID())) {
                targetTeam = SaveData.teamMap.get(target.getUniqueID());
            }
            if(SaveData.teamMap.containsKey(attacker.getUniqueID())) {
                attackerTeam = SaveData.teamMap.get(attacker.getUniqueID());
            }
            if(targetTeam!=null && attackerTeam!=null && targetTeam.equals(attackerTeam)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void configUpdate(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(TeamMod.MODID)) {
            ConfigManager.sync(TeamMod.MODID, Config.Type.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.player.getEntityWorld().isRemote) {
            PacketHandler.INSTANCE.sendToAll(new MessageSaveData(SaveData.teamsMap));
            if(((EntityPlayerMP)event.player).getStatFile().readStat(StatList.LEAVE_GAME)==0) {
                event.player.sendMessage(new TextComponentString("Welcome to the server! This server has a teams system allowing you to disable PvP, sync advancements and see health/hunger of your teammates!\nType /team to get started"));
            }
        }
    }

    @SubscribeEvent
    public static void playerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if(!event.player.getEntityWorld().isRemote) {
            PacketHandler.INSTANCE.sendToAll(new MessageSaveData(SaveData.teamsMap));
            PacketHandler.INSTANCE.sendTo(new MessageClear(),(EntityPlayerMP)event.player);
        }
    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        if(!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            try {
                SaveData.get(event.getWorld());
            } catch (NoClassDefFoundError ex) {}
            PacketHandler.INSTANCE.sendToAll(new MessageSaveData(SaveData.teamsMap));
        }
        if(!event.getWorld().isRemote && !ConfigHandler.server.disableAchievementSync) {
            if (event.getEntity() instanceof EntityPlayer) {
                if(SaveData.teamMap.containsKey(event.getEntity().getUniqueID())) {
                    String team = SaveData.teamMap.get(event.getEntity().getUniqueID());
                    SaveData.syncPlayers(team,(EntityPlayerMP)event.getEntity());

                }
            }
        }
    }

    @SubscribeEvent
    public static void death(LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            if(SaveData.teamMap.containsKey(event.getEntity().getUniqueID())) {
                String team = SaveData.teamMap.get(event.getEntity().getUniqueID());
                Iterator<UUID> uuidIterator = SaveData.teamsMap.get(team).iterator();
                while(uuidIterator.hasNext()) {
                    UUID uuid = uuidIterator.next();
                    if(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid)!=null) {
                        PacketHandler.INSTANCE.sendTo(new MessageDeath(),FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void achievementGet(AdvancementEvent event) {
        if(!ConfigHandler.server.disableAchievementSync) {
            EntityPlayer player = event.getEntityPlayer();
            if(SaveData.teamMap.containsKey(player.getUniqueID()) && !event.getEntity().getEntityWorld().isRemote) {
                String team = SaveData.teamMap.get(player.getUniqueID());
                Iterator<UUID> uuidIterator = SaveData.teamsMap.get(team).iterator();
                while(uuidIterator.hasNext()) {
                    UUID id = uuidIterator.next();
                    EntityPlayerMP playerMP = (EntityPlayerMP)player.getEntityWorld().getPlayerEntityByUUID(id);
                    SaveData.syncPlayers(team,playerMP);
                }
            }
        }
    }

}
