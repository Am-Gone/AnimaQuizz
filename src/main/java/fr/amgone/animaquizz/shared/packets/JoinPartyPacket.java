package fr.amgone.animaquizz.shared.packets;

import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.User;
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

        int userSize = byteBuf.readInt();
        for (int i = 0; i < userSize; i++) {
            byte[] userBytes = new byte[byteBuf.readInt()];
            byteBuf.readBytes(userBytes);
            party.addUser(new User(new String(userBytes)));
        }
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(username.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(username.getBytes(StandardCharsets.UTF_8));

        out.writeBytes(party.getId().getBytes(StandardCharsets.UTF_8));
        out.writeInt(party.getName().getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(party.getName().getBytes(StandardCharsets.UTF_8));

        out.writeInt(party.getUsers().size());
        party.getUsers().forEach(user -> {
            out.writeInt(user.getUsername().getBytes(StandardCharsets.UTF_8).length);
            out.writeBytes(user.getUsername().getBytes(StandardCharsets.UTF_8));
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
