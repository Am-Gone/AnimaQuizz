package fr.amgone.animaquizz.app.gui;

import fr.amgone.animaquizz.app.network.Client;
import fr.amgone.animaquizz.app.network.handlers.PacketListenerImpl;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.items.Item;
import javax.swing.JFrame;
import java.awt.Dimension;

public class AppWindow extends JFrame {
    private final PartiesListForm partiesListForm;

    private PartyForm partyForm;

    public AppWindow() {
        super("AnimaQuizz");
        Client client = new Client("localhost", 9001, new PacketListenerImpl(this));
        client.connect();

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
        partyForm = new PartyForm(party);
        this.setContentPane(partyForm);
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    public void addPlayerToParty(Player player) {
        if (partyForm != null) {
            partyForm.addPlayer(player);
        }
    }

    public void removePlayerFromParty(Player player) {
        if (partyForm != null) {
            partyForm.removePlayer(player);
        }
    }

    public void setItem(Item item) {
        if (partyForm != null) {
            partyForm.setItem(item);
        }
    }
}
