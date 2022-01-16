package fr.amgone.animaquizz.app.network;

import fr.amgone.animaquizz.app.network.handlers.ServerHandler;
import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import fr.amgone.animaquizz.shared.packets.Packets;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

public class Client {
    private final String host;
    private final int port;
    private final PacketListener packetListener;

    private ChannelFuture serverChannelFuture;
    private EventLoopGroup group;

    public Client(String host, int port, PacketListener packetListener) {
        this.host = host;
        this.port = port;
        this.packetListener = packetListener;
    }

    public void connect() {
        try {
        group = new NioEventLoopGroup();
        Bootstrap clientBootstrap = new Bootstrap();

        clientBootstrap.group(group);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.remoteAddress(new InetSocketAddress(host, port));
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new ServerHandler(Client.this, packetListener));
            }
        });

        serverChannelFuture = clientBootstrap.connect().sync();
        Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        System.out.println("Shutting down netty...");
        try {
            serverChannelFuture.channel().close().sync();
            group.shutdownGracefully().sync();
            System.out.println("Netty has been shat down.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet packet) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(Packets.getIDByPacket(packet));
        packet.write(byteBuf);

        byte[] packetBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(packetBytes);

        byteBuf.resetReaderIndex();
        byteBuf.resetWriterIndex();

        byteBuf.writeInt(packetBytes.length);
        byteBuf.writeBytes(packetBytes);

        serverChannelFuture.channel().writeAndFlush(byteBuf);
    }
}
