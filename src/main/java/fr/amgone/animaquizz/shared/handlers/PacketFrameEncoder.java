package fr.amgone.animaquizz.shared.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketFrameEncoder extends MessageToByteEncoder<ByteBuf> {
    private static final int NETTY_MAX_PACKET_SIZE = 16384;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf message, ByteBuf out) {
        int length = message.readableBytes();
        byte[] messageBytes = new byte[length];
        message.readBytes(messageBytes);
        out.writeInt(length);
        out.writeBytes(messageBytes);

        if(out.readableBytes() > NETTY_MAX_PACKET_SIZE) {
            while (out.readableBytes() > NETTY_MAX_PACKET_SIZE) {
                splitPacket(NETTY_MAX_PACKET_SIZE, out, channelHandlerContext);
            }

            splitPacket(message.readableBytes(), out, channelHandlerContext);
        }
    }

    private void splitPacket(int size, ByteBuf message, ChannelHandlerContext channelHandlerContext) {
        byte[] splitPacket = new byte[size];
        message.writeBytes(splitPacket);

        ByteBuf splitPacketBuf = Unpooled.buffer(size);
        splitPacketBuf.readBytes(splitPacket);

        channelHandlerContext.write(splitPacketBuf);
    }
}
