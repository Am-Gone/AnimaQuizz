package fr.amgone.animaquizz.server;

import fr.amgone.animaquizz.server.handlers.ClientHandler;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import fr.amgone.animaquizz.shared.packets.PlayerPartyPresencePacket;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PartiesManager {
    private final HashMap<String, Party> parties = new HashMap<>();

    public Party createParty(String name) {
        Party party = new Party(getRandomID(), name);
        parties.put(party.getId(), party);

        return party;
    }

    public void addPlayer(ClientHandler clientHandler, String partyID) {
        Party party = parties.get(partyID);
        if(party == null) return;

        if(party.addPlayer(clientHandler.getPlayer())) {
            Player player = clientHandler.getPlayer();
            player.setCurrentParty(party);

            Server.writePacket(player.getConnection(), // Tell the player who clicked he joined the game
                    new JoinPartyPacket(player.getUsername(), party));

            party.getPlayers().forEach(players -> {
                if(!players.equals(player)) {
                    Server.writePacket(players.getConnection(), // Tell everyone in the party that a new player joined
                            new PlayerPartyPresencePacket(PlayerPartyPresencePacket.Action.JOIN, player.getUsername()));
                }
            });
        }
    }

    public void removePlayerFromParty(Player player) {
        if(player.getCurrentParty() != null) {
            if(player.getCurrentParty().removePlayer(player)) {
                parties.remove(player.getCurrentParty().getId());


                ClientHandler.getClients().forEach(clients -> {
                    if(clients.getPlayer().getCurrentParty() == null) {
                        Server.writePacket(clients.getPlayer().getConnection(), new FetchPartiesPacket(FetchPartiesPacket.Action.RECEIVE, parties.values().toArray(new Party[0])));
                    }
                });
            }

            player.getCurrentParty().getPlayers().forEach(players -> Server.writePacket(players.getConnection(),
                    new PlayerPartyPresencePacket(PlayerPartyPresencePacket.Action.LEAVE, player.getUsername())));
        }
    }

    private String getRandomID() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            id.append(characters.charAt(ThreadLocalRandom.current().nextInt(0, characters.length())));
        }

        return id.toString();
    }

    public HashMap<String, Party> getParties() {
        return new HashMap<>(parties);
    }
}
