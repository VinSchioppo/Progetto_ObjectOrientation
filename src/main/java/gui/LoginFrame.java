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

    public void showRegister() {

        JFrame frame = new JFrame("Registrazione");

        Register register = new Register(controller);

        frame.setContentPane(register.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

        public void openUserArea() {
        UserAreaFrame userAreaFrame = new UserAreaFrame(controller);
        userAreaFrame.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}