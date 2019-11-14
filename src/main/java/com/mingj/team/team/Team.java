package com.mingj.team.team;

import java.util.ArrayList;
import java.util.UUID;


public class Team {
	private ArrayList<UUID> members;
	private String name;
	private UUID owner;
	private boolean pvp;
	private int limit;
	
	public Team(String name, UUID owner){
		this.name = name;
		this.owner = owner;
		this.pvp = false;
		this.limit = 5;
		this.members = new ArrayList<>();
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
		this.members = null;
		this.name = null;
		this.owner = null;
	}
	
	public boolean isMember( UUID uuid ){
		return this.members.contains( uuid );
	}
	
	
}
