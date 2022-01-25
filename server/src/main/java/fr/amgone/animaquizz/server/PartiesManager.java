package fr.amgone.animaquizz.server;

import fr.amgone.animaquizz.server.handlers.ClientHandler;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyErrorPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import fr.amgone.animaquizz.shared.packets.PlayerPartyPresencePacket;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PartiesManager {
    private final HashMap<String, ServerParty> parties = new HashMap<>();

    public ServerParty createParty(String name) {
        ServerParty party = new ServerParty(getRandomID(), name);
        parties.put(party.getId(), party);

        return party;
    }

    public void addPlayer(Player player, String partyID) {
        ServerParty party = parties.get(partyID);
        if(party == null) {
            player.getConnection().writeAndFlush(new JoinPartyErrorPacket(JoinPartyErrorPacket.Errors.PARTY_DOES_NOT_EXISTS));
            return;
        }

        if(party.isFull()) {
            player.getConnection().writeAndFlush(new JoinPartyErrorPacket(JoinPartyErrorPacket.Errors.PARTY_FULL));
            return;
        }

        if(party.getPlayerFromUsername(player.getUsername()) != null) {
            player.getConnection().writeAndFlush(new JoinPartyErrorPacket(JoinPartyErrorPacket.Errors.USERNAME_ALREADY_TAKEN));
            return;
        }

        if(party.addPlayer(player)) {
            player.setCurrentParty(party);

            player.getConnection().writeAndFlush(new JoinPartyPacket(player.getUsername(), party)).addListener(future -> party.sendItemToPlayer(player));

            party.getPlayers().forEach(players -> {
                if(!players.equals(player)) {
                    players.getConnection().writeAndFlush(new PlayerPartyPresencePacket(PlayerPartyPresencePacket.Action.JOIN,
                            player.getUsername())); // Tell everyone in the party that a new player joined
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
                        clients.getPlayer().getConnection().writeAndFlush(new FetchPartiesPacket(FetchPartiesPacket.Action.RECEIVE, parties.values().toArray(new ServerParty[0])));
                    }
                });
            }

            player.getCurrentParty().getPlayers().forEach(players -> players.getConnection().writeAndFlush(
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

    public HashMap<String, ServerParty> getParties() {
        return new HashMap<>(parties);
    }
}
