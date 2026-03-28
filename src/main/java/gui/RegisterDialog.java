package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {

    public RegisterDialog(JFrame owner) {
        super(owner, "Registrazione", true);

        gui.Register registerView = new Register();
        Controller controller = new Controller();

        JPanel panel = registerView.getMainPanel();
        setContentPane(panel);

        JTextField usernameField = null;
        JPasswordField passwordField1 = null;
        JPasswordField passwordField2 = null;
        JButton registerButton = null;

        // Recupero componenti dal pannello
        for (Component c : panel.getComponents()) {

            if (c instanceof JTextField jtextfield) {
                usernameField = (JTextField) c;
            }

            if (c instanceof JPasswordField jpasswordField) {
                if (passwordField1 == null)
                    passwordField1 = (JPasswordField) c;
                else
                    passwordField2 = (JPasswordField) c;
            }

            if (c instanceof JButton b) {
                b = (JButton) c;
                if ("Registrati".equalsIgnoreCase(b.getText())) {
                    registerButton = b;
                }
            }
        }

        // Listener registrazione
        if (registerButton != null) {
            JTextField finalUsernameField = usernameField;
            JPasswordField finalPasswordField1 = passwordField1;
            JPasswordField finalPasswordField2 = passwordField2;

            registerButton.addActionListener(e -> {

                String username = finalUsernameField.getText();
                String password = new String(finalPasswordField1.getPassword());
                String confirm  = new String(finalPasswordField2.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Compila tutti i campi");
                    return;
                }

                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(this, "Le password non coincidono");
                    return;
                }

                boolean ok = controller.registerUtente(username, password);

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Registrazione completata");
                    dispose(); // torni al login
                } else {
                    JOptionPane.showMessageDialog(this, "Username già esistente");
                }
            });
        }

        pack();
        setLocationRelativeTo(owner);
    }
}