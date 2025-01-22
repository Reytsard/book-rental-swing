package server.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import shared.User;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsersDatabase {
    private ArrayList<User> users;

    public UsersDatabase() {
        initList();
    }

    public synchronized void initList() {
        Document document = null;
        File inputFile = new File("res/renters.xml");

        List<User> users = new ArrayList<>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();

            NodeList nodes = document.getElementsByTagName("users");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Element element = (Element) node;
                int userId = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String middleName = element.getElementsByTagName("middleName").item(0).getTextContent();
                LocalDate birthdate = LocalDate.parse(element.getElementsByTagName("birthdate").item(0).getTextContent());
                ArrayList<Integer> books = getBooks(element.getElementsByTagName("borrowedBooks"));
                String username = element.getElementsByTagName("username").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();
                users.add(new User(userId, firstName, lastName, middleName, birthdate, books, username, password));
            }
            this.users = (ArrayList<User>) users;

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized ArrayList<Integer> getBooks(NodeList borrowedBooks) {
        ArrayList<Integer> books = new ArrayList<>();
        for (int i = 0; i < borrowedBooks.getLength(); i++) {
            books.add(Integer.parseInt(borrowedBooks.item(i).getTextContent()));
        }
        return books;
    }

    public synchronized List<User> getUsers(){
        return users;
    }

    public synchronized void add(User user){
        users.add(user);
        //add writeUsersToXML(users);
    }

    public synchronized User match(String username, String password){
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getUsername().equals(password)) {
                return user;
            }

        }
        return new User();
    }

    public synchronized boolean exists(String username){
        boolean exists = false;
        for(User user: users){
            if (user.getUsername().equals(username)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public synchronized void createElement(Document doc, Element parent, String tagName, String textContent){
        Element elem = doc.createElement(tagName);
        elem.appendChild(doc.createTextNode(textContent));
        parent.appendChild(elem);
    }

    public synchronized void writeUsersToXML(List<User> usersList){
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("users");
            document.appendChild(root);

            for(User user: users){
                Element userElement = document.createElement("user");
                root.appendChild(userElement);

//                userElement attribute? user or admin or what
                createElement(document, userElement, "id", user.getId()+"");
                createElement(document, userElement,"firstName",user.getFirstName());
                createElement(document, userElement,"lastName",user.getLastName());
                createElement(document, userElement,"lastName",user.getLastName());
                createElement(document, userElement,"birthdate", user.getBirthdate().toString());
                createElement(document, userElement,"birthdate", user.getBirthdate().toString());
                for (int i = 0; i < user.getBorrowedBooks().size(); i++) {
                    int bookId = user.getBorrowedBooks().get(i);
                    createElement(document,document.createElement("borrowedBooks"),"book",bookId+"" );
                }
                createElement(document, userElement,"username", user.getUsername());
                createElement(document, userElement,"password", user.getPassword());

            }

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("res/renters.xml"));
            transformer.transform(domSource, streamResult);

        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


}
