package fr.amgone.animaquizz.shared;

import java.util.ArrayList;

public class Party {
    private final String id;
    private final String name;
    private final ArrayList<Player> players = new ArrayList<>();

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

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public boolean addPlayer(Player player) {
        if(players.size() < 10) {
            players.add(player);
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
        players.remove(player);
        return players.size() == 0;
    }
}
