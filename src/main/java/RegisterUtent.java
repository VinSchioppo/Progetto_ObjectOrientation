package Controller;

import javax.swing.*;

public class RegisterUtent {

    // Root panel
    private JPanel RegisterUtentPanel;

    // Campi di registrazione
    private JTextField textField1;        // nomeutente
    private JPasswordField passwordField1; // password
    private JPasswordField passwordField2; //conferma della password

    private JButton registratiButton;

    private Controller controller;


    public JPanel getRegisterUtentPanel() {
        return RegisterUtentPanel;
    }

    public RegisterUtent(Controller controller) {
        this.controller = controller;
        setupRegistratiButton();
    }

    private void setupRegistratiButton() {
        registratiButton.addActionListener(e -> {

            String username = textField1.getText().trim();
            String password = new String(passwordField1.getPassword());
            String conferma = new String(passwordField2.getPassword());

            // 1) Campi obbligatori
            if (username.isEmpty() || password.isEmpty() || conferma.isEmpty()) {
                JOptionPane.showMessageDialog(
                        RegisterUtentPanel,
                        "Compila username e password (e conferma password).",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 2) Password uguali
            if (!password.equals(conferma)) {
                JOptionPane.showMessageDialog(
                        RegisterUtentPanel,
                        "Le password non coincidono!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 3) Utente esiste già?
            if (controller.TrovaUtente(username, password)) {
                JOptionPane.showMessageDialog(
                        RegisterUtentPanel,
                        "Impossibile registrare l'Utente! (utente già esistente)",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        RegisterUtentPanel,
                        "Utente registrato con Successo!",
                        "Confirmation",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }
}
