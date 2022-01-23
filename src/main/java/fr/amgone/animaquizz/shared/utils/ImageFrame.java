package fr.amgone.animaquizz.shared.utils;

import fr.amgone.animaquizz.shared.packets.items.ImageItemPacket;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ImageFrame {
    private byte[] hash;
    private int imageSize;
    private byte[] imageBuf;

    public boolean addFrame(ImageItemPacket imageItemPacket) {
        if(!Arrays.equals(hash, imageItemPacket.getHash())) {
            hash = imageItemPacket.getHash();
            imageSize = imageItemPacket.getImageSize();
            imageBuf = new byte[0];
        }

        int length = imageBuf.length + imageItemPacket.getImageData().length;

        byte[] result = new byte[length];
        System.arraycopy(imageBuf, 0, result, 0, imageBuf.length);
        System.arraycopy(imageItemPacket.getImageData(), 0, result, imageBuf.length, imageItemPacket.getImageData().length);
        imageBuf = result;

        return imageSize == result.length;
    }

    public BufferedImage getImage() {
        ByteArrayInputStream imageBufInputStream = new ByteArrayInputStream(imageBuf);
        imageBuf = null;
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageBufInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bufferedImage;
    }
}
