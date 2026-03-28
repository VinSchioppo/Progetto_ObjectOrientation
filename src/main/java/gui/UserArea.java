package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class UserArea {

    private JPanel mainPanel;
    private JTable datiTable;
    private JList<String> listEventiIscritti;
    private JButton setDatiButton;
    private JButton creaEventoButton;
    private JButton logOutButton;
    private JButton selectEventoButton;
    private JButton iscriviEventoButton;
    private JList ListaRichiesteTeam;
    private JList<String> listRichiestaGiudice;
    private List<String> eventiUtente;

    private gui.UserAreaFrame parentFrame;
    private Controller controller;
    private ArrayList<Integer> richiesteTeamId;
    private List<String> invitiCompleti;

    public UserArea(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabellaUtente();
        inizializzaListaEventi();
        inizializzaBottoni();
        inizializzaListaInvitiGiudice();
        System.out.println(controller.listaInvitiGiudice());
    }

    /* ============================================================
       =============== DATI UTENTE (JTABLE) =======================
       ============================================================ */
    private void inizializzaTabellaUtente() {

        String ruolo = setRuolo();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Campo", "Dato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        setDatiUtente(ruolo, model);

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

    }

    // ===== RUOLO =====
    private String setRuolo() {
        String ruolo = "";

        boolean isPartecipante = controller.isPartecipante();
        boolean isOrganizzatore = controller.isOrganizzatore();
        boolean isGiudice = controller.isGiudice();

        if (isPartecipante) {
            ruolo = "Partecipante";
        }

        if (isOrganizzatore) {
            if(!ruolo.isEmpty()) ruolo = ruolo + ", ";
            ruolo = ruolo + "Organizzatore";
        }

        if (isGiudice) {
            if(!ruolo.isEmpty()) ruolo = ruolo + ", ";
            ruolo = ruolo + "Giudice";
        }

        if(ruolo.isEmpty()) ruolo = "-";

        return ruolo;
    }

    // ==== DATI ====
    private void setDatiUtente(String ruolo, DefaultTableModel model) {
        String nome = "-";
        String secondoNome = null;
        String cognome = "-";
        String dataNascita = "-";

        String dati = controller.datiUtente();

        if (dati != null) {
            String[] parti = dati.split(" ", 4);
            if (parti.length > 0) nome = parti[0];
            if (parti.length > 1 && !"null".equalsIgnoreCase(parti[1]))
                secondoNome = parti[1];
            if (parti.length > 2) cognome = parti[2];
            if (parti.length > 3) dataNascita = parti[3];
        }
        model.addRow(new Object[]{"Nome", nome});
        if (secondoNome != null)
            model.addRow(new Object[]{"Secondo nome", secondoNome});
        model.addRow(new Object[]{"Cognome", cognome});
        model.addRow(new Object[]{"Data di nascita", dataNascita});
        model.addRow(new Object[]{"Ruolo", ruolo});
    }

    public void updateDatiUtente(String nome, String secondoNome, String cognome, String dataNascita) {
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
        model.addRow(new Object[]{"Ruolo", setRuolo()});
        datiTable.setModel(model);
    }

    //   ================= LISTA EVENTI (JLIST) =====================

    private void inizializzaListaEventi() {

        eventiUtente = controller.listaEventiPartecipante();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (eventiUtente != null) {
            for (String evento : eventiUtente) {
                model.addElement(evento);
            }
        }

        listEventiIscritti.setModel(model);

        listEventiIscritti.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listEventiIscritti.locationToIndex(e.getPoint());
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

        String evento = eventiUtente.get(index);

        // ===== ESTRAI ID =====
        int spazio = evento.indexOf(" ");
        if (spazio == -1) return;

        int idEvento;
        try {
            idEvento = Integer.parseInt(evento.substring(0, spazio));
        } catch (NumberFormatException e) {
            System.out.println("Errore parsing ID evento: " + evento);
            return;
        }

        // ===== USA NUOVO METODO =====
        String testo = controller.datiEventoPreSelezione(idEvento);

        JOptionPane.showMessageDialog(
                mainPanel,
                testo,
                "Dettagli evento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void inizializzaListaInvitiGiudice() {

        listRichiestaGiudice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listRichiestaGiudice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int index = listRichiestaGiudice.getSelectedIndex();

                    if (index == -1 || invitiCompleti == null) return;

                    String invitoCompleto = invitiCompleti.get(index);

                    int spazio = invitoCompleto.indexOf(" ");
                    if (spazio == -1) return;

                    int idEvento;

                    try {
                        idEvento = Integer.parseInt(invitoCompleto.substring(0, spazio));
                    } catch (Exception ex) {
                        return;
                    }

                    mostraDialogInvito(idEvento);
                }
            }
        });

        refreshInvitiGiudice();
    }

    private void mostraDialogInvito(int idEvento) {

        Object[] opzioni = {"Accetta", "Rifiuta", "Annulla"};

        int scelta = JOptionPane.showOptionDialog(
                mainPanel,
                "Vuoi accettare l'invito a diventare giudice?",
                "Invito giudice",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opzioni,
                opzioni[0]
        );

        boolean ok = false;

        if (scelta == 0) { // Accetta
            ok = controller.acceptInvitoGiudiceEvento(idEvento);
        }
        else if (scelta == 1) { // Rifiuta
            ok = controller.refuseInvitoGiudiceEvento(idEvento);
        }
        else {
            return; // Annulla → niente
        }

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Operazione completata",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshInvitiGiudice();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore operazione",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshInvitiGiudice() {

        DefaultListModel<String> model = new DefaultListModel<>();

        invitiCompleti = controller.listaInvitiGiudice();

        if (invitiCompleti != null) {

            for (String invito : invitiCompleti) {

                int spazio = invito.indexOf(" ");

                if (spazio != -1) {

                    // SOLO TITOLO (senza rompere nulla)
                    String titolo = invito.substring(spazio + 1);

                    model.addElement(titolo);
                } else {
                    // fallback (sicurezza)
                    model.addElement(invito);
                }
            }
        }

        listRichiestaGiudice.setModel(model);
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