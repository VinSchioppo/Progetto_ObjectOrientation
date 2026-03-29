package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

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

    private static final String ACCETTA = "Accetta";
    private static final String RIFIUTA = "Rifiuta";
    private static final String ERRORE = "Errore";

    private SelectEventoFrame parentFrame;
    private Controller controller;

    private static final Logger logger = Logger.getLogger(TeamGUI.class.getName());

    public TeamGUI(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaDati();
        inizializzaBottoni();
        inizializzaRichieste();
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
                nomeEvento.setText(dati[0]);

                StringBuilder descrizione = new StringBuilder();
                for (int i = 10; i < dati.length; i++) {
                    descrizione.append(dati[i]).append(" ");
                }

                descrizioneEvento.setText(descrizione.toString().trim());

            } catch (Exception _) {
                nomeEvento.setText(ERRORE);
                descrizioneEvento.setText("Errore parsing");
            }
        }

        // ===== TEAM =====
        String nomeTeam = controller.nomeTeamCorrente();
        nomeTeamLabel.setText(nomeTeam != null ? nomeTeam : "Nessun team");

        aggiornaMembri();

    }

    /* ============================================================
       =================== RICHIESTE TEAM =========================
       ============================================================ */

    private void inizializzaRichieste() {

        aggiornaRichieste();

        listRichiesteTeam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int index = listRichiesteTeam.locationToIndex(e.getPoint());

                    if (index == -1) return;

                    String utente = listRichiesteTeam.getModel().getElementAt(index);

                    gestisciRichiesta(utente);
                }
            }
        });
    }

    private void aggiornaRichieste() {

        List<String> richieste = controller.requestTeamNotifications();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (richieste != null) {
            for (String r : richieste) {
                model.addElement(r);
            }
        }

        listRichiesteTeam.setModel(model);
    }

    private void gestisciRichiesta(String nomeUtente) {

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

        controlloRichiesta(scelta, nomeUtente);
    }

    private void controlloRichiesta(int scelta, String nomeUtente) {

        boolean ok = false;

        if (scelta == 0) {
            logger.info(ACCETTA);
            ok = controller.acceptRichiestaTeam(nomeUtente);
        } else if (scelta == 1) {
            logger.info(RIFIUTA);
            ok = controller.refuseRichiestaTeam(nomeUtente);
        }

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Operazione completata",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            aggiornaRichieste();
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

    /* ============================================================
       =================== BOTTONI ============================
       ============================================================ */

    private void inizializzaBottoni() {

        backButton.addActionListener(e ->
                parentFrame.showPartecipanteGUI()
        );

        progressiButton.addActionListener(e ->
                parentFrame.showProgressiGUI(controller.idTeamCorrente())
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