package fr.amgone.animaquizz.shared.packets;

public interface PacketListener {
    void handleFetchParties(FetchPartiesPacket fetchPartiesPacket);

    void handleCreateParty(CreatePartyPacket createPartyPacket);

    void handleJoinParty(JoinPartyPacket joinPartyPacket);

    void handlePlayerPartyPresence(PlayerPartyPresencePacket playerPartyPresencePacket);
}
