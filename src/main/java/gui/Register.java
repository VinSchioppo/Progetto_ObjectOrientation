package gui;

import javax.swing.*;
import java.awt.*;

public class Register {

    private JPanel mainPanel;              // Root panel
    private JTextField textField1;        // nomeutente
    private JPasswordField passwordField1; // password
    private JPasswordField passwordField2; //conferma della password

    private JButton registratiButton;
    private JButton backButton;

    public Register() {

        backButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(mainPanel);
            if (window != null) {
                window.dispose();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
