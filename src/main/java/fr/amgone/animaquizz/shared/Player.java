package fr.amgone.animaquizz.shared;

import io.netty.channel.ChannelHandlerContext;

public class Player {
    private String username;
    private Party currentParty = null;
    private final ChannelHandlerContext connection;

    public Player(String username) {
        this.connection = null;
        this.username = username;
    }

    public Player(ChannelHandlerContext connection) {
        this.connection = connection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setCurrentParty(Party currentParty) {
        this.currentParty = currentParty;
    }

    public Party getCurrentParty() {
        return currentParty;
    }

    public ChannelHandlerContext getConnection() {
        return connection;
    }
}
