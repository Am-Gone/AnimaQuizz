package fr.amgone.animaquizz.app.gui;

import fr.amgone.animaquizz.app.network.Client;
import fr.amgone.animaquizz.app.network.handlers.PacketListenerImpl;
import fr.amgone.animaquizz.shared.Party;

import javax.swing.JFrame;
import java.awt.Dimension;

public class AppWindow extends JFrame {
    private final Client client;

    private final PartiesListForm partiesListForm;
    private PartyForm partyForm;

    public AppWindow() {
        super("AnimaQuizz");
        Client client = new Client("localhost", 9001, new PacketListenerImpl(this));
        client.connect();
        this.client = client;

        partiesListForm = new PartiesListForm(this, client);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 700);
        setMinimumSize(new Dimension(1200, 700));
        this.setLocationRelativeTo(null);
        this.setContentPane(partiesListForm);
        setVisible(true);
    }

    public void updateParties(Party[] newParties) {
        partiesListForm.updateParties(newParties);
    }

    public void setParty(Party party) {
        partyForm = new PartyForm(this, client, party);
        this.setContentPane(partyForm);
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    public void addUserToParty(String user) {
        if (partyForm != null) {
            partyForm.addUser(user);
        }
    }

    public void removeUserFromParty(String user) {
        if (partyForm != null) {
            partyForm.removeUser(user);
        }
    }
}
