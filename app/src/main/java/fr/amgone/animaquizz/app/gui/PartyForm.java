package fr.amgone.animaquizz.app.gui;

import fr.amgone.animaquizz.app.gui.component.ATextField;
import fr.amgone.animaquizz.app.network.Client;
import fr.amgone.animaquizz.shared.Party;
import org.jdesktop.swingx.JXTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

public class PartyForm extends JPanel {
    private final ArrayList<String> players = new ArrayList<>();
    private final Box playersBox;

    public PartyForm(Party party) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(new Color(63, 124, 172));

        Box partyBox = Box.createVerticalBox();
        JLabel partyName = new JLabel(party.getName());
        partyName.setPreferredSize(new Dimension(200, 80));
        partyName.setMaximumSize(new Dimension(200, 80));
        partyName.setFont(new Font(Font.SERIF, Font.BOLD, 32));
        partyName.setHorizontalAlignment(JLabel.CENTER);
        partyName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        partyName.setForeground(Color.BLACK);
        partyBox.add(partyName);

        Box container = Box.createVerticalBox();
        container.setPreferredSize(new Dimension(800, 570));
        container.setMaximumSize(new Dimension(800, 570));
        container.setMinimumSize(new Dimension(800, 570));
        container.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        container.setBackground(Color.WHITE);

        JLabel guess = new JLabel("Devine ça");
        guess.setHorizontalAlignment(JLabel.CENTER);
        guess.setVerticalAlignment(JLabel.CENTER);
        container.add(guess);
        partyBox.add(container);

        ATextField answer = new ATextField(20);
        answer.setPrompt("Écrivez votre réponse");
        answer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));

        answer.setPreferredSize(new Dimension(900, 50));
        answer.setMaximumSize(new Dimension(900, 50));
        answer.setAlignmentX(JXTextField.CENTER_ALIGNMENT);
        answer.setHorizontalAlignment(JXTextField.CENTER);
        partyBox.add(answer);

        this.add(partyBox);

        playersBox = Box.createVerticalBox();
        this.add(playersBox);
    }

    public void addPlayer(String player) {
        players.add(player);
        playersBox.removeAll();

        players.forEach(playerIteration -> playersBox.add(new JLabel(playerIteration)));

        updateUI();
    }

    public void removePlayer(String player) {
        players.remove(player);
        playersBox.removeAll();

        players.forEach(playerIteration -> playersBox.add(new JLabel(playerIteration)));

        updateUI();
    }
}
