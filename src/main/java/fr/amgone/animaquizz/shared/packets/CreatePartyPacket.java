package fr.amgone.animaquizz.shared.packets;

import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

public class CreatePartyPacket extends Packet {
    private final String partyName;

    public CreatePartyPacket(String partyName) {
        this.partyName = partyName;
    }

    public CreatePartyPacket(ByteBuf byteBuf) {
        super(byteBuf);
        byte[] partyNameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(partyNameBytes);
        partyName = new String(partyNameBytes);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(partyName.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(partyName.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleCreateParty(this);
    }

    public String getPartyName() {
        return partyName;
    }
}
