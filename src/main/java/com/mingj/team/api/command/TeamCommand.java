package com.mingj.team.api.command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeamCommand extends CommandBase{
	public static TeamCommand command;
	private final List<SubCommand> subCommands;
	public TeamCommand( List<SubCommand> subCommand ){
		this.subCommands = subCommand;
		command = this;
		
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if( ! (sender instanceof EntityPlayerMP) ){
			return; 
		}
		if (args.length >= 1) {
            for (SubCommand cmd : this.subCommands) {
                if (cmd.getCommandName().equalsIgnoreCase(args[0]) ){
                    if (!cmd.execute(server, sender, args)) {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + I18n.translateToLocal("cmd.usage")+": /team " + cmd.getCommandName() + ' ' + cmd.getUsage() + " - " + cmd.getInfo()));
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + I18n.translateToLocal("cmd.error")));
                    }
                    return;
                }
            }
        }
		
        sender.sendMessage( new TextComponentString(getUsage(sender)) );
        

	}
	
	@Nonnull
	@Override
	public String getName() {
		return "team";
	}
	
	

	
	@Override
	public String getUsage(@Nonnull ICommandSender arg0) {
		StringBuilder builder = new StringBuilder("Usage:\n");
        for (SubCommand cmd : this.subCommands) {
            builder.append("/team " + cmd.getCommandName()+ " " +cmd.getUsage() + " - " + cmd.getInfo() + "\n");
        }
        return builder.toString();
	}
	
	@Override
	public boolean checkPermission( MinecraftServer server, ICommandSender sender ){
		return true;
	}
	
	@Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return this.subCommands.stream().map( cmd -> cmd.getCommandName()).filter( name -> name.startsWith(args[0])).collect(Collectors.toList());
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("create") ) {
            	return Collections.emptyList();
            }
            if( args[0].equalsIgnoreCase("leave") ){
            	return Collections.emptyList();
            }
            if( args[0].equalsIgnoreCase("help") ){
            	return Collections.emptyList();
            }
            return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().stream().map(EntityPlayer::getName).collect(Collectors.toList());
            
        }
        return Collections.emptyList();
    }
	
	public List<SubCommand> getSubCommands(){
		return this.subCommands;
	}
}
