package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JList listRichiestaGiudice;
    private List<String> eventiUtente;

    private GUI.UserAreaFrame parentFrame;
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

    }

    //   ================= LISTA EVENTI (JLIST) =====================

    private void inizializzaListaEventi() {

        eventiUtente = controller.listaEventiPartecipante();


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

        String testo = controller.datiEvento();

        JOptionPane.showMessageDialog(
                mainPanel,
                testo,
                "Dettagli evento",
                JOptionPane.INFORMATION_MESSAGE
        );
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