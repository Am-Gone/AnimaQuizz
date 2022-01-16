package fr.amgone.animaquizz.server;

import fr.amgone.animaquizz.server.handlers.ClientHandler;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.User;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import fr.amgone.animaquizz.shared.packets.UserPartyPresencePacket;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PartiesManager {
    private final HashMap<String, Party> parties = new HashMap<>();

    public Party createParty(String name) {
        Party party = new Party(getRandomID(), name);
        parties.put(party.getId(), party);

        return party;
    }

    public void addUser(ClientHandler clientHandler, String partyID) {
        Party party = parties.get(partyID);
        if(party == null) return;

        if(party.addUser(clientHandler.getUser())) {
            clientHandler.getUser().setCurrentParty(party);

            Server.writePacket(clientHandler.getUser().getConnection(), // Tell the player who clicked he joined the game
                    new JoinPartyPacket(clientHandler.getUser().getUsername(), party));

            party.getUsers().forEach(users -> {
                if(!users.equals(clientHandler.getUser())) {
                    Server.writePacket(users.getConnection(), // Tell everyone in the party that a new player joined
                            new UserPartyPresencePacket(UserPartyPresencePacket.Action.JOIN, clientHandler.getUser().getUsername()));
                }
            });
        }
    }

    public void removeUserFromParty(User user) {
        if(user.getCurrentParty() != null) {
            if(user.getCurrentParty().removeUser(user)) {
                parties.remove(user.getCurrentParty().getId());


                ClientHandler.getClients().forEach(clients -> {
                    if(clients.getUser().getCurrentParty() == null) {
                        Server.writePacket(clients.getUser().getConnection(), new FetchPartiesPacket(FetchPartiesPacket.Action.RECEIVE, parties.values().toArray(new Party[0])));
                    }
                });
            }

            user.getCurrentParty().getUsers().forEach(users -> Server.writePacket(users.getConnection(),
                    new UserPartyPresencePacket(UserPartyPresencePacket.Action.LEAVE, user.getUsername())));
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
