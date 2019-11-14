package com.mingj.team.commands;

import com.mingj.team.api.command.SubCommand;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;

public class InviteCommand extends SubCommand{
	private static final String NAME = "invite";
	public InviteCommand() {
		super( InviteCommand.NAME );
	
	}

	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		sender.sendMessage(new TextComponentString("invite"));
		return false;
	}

	@Override
	public String getUsage() {
		return "<player_name>";
	}



}
