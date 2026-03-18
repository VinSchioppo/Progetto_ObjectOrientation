package GUI;

import ClassModel.Evento;
import Controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class iscriviEvento {

    private UserAreaFrame parentFrame;
    private JPanel mainPanel;
    private JTable table1;
    private JButton backButton;
    private JButton confermaButton;

    private Controller controller;
    private ArrayList<Evento> eventiCorrenti;

    public iscriviEvento(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabella();
        inizializzaBottoni();
    }

    //NON PUÒ USARE EVENTO
    private void inizializzaTabella() {

        String[] colonne = {
                "Evento",
                "Luogo",
                "Date evento",
                "Date registrazione",
                "Tipo",
                "Max membri team",
                "Dettagli"
        };

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // solo "Dettagli"
            }
        };

        eventiCorrenti = controller.listaEventiAperti();

        if (eventiCorrenti != null) {
            for (Evento e : eventiCorrenti) {
                model.addRow(new Object[]{
                        e.getTitolo(),
                        e.getIndirizzoSede() + " " + e.getNCivicoSede(),
                        e.getDataInizio() + " → " + e.getDataFine(),
                        e.getDataInizioReg() + " → " + e.getDataFineReg(),
                        (e.getMaxTeam() > 1 ? "Team" : "Singolo"),
                        e.getMaxTeam(),
                        "Dettagli"
                });
            }
        }

        table1.setModel(model);

        // ===== SELEZIONE RIGA =====
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);

        table1.setRowHeight(30);
        table1.setRowSelectionAllowed(true);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.getTableHeader().setResizingAllowed(false);

        // ===== COLORE SELEZIONE =====
        table1.setSelectionBackground(new java.awt.Color(184, 207, 229));
        table1.setSelectionForeground(java.awt.Color.BLACK);

        // ===== CENTRATURA CELLE =====
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table1.getColumnCount(); i++) {
            if (!table1.getColumnName(i).equals("Dettagli")) {
                table1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // ===== CENTRATURA HEADER =====
        DefaultTableCellRenderer headerRenderer =
                (DefaultTableCellRenderer) table1.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // ===== BOTTONE DETTAGLI =====
        table1.getColumn("Dettagli").setCellRenderer(new ButtonRenderer());
        table1.getColumn("Dettagli").setCellEditor(
                new ButtonEditor(table1, eventiCorrenti)
        );

        // ===== ABILITA CONFERMA ALLA SELEZIONE =====
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                confermaButton.setEnabled(table1.getSelectedRow() != -1);
            }
        });
    }

    private void inizializzaBottoni() {

        // Conferma disabilitato di default
        confermaButton.setEnabled(false);

        backButton.addActionListener(e -> parentFrame.showHome());

        confermaButton.addActionListener(e -> {

            int selectedRow = table1.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Seleziona prima un evento",
                        "Attenzione",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Evento eventoSelezionato = eventiCorrenti.get(selectedRow);
            boolean ok = controller.iscriviEvento(eventoSelezionato.getIdEvento());

            if (ok) {
                controller.exitApplication();

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Iscrizione completata con successo",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                parentFrame.showHome();
            } else {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Impossibile iscriversi all'evento",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}