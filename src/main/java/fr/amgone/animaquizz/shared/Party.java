package fr.amgone.animaquizz.shared;

import java.util.ArrayList;

public final class Party {
    private final String id;
    private final String name;
    private final ArrayList<User> users = new ArrayList<>();

    public Party(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

    public boolean addUser(User user) {
        if(users.size() < 10) {
            users.add(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a user from the party
     * @param user that needs to be removed
     * @return true if there are no users left
     */
    public boolean removeUser(User user) {
        users.remove(user);
        return users.size() == 0;
    }
}
