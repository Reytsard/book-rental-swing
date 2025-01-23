package server.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import shared.Book;
import shared.User;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BooksDatabase {
    ArrayList<Book> books = new ArrayList<>();
    public BooksDatabase(){
        initList();
    }

    public synchronized void initList() {
        Document document = null;
        File inputFile = new File("res/books.xml");

        List<Book> books = new ArrayList<>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();

            NodeList nodes = document.getElementsByTagName("books");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Element element = (Element) node;
                int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                int lenderId = Integer.parseInt(element.getElementsByTagName("lenderId").item(0).getTextContent());
                String title = element.getElementsByTagName("title").item(0).getTextContent();
                String author = element.getElementsByTagName("author").item(0).getTextContent();
                String category = element.getElementsByTagName("category").item(0).getTextContent();
                String available = element.getElementsByTagName("available").item(0).getTextContent();
                String price = element.getElementsByTagName("price").item(0).getTextContent();
                LocalDate dateBorrowed = LocalDate.parse(element.getElementsByTagName("dateBorrowed").item(0).getTextContent());
                books.add(new Book(id, lenderId, title,  author, category, Boolean.parseBoolean(available), Float.parseFloat(price), dateBorrowed));
            }
            books = (ArrayList<Book>) books;

         } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<Book> getBooks(){
        return books;
    }

    public synchronized void add(Book book){
        books.add(book);
        writeBooksToXML(books);
    }

    public synchronized Book match(String title) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(title) ) {
                return books.get(i);
            }
        }
        return new Book();
    }

    public synchronized boolean exists(String title){
        boolean exists = false;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(title)) {
                exists = true;
            }
        }
        return exists;
    }

    private synchronized void createElement(Document doc, Element parent, String tagName, String textContent) {
        Element elem = doc.createElement(tagName);
        elem.appendChild(doc.createTextNode(textContent));
        parent.appendChild(elem);
    }

    public synchronized void writeBooksToXML(List<Book> renterList) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("books");
            document.appendChild(root);

            for (Book book: books) {
                Element bookElement = document.createElement("renter");
                root.appendChild(bookElement);

//                bookElement.setAttribute("userType", book.getUserType());
                createElement(document, bookElement, "id", book.getId()+"");
                createElement(document, bookElement, "lenderId", book.getLenderId()+"");
                createElement(document, bookElement, "title", book.getTitle());
                createElement(document, bookElement, "author", book.getAuthor());
                createElement(document, bookElement, "category", book.getCategory());
                createElement(document, bookElement, "available", book.isAvailable()+"");
                createElement(document, bookElement, "price", book.getPrice()+"");
                createElement(document, bookElement, "dateBorrowed", book.getDateBorrowed()+"");
            }
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("res/books.xml"));

            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
