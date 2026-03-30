package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class UserArea {

    private JPanel mainPanel;
    private JTable datiTable;
    private JList<String> listEventiIscritti;
    private JButton setDatiButton;
    private JButton creaEventoButton;
    private JButton logOutButton;
    private JButton selectEventoButton;
    private JButton iscriviEventoButton;
    private JList<String> listRichiestaGiudice;
    private JButton classificaButton;
    private List<String> eventiUtente;
    private Set<Integer> invitiGestiti = new HashSet<>();

    private static final String CAMPO = "Campo";

    private gui.UserAreaFrame parentFrame;
    private Controller controller;

    private List<String> invitiCompleti;

    private static final Logger logger = Logger.getLogger(UserArea.class.getName());

    public UserArea(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabellaUtente();
        inizializzaListaEventi();
        inizializzaBottoni();
        inizializzaListaInvitiGiudice();
    }

    /* ============================================================
       =============== DATI UTENTE (JTABLE) =======================
       ============================================================ */
    private void inizializzaTabellaUtente() {

        String ruolo = setRuolo();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{CAMPO, "Dato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        setDatiUtente(ruolo, model);

        datiTable.setModel(model);

        datiTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        datiTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        datiTable.getColumnModel().getColumn(1).setPreferredWidth(360);
        datiTable.setRowHeight(36);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < datiTable.getColumnCount(); i++) {
            datiTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        datiTable.setRowSelectionAllowed(false);
        datiTable.getTableHeader().setReorderingAllowed(false);
        datiTable.setFocusable(false);
        datiTable.setFillsViewportHeight(false);
    }

    private String setRuolo() {
        String ruolo = "";

        boolean isPartecipante = controller.isPartecipante();
        boolean isOrganizzatore = controller.isOrganizzatore();
        boolean isGiudice = controller.isGiudice();

        if (isPartecipante) ruolo = "Partecipante";

        if (isOrganizzatore) {
            if(!ruolo.isEmpty()) ruolo += ", ";
            ruolo += "Organizzatore";
        }

        if (isGiudice) {
            if(!ruolo.isEmpty()) ruolo += ", ";
            ruolo += "Giudice";
        }

        if(ruolo.isEmpty()) ruolo = "-";

        return ruolo;
    }

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
                new String[]{CAMPO, "Dato"}, 0
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

    /* ================= LISTA EVENTI ================= */

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

        if (eventiUtente == null || index >= eventiUtente.size()) return;

        String evento = eventiUtente.get(index);

        int spazio = evento.indexOf(" ");
        if (spazio == -1) return;

        int idEvento;

        try {
            idEvento = Integer.parseInt(evento.substring(0, spazio));
        } catch (NumberFormatException _) {
            logger.info("Errore parsing ID evento: " + evento);
            return;
        }

        String testo = controller.datiEventoPreSelezione(idEvento);

        JOptionPane.showMessageDialog(
                mainPanel,
                testo,
                "Dettagli evento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /* ================= INVITI GIUDICE ================= */

    private void inizializzaListaInvitiGiudice() {

        listRichiestaGiudice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listRichiestaGiudice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int index = listRichiestaGiudice.getSelectedIndex();

                    // PROTEZIONE FORTE
                    if (index == -1 || invitiCompleti == null || index >= invitiCompleti.size()) return;

                    String invitoCompleto = invitiCompleti.get(index);

                    int spazio = invitoCompleto.indexOf(" ");
                    if (spazio == -1) return;

                    int idEvento;

                    try {
                        idEvento = Integer.parseInt(invitoCompleto.substring(0, spazio));
                    } catch (Exception _) {
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

        switch(scelta) {
            case 0:
                ok = controller.acceptInvitoGiudiceEvento(idEvento);
                break;
            case 1:
                ok = controller.refuseInvitoGiudiceEvento(idEvento);
                break;
            default:
                return;
        }

        invitiGestiti.add(idEvento);

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Operazione completata",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshInvitiGiudice();
            refreshDatiUtente();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Hai già risposto a questa richiesta.",
                    "Informazione",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        refreshInvitiGiudice();
    }

    public void refreshInvitiGiudice() {

        DefaultListModel<String> model = new DefaultListModel<>();

        List<String> nuoviInviti = controller.listaInvitiGiudice();

        List<String> invitiUnici = new java.util.ArrayList<>();
        java.util.HashSet<String> visti = new java.util.HashSet<>();

        // ===== Inviti =====
        inviti(visti, nuoviInviti, invitiUnici);

        // ===== POPOLAMENTO =====
        popolamento(model, invitiUnici);

        this.invitiCompleti = invitiUnici;

        listRichiestaGiudice.setModel(model);
    }

    public void inviti(HashSet<String> visti, List<String> nuoviInviti, List<String> invitiUnici) {

        if (nuoviInviti == null) return;

        for (String invito : nuoviInviti) {

            if (isInvitoValido(invito, visti)) {
                invitiUnici.add(invito);
            }
        }
    }

    private boolean isInvitoValido(String invito, HashSet<String> visti) {

        if (invito == null || !visti.add(invito)) return false;

        int spazio = invito.indexOf(" ");
        if (spazio == -1) return false;

        int idEvento;

        try {
            idEvento = Integer.parseInt(invito.substring(0, spazio));
        } catch (Exception _) {
            return false;
        }

        return !invitiGestiti.contains(idEvento);
    }

    public void popolamento(DefaultListModel<String> model, List<String> invitiUnici ) {

        for (String invito : invitiUnici) {

            int spazio = invito.indexOf(" ");

            if (spazio != -1) {
                model.addElement(invito.substring(spazio + 1));
            } else {
                model.addElement(invito);
            }
        }

    }

    public void refreshDatiUtente() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{CAMPO, "Dato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        String ruolo = setRuolo();
        setDatiUtente(ruolo, model);

        datiTable.setModel(model);
    }

    public void refreshEventiIscritti() {

        eventiUtente = controller.listaEventiPartecipante();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (eventiUtente != null) {
            for (String evento : eventiUtente) {
                model.addElement(evento);
            }
        }

        listEventiIscritti.setModel(model);
    }

    private void inizializzaBottoni() {

        logOutButton.addActionListener(e -> parentFrame.logout());
        setDatiButton.addActionListener(e -> parentFrame.showSetDati());
        iscriviEventoButton.addActionListener(e -> parentFrame.showIscriviEvento());
        selectEventoButton.addActionListener(e -> parentFrame.showSelectEvento());
        creaEventoButton.addActionListener(e -> parentFrame.showCreaEvento());
        classificaButton.addActionListener(e -> {

            String input = JOptionPane.showInputDialog(
                    mainPanel,
                    "Inserisci ID evento",
                    "Classifica",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) return;

            int idEvento;

            try {
                idEvento = Integer.parseInt(input);
            } catch (NumberFormatException _) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "ID non valido",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            parentFrame.showClassificaGUI(idEvento);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}