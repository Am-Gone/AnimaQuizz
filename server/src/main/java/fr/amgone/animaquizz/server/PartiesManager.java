package fr.amgone.animaquizz.server;

import fr.amgone.animaquizz.server.handlers.ClientHandler;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
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
        if(parties.get(partyID).addUser(clientHandler.getUser())) {
            //clientHandler.getUser().setCurrentParty(parties.get(partyID));
            Server.writePacket(clientHandler.getUser().getConnection(), new JoinPartyPacket(parties.get(partyID)));
            System.out.println("added to party");
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
