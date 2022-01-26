package fr.amgone.animaquizz.app.gui;

import fr.amgone.animaquizz.app.gui.component.ATextField;
import fr.amgone.animaquizz.app.network.Client;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.items.ImageItem;
import fr.amgone.animaquizz.shared.items.Item;
import fr.amgone.animaquizz.shared.items.TextItem;
import fr.amgone.animaquizz.shared.packets.AnswerPacket;
import org.jdesktop.swingx.JXTextField;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class PartyForm extends JPanel {
    private final Party party;
    private final Box playersBox;
    private final Box itemBox;

    private BufferedImage image;
    private int lastResizeWidth = 0;
    private int lastResizeHeight = 0;

    public PartyForm(Client client, Party party) {
        this.party = party;
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(49, 116, 158));

        JLabel partyName = new JLabel(party.getName());
        partyName.setFont(new Font(Font.SERIF, Font.BOLD, 32));
        partyName.setHorizontalAlignment(JLabel.CENTER);
        partyName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        partyName.setForeground(Color.BLACK);

        partyName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(partyName, BorderLayout.PAGE_START);

        itemBox = Box.createVerticalBox();
        itemBox.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        itemBox.setBackground(Color.GRAY);
        itemBox.setOpaque(true);

        JLabel guess = new JLabel("Devine ça");
        itemBox.add(guess);
        this.add(itemBox, BorderLayout.CENTER);

        ATextField answer = new ATextField(20);
        answer.setPrompt("Écrivez votre réponse");
        answer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));

        answer.setAlignmentX(JXTextField.CENTER_ALIGNMENT);
        answer.setHorizontalAlignment(JXTextField.CENTER);
        answer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        answer.addActionListener(actionEvent -> {
            if(!answer.getText().equals("")) {
                client.getServer().writeAndFlush(new AnswerPacket(answer.getText()));
                answer.setText("");
            }
        });

        this.add(answer, BorderLayout.PAGE_END);

        playersBox = Box.createVerticalBox();
        playersBox.setBackground(new Color(20, 25, 35));
        playersBox.setOpaque(true);
        this.add(playersBox, BorderLayout.LINE_END);

        reloadPlayers();
    }

    public void addPlayer(Player player) {
        party.addPlayer(player);
        reloadPlayers();
    }

    public void removePlayer(Player player) {
        party.removePlayer(player);
        reloadPlayers();
    }

    public void reloadPlayers() {
        playersBox.removeAll();

        party.getPlayers().forEach(players -> {
            JLabel playerName = new JLabel(players.getUsername() + " (" + players.getPoints() + " points)");
            playerName.setForeground(Color.WHITE);
            playerName.setFont(new Font(Font.SANS_SERIF, players.hasFoundAnswer() ? Font.ITALIC : Font.BOLD, 32));

            Border border = playerName.getBorder();
            Border margin = new EmptyBorder(10, 10, 10, 10);

            playerName.setBorder(new CompoundBorder(border, margin));
            playersBox.add(playerName);
        });

        updateUI();
    }

    public void setItem(Item item) {
        itemBox.removeAll();
        party.getPlayers().forEach(players -> players.setHasFoundAnswer(false));
        if(item instanceof TextItem textItem) {
            itemBox.add(new JLabel(textItem.getText()));
        } else if(item instanceof ImageItem imageItem) {
            image = imageItem.getBufferedImage();
            JLabel jLabel = new JLabel(new ImageIcon(resize(image, itemBox.getWidth(), itemBox.getHeight())));
            itemBox.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (lastResizeWidth != itemBox.getWidth() || lastResizeHeight != itemBox.getHeight()) {
                        lastResizeWidth = itemBox.getWidth();
                        lastResizeHeight = itemBox.getHeight();

                        Dimension imageDimension = getScaledDimension(image.getWidth(), image.getHeight(), new Dimension(itemBox.getWidth(), itemBox.getHeight()));
                        jLabel.setIcon(new ImageIcon(resize(image, (int) imageDimension.getWidth(), (int) imageDimension.getHeight())));
                        updateUI();
                        System.out.println("refreshed " + imageDimension.getWidth() + " " + imageDimension.getHeight());
                    }
                }
            });
            itemBox.add(jLabel);
        }

        reloadPlayers();
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    public static Dimension getScaledDimension(int original_width, int original_height, Dimension boundary) {
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}
