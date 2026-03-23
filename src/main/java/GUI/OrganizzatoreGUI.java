package GUI;

import Controller.Controller;
import Controller.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class OrganizzatoreGUI {

    private JPanel mainPanel;
    private JTable table1;
    private JButton setDatiEventoButton;
    private JButton invitaGiudiceButton;
    private JButton backButton;

    private List<String> eventiOrganizzatore;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    public OrganizzatoreGUI(SelectEventoFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabella();
        inizializzaBottoni();
    }

    /* ============================================================
       ================== TABELLA EVENTI ==========================
       ============================================================ */
    private void inizializzaTabella() {

        // UNA SOLA COLONNA → niente parsing fragile
        String[] colonne = { "Evento" };

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        eventiOrganizzatore = controller.listaEventiOrganizzatore();

        // Mostra direttamente la stringa intera
        for (String evento : eventiOrganizzatore) {
            model.addRow(new Object[]{ evento });
        }

        table1.setModel(model);
        table1.setRowHeight(32);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);

        // ===== CENTRATURA =====
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table1.getColumnModel().getColumn(0).setCellRenderer(center);

        table1.getTableHeader().setReorderingAllowed(false);

        // ===== SELEZIONE =====
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

        // ===== DOPPIO CLICK → DETTAGLI =====
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

        // Parsing MINIMO e sicuro → solo ID
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

        // Abilitazione bottoni
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