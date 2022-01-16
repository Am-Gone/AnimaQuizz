package fr.amgone.animaquizz.shared;

import io.netty.channel.ChannelHandlerContext;

public class User {
    private String username;
    private Party currentParty = null;
    private final ChannelHandlerContext connection;

    public User(ChannelHandlerContext connection) {
        this.connection = connection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Party getCurrentParty() {
        return currentParty;
    }

    public void setCurrentParty(Party currentParty) {
        this.currentParty = currentParty;
    }

    public ChannelHandlerContext getConnection() {
        return connection;
    }
}
