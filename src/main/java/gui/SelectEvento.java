package gui;

import javax.swing.*;

public class SelectEvento {

    private JButton organizzatoreButton;
    private JButton partecipanteButton;
    private JButton giudiceButton;
    private JButton areaUtenteButton;
    private JPanel mainPanel;

    public SelectEvento(UserAreaFrame parentFrame) {

        areaUtenteButton.addActionListener(e ->
                parentFrame.showHome()
        );

        organizzatoreButton.addActionListener(e ->
                parentFrame.showOrganizzatoreGUI()
        );

        partecipanteButton.addActionListener(e ->
                parentFrame.showPartecipanteGUI()
        );

        giudiceButton.addActionListener(e ->
                parentFrame.showGiudiceGUI()
        );
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}