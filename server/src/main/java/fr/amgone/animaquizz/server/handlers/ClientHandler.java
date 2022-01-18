package fr.amgone.animaquizz.server.handlers;

import fr.amgone.animaquizz.server.PartiesManager;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.ArrayList;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final ArrayList<ClientHandler> CLIENTS = new ArrayList<>();

    private final PartiesManager partiesManager;

    private PacketListener packetListener;
    private Player player;

    public ClientHandler(PartiesManager partiesManager) {
        this.partiesManager = partiesManager;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        CLIENTS.add(this);
        player = new Player(ctx);
        packetListener = new PacketListenerImpl(partiesManager, player);
        System.out.println("Client connected");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        CLIENTS.remove(this);
        partiesManager.removePlayerFromParty(player);
        System.out.println("Client disconnected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        ((Packet) message).handle(packetListener);
    }

    public Player getPlayer() {
        return player;
    }

    public static ArrayList<ClientHandler> getClients() {
        return new ArrayList<>(CLIENTS);
    }
}
