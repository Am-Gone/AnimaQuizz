package fr.amgone.animaquizz.shared.packets;

import fr.amgone.animaquizz.shared.packets.items.ImageItemPacket;
import fr.amgone.animaquizz.shared.packets.items.TextItemPacket;

public interface PacketListener {
    void handleFetchParties(FetchPartiesPacket fetchPartiesPacket);

    void handleCreateParty(CreatePartyPacket createPartyPacket);

    void handleJoinParty(JoinPartyPacket joinPartyPacket);

    void handlePlayerPartyPresence(PlayerPartyPresencePacket playerPartyPresencePacket);

    void handleTextItem(TextItemPacket textItemPacket);

    void handleImageItem(ImageItemPacket imageItemPacket);
}
