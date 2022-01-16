package fr.amgone.animaquizz.app.network.handlers;

import fr.amgone.animaquizz.app.gui.AppWindow;
import fr.amgone.animaquizz.shared.packets.CreatePartyPacket;
import fr.amgone.animaquizz.shared.packets.FetchPartiesPacket;
import fr.amgone.animaquizz.shared.packets.JoinPartyPacket;
import fr.amgone.animaquizz.shared.packets.PacketListener;

public class PacketListenerImpl implements PacketListener {
    private final AppWindow appWindow;

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
    }
}
