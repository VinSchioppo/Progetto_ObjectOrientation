package gui;

import controller.Controller;

import javax.swing.*;

public class CreaTeam extends JDialog {

    private JPanel mainPanel;
    private JTextField nomeTeam;
    private JButton backButton;
    private JButton saveButton;

    private static final String ERRORE = "Errore";

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

        String nomeTeamTMP = this.nomeTeam.getText().trim();

        // ===== CONTROLLO INPUT =====

        controlloInput(nomeTeamTMP);

        // 🔥 CONTROLLO: ESISTE GIÀ UN TEAM?

        existTeam();

        // ===== CREAZIONE TEAM =====
        creazioneTeam(nomeTeamTMP);

    }

    private void controlloInput(String nomeTeam) {

        if (nomeTeam.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Inserisci nome team",
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void existTeam(){

        if (controller.teamPartecipante() != null) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Sei già in un team per questo evento",
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void creazioneTeam(String nomeTeam) {

        boolean ok = controller.creaTeamPartecipante(nomeTeam);

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
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

}