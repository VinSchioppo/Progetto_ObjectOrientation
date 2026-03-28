package gui;

import controller.Controller;

import javax.swing.*;

public class LoginFrame extends JFrame {

    private Controller controller;

    public LoginFrame() {
        this.controller = new Controller();

        setTitle("Login");
        setContentPane(new LogIn(this, controller).getMainPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public void openRegisterDialog() {
        RegisterDialog dialog = new RegisterDialog(this);
        dialog.setVisible(true);
    }

    public void openUserArea() {
        UserAreaFrame userAreaFrame = new UserAreaFrame(controller);
        userAreaFrame.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true)
        );
    }
}