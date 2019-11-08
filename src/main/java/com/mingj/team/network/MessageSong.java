package com.mingj.team.network;

import com.mingj.team.TeamMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSong implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class MessageHandler implements IMessageHandler<MessageSong,IMessage> {
        @Override
        public IMessage onMessage(MessageSong message, MessageContext ctx) {
            FMLClientHandler.instance().getClient().addScheduledTask(() -> {
               FMLClientHandler.instance().getClientPlayerEntity().playSound(TeamMod.SONG,200.0F,1.0F);
            });
            return null;
        }
    }
}
