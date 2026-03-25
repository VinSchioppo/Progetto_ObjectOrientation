package gui;

import javax.swing.*;

public class PartecipanteGUI {

    private JPanel mainPanel;
    private JButton joinTeamButton;
    private JButton creaTeamButton;
    private JButton teamButton;
    private JButton backButton;
    private JList listEventi;

    private SelectEventoFrame parentFrame;

    public PartecipanteGUI(SelectEventoFrame parentFrame) {
        this.parentFrame = parentFrame;

        backButton.addActionListener(e -> {
            parentFrame.showHome();
        });

        teamButton.addActionListener(e -> {
            parentFrame.showTeamGUI();
        });

        creaTeamButton.addActionListener(e -> {
            parentFrame.openCreaTeamDialog();
        });

        joinTeamButton.addActionListener(e -> {
            JoinTeamDialog dialog = new JoinTeamDialog(parentFrame, parentFrame.getController());
            dialog.setVisible(true);
        });

    }



    public JPanel getMainPanel() {
        return mainPanel;
    }

}
