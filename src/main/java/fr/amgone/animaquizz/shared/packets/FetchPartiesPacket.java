package fr.amgone.animaquizz.shared.packets;

import fr.amgone.animaquizz.shared.Party;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

public class FetchPartiesPacket extends Packet {
    private final Action action;
    private final Party[] parties;

    public FetchPartiesPacket(Action action, Party[] parties) {
        this.action = action;
        this.parties = parties;
    }

    public FetchPartiesPacket(ByteBuf byteBuf) {
        action = Action.getActionFromID(byteBuf.readInt());

        if(action == Action.FETCH) {
            parties = new Party[0];
        } else {
            int partiesSize = byteBuf.readInt();
            parties = new Party[partiesSize];

            for (int i = 0; i < partiesSize; i++) {
                byte[] partyIDBytes = new byte[4];
                byteBuf.readBytes(partyIDBytes);
                String partyID = new String(partyIDBytes);

                int partyNameSize = byteBuf.readInt();
                byte[] partyNameBytes = new byte[partyNameSize];
                byteBuf.readBytes(partyNameBytes);
                String partyName = new String(partyNameBytes);

                parties[i] = new Party(partyID, partyName);
            }
        }

    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(action.action);

        if(action == Action.RECEIVE) {
            out.writeInt(parties.length);
            for (Party party : parties) {
                out.writeBytes(party.getId().getBytes(StandardCharsets.UTF_8));
                out.writeInt(party.getName().getBytes(StandardCharsets.UTF_8).length);
                out.writeBytes(party.getName().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleFetchParties(this);
    }

    public Party[] getParties() {
        return parties;
    }

    public enum Action {
        FETCH(0), RECEIVE(1);

        private final int action;

        Action(int action) {
            this.action = action;
        }

        private static Action getActionFromID(int action) {
            if(action == 0) {
                return FETCH;
            } else if(action == 1) {
                return RECEIVE;
            }

            return null;
        }
    }
}
