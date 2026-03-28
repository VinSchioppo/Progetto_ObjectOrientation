package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class Register {

    private JPanel mainPanel;
    private JTextField textField1;          // username
    private JPasswordField passwordField1;  // password
    private JPasswordField passwordField2;  // conferma password

    private JButton registratiButton;
    private JButton backButton;

    private Controller controller;

    public Register(Controller controller) {

        this.controller = controller;

        inizializzaBottoni();
    }

    /* ============================================================
       =================== BOTTONI ================================
       ============================================================ */

    private void inizializzaBottoni() {

        // ===== BACK =====
        backButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(mainPanel);
            if (window != null) {
                window.dispose();
            }
        });

        // ===== REGISTRA =====
        registratiButton.addActionListener(e -> registraUtente());
    }

    /* ============================================================
       =================== LOGICA REGISTRAZIONE ===================
       ============================================================ */

    private void registraUtente() {

        String username = textField1.getText().trim();
        String password = new String(passwordField1.getPassword());
        String conferma = new String(passwordField2.getPassword());

        // ===== VALIDAZIONI =====
        if (username.isEmpty() || password.isEmpty() || conferma.isEmpty()) {
            mostraErrore("Compila tutti i campi");
            return;
        }

        if (!password.equals(conferma)) {
            mostraErrore("Le password non coincidono");
            return;
        }

        // ===== REGISTRA =====
        boolean registrato = controller.registerUtente(username, password);

        if (!registrato) {
            mostraErrore("Username già esistente o errore DB");
            return;
        }

        // ===== LOGIN AUTOMATICO =====
        boolean loginOk = controller.logInUtente(username, password);

        if (!loginOk) {
            mostraErrore("Errore durante login automatico");
            return;
        }

        JOptionPane.showMessageDialog(
                mainPanel,
                "Registrazione completata",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE
        );

        // ===== APERTURA USER AREA =====
        JFrame userFrame = new UserAreaFrame(controller);
        userFrame.setVisible(true);

        // ===== CHIUSURA REGISTER =====
        Window window = SwingUtilities.getWindowAncestor(mainPanel);
        if (window != null) {
            window.dispose();
        }
    }


    /* ============================================================
       =================== UTILITY ================================
       ============================================================ */

    private void mostraErrore(String msg) {
        JOptionPane.showMessageDialog(
                mainPanel,
                msg,
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}