package gui;

import controller.Controller;

import javax.swing.*;

public class LogIn {
    private JTextField textField1;
    private JPasswordField passwordField2;
    private JButton logInButton;
    private JPanel mainPanel;
    private JButton registrazioneButton;

    private LoginFrame parentFrame;
    private Controller controller;

    public LogIn(LoginFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        logInButton.addActionListener(e -> {

            String username = textField1.getText();
            String password = new String(passwordField2.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Inserisci username e password"
                );
                return;
            }

            boolean ok = controller.logInUtente(username, password);

            if (ok) {
                parentFrame.openUserArea(); //QUI è L'ERRORE
            } else {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Credenziali non valide"
                );
            }
        });

        registrazioneButton.addActionListener(e -> {
            parentFrame.showRegister();
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
