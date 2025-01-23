package server.controller;

import server.model.BooksDatabase;
import server.model.UsersDatabase;
import server.view.ServerView;
import shared.Book;
import shared.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServerController implements Runnable {
    UsersDatabase usersDatabase;
    BooksDatabase booksDatabase;
    ServerView serverView;

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2000);
            usersDatabase = new UsersDatabase();
            booksDatabase = new BooksDatabase();
            serverView = new ServerView();
            serverSocket.setReuseAddress(true);
            while (true) {
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

    public class ClientHandler implements Runnable {

        Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream objectInputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                String request;
                while (!(request = (String) objectInputStream.readObject()).equals("exit")) {
                    switch (request) {
                        case "log in":
                            User account = new User();
                            String username = (String) objectInputStream.readObject();
                            String password = (String) objectInputStream.readObject();
                            if (!username.isEmpty() || !password.isEmpty()) {
                                account = usersDatabase.match(username, password);
                                if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                                    objectOutputStream.writeObject(account);
                                } else {
                                    objectOutputStream.writeObject("Invalid Credentials");
                                }
                            } else {
                                objectOutputStream.writeObject("Fill out empty fields.");
                            }
                            request = "";
                            break;
                        case "create account":
                            User newAccount = new User();
                            int id = usersDatabase.getUsers().size();
                            String firstName = (String) objectInputStream.readObject();
                            String lastName = (String) objectInputStream.readObject();
                            String middleName = (String) objectInputStream.readObject();
                            LocalDate birthdate = (LocalDate) objectInputStream.readObject();
                            List<Integer> borrowedBooks = (ArrayList<Integer>) objectInputStream.readObject();
                            String newUsername = (String) objectInputStream.readObject();
                            String newPassword = (String) objectInputStream.readObject();
                            String confirmPassword = (String) objectInputStream.readObject();
                            newAccount = new User(id, firstName, lastName, middleName, birthdate, borrowedBooks, newUsername, newPassword);
                            if (!newUsername.isEmpty() || !firstName.isEmpty() || !lastName.isEmpty() || !middleName.isEmpty() || !confirmPassword.isEmpty()) {
                                if (confirmPassword.equals(newPassword)) {
                                    if (!usersDatabase.exists(newAccount.getUsername())) {
                                        usersDatabase.add(newAccount);
                                        objectOutputStream.writeObject(newAccount);
                                    } else {
                                        objectOutputStream.writeObject(newAccount.getUsername() + " already exists.");
                                    }

                                } else {
                                    objectOutputStream.writeObject("Passwords do not match");
                                }
                            } else {
                                objectOutputStream.writeObject("Fill out empty Fields");
                            }
                            request = "";
                            break;
                        case "add":
                            if (((String) objectInputStream.readObject()).equals("confirm")) {
                                int lenderId = -1;
                                String title = (String) objectInputStream.readObject();
                                String author = (String) objectInputStream.readObject();
                                String category = (String) objectInputStream.readObject();
                                boolean available = true;
                                float price = Float.parseFloat((String) objectInputStream.readObject());
                                LocalDate dateBorrowed = LocalDate.parse((String) objectInputStream.readObject());

                                Book newBook = new Book(
                                        booksDatabase.getBooks().size(),
                                        lenderId,
                                        title,
                                        author,
                                        category,
                                        available,
                                        price,
                                        dateBorrowed
                                );

                                if (!booksDatabase.exists(newBook)) {
                                    booksDatabase.add(newBook);
                                    objectOutputStream.writeObject("Car added.");
                                } else objectOutputStream.writeObject("Car already Exists");

                            }
                            request = "";
                            break;
                        case "books":
                            for (Book book : booksDatabase.getBooks()) {
                                objectOutputStream.writeObject(book.toString());
                            }
                            objectOutputStream.writeObject("end");
                            request = "";
                            break;
                        case "edit":
                            if (((String) objectInputStream.readObject()).equals("confirm")) {
                                int bookId = Integer.parseInt((String) objectInputStream.readObject());
                                int lenderId = Integer.parseInt((String) objectInputStream.readObject());
                                String editTitle = (String) objectInputStream.readObject();
                                String editAuthor = (String) objectInputStream.readObject();
                                String editCategory = (String) objectInputStream.readObject();
                                boolean editAvailable = Boolean.parseBoolean((String) objectInputStream.readObject());
                                float editPrice = Float.parseFloat((String) objectInputStream.readObject());
                                LocalDate editDateBorrowed = LocalDate.parse((String) objectInputStream.readObject());
                                Book editBook = new Book(
                                        bookId,
                                        lenderId,
                                        editTitle,
                                        editAuthor,
                                        editCategory,
                                        editAvailable,
                                        editPrice,
                                        editDateBorrowed
                                );

                                if (booksDatabase.exists(editBook)) {
                                    booksDatabase.edit(editBook);
                                    objectOutputStream.writeObject("Car edited.");
                                } else {
                                    objectOutputStream.writeObject("Avoid making changes to book id");
                                }
                            }
                            request = "";
                            break;
                        case "delete":
                            if (((String) objectInputStream.readObject()).equals("confirm")) {
                                int bookId = Integer.parseInt((String) objectInputStream.readObject());
                                int lenderId = Integer.parseInt((String) objectInputStream.readObject());
                                String toRemoveTitle = (String) objectInputStream.readObject();
                                String toRemoveAuthor = (String) objectInputStream.readObject();
                                String toRemoveCategory = (String) objectInputStream.readObject();
                                boolean toRemoveAvailable = Boolean.parseBoolean((String) objectInputStream.readObject());
                                float toRemovePrice = Float.parseFloat((String) objectInputStream.readObject());
                                LocalDate toRemoveDateBorrowed = LocalDate.parse((String) objectInputStream.readObject());
                                Book toRemoveBook = new Book(
                                        bookId,
                                        lenderId,
                                        toRemoveTitle,
                                        toRemoveAuthor,
                                        toRemoveCategory,
                                        toRemoveAvailable,
                                        toRemovePrice,
                                        toRemoveDateBorrowed
                                );

                                if (booksDatabase.exists(toRemoveBook)) {
                                    booksDatabase.delete(toRemoveBook);
                                    objectOutputStream.writeObject("Book Deleted.");
                                } else {
                                    objectOutputStream.writeObject("Error Deleting book.");
                                }
                            }
                            request = "";
                            break;
                        case "available":
                            for (Book book : booksDatabase.getBooks()) {
                                objectOutputStream.writeObject(book.toString());
                            }
                            objectOutputStream.writeObject("end");
                            request = "";
                            break;
                        case "rent":
                            if (((String) objectInputStream.readObject()).equals("confirm")) {
                                int bookId = Integer.parseInt((String) objectInputStream.readObject());
                                int lenderId = Integer.parseInt((String) objectInputStream.readObject());
                                String toRentTitle = (String) objectInputStream.readObject();
                                String toRentAuthor = (String) objectInputStream.readObject();
                                String toRentCategory = (String) objectInputStream.readObject();
                                boolean toRentAvailable = Boolean.parseBoolean((String) objectInputStream.readObject());
                                float toRentPrice = Float.parseFloat((String) objectInputStream.readObject());
                                LocalDate toRentDateBorrowed = LocalDate.parse((String) objectInputStream.readObject());
                                Book toRentBook = new Book(
                                        bookId,
                                        lenderId,
                                        toRentTitle,
                                        toRentAuthor,
                                        toRentCategory,
                                        toRentAvailable,
                                        toRentPrice,
                                        toRentDateBorrowed
                                );
                                if (booksDatabase.exists(toRentBook)) {
                                    booksDatabase.edit(toRentBook);
                                    objectOutputStream.writeObject("Book rented.");
                                } else {
                                    objectOutputStream.writeObject("Error in renting book.");
                                }
                            }
                            request = "";
                            break;
                        case "log out":
                            request = "";
                        default:
                            break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                try{
                    if(objectInputStream != null){
                        objectInputStream.close();
                    }
                    if(objectOutputStream != null){
                        objectOutputStream.close();
                    }
                }catch (IOException e){
                    throw new RuntimeException();
                }
            }
        }
    }
}
