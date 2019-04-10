package Main;

import javax.swing.*;

public class MainFrame {

    public static void main(String[] args) {
        JFrame window = new JFrame("Sherepa`s Journey");
        window.setContentPane(new MainFramePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
