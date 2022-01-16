package fr.amgone.animaquizz.shared.packets;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
    public Packet() {

    }

    public Packet(ByteBuf byteBuf) {

    }

    public abstract void write(ByteBuf out);

    public abstract void handle(PacketListener packetListener);
}
