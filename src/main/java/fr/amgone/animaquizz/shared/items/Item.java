package fr.amgone.animaquizz.shared.items;

import fr.amgone.animaquizz.shared.packets.Packet;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Item {
    private final String question;
    private final String answer;

    protected Item(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public abstract ArrayList<Packet> getPackets();

    public static Item getRandomItem() {
        switch (ThreadLocalRandom.current().nextInt(0, 2)) {
            case 0 -> {
                return new TextItem("Quel est ce texte?", "random uuid", UUID.randomUUID().toString());
            }

            case 1 -> {
                BufferedImage bufferedImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < 1920; x++) {
                    for (int y = 0; y < 1080; y++) {
                        int red = ThreadLocalRandom.current().nextInt(0, 255);
                        int green = ThreadLocalRandom.current().nextInt(0, 255);
                        int blue = ThreadLocalRandom.current().nextInt(0, 255);
                        int rgb = red;
                        rgb = (rgb << 8) + green;
                        rgb = (rgb << 8) + blue;

                        bufferedImage.setRGB(x, y, rgb);
                    }
                }
                return new ImageItem("D'où vient cette image?", "4012", bufferedImage);
            }

            default -> {
                return new TextItem("Qu'est-ce que c'est?", "unknown item", "Unknown Item");
            }
        }
    }
}
