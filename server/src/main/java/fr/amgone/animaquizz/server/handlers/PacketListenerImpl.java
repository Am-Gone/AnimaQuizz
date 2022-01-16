package fr.amgone.animaquizz.server.handlers;

import fr.amgone.animaquizz.server.PartiesManager;
import fr.amgone.animaquizz.server.Server;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.packets.CreatePartyPacket;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import fr.amgone.animaquizz.shared.packets.UserPartyPresencePacket;

public class PacketListenerImpl implements PacketListener {
    private final PartiesManager partiesManager;
    private final ClientHandler clientHandler;

    public PacketListenerImpl(PartiesManager partiesManager, ClientHandler clientHandler) {
        this.partiesManager = partiesManager;
        this.clientHandler = clientHandler;
    }

    @Override
    public void handleFetchParties(FetchPartiesPacket fetchPartiesPacket) {
        Server.writePacket(clientHandler.getUser().getConnection(), new FetchPartiesPacket(FetchPartiesPacket.Action.RECEIVE, partiesManager.getParties().values().toArray(new Party[0])));
    }

    @Override
    public void handleCreateParty(CreatePartyPacket createPartyPacket) {
        String partyID = partiesManager.createParty(createPartyPacket.getPartyName()).getId();
        clientHandler.getUser().setUsername(createPartyPacket.getUsername());
        partiesManager.addUser(clientHandler, partyID);

        ClientHandler.getClients().forEach(clients -> {
            if(clients.getUser().getCurrentParty() == null) {
                Server.writePacket(clients.getUser().getConnection(), new FetchPartiesPacket(FetchPartiesPacket.Action.RECEIVE, partiesManager.getParties().values().toArray(new Party[0])));
            }
        });
    }

    @Override
    public void handleJoinParty(JoinPartyPacket joinPartyPacket) {
        clientHandler.getUser().setUsername(joinPartyPacket.getUsername());
        partiesManager.addUser(clientHandler, joinPartyPacket.getParty().getId());
    }

    @Override
    public void handleUserPartyPresence(UserPartyPresencePacket userJoinPartyPacket) {
        // We do nothing.
    }
}
