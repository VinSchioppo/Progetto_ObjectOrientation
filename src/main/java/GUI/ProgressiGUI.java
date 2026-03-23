package GUI;

import Controller.Controller;

import javax.swing.*;


public class ProgressiGUI {

    private JPanel mainPanel;
    private JTable TabellaProgresso;
    private JLabel voto;
    private JButton backButton;

    private GUI.SelectEventoFrame parentFrame;
    private Controller controller;

    public ProgressiGUI(SelectEventoFrame parentFrame, Controller controller, int idTeam) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        backButton.addActionListener(e -> parentFrame.showTeamGUI());

    }


}