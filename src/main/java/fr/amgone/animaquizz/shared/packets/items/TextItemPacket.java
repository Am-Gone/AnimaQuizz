package fr.amgone.animaquizz.shared.packets.items;

import fr.amgone.animaquizz.shared.items.TextItem;
import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class TextItemPacket extends Packet {
    private final TextItem textItem;

    public TextItemPacket(TextItem textItem) {
        this.textItem = textItem;
    }

    public TextItemPacket(ByteBuf byteBuf) {
        byte[] questionBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(questionBytes);

        byte[] textBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(textBytes);
        this.textItem = new TextItem(new String(questionBytes), new String(textBytes));
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(textItem.getQuestion().getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(textItem.getQuestion().getBytes(StandardCharsets.UTF_8));

        out.writeInt(textItem.getText().getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(textItem.getText().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleTextItem(this);
    }

    public TextItem getTextItem() {
        return textItem;
    }
}
