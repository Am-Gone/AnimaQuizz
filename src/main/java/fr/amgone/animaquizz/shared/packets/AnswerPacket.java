package fr.amgone.animaquizz.shared.packets;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class AnswerPacket extends Packet {
    private final String answer;

    public AnswerPacket(String answer) {
        this.answer = answer;
    }

    public AnswerPacket(ByteBuf byteBuf) {
        byte[] answerBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(answerBytes);
        this.answer = new String(answerBytes);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(answer.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(answer.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleAnswerPacket(this);
    }

    public String getAnswer() {
        return answer;
    }
}
