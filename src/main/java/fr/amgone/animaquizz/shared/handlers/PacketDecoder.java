package fr.amgone.animaquizz.shared.handlers;

import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.Packets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    private ByteBuf progressiveByteBuf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        progressiveByteBuf = ctx.alloc().buffer();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        progressiveByteBuf.writeBytes(byteBuf);

        while (progressiveByteBuf.readableBytes() >= 4) {
            int packetSize = progressiveByteBuf.readInt();
            if(progressiveByteBuf.readableBytes() >= packetSize) {
                ByteBuf packetBuf = channelHandlerContext.alloc().buffer(packetSize);
                progressiveByteBuf.readBytes(packetBuf);

                int packetID = packetBuf.readInt();
                Packet packet = Packets.getPacketByID(packetID, packetBuf);
                packetBuf.release();

                if(packet == null) {
                    if(packetID == -999) {
                        System.err.println("Received unknown packet with id " + packetID + " (Unregistered packet?)");
                    } else {
                        System.err.println("Received unknown packet with id " + packetID);
                    }
                    return;
                }

                list.add(packet);

                byte[] leftBytes = new byte[progressiveByteBuf.readableBytes()];
                progressiveByteBuf.readBytes(leftBytes);
                progressiveByteBuf.release();

                progressiveByteBuf = channelHandlerContext.alloc().buffer();
                progressiveByteBuf.writeBytes(leftBytes);
            } else {
                progressiveByteBuf.resetReaderIndex();
                break;
            }
        }
    }
}
