package fr.amgone.animaquizz.server.handlers;

import fr.amgone.animaquizz.server.PartiesManager;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import fr.amgone.animaquizz.shared.packets.Packets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.ArrayList;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final ArrayList<ClientHandler> CLIENTS = new ArrayList<>();

    private final PartiesManager partiesManager;

    private ByteBuf byteBuffer;
    private PacketListener packetListener;
    private Player player;

    public ClientHandler(PartiesManager partiesManager) {
        this.partiesManager = partiesManager;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        CLIENTS.add(this);
        player = new Player(ctx);
        byteBuffer = ctx.alloc().buffer(4);
        packetListener = new PacketListenerImpl(partiesManager, this);
        System.out.println("Client connected");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        CLIENTS.remove(this);
        byteBuffer.release();
        byteBuffer = null;

        partiesManager.removePlayerFromParty(player);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        ByteBuf incomingData = (ByteBuf) message;
        byteBuffer.writeBytes(incomingData);
        incomingData.release();

        if(byteBuffer.readableBytes() >= 4) {
            int packetSize = byteBuffer.readInt();
            if(byteBuffer.readableBytes() >= packetSize) {
                ByteBuf packetBuf = ctx.alloc().buffer(packetSize);
                byteBuffer.readBytes(packetBuf);

                int packetID = packetBuf.readInt();
                Packet packet = Packets.getPacketByID(packetID, packetBuf);
                if(packet == null) {
                    System.err.println("Received unknown packet with id " + packetID);
                    return;
                }

                packet.handle(packetListener);
            } else {
                byteBuffer.resetReaderIndex();
                byteBuffer.resetWriterIndex();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public static ArrayList<ClientHandler> getClients() {
        return new ArrayList<>(CLIENTS);
    }
}
