package server.controller;

import server.model.BooksDatabase;
import server.model.UsersDatabase;
import server.view.ServerView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerController implements Runnable {
    UsersDatabase usersDatabase;
    BooksDatabase booksDatabase;
    ServerView serverView;

    public ServerController(){

    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(2000);
            usersDatabase = new UsersDatabase();
            booksDatabase = new BooksDatabase();
            serverView = new ServerView();
            serverSocket.setReuseAddress(true);
            while(true){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                (new Thread(clientHandler)).start();
            }
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class ClientHandler implements Runnable{

        Socket socket;

        public ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {

        }
    }
}
