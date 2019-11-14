package com.mingj.team.commands;

import com.mingj.team.api.command.SubCommand;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class AcceptCommand extends SubCommand{
	public static final String NAME = "create";
	public AcceptCommand() {
		super(NAME);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
