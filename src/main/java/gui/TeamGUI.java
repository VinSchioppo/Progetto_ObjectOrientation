package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamGUI {

    private JButton backButton;
    private JPanel mainPanel;
    private JLabel nomeEvento;
    private JLabel descrizioneEvento;
    private JList<String> listPartecipantiTeam;
    private JButton lasciaTeamButton;
    private JButton progressiButton;
    private JLabel nomeTeamLabel;
    private JList<String> listRichiesteTeam;
    private JLabel nomeTeamLeader;
    private JTextArea descrizioneProblema;
    private Set<String> richiesteGestite = new HashSet<>();

    private static final String ACCETTA = "Accetta";
    private static final String RIFIUTA = "Rifiuta";
    private static final String ERRORE = "Errore";

    private UserAreaFrame parentFrame;
    private Controller controller;

    public TeamGUI(UserAreaFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        configuraDescrizione();

        inizializzaDati();
        inizializzaBottoni();
        inizializzaRichieste();
        inizializzaListenerRichieste();


    }

    /* ============================================================
       =================== DATI ============================
       ============================================================ */

    private void inizializzaDati() {

        String datiEvento = controller.datiEventoCorrente();

        if (datiEvento == null) {
            nomeEvento.setText("-");
            descrizioneEvento.setText("-");
            return;
        }

        // ===== SPLIT PRINCIPALE (STRUTTURA | DESCRIZIONE) =====
        String[] parti = datiEvento.split("\\|", 2);

        String parteDati = parti[0];
        String descrizione = (parti.length > 1) ? parti[1].trim() : "descrizione ancora non disponibili";

        if (descrizione.isEmpty() || descrizione.equalsIgnoreCase("null")) {
            descrizione = "descrizione ancora non disponibili";
        }

        // ===== PARSING DATI =====
        String[] dati = parteDati.split(" ");

        // ===== NOME EVENTO =====
        nomeEvento.setText(dati[0]);

        // ===== DATE EVENTO =====
        String dataInizio = "-";
        String dataFine = "-";

        int dateTrovate = 0;

        for (String d : dati) {
            if (d.matches("\\d{4}-\\d{2}-\\d{2}")) {

                if (dateTrovate == 0) {
                    dataInizio = d;
                } else if (dateTrovate == 1) {
                    dataFine = d;
                    break;
                }

                dateTrovate++;
            }
        }

        // DATE
        descrizioneEvento.setText(dataInizio + " - " + dataFine);

        // DESCRIZIONE PROBLEMA
        descrizioneProblema.setText(descrizione);

        // ===== TEAM =====
        String infoTeam = controller.teamPartecipante();

        if (infoTeam != null) {

            String[] datiTeam = infoTeam.split(" ");

            nomeTeamLabel.setText(datiTeam[1]);

            String leader = controller.teamLeaderCorrente();

            if (leader != null) {
                nomeTeamLeader.setText(leader);
            } else {
                nomeTeamLeader.setText("-");
            }

        } else {
            nomeTeamLabel.setText("Nessun team");
            nomeTeamLeader.setText("-");
        }

        aggiornaMembri();
    }

    /* ============================================================
       =================== RICHIESTE TEAM =========================
       ============================================================ */

    private void inizializzaRichieste() {

        aggiornaRichieste();

    }

    private void configuraDescrizione() {
        descrizioneProblema.setLineWrap(true);
        descrizioneProblema.setWrapStyleWord(true);
        descrizioneProblema.setEditable(false);
        descrizioneProblema.setOpaque(false);
    }

    private void inizializzaListenerRichieste() {

        listRichiesteTeam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int index = listRichiesteTeam.getSelectedIndex();

                    if (index == -1) return;

                    String utente = listRichiesteTeam.getSelectedValue();

                    gestisciRichiesta(utente, index);
                }
            }
        });
    }

    private void aggiornaRichieste() {

        List<String> richieste = controller.requestTeamNotifications();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (richieste != null) {

            for (String r : richieste) {

                String chiave = getChiaveRichiesta(r);

                if (richiesteGestite.contains(chiave)) continue;

                model.addElement(r);
            }
        }

        listRichiesteTeam.setModel(model);
    }

    private void gestisciRichiesta(String nomeUtente, int index) {

        int scelta = JOptionPane.showOptionDialog(
                mainPanel,
                "Gestire la richiesta di " + nomeUtente + "?",
                "Richiesta Team",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{ACCETTA, RIFIUTA},
                ACCETTA
        );

        if (scelta == JOptionPane.CLOSED_OPTION) return;

        boolean ok = false;

        if (scelta == 0) {
            ok = controller.acceptRichiestaTeam(nomeUtente);
        } else if (scelta == 1) {
            ok = controller.refuseRichiestaTeam(nomeUtente);
        }

        if (ok) {

            String chiave = getChiaveRichiesta(nomeUtente);
            richiesteGestite.add(chiave);

            DefaultListModel<String> model =
                    (DefaultListModel<String>) listRichiesteTeam.getModel();

            if (index >= 0 && index < model.size()) {
                model.remove(index);
            }

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Operazione completata",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            aggiornaMembri();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore durante l'operazione",
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /* ============================================================
       =================== MEMBRI TEAM ============================
       ============================================================ */

    private void aggiornaMembri() {

        List<String> membri = controller.listaMembriTeam();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (membri != null) {
            for (String m : membri) {
                model.addElement(m);
            }
        }

        listPartecipantiTeam.setModel(model);
    }

    private String getChiaveRichiesta(String nomeUtente) {

        String team = controller.nomeTeamCorrente();

        if (team == null) team = "NO_TEAM";

        return nomeUtente + "_" + team;
    }

    public void refresh() {
        inizializzaDati();
        aggiornaRichieste();
    }

    /* ============================================================
       =================== BOTTONI ============================
       ============================================================ */

    private void inizializzaBottoni() {

        backButton.addActionListener(e ->
                parentFrame.showPartecipanteGUI()
        );

        progressiButton.addActionListener(e ->
                parentFrame.showProgressiGUI()
        );

        lasciaTeamButton.addActionListener(e -> {

            int idTeam = controller.idTeamCorrente();

            if (idTeam == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Nessun team selezionato",
                        ERRORE,
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            messaggioRisposta(idTeam);
        });
    }

    private void messaggioRisposta(int idTeam) {

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
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}