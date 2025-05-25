package Controller;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;


public class Gui extends JDialog {
    private JPanel SignUpPanel;
    private JTextField NomeUtente;
    private JPasswordField PasswordUtente;
    private JButton logInButton;
    private JTextArea Messaggio;
    private JLabel PasswordLabel;
    private JLabel NomeUtenteLabel;

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

    public static class UserData extends JDialog {
        private JPanel UserDataPanel;
        private JButton ComfirmationButton;
        private JTextField FNameField;
        private JTextField MNameField;
        private JTextField LNameField;
        private JFormattedTextField DataNascita;
        private JLabel FNomeLabel;
        private JLabel MNomeLabel;
        private JLabel DataDiNascitaLabel;
        private JLabel LNomeLabel;
        private JTextArea Messaggio;




        UserData(Controller controller) {
            setContentPane(UserDataPanel);
            setModal(true);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            DataNascita.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
            DataNascita.setForeground(Color.GRAY);
            DataNascita.setText("dd-MM-yyyy");

            DataNascita.addFocusListener(new FocusAdapter() {
                 public void focusGained(FocusEvent e) {
                     if (DataNascita.getText().equals("dd-MM-yyyy")) {
                         DataNascita.setText("");
                         DataNascita.setForeground(Color.BLACK);
                     }
                 }
             });

           ComfirmationButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(!controller.InserisciDatiUtente(FNameField.getText(), MNameField.getText(), LNameField.getText(), DataNascita.getText())){
                        JOptionPane.showMessageDialog(null, "Impossibile salvare l'Utente!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Dati Salvati con Successo!", "Comfirmation", JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                    }
                }
            });
        }
    }

    public static class UserPersonalArea extends JDialog {
        private JPanel UserPersonalAreaPanel;
        private JComboBox ListaEventi;
        private JButton ListaEventiButton;
        private JLabel ListaEventiLabel;
        private JLabel JoinTeamLabel;
        private JButton JoinButton;
        private JComboBox TeamBox;
        private JButton CreaTeamButton;
        private JLabel CreaTeamLabel;
        private JButton CreaEventoButton;
        private JLabel CreaEventoLabel;

        UserPersonalArea(Controller controller) {
            setContentPane(UserPersonalAreaPanel);
            setModal(true);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
        }
    }


    public static void main(String[] args) {
        Controller controller = new Controller();
        /*Gui dialog = new Gui(controller);
        dialog.pack();
        dialog.setVisible(true);
        UserData userData = new UserData(controller);
        userData.pack();
        userData.setVisible(true);*/
        UserPersonalArea userPersonalArea = new UserPersonalArea(controller);
        userPersonalArea.pack();
        userPersonalArea.setVisible(true);
        System.exit(0);
    }
}


