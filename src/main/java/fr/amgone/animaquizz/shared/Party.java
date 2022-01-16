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
        if(users.size() < 4) {
            users.add(user);
            return true;
        } else {
            return false;
        }
    }
}
