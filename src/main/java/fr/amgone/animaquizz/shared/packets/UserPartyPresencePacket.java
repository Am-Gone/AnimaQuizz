package fr.amgone.animaquizz.shared.packets;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class UserPartyPresencePacket extends Packet {
    private final Action action;
    private final String username;

    public UserPartyPresencePacket(Action action, String username) {
        this.action = action;
        this.username = username;
    }

    public UserPartyPresencePacket(ByteBuf byteBuf) {
        action = Action.getActionFromID(byteBuf.readInt());

        byte[] usernameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(usernameBytes);
        username = new String(usernameBytes);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(action.id);
        out.writeInt(username.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(username.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleUserPartyPresence(this);
    }

    public Action getAction() {
        return action;
    }

    public String getUsername() {
        return username;
    }

    public enum Action {
        JOIN(0),
        LEAVE(1);

        private final int id;

        Action(int id) {
            this.id = id;
        }

        public static Action getActionFromID(int id) {
            if(id == 0) {
                return JOIN;
            } else if(id == 1) {
                return LEAVE;
            } else {
                return null;
            }
        }
    }
}
