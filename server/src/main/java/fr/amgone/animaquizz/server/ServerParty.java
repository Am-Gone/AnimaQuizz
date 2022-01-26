package fr.amgone.animaquizz.server;

import fr.amgone.animaquizz.shared.Party;
import fr.amgone.animaquizz.shared.Player;
import fr.amgone.animaquizz.shared.items.Item;
import fr.amgone.animaquizz.shared.packets.UpdatePlayerPointsPacket;

public class ServerParty extends Party {
    private boolean hasStopped = false;
    private final Object threadLocker = new Object();

    private Item item = null;
    private long nextItemChange = 0;

    public ServerParty(String id, String name) {
        super(id, name);

        Thread thread = new Thread(() -> {
            while(!hasStopped) {
                if(getPlayers().size() > 0) {
                    if(item == null || (System.currentTimeMillis() >= nextItemChange) || getPlayers().stream().allMatch(Player::hasFoundAnswer)) {
                        item = Item.getRandomItem();
                        item.getPackets().forEach(packet -> getPlayers().forEach(player -> player.getConnection().write(packet)));
                        getPlayers().forEach(player -> {
                            player.setHasFoundAnswer(false);
                            player.getConnection().flush();
                        });
                        nextItemChange = System.currentTimeMillis() + 15 * 1000; // 15 sec
                        synchronized (threadLocker) {
                            try {
                                threadLocker.wait(15000); // We wait for 15000 seconds
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        synchronized (threadLocker) {
                            try {
                                threadLocker.wait(nextItemChange - System.currentTimeMillis()); // We wait until the time is left
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        thread.setName("Party thread " + id);
        thread.start();
    }

    @Override
    public boolean removePlayer(Player player) {
        boolean success = super.removePlayer(player);

        if(success) {
            hasStopped = true;
            synchronized (threadLocker) {
                threadLocker.notify();
            }
        }

        return success;
    }

    public void sendItemToPlayer(Player player) {
        if(item != null) {
            item.getPackets().forEach(packet -> player.getConnection().write(packet));
            player.getConnection().flush();
        }
    }

    public void handleAnswer(Player player, String answer) {
        if(item != null) {
            if(!player.hasFoundAnswer() && item.getAnswer().equals(answer)) {
                player.setPoints(player.getPoints() + 10);
                player.setHasFoundAnswer(true);

                synchronized (threadLocker) {
                    threadLocker.notify();
                }

                UpdatePlayerPointsPacket updatePlayerPointsPacket = new UpdatePlayerPointsPacket(player);
                getPlayers().forEach(players -> players.getConnection().writeAndFlush(updatePlayerPointsPacket));
            }
        }
    }
}
