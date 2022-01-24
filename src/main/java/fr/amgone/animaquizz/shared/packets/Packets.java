package fr.amgone.animaquizz.shared.packets;

import fr.amgone.animaquizz.shared.packets.items.ImageItemPacket;
import fr.amgone.animaquizz.shared.packets.items.TextItemPacket;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.InvocationTargetException;

public enum Packets {
    FETCH_PARTIES_PACKET(0, FetchPartiesPacket.class),
    CREATE_PARTY_PACKET(1, CreatePartyPacket.class),
    JOIN_PARTY_PACKET(2, JoinPartyPacket.class),
    JOIN_PARTY_ERROR_PACKET(3, JoinPartyErrorPacket.class),
    PLAYER_PARTY_PRESENCE_PACKET(4, PlayerPartyPresencePacket.class),
    TEXT_ITEM_PACKET(5, TextItemPacket.class),
    IMAGE_ITEM_PACKET(6, ImageItemPacket.class),
    ANSWER_PACKET(7, AnswerPacket.class),
    UPDATE_PLAYER_POINTS_PACKET(8, UpdatePlayerPointsPacket.class);

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

        return -999;
    }
}
