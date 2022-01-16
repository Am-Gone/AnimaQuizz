package fr.amgone.animaquizz.app.gui;

import fr.amgone.animaquizz.app.network.Client;
import fr.amgone.animaquizz.app.network.handlers.PacketListenerImpl;
import fr.amgone.animaquizz.shared.Party;

import javax.swing.JFrame;
import java.awt.Dimension;

public class AppWindow extends JFrame {
    private final PartiesListForm partiesListForm;

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
}
