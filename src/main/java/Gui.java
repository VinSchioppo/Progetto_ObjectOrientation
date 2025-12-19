package Controller;

import javax.swing.*;
import java.awt.event.*;

//per test

import javax.swing.JFrame;



public class Gui extends JDialog {
    private JPanel SignUpPanel;
    private JTextField NomeUtente;
    private JPasswordField PasswordUtente;
    private JButton logInButton;
    private JTextArea Messaggio;
    private JButton nonHaiUnAccountButton;

    public Gui(Controller controller) {
        setContentPane(SignUpPanel);
        //setModal(true);
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

        nonHaiUnAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //System.out.println("Il bottone è stato premuto!"); // Debug

                setVisible(true);
                dispose();

                RegisterUtent registerUtent = new RegisterUtent(controller);

                JFrame frame = new JFrame("Registrazione Utente");
                frame.setContentPane(registerUtent.getRegisterUtentPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

        });

    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        Gui dialog = new Gui(controller);
        dialog.pack();
        dialog.setVisible(true);
        /*UserData userData = new UserData(controller);
        userData.pack();
        userData.setVisible(true);
        System.exit(0);*/
    }

    public static class UserData extends JDialog {
        private JPanel UserDataPanel;
        private JButton TestButton;


        /*UserData(Controller controller) {
            setContentPane(UserDataPanel);
            setModal(true);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            TestButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    RegisterUtent registerUtent = new RegisterUtent(Controller controller);

                    JDialog dialog = new JDialog(UserData.this, "Registrazione Utente", true);
                    dialog.setContentPane(registerUtent.getRegisterUtentPanel());
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.pack();
                    dialog.setLocationRelativeTo(UserData.this);
                    dialog.setVisible(true);
                }
            });
        }*/

    }
}
