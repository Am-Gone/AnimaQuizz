package fr.amgone.animaquizz.server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(9001);
        server.connect();
        System.out.println("Started the server on port 9001!");
    }
}
