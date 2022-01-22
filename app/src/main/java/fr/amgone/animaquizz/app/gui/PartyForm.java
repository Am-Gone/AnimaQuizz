package fr.amgone.animaquizz.app.gui;

import fr.amgone.animaquizz.app.gui.component.ATextField;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.items.ImageItem;
import fr.amgone.animaquizz.shared.items.Item;
import fr.amgone.animaquizz.shared.items.TextItem;
import org.jdesktop.swingx.JXTextField;
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
import java.util.ArrayList;

public class PartyForm extends JPanel {
    private final ArrayList<String> players = new ArrayList<>();
    private final Box playersBox;
    private final Box itemBox;

    public PartyForm(Party party) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(49, 116, 158));

        Box partyBox = Box.createVerticalBox();
        JLabel partyName = new JLabel(party.getName());
        partyName.setPreferredSize(new Dimension(200, 80));
        partyName.setMaximumSize(new Dimension(200, 80));
        partyName.setFont(new Font(Font.SERIF, Font.BOLD, 32));
        partyName.setHorizontalAlignment(JLabel.CENTER);
        partyName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        partyName.setForeground(Color.BLACK);
        partyBox.add(partyName);

        itemBox = Box.createVerticalBox();
        itemBox.setPreferredSize(new Dimension(800, 570));
        itemBox.setMaximumSize(new Dimension(800, 570));
        itemBox.setMinimumSize(new Dimension(800, 570));
        itemBox.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        itemBox.setBackground(Color.GRAY);
        itemBox.setOpaque(true);

        JLabel guess = new JLabel("Devine ça");
        guess.setHorizontalAlignment(JLabel.CENTER);
        guess.setVerticalAlignment(JLabel.CENTER);
        itemBox.add(guess);
        partyBox.add(itemBox);

        ATextField answer = new ATextField(20);
        answer.setPrompt("Écrivez votre réponse");
        answer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));

        answer.setPreferredSize(new Dimension(900, 50));
        answer.setMaximumSize(new Dimension(900, 50));
        answer.setAlignmentX(JXTextField.CENTER_ALIGNMENT);
        answer.setHorizontalAlignment(JXTextField.CENTER);
        partyBox.add(answer);

        this.add(partyBox, BorderLayout.CENTER);

        playersBox = Box.createVerticalBox();
        playersBox.setMaximumSize(new Dimension(300, 600));
        playersBox.setPreferredSize(new Dimension(300, 600));
        playersBox.setBackground(new Color(20, 25, 35));
        playersBox.setOpaque(true);
        this.add(playersBox, BorderLayout.LINE_END);
    }

    public void addPlayer(String player) {
        players.add(player);
        playersBox.removeAll();

        reloadPlayers();
    }

    public void removePlayer(String player) {
        players.remove(player);
        playersBox.removeAll();

        reloadPlayers();
    }

    private void reloadPlayers() {
        players.forEach(playerIteration -> {
            JLabel playerName = new JLabel(playerIteration);
            playerName.setForeground(Color.WHITE);
            playerName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));

            Border border = playerName.getBorder();
            Border margin = new EmptyBorder(10, 10, 10, 10);

            playerName.setBorder(new CompoundBorder(border, margin));
            playersBox.add(playerName);
        });

        updateUI();
    }

    public void setItem(Item item) {
        itemBox.removeAll();
        if(item instanceof TextItem textItem) {
            itemBox.add(new JLabel(textItem.getText()));
        } else if(item instanceof ImageItem imageItem) {
            itemBox.add(new JLabel(new ImageIcon(imageItem.getBufferedImage())));
        }

        updateUI();
    }
}
