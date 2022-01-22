package fr.amgone.animaquizz.server;

import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.items.Item;
import fr.amgone.animaquizz.shared.packets.Packet;

public class ServerParty extends Party {
    private boolean hasStopped = false;
    private final Object threadLock = new Object();

    public ServerParty(String id, String name) {
        super(id, name);

        Thread thread = new Thread(() -> {
            while(!hasStopped) {
                try {
                    if(getPlayers().size() > 0) {
                        runThread();
                    } else {
                        synchronized (threadLock) {
                            threadLock.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("Party thread " + id);
        thread.start();
    }

    private void runThread() throws InterruptedException {
        Item item = Item.getRandomItem();
        for (Player player : getPlayers()) {
            for (Packet packet : item.getPackets()) {
                player.getConnection().write(packet);
            }
            player.getConnection().flush();
        }
        synchronized (threadLock) {
            threadLock.wait(15000);
        }
    }

    @Override
    public boolean addPlayer(Player player) {
        boolean success = super.addPlayer(player);

        if(success) {
            synchronized (threadLock) {
                threadLock.notify();
            }
        }

        return success;
    }

    @Override
    public boolean removePlayer(Player player) {
        boolean success = super.removePlayer(player);

        if(success) {
            synchronized (threadLock) {
                threadLock.notify();
            }
        }

        return success;
    }

    public void stopThread() {
        hasStopped = true;
        synchronized (threadLock) {
            threadLock.notify();
        }
    }
}
