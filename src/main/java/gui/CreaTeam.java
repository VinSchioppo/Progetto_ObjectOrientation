package gui;

import controller.Controller;

import javax.swing.*;

public class CreaTeam extends JDialog {

    private JPanel mainPanel;
    private JTextField NomeTeam;
    private JList<String> ListaEventi;
    private JButton backButton;
    private JButton saveButton;

    private Controller controller;


    public CreaTeam(JFrame owner, Controller controller) {

        super(owner, "Crea Team", true);

        this.controller = controller;

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);

        caricaEventi();

        backButton.addActionListener(e -> dispose());

        saveButton.addActionListener(e -> creaTeam());
    }

    /* ====================== EVENTI ====================== */

    private void caricaEventi() {

        DefaultListModel<String> model = new DefaultListModel<>();

        controller.listaEventiAperti();

        ListaEventi.setModel(model);
    }

    /* ====================== CREA TEAM ====================== */

    private void creaTeam() {

        String nomeTeam = NomeTeam.getText().trim();

        int index = ListaEventi.getSelectedIndex();

        if (nomeTeam.isEmpty() || index == -1) {

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