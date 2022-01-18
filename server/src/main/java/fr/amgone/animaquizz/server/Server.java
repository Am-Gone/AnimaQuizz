package fr.amgone.animaquizz.server;

import fr.amgone.animaquizz.server.handlers.ClientHandler;
import fr.amgone.animaquizz.shared.handlers.PacketDecoder;
import fr.amgone.animaquizz.shared.handlers.PacketEncoder;
import fr.amgone.animaquizz.shared.handlers.PacketFrameEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

public class Server {
    private final int port;

    private ChannelFuture serverChannelFuture;
    private EventLoopGroup group;

    private final PartiesManager partiesManager = new PartiesManager();

    public Server(int port) {
        this.port = port;
    }

    public void connect() {
        group = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress("localhost", port));

            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(new PacketFrameEncoder());
                    socketChannel.pipeline().addLast(new PacketEncoder());
                    socketChannel.pipeline().addLast(new PacketDecoder());
                    socketChannel.pipeline().addLast(new ClientHandler(partiesManager));
                }
            });

            serverChannelFuture = serverBootstrap.bind().sync();

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
}
