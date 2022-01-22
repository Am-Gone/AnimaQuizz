package fr.amgone.animaquizz.shared.packets.items;

import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import io.netty.buffer.ByteBuf;

public class ImageItemPacket extends Packet {
    private final byte[] hash;
    private final int imageSize;
    private final byte[] imageData;

    public ImageItemPacket(byte[] hash, int imageSize, byte[] imageData) {
        this.hash = hash;
        this.imageSize = imageSize;
        this.imageData = imageData;
    }

    public ImageItemPacket(ByteBuf byteBuf) {
        hash = new byte[byteBuf.readInt()];
        byteBuf.readBytes(hash);

        imageSize = byteBuf.readInt();

        imageData = new byte[byteBuf.readInt()];
        byteBuf.readBytes(imageData);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(hash.length);
        out.writeBytes(hash);

        out.writeInt(imageSize);

        out.writeInt(imageData.length);
        out.writeBytes(imageData);
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleImageItem(this);
    }

    public byte[] getHash() {
        return hash;
    }

    public int getImageSize() {
        return imageSize;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
