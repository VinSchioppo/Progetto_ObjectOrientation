package gui;

import controller.Controller;
import controller.Role;

import javax.swing.*;

public class CreaTeam extends JDialog {

    private JPanel mainPanel;
    private JTextField nomeTeam;
    private JButton backButton;
    private JButton saveButton;

    private static final String ERRORE = "Errore";

    private Controller controller;
    private int idEvento;

    public CreaTeam(JFrame owner, Controller controller, int idEvento) {

        super(owner, "Crea Team", true);

        this.controller = controller;
        this.idEvento = idEvento;

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);

        backButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> creaTeam());
    }

    /* ====================== CREA TEAM ====================== */

    private void creaTeam() {

        String nomeTeamTMP = this.nomeTeam.getText().trim();

        // ===== 1. VALIDAZIONE INPUT =====
        if (nomeTeamTMP.isEmpty()) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Inserisci nome team",
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ===== 2. SET EVENTO CORRENTE =====
        controller.selectEvento(idEvento, Role.PARTECIPANTE);

        // ===== 3. CONTROLLO TEAM ESISTENTE =====
        if (controller.teamPartecipante() != null) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Sei già in un team per questo evento",
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ===== 4. CREAZIONE TEAM =====
        boolean ok = controller.creaTeamPartecipante(nomeTeamTMP);

        if (ok) {

            controller.selectEvento(idEvento, Role.PARTECIPANTE);

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