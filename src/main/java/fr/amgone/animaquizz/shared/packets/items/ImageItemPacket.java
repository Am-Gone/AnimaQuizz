package fr.amgone.animaquizz.shared.packets.items;

import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ImageItemPacket extends Packet {
    private final String question;
    private final byte[] hash;
    private final int imageSize;
    private final byte[] imageData;

    public ImageItemPacket(String question, byte[] hash, int imageSize, byte[] imageData) {
        this.question = question;
        this.hash = hash;
        this.imageSize = imageSize;
        this.imageData = imageData;
    }

    public ImageItemPacket(ByteBuf byteBuf) {
        if(byteBuf.readBoolean()) {
            byte[] questionBytes = new byte[byteBuf.readInt()];
            byteBuf.readBytes(questionBytes);
            this.question = new String(questionBytes);
        } else {
            this.question = null;
        }

        hash = new byte[byteBuf.readInt()];
        byteBuf.readBytes(hash);

        imageSize = byteBuf.readInt();

        imageData = new byte[byteBuf.readInt()];
        byteBuf.readBytes(imageData);
    }

    @Override
    public void write(ByteBuf out) {
        if(question != null) {
            out.writeBoolean(true);
            out.writeInt(question.getBytes(StandardCharsets.UTF_8).length);
            out.writeBytes(question.getBytes(StandardCharsets.UTF_8));
        } else {
            out.writeBoolean(false);
        }

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

    public String getQuestion() {
        return question;
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
