package server.view;

import javax.swing.*;

public class ServerView {
    JFrame frame;
    JLabel label;
    JPanel panel;

    public ServerView() {
        frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(50,50,200,100);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0,0,200,20);

        label = new JLabel("Server is Running");
        label.setBounds(0,0,200,20);

        panel.add(label);

        frame.add(panel);

        frame.setVisible(true);
    }
}
