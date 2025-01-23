package server;

import server.controller.ServerController;

public class ServerMain {
    public static void main(String[] args) {
        new Thread(new ServerController()).run();
    }
}
