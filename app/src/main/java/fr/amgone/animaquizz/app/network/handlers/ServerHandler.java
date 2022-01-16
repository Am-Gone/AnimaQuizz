package fr.amgone.animaquizz.app.network.handlers;

import fr.amgone.animaquizz.app.network.Client;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import fr.amgone.animaquizz.shared.packets.Packets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final Client client;

    private ByteBuf byteBuffer;
    private final PacketListener packetListener;

    public ServerHandler(Client client, PacketListener packetListener) {
        this.client = client;
        this.packetListener = packetListener;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        byteBuffer = ctx.alloc().buffer(4);
        new Thread(() -> {
            try {
                Thread.sleep(100);
                client.sendPacket(new FetchPartiesPacket(FetchPartiesPacket.Action.FETCH, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        byteBuffer.release();
        byteBuffer = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        ByteBuf inBuffer = (ByteBuf) message;
        byteBuffer.writeBytes(inBuffer);
        inBuffer.release();

        if(byteBuffer.readableBytes() >= 4) {
            int packetSize = byteBuffer.readInt();
            if(byteBuffer.readableBytes() >= packetSize) {
                ByteBuf packetBuf = ctx.alloc().buffer(packetSize);
                byteBuffer.readBytes(packetBuf);

                int packetID = packetBuf.readInt();
                Packet packet = Packets.getPacketByID(packetID, packetBuf);
                if(packet == null) {
                    System.err.println("Received unknown packet with ID " + packetID);
                    return;
                }

                packet.handle(packetListener);
            } else {
                byteBuffer.resetReaderIndex();
                byteBuffer.resetWriterIndex();
            }
        }
    }
}
