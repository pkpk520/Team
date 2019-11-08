package com.mingj.team.network;

import com.mingj.team.client.GuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageHunger implements IMessage {

    private NBTTagCompound tag = new NBTTagCompound();


    public MessageHunger() {

    }

    public MessageHunger(UUID id,int hunger,int health) {
        tag.setString("id",id.toString());
        tag.setInteger("hunger",hunger);
        tag.setInteger("health",health);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,tag);
    }

    public static class MessageHandler implements IMessageHandler<MessageHunger, IMessage> {
        @Override
        public IMessage onMessage(MessageHunger message, MessageContext ctx) {
            NBTTagCompound tagCompound = message.tag;
            Minecraft.getMinecraft().addScheduledTask(() -> {
                GuiHandler.healthMap.put(UUID.fromString(tagCompound.getString("id")),tagCompound.getInteger("health"));
                GuiHandler.hungerMap.put(UUID.fromString(tagCompound.getString("id")),tagCompound.getInteger("hunger"));
            });
            return null;
        }
    }
}
