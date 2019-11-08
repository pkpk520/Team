package com.mingj.team;

import java.util.Arrays;

import com.mingj.team.api.command.TeamCommand;
import com.mingj.team.client.GuiHandler;
import com.mingj.team.client.Keybind;
import com.mingj.team.commands.CommandTeam;
import com.mingj.team.commands.HelpCommand;
import com.mingj.team.commands.InviteCommand;
import com.mingj.team.handlers.ClientEventHandler;
import com.mingj.team.network.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
		modid = TeamMod.MODID, 
		name = TeamMod.NAME, 
		version = TeamMod.VERSION
		)
public class TeamMod
{


    public static final SoundEvent SONG = new SoundEvent(new ResourceLocation("teamsmod","time_to_mine"));

    public static final String MODID = "teammod";
    public static final String NAME = "Teams Mod";
    public static final String VERSION = "1.1";

    @Instance(MODID)
    public static TeamMod instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.INSTANCE.registerMessage(MessageSaveData.MessageHandler.class,MessageSaveData.class,0, Side.SERVER);
        PacketHandler.INSTANCE.registerMessage(MessageHunger.MessageHandler.class, MessageHunger.class,1,Side.SERVER);
        PacketHandler.INSTANCE.registerMessage(MessageClear.MessageHandler.class,MessageClear.class,5,Side.SERVER);
        PacketHandler.INSTANCE.registerMessage(MessageDeath.MessageHandler.class,MessageDeath.class,3,Side.SERVER);
        PacketHandler.INSTANCE.registerMessage(MessageGui.MessageHandler.class, MessageGui.class,4,Side.SERVER);
        if(FMLCommonHandler.instance().getSide()==Side.CLIENT) {
            Keybind.register();
            MinecraftForge.EVENT_BUS.register(GuiHandler.instance());
            MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
            PacketHandler.INSTANCE.registerMessage(MessageSaveData.MessageHandler.class,MessageSaveData.class,0,Side.CLIENT);
            PacketHandler.INSTANCE.registerMessage(MessageHunger.MessageHandler.class, MessageHunger.class,1,Side.CLIENT);
            PacketHandler.INSTANCE.registerMessage(MessageClear.MessageHandler.class,MessageClear.class,5,Side.CLIENT);
            PacketHandler.INSTANCE.registerMessage(MessageDeath.MessageHandler.class,MessageDeath.class,3,Side.CLIENT);
            PacketHandler.INSTANCE.registerMessage(MessageGui.MessageHandler.class, MessageGui.class,4,Side.CLIENT);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
    	TeamCommand command = new TeamCommand(Arrays.asList(
    			new InviteCommand(),
    			new HelpCommand()
    			));
        event.registerServerCommand( command );
        
        if(FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
            PacketHandler.INSTANCE.sendToAll(new MessageClear());
        }
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {
        PacketHandler.INSTANCE.sendToAll(new MessageClear());
    }
}
