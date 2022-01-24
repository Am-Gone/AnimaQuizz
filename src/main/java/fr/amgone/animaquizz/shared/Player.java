package fr.amgone.animaquizz.shared;

import io.netty.channel.ChannelHandlerContext;

public class Player {
    private String username;
    private int points;
    private boolean hasFoundAnswer = false;

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

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setHasFoundAnswer(boolean hasFoundAnswer) {
        this.hasFoundAnswer = hasFoundAnswer;
    }

    public boolean hasFoundAnswer() {
        return hasFoundAnswer;
    }
}
