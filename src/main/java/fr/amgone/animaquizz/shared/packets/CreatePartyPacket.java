package fr.amgone.animaquizz.shared.packets;

import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

public class CreatePartyPacket extends Packet {
    private final String username;
    private final String partyName;

    public CreatePartyPacket(String username, String partyName) {
        this.username = username;
        this.partyName = partyName;
    }

    public CreatePartyPacket(ByteBuf byteBuf) {
        super(byteBuf);
        byte[] usernameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(usernameBytes);
        username = new String(usernameBytes);

        byte[] partyNameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(partyNameBytes);
        partyName = new String(partyNameBytes);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(username.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(username.getBytes(StandardCharsets.UTF_8));

        out.writeInt(partyName.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(partyName.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleCreateParty(this);
    }

    public String getUsername() {
        return username;
    }

    public String getPartyName() {
        return partyName;
    }
}
