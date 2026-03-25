package gui;

import controller.Controller;

import javax.swing.*;

public class TeamGUI {

    private JList<String> listTeam;
    private JButton backButton;
    private JButton invitaTeamButton;
    private JPanel mainPanel;
    private JLabel NomeEvento;
    private JLabel DescrizioneEvento;
    private JList<String> listPartecipantiTeam;
    private JButton lasciaTeamButton;
    private JButton progressiButton;

    private gui.SelectEventoFrame parentFrame;
    private Controller controller;

    public TeamGUI(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;


        backButton.addActionListener(e ->
                parentFrame.showPartecipanteGUI()
        );

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}