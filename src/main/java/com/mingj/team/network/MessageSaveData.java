package com.mingj.team.network;

import com.mingj.team.handlers.ClientEventHandler;
import com.mingj.team.team.SaveData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.*;

public class MessageSaveData implements IMessage {

    private NBTTagCompound tagTeam = new NBTTagCompound();

    public MessageSaveData() {

    }

    public MessageSaveData(Map<String,List<UUID>> teamsMap) {

        NBTTagList tagList = new NBTTagList();
        Iterator<String> iteratorTeams = teamsMap.keySet().iterator();
        while(iteratorTeams.hasNext()) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            String team = iteratorTeams.next();
            tagCompound.setString("Team Name",team);


            NBTTagList playerListTag = new NBTTagList();
            Iterator<UUID> uuidIterator = teamsMap.get(team).iterator();
            while(uuidIterator.hasNext()) {
                UUID id = uuidIterator.next();
                NBTTagCompound playerTag = new NBTTagCompound();
                playerTag.setString("uuid",id.toString());
                playerListTag.appendTag(playerTag);
            }
            tagCompound.setTag("Player List",playerListTag);
            tagList.appendTag(tagCompound);
        }
        tagTeam.setTag("Teams",tagList);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tagTeam = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,tagTeam);
    }

    public static class MessageHandler implements IMessageHandler<MessageSaveData, IMessage> {
        @Override
        public IMessage onMessage(MessageSaveData message, MessageContext ctx) {
            NBTTagCompound nbt = message.tagTeam;
            Minecraft.getMinecraft().addScheduledTask(() -> {
                SaveData.teamsMap.clear();
                SaveData.teamMap.clear();
                ClientEventHandler.skinMap.clear();
                String name = "";
                Iterator<NBTBase> tagList = nbt.getTagList("Teams", Constants.NBT.TAG_COMPOUND).iterator();
                while(tagList.hasNext()) {
                    NBTTagCompound tagCompound = (NBTTagCompound)tagList.next();
                    Iterator<NBTBase> playerTagListIterator = tagCompound.getTagList("Player List",Constants.NBT.TAG_COMPOUND).iterator();
                    List<UUID> uuidList = new ArrayList();
                    while(playerTagListIterator.hasNext()) {
                        NBTTagCompound playerTag = (NBTTagCompound)playerTagListIterator.next();
                        UUID id = UUID.fromString(playerTag.getString("uuid"));
                        AbstractClientPlayer p = (AbstractClientPlayer)FMLClientHandler.instance().getWorldClient().getPlayerEntityByUUID(id);
                        name = tagCompound.getString("Team Name");
                        if(p!=null) {
                            ClientEventHandler.skinMap.put(p.getDisplayNameString(),p.getLocationSkin());
                            ClientEventHandler.idtoNameMap.put(id,p.getDisplayNameString());
                        }
                        SaveData.teamMap.put(id,name);
                        uuidList.add(id);
                    }
                    SaveData.teamsMap.put(name,uuidList);
                }
            });
            return null;
        }
    }

}
