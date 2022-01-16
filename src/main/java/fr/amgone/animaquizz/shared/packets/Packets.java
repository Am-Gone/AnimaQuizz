package fr.amgone.animaquizz.shared.packets;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

public enum Packets {
    FETCH_PARTIES_PACKET(0, FetchPartiesPacket.class),
    CREATE_PARTY_PACKET(1, CreatePartyPacket.class),
    JOIN_PARTY_PACKET(2, JoinPartyPacket.class);

    private final int packetID;
    private final Class<? extends Packet> packet;

    Packets(int packetID, Class<? extends Packet> packet) {
        this.packetID = packetID;
        this.packet = packet;
    }

    public int getPacketID() {
        return packetID;
    }

    public Class<? extends Packet> getPacket() {
        return packet;
    }

    public static Packet getPacketByID(int id, ByteBuf packetContent) {
        for(Packets packets : values()) {
            if(packets.packetID == id) {
                try {
                    return packets.getPacket().getConstructor(new Class[] {ByteBuf.class}).newInstance(packetContent);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static int getIDByPacket(Packet packet) {
        for(Packets packets : values()) {
            if(packets.getPacket().equals(packet.getClass())) {
                return packets.getPacketID();
            }
        }

        return -1;
    }
}
