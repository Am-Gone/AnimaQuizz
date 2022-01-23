package fr.amgone.animaquizz.shared;

import java.util.Collection;
import java.util.HashMap;

public class Party {
    private static final int MAX_PLAYERS = 2;

    private final String id;
    private final String name;
    private final HashMap<String, Player> players = new HashMap<>(); // String is the username of the player. We use it to get the player object.

    public Party(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public boolean addPlayer(Player player) {
        if(players.size() < MAX_PLAYERS) {
            players.put(player.getUsername(), player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a player from the party
     * @param player that needs to be removed
     * @return true if there are no players left
     */
    public boolean removePlayer(Player player) {
        players.remove(player.getUsername());
        return players.size() == 0;
    }

    public Player getPlayerFromUsername(String username) {
        return players.get(username);
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }
}
