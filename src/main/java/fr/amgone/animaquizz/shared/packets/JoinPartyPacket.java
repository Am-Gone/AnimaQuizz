package fr.amgone.animaquizz.shared.packets;

import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

public class JoinPartyPacket extends Packet {
    private final String username;
    private final Party party;

    public JoinPartyPacket(String username, Party party) {
        this.username = username;
        this.party = party;
    }

    public JoinPartyPacket(ByteBuf byteBuf) {
        super(byteBuf);
        byte[] usernameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(usernameBytes);
        username = new String(usernameBytes);

        byte[] partyIDBytes = new byte[4];
        byteBuf.readBytes(partyIDBytes);
        String partyID = new String(partyIDBytes);

        byte[] partyNameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(partyNameBytes);
        String partyName = new String(partyNameBytes);

        party = new Party(partyID, partyName);

        int playerSize = byteBuf.readInt();
        for (int i = 0; i < playerSize; i++) {
            byte[] playerBytes = new byte[byteBuf.readInt()];
            byteBuf.readBytes(playerBytes);
            party.addPlayer(new Player(new String(playerBytes)));
        }
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(username.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(username.getBytes(StandardCharsets.UTF_8));

        out.writeBytes(party.getId().getBytes(StandardCharsets.UTF_8));
        out.writeInt(party.getName().getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(party.getName().getBytes(StandardCharsets.UTF_8));

        out.writeInt(party.getPlayers().size());
        party.getPlayers().forEach(player -> {
            out.writeInt(player.getUsername().getBytes(StandardCharsets.UTF_8).length);
            out.writeBytes(player.getUsername().getBytes(StandardCharsets.UTF_8));
        });
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleJoinParty(this);
    }

    public String getUsername() {
        return username;
    }

    public Party getParty() {
        return party;
    }
}
