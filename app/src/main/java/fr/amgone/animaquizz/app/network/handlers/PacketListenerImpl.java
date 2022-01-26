package fr.amgone.animaquizz.app.network.handlers;

import fr.amgone.animaquizz.app.gui.AppWindow;
import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.items.ImageItem;
import fr.amgone.animaquizz.shared.packets.*;
import fr.amgone.animaquizz.shared.packets.items.ImageItemPacket;
import fr.amgone.animaquizz.shared.packets.items.TextItemPacket;
import fr.amgone.animaquizz.shared.utils.ImageFrame;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;

public class PacketListenerImpl implements PacketListener {
    private final AppWindow appWindow;
    private Party party = null;
    private final ImageFrame imageFrame = new ImageFrame();

    public PacketListenerImpl(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void handleFetchParties(FetchPartiesPacket fetchPartiesPacket) {
        appWindow.updateParties(fetchPartiesPacket.getParties());
    }

    @Override
    public void handleCreateParty(CreatePartyPacket createPartyPacket) {
        // We do nothing.
    }

    @Override
    public void handleJoinParty(JoinPartyPacket joinPartyPacket) {
        System.out.println("Joined party \"" + joinPartyPacket.getParty().getName() + "\" with room ID " + joinPartyPacket.getParty().getId());

        party = joinPartyPacket.getParty();
        appWindow.setParty(party);
    }

    @Override
    public void handleJoinPartyError(JoinPartyErrorPacket joinPartyErrorPacket) {
        switch (joinPartyErrorPacket.getError()) {
            case PARTY_FULL -> JOptionPane.showMessageDialog(appWindow, "La partie que vous essayez de rejoindre est pleine.", "Erreur", JOptionPane.WARNING_MESSAGE);
            case USERNAME_ALREADY_TAKEN -> JOptionPane.showMessageDialog(appWindow, "Ce nom d'utilisateur est déjà utilisé dans la partie.", "Erreur", JOptionPane.WARNING_MESSAGE);
            case PARTY_DOES_NOT_EXISTS -> JOptionPane.showMessageDialog(appWindow, "Cette partie n'existe pas.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void handlePlayerPartyPresence(PlayerPartyPresencePacket playerJoinPartyPacket) {
        if(playerJoinPartyPacket.getAction() == PlayerPartyPresencePacket.Action.JOIN) {
            appWindow.addPlayerToParty(new Player(playerJoinPartyPacket.getUsername()));
        } else {
            appWindow.removePlayerFromParty(party.getPlayerFromUsername(playerJoinPartyPacket.getUsername()));
        }
    }

    @Override
    public void handleTextItem(TextItemPacket textItemPacket) {
        appWindow.setItem(textItemPacket.getTextItem());
    }

    @Override
    public void handleImageItem(ImageItemPacket imageItemPacket) {
        if(imageFrame.addFrame(imageItemPacket)) {
            BufferedImage bufferedImage = imageFrame.getImage();
            appWindow.setItem(new ImageItem(imageFrame.getQuestion(), bufferedImage));
        }
    }

    @Override
    public void handleAnswerPacket(AnswerPacket answerPacket) {
        // We do nothing.
    }

    @Override
    public void handleUpdatePlayerPoints(UpdatePlayerPointsPacket updatePlayerPointsPacket) {
        appWindow.setPoints(party.getPlayerFromUsername(updatePlayerPointsPacket.getUsername()), updatePlayerPointsPacket.getPoints());
    }
}
