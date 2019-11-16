package com.mingj.team.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.mingj.team.TeamMod;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class TeamList extends WorldSavedData{
	private static HashMap<UUID,Team> playerTeam = new HashMap<>();
	private static HashMap<String,Team> teamList = new HashMap<>();
	
	public TeamList() {
		super( TeamMod.MODID );
	}

	
	public static TeamList load(){
		return new TeamList();
	}
	
	public static TeamList save(World world){
		MapStorage storage = world.getMapStorage();
        TeamList data = (TeamList)storage.getOrLoadData( TeamList.class,TeamMod.MODID );
        if (data==null) {
            data = new TeamList();
            world.setData(TeamMod.MODID,data);
        }
        return data;
	}


	@Override
	public void readFromNBT(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean createTeam(String name, UUID uuid){
		Team team = new Team( name, uuid );
		teamList.put(name, team);
		playerTeam.put( uuid, team );
		return true;
	}
	
	public Team getTeamByUUID( UUID uuid ){
		return playerTeam.getOrDefault( uuid, null);
	}
	
	public Team getTeamByName( String name ){
		return teamList.getOrDefault(name, null);
	}
	
	public HashMap<String,Team> getTeamList(){
		return teamList;
	}
	
	public void leave( UUID uuid ){
		playerTeam.remove( uuid );
	}
	
	public void disband( String teamname ){
		this.getTeamByName( teamname ).delete();
		teamList.remove( teamname );
	}
	public void join(UUID uuid ,Team team ){
		team.add( uuid );
		team.removeInvite( uuid );
		this.playerTeam.put( uuid, team);
	}
}
