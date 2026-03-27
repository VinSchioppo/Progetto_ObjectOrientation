package gui;

import controller.Controller;

import javax.swing.*;
import java.util.List;

public class TeamGUI {

    private JButton backButton;
    private JPanel mainPanel;
    private JLabel NomeEvento;
    private JLabel DescrizioneEvento;
    private JList<String> listPartecipantiTeam;
    private JButton lasciaTeamButton;
    private JButton progressiButton;
    private JLabel nomeTeamLabel;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    public TeamGUI(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaDati();
        inizializzaBottoni();
    }

    /* ============================================================
       =================== DATI ============================
       ============================================================ */

    private void inizializzaDati() {

        // ===== EVENTO =====
        String datiEvento = controller.datiEvento();

        if (datiEvento != null) {
            String[] dati = datiEvento.split(" ");

            try {
                NomeEvento.setText(dati[0]);

                StringBuilder descrizione = new StringBuilder();
                for (int i = 10; i < dati.length; i++) {
                    descrizione.append(dati[i]).append(" ");
                }

                DescrizioneEvento.setText(descrizione.toString().trim());

            } catch (Exception e) {
                NomeEvento.setText("Errore");
                DescrizioneEvento.setText("Errore parsing");
            }
        }

        // ===== TEAM =====
        String nomeTeam = controller.nomeTeamCorrente();
        if (nomeTeam != null) {
            nomeTeamLabel.setText(nomeTeam);
        } else {
            nomeTeamLabel.setText("Nessun team");
        }

        // ===== MEMBRI =====
        List<String> membri = controller.listaMembriTeam();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (membri != null) {
            for (String m : membri) {
                model.addElement(m);
            }
        }

        listPartecipantiTeam.setModel(model);
    }

    /* ============================================================
       =================== BOTTONI ============================
       ============================================================ */

    private void inizializzaBottoni() {

        backButton.addActionListener(e ->
                parentFrame.showPartecipanteGUI()
        );

        lasciaTeamButton.addActionListener(e -> {

            int idTeam = controller.idTeamCorrente();

            if (idTeam == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Nessun team selezionato",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean ok = controller.leaveTeam(idTeam);

            if (ok) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Hai lasciato il team",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                parentFrame.showPartecipanteGUI();

            } else {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Impossibile lasciare il team",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        /*progressiButton.addActionListener(e -> {
            parentFrame.showProgressiTeam();
        });*/
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}