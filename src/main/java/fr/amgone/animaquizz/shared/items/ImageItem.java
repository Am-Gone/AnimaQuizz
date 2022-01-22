package fr.amgone.animaquizz.shared.items;

import fr.amgone.animaquizz.shared.packets.Packet;
import fr.amgone.animaquizz.shared.packets.items.ImageItemPacket;
import fr.amgone.animaquizz.shared.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class ImageItem extends Item {
    private static final short MAX_IMAGE_FRAME_SIZE = 15000;
    private final BufferedImage bufferedImage;

    public ImageItem(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public ArrayList<Packet> getPackets() {
        ArrayList<Packet> packets = new ArrayList<>();

        ByteBuf imageBuf = Unpooled.buffer();
        ByteBufOutputStream imageBufOutputStream = new ByteBufOutputStream(imageBuf);
        try {
            ImageIO.write(bufferedImage, "png", imageBufOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageHash = Utils.getHash(imageBuf.array());
        int imageSize = imageBuf.readableBytes();

        while(imageBuf.readableBytes() > MAX_IMAGE_FRAME_SIZE) {
            byte[] imageFrame = new byte[MAX_IMAGE_FRAME_SIZE];
            imageBuf.readBytes(imageFrame);

            packets.add(new ImageItemPacket(imageHash, imageSize, imageFrame));
        }

        byte[] imageFrame = new byte[imageBuf.readableBytes()];
        imageBuf.readBytes(imageFrame);
        packets.add(new ImageItemPacket(imageHash, imageSize, imageFrame));

        return packets;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
