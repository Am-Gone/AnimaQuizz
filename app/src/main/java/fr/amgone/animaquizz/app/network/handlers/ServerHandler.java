package fr.amgone.animaquizz.app.network.handlers;

import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final PacketListener packetListener;

    public ServerHandler(PacketListener packetListener) {
        this.packetListener = packetListener;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        new Thread(() -> {
            try {
                Thread.sleep(100);
                ctx.writeAndFlush(new FetchPartiesPacket(FetchPartiesPacket.Action.FETCH, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        ((Packet) message).handle(packetListener);
    }
}
