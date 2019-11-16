package com.mingj.team.team;

import java.util.UUID;

public class TeamInvite {
	private UUID sender; 
	private UUID reciever;
	public TeamInvite( UUID sender , UUID recv){
		this.sender = sender;
		this.reciever = recv;
	}
}
