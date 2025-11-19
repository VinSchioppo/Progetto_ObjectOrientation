package Controller;

import javax.swing.*;
import java.awt.event.*;


public class Gui extends JDialog {
    private JPanel SignUpPanel;
    private JTextField NomeUtente;
    private JPasswordField PasswordUtente;
    private JButton logInButton;
    private JTextArea Messaggio;

    public Gui(Controller controller) {
        setContentPane(SignUpPanel);
        setModal(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        logInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!controller.TrovaUtente(NomeUtente.getText(), PasswordUtente.getText())) {
                    JOptionPane.showMessageDialog(null, "Impossibile registrare l'Utente!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Utente registrato con Successo!", "Comfirmation", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                }

            }
        });
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        Gui dialog = new Gui(controller);
        dialog.pack();
        dialog.setVisible(true);
        UserData userData = new UserData(controller);
        userData.pack();
        userData.setVisible(true);
        System.exit(0);
    }

    public static class UserData extends JDialog {
        private JPanel UserDataPanel;
        private JButton TestButton;


        UserData(Controller controller) {
            setContentPane(UserDataPanel);
            setModal(true);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            TestButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Ciao\n");
                }
            });
        }
    }
}
