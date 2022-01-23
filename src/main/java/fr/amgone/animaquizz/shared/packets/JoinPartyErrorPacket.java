package fr.amgone.animaquizz.shared.packets;

import io.netty.buffer.ByteBuf;

public class JoinPartyErrorPacket extends Packet {
    private final Errors error;

    public JoinPartyErrorPacket(Errors error) {
        this.error = error;
    }

    public JoinPartyErrorPacket(ByteBuf byteBuf) {
        error = Errors.getErrorFromCode(byteBuf.readInt());
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(error.errorCode);
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleJoinPartyError(this);
    }

    public Errors getError() {
        return error;
    }

    public enum Errors {
        PARTY_FULL(0),
        USERNAME_ALREADY_TAKEN(1),
        PARTY_DOES_NOT_EXISTS(2);

        private final int errorCode;

        Errors(int errorCode) {
            this.errorCode = errorCode;
        }

        public static Errors getErrorFromCode(int errorCode) {
            for(Errors errors : values()) {
                if(errors.errorCode == errorCode) {
                    return errors;
                }
            }

            return null;
        }
    }
}
