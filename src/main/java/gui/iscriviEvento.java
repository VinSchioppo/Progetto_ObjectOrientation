package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class iscriviEvento {

    private gui.UserAreaFrame parentFrame;
    private JPanel mainPanel;
    private JTable table1;
    private JButton backButton;
    private JButton confermaButton;

    private Controller controller;
    private List<String> eventiCorrenti;
    private List<Integer> idEventiMostrati;
    private List<String> eventiMostrati;

    public iscriviEvento(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaTabella();
        inizializzaBottoni();
    }

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
                return column == 6;
            }
        };

        table1.setModel(model);

        eventiCorrenti = controller.listaEventiAperti();
        eventiMostrati = new ArrayList<>();
        idEventiMostrati = new ArrayList<>();

        // ===== EVENTI GIÀ ISCRITTI =====
        Set<Integer> eventiIscrittiId = new HashSet<>();
        List<String> eventiIscritti = controller.listaEventiPartecipante();

        if (eventiIscritti != null) {
            for (String e : eventiIscritti) {
                int spazio = e.indexOf(" ");
                if (spazio != -1) {
                    int id = Integer.parseInt(e.substring(0, spazio));
                    eventiIscrittiId.add(id);
                }
            }
        }

        if (eventiCorrenti != null) {

            for (String evento : eventiCorrenti) {

                try {
                    // ===== ID EVENTO =====
                    int spazio = evento.indexOf(" ");
                    int idEvento = Integer.parseInt(evento.substring(0, spazio));

                    // ===== FILTRO: GIÀ ISCRITTO =====
                    if (eventiIscrittiId.contains(idEvento)) continue;

                    idEventiMostrati.add(idEvento);
                    eventiMostrati.add(evento);


                    // ===== SPLIT SOLO DOPO FILTRO =====
                    String[] dati = evento.split(" ");

                    // ===== TROVA DATE =====
                    int firstDateIndex = -1;

                    for (int i = 1; i < dati.length; i++) {
                        if (dati[i].matches("\\d{4}-\\d{2}-\\d{2}")) {
                            firstDateIndex = i;
                            break;
                        }
                    }

                    if (firstDateIndex == -1) {
                        System.out.println("Errore parsing (date non trovate): " + evento);
                        continue;
                    }

                    // ===== COSTRUZIONE TESTO =====
                    StringBuilder titoloBuilder = new StringBuilder();
                    StringBuilder luogoBuilder = new StringBuilder();

                    int splitIndex = 1 + (firstDateIndex - 1) / 2;

                    for (int i = 1; i < splitIndex; i++) {
                        titoloBuilder.append(dati[i]).append(" ");
                    }

                    for (int i = splitIndex; i < firstDateIndex; i++) {
                        luogoBuilder.append(dati[i]).append(" ");
                    }

                    String titolo = titoloBuilder.toString().trim();
                    String luogo = luogoBuilder.toString().trim();

                    // ===== DATE =====
                    String dateEvento = dati[firstDateIndex] + " - " + dati[firstDateIndex + 1];
                    String dateReg = dati[firstDateIndex + 4] + " - " + dati[firstDateIndex + 5];

                    // ===== MAX TEAM =====
                    String maxTeam = dati[firstDateIndex + 3];

                    model.addRow(new Object[]{
                            titolo,
                            luogo,
                            dateEvento,
                            dateReg,
                            "N/A",
                            maxTeam,
                            "Dettagli"
                    });

                } catch (Exception ex) {
                    System.out.println("Errore parsing evento: " + evento);
                    ex.printStackTrace();
                }
            }

            // ===== CONFIG TABELLA =====
            table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table1.setRowSelectionAllowed(true);
            table1.setColumnSelectionAllowed(false);
            table1.setRowHeight(30);

            table1.getTableHeader().setReorderingAllowed(false);
            table1.getTableHeader().setResizingAllowed(false);

            table1.setSelectionBackground(new java.awt.Color(184, 207, 229));
            table1.setSelectionForeground(java.awt.Color.BLACK);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            for (int i = 0; i < table1.getColumnCount(); i++) {
                if (!table1.getColumnName(i).equals("Dettagli")) {
                    table1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }
            }

            DefaultTableCellRenderer headerRenderer =
                    (DefaultTableCellRenderer) table1.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            table1.getColumn("Dettagli").setCellRenderer(new ButtonRenderer());
            table1.getColumn("Dettagli").setCellEditor(
                    new ButtonEditor(table1, eventiMostrati)
            );
        }

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

            int idEvento = idEventiMostrati.get(selectedRow);

            boolean ok = controller.iscriviEvento(idEvento);

            if (ok) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Iscrizione completata con successo",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                parentFrame.showIscriviEvento(); // refresh

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

    public void refreshTabella(){
        inizializzaTabella();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}