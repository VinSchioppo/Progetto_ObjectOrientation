package gui;

import controller.Controller;
import controller.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

public class OrganizzatoreGUI {

    private JPanel mainPanel;
    private JTable table1;
    private JButton setDatiEventoButton;
    private JButton invitaGiudiceButton;
    private JButton backButton;

    private List<String> eventiOrganizzatore;

    private UserAreaFrame parentFrame;
    private Controller controller;

    private static final Logger logger = Logger.getLogger(OrganizzatoreGUI.class.getName());

    public OrganizzatoreGUI(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabella();
        inizializzaBottoni();
    }

    /* ============================================================
       ================== TABELLA EVENTI ==========================
       ============================================================ */
    private void inizializzaTabella() {

        String[] colonne = { "Titolo", "Stato" };

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        eventiOrganizzatore = controller.listaEventiOrganizzatore();

        if (eventiOrganizzatore != null) {

            for (String evento : eventiOrganizzatore) {

                try {
                    // ===== ID =====
                    int spazio = evento.indexOf(" ");
                    if (spazio == -1) continue;

                    int idEvento = Integer.parseInt(evento.substring(0, spazio));

                    // ===== TITOLO =====
                    String titolo = evento.substring(spazio + 1);

                    // ===== DATI COMPLETI =====
                    controller.selectEvento(idEvento, Role.ORGANIZZATORE);
                    String dati = controller.datiEvento();

                    // ===== STATO =====
                    String stato;
                    if (dati == null || dati.contains("null")) {
                        stato = "❌";
                    } else {
                        stato = "✅";
                    }

                    model.addRow(new Object[]{ titolo, stato });

                } catch (Exception _) {
                    logger.info("Errore evento: " + evento);
                }
            }
        }

        table1.setModel(model);

        // ===== CONFIG TABELLA =====
        configurazioneTabella();

        // ===== CENTRATURA =====
        centratura();

        // ===== SELEZIONE =====
        selezione();

        // ===== DOPPIO CLICK =====
        doppioclick();
    }

    private void configurazioneTabella() {

        table1.setRowHeight(32);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);

    }

    private void centratura(){

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table1.getTableHeader().setReorderingAllowed(false);

    }

    private void selezione(){

        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table1.getSelectedRow();

                if (row != -1) {
                    selezionaEvento(row);
                    setDatiEventoButton.setEnabled(true);
                    invitaGiudiceButton.setEnabled(true);
                }
            }
        });

    }

    private void doppioclick(){

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table1.getSelectedRow();
                    if (row != -1) {
                        mostraDettagliEvento(row);
                    }
                }
            }
        });

    }

    /* ============================================================
       ================= SELEZIONE EVENTO =========================
       ============================================================ */
    private void selezionaEvento(int row) {

        String evento = eventiOrganizzatore.get(row);

        int spazio = evento.indexOf(" ");
        if (spazio == -1) return;

        int idEvento = Integer.parseInt(evento.substring(0, spazio));

        controller.selectEvento(idEvento, Role.ORGANIZZATORE);
    }

    /* ============================================================
       ================= DETTAGLI EVENTO ==========================
       ============================================================ */
    private void mostraDettagliEvento(int row) {

        selezionaEvento(row);

        String testo = controller.datiEvento();

        JOptionPane.showMessageDialog(
                mainPanel,
                testo,
                "Dettagli evento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /* ============================================================
       ===================== BOTTONI ==============================
       ============================================================ */
    private void inizializzaBottoni() {

        setDatiEventoButton.setEnabled(false);
        invitaGiudiceButton.setEnabled(false);

        backButton.addActionListener(e -> parentFrame.showHome());

        setDatiEventoButton.addActionListener(e -> {
            if (table1.getSelectedRow() != -1) {
                parentFrame.showSetDatiEvento();
            }
        });

        invitaGiudiceButton.addActionListener(e -> {
            if (table1.getSelectedRow() != -1) {
                parentFrame.showInvitaGiudice();
            }
        });

        table1.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table1.getSelectedRow() != -1;
            setDatiEventoButton.setEnabled(selected);
            invitaGiudiceButton.setEnabled(selected);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}