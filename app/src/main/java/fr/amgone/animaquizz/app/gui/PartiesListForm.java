package fr.amgone.animaquizz.app.gui;

import fr.amgone.animaquizz.app.gui.component.ATextField;
import fr.amgone.animaquizz.app.network.Client;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.packets.CreatePartyPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class PartiesListForm extends JPanel {
    private final Client client;
    private final Box box1;
    private final Box box2;

    public PartiesListForm(JFrame jFrame, Client client) {
        this.client = client;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(63, 124, 172));

        ATextField username = new ATextField(20);
        username.setPreferredSize(new Dimension(jFrame.getWidth() / 2, 32));
        username.setMaximumSize(new Dimension(jFrame.getWidth() / 2, 32));
        username.setPrompt("Username");
        username.setHorizontalAlignment(JTextField.CENTER);
        this.add(username);

        Box bigBox = Box.createHorizontalBox();
        bigBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        box1 = Box.createVerticalBox();
        box2 = Box.createVerticalBox();

        box1.setMaximumSize(new Dimension(210, 600));
        box1.setPreferredSize(new Dimension(210, 600));

        box2.setMaximumSize(new Dimension(210, 600));
        box2.setPreferredSize(new Dimension(210, 600));

        bigBox.add(box1);
        bigBox.add(box2);

        JScrollPane jScrollPane = new JScrollPane(bigBox);
        jScrollPane.setMaximumSize(new Dimension(450, 600));
        jScrollPane.setPreferredSize(new Dimension(450, 600));
        this.add(jScrollPane);


        Box createPartyBox = Box.createHorizontalBox();
        ATextField partyNameField = new ATextField(20);
        partyNameField.setPreferredSize(new Dimension(300, 32));
        partyNameField.setMaximumSize(new Dimension(300, 32));
        partyNameField.setHorizontalAlignment(JTextField.CENTER);
        partyNameField.setPrompt("Nom de la partie");

        createPartyBox.add(partyNameField);
        JButton createPartyButton = new JButton("Créer la partie");
        createPartyBox.add(createPartyButton);

        this.add(createPartyBox);

        createPartyButton.addActionListener(actionEvent -> {
            if(partyNameField.getText().length() == 0) {
                JOptionPane.showMessageDialog(jFrame, "Veuillez entrer un nom pour votre partie.");
            } else if(partyNameField.getText().length() > 24) {
                JOptionPane.showMessageDialog(jFrame, "Le nom de la partie doit faire 24 charactères maximum.");
            } else {
                client.sendPacket(new CreatePartyPacket(partyNameField.getText()));
            }
        });
    }

    public void updateParties(Party[] newParties) {
        box1.removeAll();
        box2.removeAll();

        for (int i = 0; i < newParties.length; i++) {
            JButton jButton = new JButton(newParties[i].getName());
            jButton.setName(newParties[i].getId());
            jButton.setMaximumSize(new Dimension(200, 20));
            jButton.setPreferredSize(new Dimension(200, 20));
            jButton.addActionListener(actionEvent -> client.sendPacket(new JoinPartyPacket(new Party(jButton.getName(), jButton.getText()))));

            if(i % 2 == 0) {
                box1.add(jButton);
            } else {
                box2.add(jButton);
            }
        }

        updateUI();
    }
}
