package gui;

import controller.Controller;

import javax.swing.*;

public class CreaTeam extends JDialog {

    private JPanel mainPanel;
    private JTextField NomeTeam;
    private JButton backButton;
    private JButton saveButton;

    private Controller controller;


    public CreaTeam(JFrame owner, Controller controller) {

        super(owner, "Crea Team", true);

        this.controller = controller;

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);

        backButton.addActionListener(e -> dispose());

        saveButton.addActionListener(e -> creaTeam());
    }
    /* ====================== CREA TEAM ====================== */

    private void creaTeam() {

        String nomeTeam = NomeTeam.getText().trim();

        if (nomeTeam.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Inserisci nome team e seleziona evento",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        boolean ok = controller.creaTeamPartecipante(
                nomeTeam
        );

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Team creato con successo",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore creazione team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}