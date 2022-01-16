package fr.amgone.animaquizz.shared.packets;

import fr.amgone.animaquizz.shared.Party;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

public class JoinPartyPacket extends Packet {
    private final Party party;

    public JoinPartyPacket(Party party) {
        this.party = party;
    }

    public JoinPartyPacket(ByteBuf byteBuf) {
        super(byteBuf);
        byte[] partyIDBytes = new byte[4];
        byteBuf.readBytes(partyIDBytes);
        String partyID = new String(partyIDBytes);

        byte[] partyNameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(partyNameBytes);
        String partyName = new String(partyNameBytes);

        party = new Party(partyID, partyName);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeBytes(party.getId().getBytes(StandardCharsets.UTF_8));
        out.writeInt(party.getName().getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(party.getName().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleJoinParty(this);
    }

    public Party getParty() {
        return party;
    }
}
