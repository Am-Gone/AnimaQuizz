package fr.amgone.animaquizz.shared.packets;

import fr.amgone.animaquizz.shared.Player;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class UpdatePlayerPointsPacket extends Packet {
    private final String username;
    private final int points;

    public UpdatePlayerPointsPacket(Player player) {
        this.username = player.getUsername();
        this.points = player.getPoints();
    }

    public UpdatePlayerPointsPacket(ByteBuf byteBuf) {
        byte[] usernameBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes(usernameBytes);
        this.username = new String(usernameBytes);

        this.points = byteBuf.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(username.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(username.getBytes(StandardCharsets.UTF_8));

        out.writeInt(points);
    }

    @Override
    public void handle(PacketListener packetListener) {
        packetListener.handleUpdatePlayerPoints(this);
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }
}
