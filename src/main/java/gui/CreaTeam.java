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

        // ===== CONTROLLO INPUT =====

        ControlloInput(nomeTeam);

        // 🔥 CONTROLLO: ESISTE GIÀ UN TEAM?

        ExistTeam();

        // ===== CREAZIONE TEAM =====
        CreazioneTeam(nomeTeam);

    }

    private void ControlloInput(String nomeTeam) {

        if (nomeTeam.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Inserisci nome team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }
    }

    private void ExistTeam(){

        if (controller.teamPartecipante() != null) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Sei già in un team per questo evento",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
    }

    private void CreazioneTeam(String nomeTeam) {

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
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

}