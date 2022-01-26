package fr.amgone.animaquizz.shared.items;

import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.items.TextItemPacket;
import java.util.ArrayList;

public class TextItem extends Item {
    private final String text;

    public TextItem(String question, String text) {
        this(question, "", text);
    }

    public TextItem(String question, String answer, String text) {
        super(question, answer);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public ArrayList<Packet> getPackets() {
        ArrayList<Packet> packets = new ArrayList<>();
        packets.add(new TextItemPacket(this));
        return packets;
    }
}
