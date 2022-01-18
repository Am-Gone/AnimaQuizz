package fr.amgone.animaquizz.server.handlers;

import fr.amgone.animaquizz.server.PartiesManager;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.packets.CreatePartyPacket;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import fr.amgone.animaquizz.shared.packets.PlayerPartyPresencePacket;

public class PacketListenerImpl implements PacketListener {
    private final PartiesManager partiesManager;
    private final Player player;

    public PacketListenerImpl(PartiesManager partiesManager, Player player) {
        this.partiesManager = partiesManager;
        this.player = player;
    }

    @Override
    public void handleFetchParties(FetchPartiesPacket fetchPartiesPacket) {
        player.getConnection().writeAndFlush(new FetchPartiesPacket(FetchPartiesPacket.Action.RECEIVE, partiesManager.getParties().values().toArray(new Party[0])));
    }

    @Override
    public void handleCreateParty(CreatePartyPacket createPartyPacket) {
        String partyID = partiesManager.createParty(createPartyPacket.getPartyName()).getId();
        player.setUsername(createPartyPacket.getUsername());
        partiesManager.addPlayer(player, partyID);

        ClientHandler.getClients().forEach(clients -> {
            if(clients.getPlayer().getCurrentParty() == null) {
                clients.getPlayer().getConnection().writeAndFlush(new FetchPartiesPacket(FetchPartiesPacket.Action.RECEIVE, partiesManager.getParties().values().toArray(new Party[0])));
            }
        });
    }

    @Override
    public void handleJoinParty(JoinPartyPacket joinPartyPacket) {
        player.setUsername(joinPartyPacket.getUsername());
        partiesManager.addPlayer(player, joinPartyPacket.getParty().getId());
    }

    @Override
    public void handlePlayerPartyPresence(PlayerPartyPresencePacket playerPartyPresencePacket) {
        // We do nothing.
    }
}
