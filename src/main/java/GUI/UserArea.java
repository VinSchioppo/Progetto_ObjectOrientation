package GUI;

import ClassModel.Evento;
import Controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UserArea {

    private JPanel mainPanel;
    private JTable datiTable;
    private JList<String> list1;
    private JButton setDatiButton;
    private JButton creaEventoButton;
    private JButton logOutButton;
    private JButton selectEventoButton;
    private JButton iscriviEventoButton;
    private JList ListaRichiesteTeam;
    private ArrayList<Evento> eventiUtente;

    private UserAreaFrame parentFrame;
    private Controller controller;
    private ArrayList<Integer> richiesteTeamId;

    public UserArea(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabellaUtente();
        inizializzaListaEventi();
        inizializzaBottoni();
    }

    /* ============================================================
       =============== DATI UTENTE (JTABLE) =======================
       ============================================================ */
    private void inizializzaTabellaUtente() {

        String dati = controller.datiUtente();

        String nome = "-";
        String secondoNome = null;
        String cognome = "-";
        String dataNascita = "-";

        if (dati != null) {
            String[] parti = dati.split(" ", 4);
            if (parti.length > 0) nome = parti[0];
            if (parti.length > 1 && !"null".equalsIgnoreCase(parti[1]))
                secondoNome = parti[1];
            if (parti.length > 2) cognome = parti[2];
            if (parti.length > 3) dataNascita = parti[3];
        }

        // ===== RUOLO =====
        String ruolo = "Partecipante";

        boolean isOrganizzatore = controller.isOrganizzatore();
        boolean isGiudice = controller.isGiudice();

        if (isOrganizzatore && isGiudice) {
            ruolo = "Partecipante, Organizzatore, Giudice";
        } else if (isOrganizzatore) {
            ruolo = "Partecipante, Organizzatore";
        } else if (isGiudice) {
            ruolo = "Partecipante, Giudice";
        }

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Campo", "Dato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        model.addRow(new Object[]{"Nome", nome});
        if (secondoNome != null)
            model.addRow(new Object[]{"Secondo nome", secondoNome});
        model.addRow(new Object[]{"Cognome", cognome});
        model.addRow(new Object[]{"Data di nascita", dataNascita});
        model.addRow(new Object[]{"Ruolo", ruolo});

        datiTable.setModel(model);

        // ===== DIMENSIONI COLONNE =====
        datiTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        datiTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        datiTable.getColumnModel().getColumn(1).setPreferredWidth(360);
        datiTable.setRowHeight(36);

        // ===== CENTRATURA TESTO =====
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < datiTable.getColumnCount(); i++) {
            datiTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ===== BLOCCO COMPORTAMENTO =====
        datiTable.setRowSelectionAllowed(false);
        datiTable.getTableHeader().setReorderingAllowed(false);
        datiTable.setFocusable(false);
        datiTable.setFillsViewportHeight(false);

        inizializzaListaRichieste();

    }

    /* ============================================================
       ================= LISTA EVENTI (JLIST) =====================
       ============================================================ */
    private void inizializzaListaEventi() {

        DefaultListModel<String> model = new DefaultListModel<>();
        eventiUtente = controller.listaEventiPartecipante();

        if (eventiUtente != null) {
            for (Evento e : eventiUtente) {
                model.addElement(e.getTitolo()); // SOLO titolo
            }
        }

        list1.setModel(model);

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list1.locationToIndex(e.getPoint());
                    if (index != -1) {
                        mostraDettagliEvento(index);
                    }
                }
            }
        });
    }

    private void mostraDettagliEvento(int index) {

        if (eventiUtente == null || index >= eventiUtente.size())
            return;

        Evento evento = eventiUtente.get(index);

        String testo =
                "Titolo: " + evento.getTitolo() + "\n\n" +
                        "Luogo: " + evento.getIndirizzoSede() + " " + evento.getNCivicoSede() + "\n\n" +
                        "Date evento: " + evento.getDataInizio() + " → " + evento.getDataFine() + "\n\n" +
                        "Registrazioni: " + evento.getDataInizioReg() + " → " + evento.getDataFineReg() + "\n\n" +
                        "Descrizione:\n" + evento.getDescrizioneProblema();

        JOptionPane.showMessageDialog(
                mainPanel,
                testo,
                "Dettagli evento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

       //===================== BOTTONI ==============================

    //NON SI PUÒ USARE RICHIESTE
    private void inizializzaListaRichieste() {

        DefaultListModel<String> model = new DefaultListModel<>();

        richiesteTeamId = new ArrayList<>();

        ArrayList<Object[]> richieste = controller.listaRichiesteTeam();

        if (richieste != null) {

            for (Object[] r : richieste) {

                String nomeTeam = (String) r[0];
                int idTeam = (int) r[1];

                model.addElement(nomeTeam);
                richiesteTeamId.add(idTeam);
            }
        }

        ListaRichiesteTeam.setModel(model);

        ListaRichiesteTeam.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int index = ListaRichiesteTeam.locationToIndex(e.getPoint());

                    if (index != -1) {
                        gestisciRichiesta(index);
                    }
                }
            }
        });
    }

    private void gestisciRichiesta(int index) {

        int idTeam = richiesteTeamId.get(index);

        int scelta = JOptionPane.showConfirmDialog(
                mainPanel,
                "Vuoi unirti al team?",
                "Invito team",
                JOptionPane.YES_NO_OPTION
        );

        boolean ok;

        if (scelta == JOptionPane.YES_OPTION) {

            ok = controller.accettaRichiestaTeam(idTeam);

        } else {

            ok = controller.rifiutaRichiestaTeam(idTeam);
        }

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Richiesta aggiornata",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            inizializzaListaRichieste();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore nell'operazione",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void inizializzaBottoni() {

        logOutButton.addActionListener(e -> parentFrame.logout());
        setDatiButton.addActionListener(e -> parentFrame.showSetDati());
        iscriviEventoButton.addActionListener(e -> parentFrame.showIscriviEvento());
        selectEventoButton.addActionListener(e -> parentFrame.openSelectEvento());
        creaEventoButton.addActionListener(e -> parentFrame.showCreaEvento());

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}