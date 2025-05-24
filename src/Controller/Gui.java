package Controller;

import javax.swing.*;
import java.awt.event.*;


public class Gui extends JDialog {
    private JPanel contentPane;
    private JTextField NomeUtente;
    private JPasswordField PasswordUtente;
    private JButton logInButton;
    private JTextArea Messaggio;

    public Gui() {
        setContentPane(contentPane);
        setModal(true);

        logInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Controller.TrovaUtente(NomeUtente.getText(), PasswordUtente.getText())) {
                    JOptionPane.showMessageDialog(null, "Impossibile registrare l'Utente!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Utente registrato con Successo!", "Comfirmation", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
    }

    public static void main(String[] args) {
        Gui dialog = new Gui();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
