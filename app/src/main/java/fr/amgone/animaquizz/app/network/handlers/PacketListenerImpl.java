package fr.amgone.animaquizz.app.network.handlers;

import fr.amgone.animaquizz.app.gui.AppWindow;
import fr.amgone.animaquizz.shared.items.ImageItem;
import fr.amgone.animaquizz.shared.packets.CreatePartyPacket;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import fr.amgone.animaquizz.shared.packets.PacketListener;
import fr.amgone.animaquizz.shared.packets.PlayerPartyPresencePacket;
import fr.amgone.animaquizz.shared.packets.items.ImageItemPacket;
import fr.amgone.animaquizz.shared.packets.items.TextItemPacket;
import fr.amgone.animaquizz.shared.utils.ImageFrame;
import java.awt.image.BufferedImage;

public class PacketListenerImpl implements PacketListener {
    private final AppWindow appWindow;
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

    }

    @Override
    public void handleJoinParty(JoinPartyPacket joinPartyPacket) {
        System.out.println("Joined party \"" + joinPartyPacket.getParty().getName() + "\" with room ID " + joinPartyPacket.getParty().getId());
        appWindow.setParty(joinPartyPacket.getParty());
        joinPartyPacket.getParty().getPlayers().forEach(player -> appWindow.addPlayerToParty(player.getUsername()));
    }

    @Override
    public void handlePlayerPartyPresence(PlayerPartyPresencePacket playerJoinPartyPacket) {
        if(playerJoinPartyPacket.getAction() == PlayerPartyPresencePacket.Action.JOIN) {
            appWindow.addPlayerToParty(playerJoinPartyPacket.getUsername());
        } else {
            appWindow.removePlayerFromParty(playerJoinPartyPacket.getUsername());
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
            appWindow.setItem(new ImageItem(bufferedImage));
        }
    }
}
