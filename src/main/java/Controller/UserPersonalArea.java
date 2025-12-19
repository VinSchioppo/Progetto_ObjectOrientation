package Controller;

import javax.swing.*;

public class UserPersonalArea {

    // ROOT PANEL (binding obbligatorio)
    private JPanel UserPersonalAreaPanel;

    // Eventi
    private JComboBox ListaEventi;
    private JLabel ListaEventiLabel;
    private JButton ListaEventiButton;

    // Team
    private JLabel JoinTeamLabel;
    private JComboBox TeamBox;
    private JButton JoinButton;

    // Creazioni
    private JLabel CreaTeamLabel;
    private JButton CreaTeamButton;
    private JLabel CreaEventoLabel;
    private JButton CreaEventoButton;

    public UserPersonalArea() {
        // NON mettere layout qui
        // Il layout viene iniettato dal .form / GUI Designer
    }

    public JPanel getUserPersonalAreaPanel() {
        return UserPersonalAreaPanel;
    }
}